package com.cyanogenmod.updater.tasks;

import android.app.AlertDialog;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;
import android.widget.Toast;

import com.cyanogenmod.updater.R;
import com.cyanogenmod.updater.UpdatePreference;
import com.cyanogenmod.updater.customTypes.UpdateInfo;
import com.cyanogenmod.updater.misc.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

public class FileIO {
    private static final String TAG = "FileIO";
    private Context mContext;
    private boolean mStartUpdateVisible = false;

    public FileIO(Context context) {
        mContext = context;
    }

    public void startDownload(UpdatePreference mDownloadingPreference, Boolean mDownloading) {
        if (mDownloading) {
            Toast.makeText(mContext, R.string.download_already_running, Toast.LENGTH_LONG)
                .show();
            return;
        }

        UpdateInfo ui = mDownloadingPreference.getUpdateInfo();
        if (ui != null) {
            PackageManager manager = mContext.getPackageManager();
            mDownloadingPreference.setStyle(UpdatePreference.STYLE_DOWNLOADING);

            // Create the download request and set some basic parameters
            String fullFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + Constants.UPDATES_FOLDER;
            // If directory doesn't exist, create it
            File directory = new File(fullFolderPath);
            if (!directory.exists()) {
                directory.mkdirs();
                Log.d(TAG, "UpdateFolder created");
            }

            // Save the Changelog content to the sdcard for later use
            writeLogFile(ui.getFileName(), ui.getChanges());

            // Build the name of the file to download, adding .partial at
            // the end. It will get
            // stripped off when the download completes
            String fullFilePath = "file://" + fullFolderPath + "/" + ui.getFileName()
                    + ".partial";
            Request request = new Request(Uri.parse(ui.getDownloadUrl()));
            request.addRequestHeader("Cache-Control", "no-cache");
            try {
                PackageInfo pinfo = manager.getPackageInfo(mContext.getPackageName(), 0);
                request.addRequestHeader("User-Agent", pinfo.packageName + "/"
                    + pinfo.versionName);
            } catch (android.content.pm.PackageManager.NameNotFoundException nnfe) {
                // Do nothing
            }
            request.setTitle(getString(R.string.app_name));
            request.setDescription(ui.getFileName());
            request.setDestinationUri(Uri.parse(fullFilePath));
            request.setAllowedOverRoaming(false);
            request.setVisibleInDownloadsUi(false);

            // TODO: this could/should be made configurable
            request.setAllowedOverMetered(true);

            // Start the download
            mEnqueue = mDownloadManager.enqueue(request);
            mFileName = ui.getFileName();
            mDownloading = true;

            // Store in shared preferences
            mPrefs.edit().putLong(Constants.DOWNLOAD_ID, mEnqueue).apply();
            mPrefs.edit().putString(Constants.DOWNLOAD_MD5, ui.getMD5()).apply();
            mUpdateHandler.post(updateProgress);
        }
    }

    Runnable updateProgress = new Runnable() {
        public void run() {
            if (mDownloadingPreference != null) {
                if (mDownloadingPreference.getProgressBar() != null && mDownloading) {
                    DownloadManager mgr = (DownloadManager) getSystemService(
                            Context.DOWNLOAD_SERVICE);
                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(mEnqueue);
                    Cursor cursor = mgr.query(q);
                    if (!cursor.moveToFirst()) {
                        return;
                    }
                    int bytes_downloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    cursor.close();
                    ProgressBar prog = mDownloadingPreference.getProgressBar();
                    if (bytes_total < 0) {
                        prog.setIndeterminate(true);
                    } else {
                        prog.setIndeterminate(false);
                        prog.setMax(bytes_total);
                    }
                    prog.setProgress(bytes_downloaded);
                }
                if (mDownloading) {
                    mUpdateHandler.postDelayed(this, 1000);
                }
            }
        }
    };

    public void stopDownload() {
        if (!mDownloading || mFileName == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.confirm_download_cancelation_dialog_title);
        builder.setMessage(R.string.confirm_download_cancelation_dialog_message);
        builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Set the preference back to new style
                UpdatePreference pref = findMatchingPreference(mFileName);
                if (pref != null) {
                    pref.setStyle(UpdatePreference.STYLE_NEW);
                }
                // We are OK to stop download, trigger it
                mDownloadManager.remove(mEnqueue);
                mUpdateHandler.removeCallbacks(updateProgress);
                mEnqueue = -1;
                mFileName = null;
                mDownloading = false;

                // Clear the stored data from sharedpreferences
                mPrefs.edit().putLong(Constants.DOWNLOAD_ID, mEnqueue).apply();
                mPrefs.edit().putString(Constants.DOWNLOAD_MD5, "").apply();
                Toast.makeText(mContext, R.string.download_cancelled,
                        Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.dialog_no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void downloadCompleted(long downloadId, String fullPathname) {
        mDownloading = false;

        String[] temp = fullPathname.split("/");
        String fileName = temp[temp.length - 1];

        // Find the matching preference so we can retrieve the UpdateInfo
        UpdatePreference pref = findMatchingPreference(fileName);
        if (pref != null) {
            UpdateInfo ui = pref.getUpdateInfo();
            pref.setStyle(UpdatePreference.STYLE_DOWNLOADED);
            startUpdate(ui);
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }

    public void startUpdate(final UpdateInfo updateInfo) {
        // Prevent the dialog from being triggered more than once
        if (mStartUpdateVisible) {
            return;
        } else {
            mStartUpdateVisible = true;
        }

        // Get the message body right
        String dialogBody = MessageFormat.format(
                getResources().getString(R.string.apply_update_dialog_text),
                updateInfo.getFileName());

        // Display the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.apply_update_dialog_title);
        builder.setMessage(dialogBody);
        builder.setPositiveButton(R.string.dialog_update, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                /*
                 * Should perform the following steps. 0.- Ask the user for a
                 * confirmation (already done when we reach here) 1.- mkdir -p
                 * /cache/recovery 2.- echo 'boot-recovery' >
                 * /cache/recovery/command 3.- if(mBackup) echo '--nandroid' >>
                 * /cache/recovery/command 4.- echo
                 * '--update_package=SDCARD:update.zip' >>
                 * /cache/recovery/command 5.- reboot recovery
                 */
                try {
                    // Set the 'boot recovery' command
                    Process p = Runtime.getRuntime().exec("sh");
                    OutputStream os = p.getOutputStream();
                    os.write("mkdir -p /cache/recovery/\n".getBytes());
                    os.write("echo 'boot-recovery' >/cache/recovery/command\n".getBytes());

                    // Add the update folder/file name
                    String cmd = "echo '--update_package=" + getStorageMountpoint()
                            + "/" + Constants.UPDATES_FOLDER + "/" + updateInfo.getFileName()
                            + "' >> /cache/recovery/command\n";
                    os.write(cmd.getBytes());
                    os.flush();

                    // Trigger the reboot
                    PowerManager powerManager = (PowerManager) mContext.getSystemService(
                            Context.POWER_SERVICE);
                    powerManager.reboot("recovery");

                } catch (IOException e) {
                    Log.e(TAG, "Unable to reboot into recovery mode:", e);
                    Toast.makeText(mContext, R.string.apply_unable_to_reboot_toast,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mStartUpdateVisible = false;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String getStorageMountpoint() {
        StorageManager sm = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        StorageVolume[] volumes = sm.getVolumeList();
        String primaryStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        boolean alternateIsInternal = mContext.getResources().getBoolean(R.bool.alternateIsInternal);

        if (volumes.length <= 1) {
            // single storage, assume only /sdcard exists
            return "/sdcard";
        }

        for (int i = 0; i < volumes.length; i++) {
            StorageVolume v = volumes[i];
            if (v.getPath().equals(primaryStoragePath)) {
                /*
                 * This is the primary storage, where we stored the update file
                 * For CM10, a non-removable storage (partition or FUSE) will
                 * always be primary. But we have older recoveries out there in
                 * which /sdcard is the microSD, and the internal partition is
                 * mounted at /emmc. At buildtime, we try to automagically guess
                 * from recovery.fstab what's the recovery configuration for
                 * this device. If "/emmc" exists, and the primary isn't
                 * removable, we assume it will be mounted there.
                 */
                if (!v.isRemovable() && alternateIsInternal) {
                    return "/emmc";
                }
            }
            ;
        }
        // Not found, assume non-alternate
        return "/sdcard";
    }

    private UpdatePreference findMatchingPreference(String key) {

        if (mUpdatesList != null) {
            // Find the matching preference
            for (int i = 0; i < mUpdatesList.getPreferenceCount(); i++) {
                UpdatePreference pref = (UpdatePreference) mUpdatesList.getPreference(i);
                if (pref.getKey().equals(key)) {
                    return pref;
                }
            }
        }
        return null;
    }

    private String mapCheckValue(Integer value) {
        Resources resources = mContext.getResources();
        String[] checkNames = resources.getStringArray(R.array.update_check_entries);
        String[] checkValues = resources.getStringArray(R.array.update_check_values);
        for (int i = 0; i < checkValues.length; i++) {
            if (Integer.decode(checkValues[i]).equals(value)) {
                return checkNames[i];
            }
        }
        return mContext.getString(R.string.unknown);
    }
    
    private void writeLogFile(String filename, String log) {
        File logFile = new File(mUpdateFolder + "/" + filename + ".changelog");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
            bw.write(log);
            bw.close();
        } catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }
    }
}
