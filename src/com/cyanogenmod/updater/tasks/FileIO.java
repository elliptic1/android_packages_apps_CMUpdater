package com.cyanogenmod.updater.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Environment;
import android.os.PowerManager;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;
import android.widget.Toast;

import com.cyanogenmod.updater.R;
import com.cyanogenmod.updater.customTypes.UpdateInfo;
import com.cyanogenmod.updater.misc.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

public class FileIO {
    private static final String TAG = "FileIO";
    private static Context mContext;
    private boolean mStartUpdateVisible = false;
    private static File mUpdateFolder;
    
    public FileIO(Context context) {
        mContext = context;
        mUpdateFolder = new File(Environment.getExternalStorageDirectory() + "/cmupdater");
    }

    public static boolean deleteDir(File dir) {
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
                getContext().getString(R.string.apply_update_dialog_text),
                updateInfo.getFileName());

        // Display the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                    PowerManager powerManager = (PowerManager) getContext().getSystemService(
                            Context.POWER_SERVICE);
                    powerManager.reboot("recovery");

                } catch (IOException e) {
                    Log.e(TAG, "Unable to reboot into recovery mode:", e);
                    Toast.makeText(getContext(), R.string.apply_unable_to_reboot_toast,
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
        StorageManager sm = (StorageManager) getContext().getSystemService(Context.STORAGE_SERVICE);
        StorageVolume[] volumes = sm.getVolumeList();
        String primaryStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        boolean alternateIsInternal = getContext().getResources().getBoolean(R.bool.alternateIsInternal);

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

    public String mapCheckValue(Integer value) {
        Resources resources = getContext().getResources();
        String[] checkNames = resources.getStringArray(R.array.update_check_entries);
        String[] checkValues = resources.getStringArray(R.array.update_check_values);
        for (int i = 0; i < checkValues.length; i++) {
            if (Integer.decode(checkValues[i]).equals(value)) {
                return checkNames[i];
            }
        }
        return getContext().getString(R.string.unknown);
    }
    
    public static String readLogFile(String filename) {
        StringBuilder text = new StringBuilder();

        File logFile = new File(mUpdateFolder + "/" + filename + ".changelog");
        try {
            BufferedReader br = new BufferedReader(new FileReader(logFile));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            return getContext().getString(R.string.no_changelog_alert);
        }

        return text.toString();
    }

    public static void writeLogFile(String filename, String log) {
        File logFile = new File(mUpdateFolder + "/" + filename + ".changelog");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
            bw.write(log);
            bw.close();
        } catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }
    }
    
    public boolean deleteUpdate(String filename) {
        boolean success = false;
        if (mUpdateFolder.exists() && mUpdateFolder.isDirectory()) {
            File zipFileToDelete = new File(mUpdateFolder + "/" + filename);
            File logFileToDelete = new File(mUpdateFolder + "/" + filename + ".changelog");
            if (zipFileToDelete.exists()) {
                zipFileToDelete.delete();
            } else {
                Log.d(TAG, "Update to delete not found");
                return false;
            }
            if (logFileToDelete.exists()) {
                logFileToDelete.delete();
            }
            zipFileToDelete = null;
            logFileToDelete = null;

            success = true;
            Toast.makeText(
                    getContext(),
                    MessageFormat
                            .format(getContext().getResources().getString(
                                    R.string.delete_single_update_success_message),
                                    filename), Toast.LENGTH_SHORT).show();

        } else if (!mUpdateFolder.exists()) {
            Toast.makeText(getContext(), R.string.delete_updates_noFolder_message,
                    Toast.LENGTH_SHORT)
                    .show();

        } else {
            Toast.makeText(getContext(), R.string.delete_updates_failure_message,
                    Toast.LENGTH_SHORT)
                    .show();
        }

        return success;
    }
    
    private static Context getContext() {
        return mContext;
    }

}
