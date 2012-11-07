package com.cyanogenmod.updater.preferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cyanogenmod.updater.R;
import com.cyanogenmod.updater.UpdatePreference;
import com.cyanogenmod.updater.customTypes.FullUpdateInfo;
import com.cyanogenmod.updater.customTypes.UpdateInfo;
import com.cyanogenmod.updater.customization.Customization;
import com.cyanogenmod.updater.interfaces.IActivityMessenger;
import com.cyanogenmod.updater.misc.Constants;
import com.cyanogenmod.updater.misc.State;
import com.cyanogenmod.updater.tasks.FileIO;
import com.cyanogenmod.updater.tasks.UpdateCheckTask;
import com.cyanogenmod.updater.utils.SysUtils;
import com.cyanogenmod.updater.utils.UpdateFilter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AvailableUpdatesFragment extends PreferenceFragment 
    implements OnSharedPreferenceChangeListener {
    private static final String TAG = "AvailableUpdatesFragment";
    private static final boolean DEBUG = true;

    private SharedPreferences mPrefs;
    private IActivityMessenger messenger;
    
    private UpdatePreference mDownloadingPreference;
    private File mUpdateFolder;
    private ArrayList<UpdateInfo> mServerUpdates;
    private ArrayList<UpdateInfo> mLocalUpdates;
    private String mFileName;
    private String mSystemMod;
    private String mSystemRom;
    private DownloadManager mDownloadManager;
    private long mEnqueue;
    private PreferenceCategory mUpdatesList;
    private FileIO fileIO;
    
    private Handler mUpdateHandler = new Handler();

    public AvailableUpdatesFragment() {
        // Empty so the header can instantiate it
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("TAG", "attaching au fragment");
        try {
            messenger = (IActivityMessenger) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement iActivityMessenger");
        }
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("TAG", "onCreate au fragment");
        
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_cat_available_updates, false);
        addPreferencesFromResource(R.xml.pref_cat_available_updates);
        
        mUpdatesList = (PreferenceCategory) findPreference(getString(R.string.available_updates_key));
        
        // Get the currently installed system Mod and Rom for later matching
        mSystemMod = SysUtils.getSystemProperty(Customization.BOARD);
        mSystemRom = SysUtils.getSystemProperty(Customization.SYS_PROP_MOD_VERSION);
        
        // Get reference to the FileIO class for file I/O
        fileIO = new FileIO(getActivity().getBaseContext());

        // Initialize the arrays
        mServerUpdates = new ArrayList<UpdateInfo>();
        mLocalUpdates = new ArrayList<UpdateInfo>();

        // Determine if there are any in-progress downloads
        mDownloadManager = (DownloadManager) getActivity().getSystemService(
                Context.DOWNLOAD_SERVICE);
        mEnqueue = mPrefs.getLong(Constants.DOWNLOAD_ID, -1);
        if (mEnqueue != -1) {
            Cursor c = mDownloadManager.query(new DownloadManager.Query().setFilterById(mEnqueue));
            if (c == null) {
                Toast.makeText(getActivity(), R.string.download_not_found, Toast.LENGTH_LONG)
                        .show();
            } else {
                if (c.moveToFirst()) {
                    String lFile = c.getString(c
                            .getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                    int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (lFile != null && status != DownloadManager.STATUS_FAILED) {
                        String[] temp = lFile.split("/");
                        // Strip the .partial at the end of the name
                        mFileName = (temp[temp.length - 1]).replace(".partial", "");

                    }
                }
            }
            c.close();
        }

        updateLayout();

    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (mPrefs == null) {
            mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        }
        mPrefs.registerOnSharedPreferenceChangeListener(this);
        mUpdateHandler.post(updateProgress);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPrefs.unregisterOnSharedPreferenceChangeListener(this);
        mUpdateHandler.removeCallbacks(updateProgress);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences mPrefs, String key) {
        // TODO Auto-generated method stub
        
        if (Constants.CHECK_FOR_UPDATE.equals(key)) {
            if (mPrefs.getBoolean(Constants.CHECK_FOR_UPDATE, false) == true) {
                checkForUpdates();
                mPrefs.edit().putBoolean(Constants.CHECK_FOR_UPDATE, false).apply();
            }
        }
        
    }
    
    public void updateLayout() {
        Log.d(TAG, "in updateLayout");
        FullUpdateInfo availableUpdates = null;
        try {
            availableUpdates = State.loadState(getActivity().getBaseContext());
        } catch (IOException e) {
            Log.e(TAG, "Unable to restore activity status", e);
        }

        // Read existing Updates
        List<String> existingFilenames = null;
        mUpdateFolder = new File(Environment.getExternalStorageDirectory() + "/cmupdater");
        FilenameFilter f = new UpdateFilter(".zip");
        File[] files = mUpdateFolder.listFiles(f);

        // If Folder Exists and Updates are present(with md5files)
        if (mUpdateFolder.exists() && mUpdateFolder.isDirectory() && files != null
                && files.length > 0) {
            // To show only the Filename. Otherwise the whole Path with
            // /sdcard/cm-updates will be shown
            existingFilenames = new ArrayList<String>();
            for (File file : files) {
                if (file.isFile()) {
                    existingFilenames.add(file.getName());
                }
            }
            // For sorting the Filenames, have to find a way to do natural
            // sorting
            existingFilenames = Collections.synchronizedList(existingFilenames);
            Collections.sort(existingFilenames, Collections.reverseOrder());
        } else {
            Log.d(TAG, "dir not there or no files");
        }
        files = null;

        // Clear the notification if one exists
        ((NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE))
                .cancel(R.string.not_new_updates_found_title);

        // Sets the Rom Variables
        List<UpdateInfo> availableRoms = null;
        if (availableUpdates != null) {
            if (availableUpdates.roms != null)
                availableRoms = availableUpdates.roms;
            // Add the incrementalUpdates
            if (availableUpdates.incrementalRoms != null)
                availableRoms.addAll(availableUpdates.incrementalRoms);
        }

        // Existing Updates Layout
        mLocalUpdates.clear();
        if (existingFilenames != null && existingFilenames.size() > 0) {
            for (String fileName : existingFilenames) {
                UpdateInfo ui = new UpdateInfo();
                ui.setName(fileName);
                ui.setFileName(fileName);
                mLocalUpdates.add(ui);
            }
        }

        // Available Roms Layout
        mServerUpdates.clear();
        if (availableRoms != null && availableRoms.size() > 0) {
            for (UpdateInfo rom : availableRoms) {

                // See if we have matching updates already downloaded
                boolean matched = false;
                for (UpdateInfo ui : mLocalUpdates) {
                    if (ui.getFileName().equals(rom.getFileName())) {
                        matched = true;
                    }
                }

                // Only add updates to the server list that are not already
                // downloaded
                if (!matched) {
                    mServerUpdates.add(rom);
                }
            }
        }

        // Update the preference list
        refreshPreferences();
    }

    private void refreshPreferences() {
        if (mUpdatesList != null) {
            // Clear the list
            mUpdatesList.removeAll();
            boolean foundMatch;
            int style;

            // Convert the systemRom name to the associated filename
            String installedZip = "cm-" + mSystemRom.toString() + ".zip";

            // Add the server based updates
            // Since these will almost always be newer, they should appear at
            // the top
            if (!mServerUpdates.isEmpty()) {
                for (UpdateInfo ui : mServerUpdates) {

                    // Determine the preference style and create the preference
                    foundMatch = ui.getFileName().equals(mFileName);
                    if (foundMatch) {
                        // In progress download
                        style = UpdatePreference.STYLE_DOWNLOADING;
                    } else if (ui.getFileName().equals(installedZip)) {
                        // This is the currently installed mod
                        style = UpdatePreference.STYLE_INSTALLED;
                    } else {
                        style = UpdatePreference.STYLE_NEW;
                    }

                    // Create a more user friendly title by stripping of the
                    // '-device.zip' at the end
                    String title = ui.getFileName().replace("-" + mSystemMod + ".zip", "");
                    UpdatePreference up = new UpdatePreference(this, ui, title, style);
                    up.setKey(ui.getFileName());

                    // If we have an in progress download, link the preference
                    if (foundMatch) {
                        mDownloadingPreference = up;
                        mUpdateHandler.post(updateProgress);
                        foundMatch = false;
                        mPrefs.edit().putBoolean("isDownloading", true).apply();
                    }

                    // Add to the list
                    mUpdatesList.addPreference(up);
                }
            }

            // Add the locally saved update files last
            // Since these will almost always be older versions, they should
            // appear at the bottom
            if (!mLocalUpdates.isEmpty()) {
                for (UpdateInfo ui : mLocalUpdates) {

                    // Retrieve the changelog
                    ui.setChanges(FileIO.readLogFile(ui.getFileName()));

                    // Create a more user friendly title by stripping of the
                    // '-device.zip' at the end
                    String title = ui.getFileName().replace("-" + mSystemMod + ".zip", "");
                    UpdatePreference up = new UpdatePreference(this, ui, title, ui
                            .getFileName().equals(installedZip)
                            ? UpdatePreference.STYLE_INSTALLED : UpdatePreference.STYLE_DOWNLOADED);
                    up.setKey(ui.getFileName());

                    // Add to the list
                    mUpdatesList.addPreference(up);
                }
            }

            // If no updates are in the list, show the default message
            if (mUpdatesList.getPreferenceCount() == 0) {
                Preference npref = new Preference(getActivity());
                npref.setLayoutResource(R.layout.preference_empty_list);
                npref.setTitle(R.string.no_available_updates_intro);
                npref.setEnabled(false);
                mUpdatesList.addPreference(npref);
            }
        }
    }

    public void checkForUpdates() {
        // Refresh the Layout when UpdateCheck finished
        Log.d(TAG, "Check for updates");
        UpdateCheckTask task = new UpdateCheckTask(getActivity());
        task.execute((Void) null);
        updateLayout();
    }

    public void onNewIntent(Intent intent) {
        // Check if we need to refresh the screen to show new updates
        Log.d(TAG, "here in onNewIntent");
        boolean doCheck = intent.getBooleanExtra(Constants.CHECK_FOR_UPDATE, false);
        if (doCheck) {
            Log.d(TAG, " -- do check");
            updateLayout();
        }

        // Check if we have been asked to start an update
        boolean startUpdate = intent.getBooleanExtra(Constants.START_UPDATE, false);
        if (startUpdate) {
            Log.d(TAG, " -- start Update");
            UpdateInfo ui = (UpdateInfo) intent.getSerializableExtra(Constants.KEY_UPDATE_INFO);
            if (ui != null) {
                UpdatePreference pref = findMatchingPreference(ui.getFileName());
                if (pref != null) {
                    pref.setStyle(UpdatePreference.STYLE_DOWNLOADED);
                    fileIO.startUpdate(ui);
                }
            }
        }

        // Check if we have been asked to start the 'download completed'
        // functionality
        boolean downloadCompleted = intent.getBooleanExtra(Constants.DOWNLOAD_COMPLETED, false);
        if (downloadCompleted) {
            long id = intent.getLongExtra(Constants.DOWNLOAD_ID, -1);
            String fullPathname = intent.getStringExtra(Constants.DOWNLOAD_FULLPATH);
            if (id != -1 && fullPathname != null) {
                downloadCompleted(id, fullPathname);
            }
        }

    }

    public void startDownload(String key) {
        if (mPrefs.getBoolean("isDownloading", false)) {
            Toast.makeText(getActivity(), R.string.download_already_running, Toast.LENGTH_LONG)
                    .show();
            return;
        }
        UpdatePreference pref = findMatchingPreference(key);
        if (pref != null) {
            // We have a match, get ready to trigger the download
            mDownloadingPreference = pref;

            UpdateInfo ui = mDownloadingPreference.getUpdateInfo();
            if (ui != null) {
                PackageManager manager = getActivity().getPackageManager();
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
                FileIO.writeLogFile(ui.getFileName(), ui.getChanges());

                // Build the name of the file to download, adding .partial at
                // the end. It will get
                // stripped off when the download completes
                String fullFilePath = "file://" + fullFolderPath + "/" + ui.getFileName()
                        + ".partial";
                Request request = new Request(Uri.parse(ui.getDownloadUrl()));
                request.addRequestHeader("Cache-Control", "no-cache");
                try {
                    PackageInfo pinfo = manager.getPackageInfo(getActivity().getPackageName(), 0);
                    request.addRequestHeader("User-Agent", pinfo.packageName + "/"
                            + pinfo.versionName);
                } catch (android.content.pm.PackageManager.NameNotFoundException nnfe) {
                    // Do nothing
                }
                request.setTitle(getActivity().getResources().getString(R.string.app_name));
                request.setDescription(ui.getFileName());
                request.setDestinationUri(Uri.parse(fullFilePath));
                request.setAllowedOverRoaming(false);
                request.setVisibleInDownloadsUi(false);

                // TODO: this could/should be made configurable
                request.setAllowedOverMetered(true);

                // Start the download
                mEnqueue = mDownloadManager.enqueue(request);
                mFileName = ui.getFileName();
                mPrefs.edit().putBoolean("isDownloading", true).apply();

                // Store in shared preferences
                mPrefs.edit().putLong(Constants.DOWNLOAD_ID, mEnqueue).apply();
                mPrefs.edit().putString(Constants.DOWNLOAD_MD5, ui.getMD5()).apply();
                mUpdateHandler.post(updateProgress);
            }
        }
    }

    public void stopDownload() {
        if (!mPrefs.getBoolean("isDownloading", false) || mFileName == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                mPrefs.edit().putBoolean("isDownloading", false).apply();

                // Clear the stored data from sharedpreferences
                mPrefs.edit().putLong(Constants.DOWNLOAD_ID, mEnqueue).apply();
                mPrefs.edit().putString(Constants.DOWNLOAD_MD5, "").apply();
                Toast.makeText(getActivity(), R.string.download_cancelled,
                        Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.dialog_no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void downloadCompleted(long downloadId, String fullPathname) {
        mPrefs.edit().putBoolean("isDownloading", false).apply();

        String[] temp = fullPathname.split("/");
        String fileName = temp[temp.length - 1];

        // Find the matching preference so we can retrieve the UpdateInfo
        UpdatePreference pref = findMatchingPreference(fileName);
        if (pref != null) {
            UpdateInfo ui = pref.getUpdateInfo();
            pref.setStyle(UpdatePreference.STYLE_DOWNLOADED);
            fileIO.startUpdate(ui);
        }
    }

    // *********************************************************
    // Supporting methods
    // *********************************************************

    Runnable updateProgress = new Runnable() {
        public void run() {
            if (mDownloadingPreference != null) {
                if (mDownloadingPreference.getProgressBar() != null
                        && mPrefs.getBoolean("isDownloading", false)) {
                    DownloadManager mgr = (DownloadManager) getActivity().getSystemService(
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
                if (mPrefs.getBoolean("isDownloading", false)) {
                    mUpdateHandler.postDelayed(this, 1000);
                }
            }
        }
    };

    private boolean deleteOldUpdates() {
        boolean success;
        // mUpdateFolder: Foldername with fullpath of SDCARD
        if (mUpdateFolder.exists() && mUpdateFolder.isDirectory()) {
            if (DEBUG) {
                Log.d(TAG, "would delete old updates now");
            } else {
                FileIO.deleteDir(mUpdateFolder);
            }
            mUpdateFolder.mkdir();
            success = true;
            Toast.makeText(getActivity(), R.string.delete_updates_success_message,
                    Toast.LENGTH_SHORT)
                    .show();
        } else if (!mUpdateFolder.exists()) {
            success = false;
            Toast.makeText(getActivity(), R.string.delete_updates_noFolder_message,
                    Toast.LENGTH_SHORT)
                    .show();
        } else {
            success = false;
            Toast.makeText(getActivity(), R.string.delete_updates_failure_message,
                    Toast.LENGTH_SHORT)
                    .show();
        }
        return success;
    }

    public void confirmDeleteAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.confirm_delete_dialog_title);
        builder.setMessage(R.string.confirm_delete_all_dialog_message);
        builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // We are OK to delete, trigger it
                deleteOldUpdates();
                updateLayout();
            }
        });
        builder.setNegativeButton(R.string.dialog_no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
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



}
