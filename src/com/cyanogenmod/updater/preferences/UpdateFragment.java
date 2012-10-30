
package com.cyanogenmod.updater.preferences;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.preference.ListPreference;
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
import com.cyanogenmod.updater.interfaces.IActivityMessenger;
import com.cyanogenmod.updater.misc.Constants;
import com.cyanogenmod.updater.misc.State;
import com.cyanogenmod.updater.service.UpdateCheckService;
import com.cyanogenmod.updater.tasks.UpdateCheckTask;
import com.cyanogenmod.updater.utils.UpdateFilter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class UpdateFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    private static final String TAG = "UpdateFragment";

    private ListPreference mUpdateCheck;
    private ListPreference mUpdateType;
    private PreferenceCategory mUpdatesList;
    private UpdatePreference mDownloadingPreference;
    private File mUpdateFolder;
    private ArrayList<UpdateInfo> mServerUpdates;
    private ArrayList<UpdateInfo> mLocalUpdates;
    private boolean mStartUpdateVisible = false;

    private DownloadManager mDownloadManager;
    private boolean mDownloading = false;
    private long mEnqueue;
    private String mFileName;

    private String mSystemMod;
    private String mSystemRom;

    private Handler mUpdateHandler = new Handler();

    private SharedPreferences mPrefs;
    
    private IActivityMessenger messenger;

    public UpdateFragment() {
        // Empty so the header can instantiate it
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
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

        PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_update, false);

        addPreferencesFromResource(R.xml.pref_update);

        mUpdatesList = (PreferenceCategory) findPreference(Constants.KEY_UPDATE_FRAGMENT);

        mUpdateCheck = (ListPreference) findPreference(Constants.KEY_UPDATE_CHECK_PREFERENCE);
        if (mUpdateCheck != null) {
            int check = mPrefs.getInt(Constants.UPDATE_CHECK_PREF,
                    Integer.valueOf(Constants.UPDATE_FREQ_WEEKLY));
            mUpdateCheck.setValue(String.valueOf(check));
            mUpdateCheck.setSummary(mapCheckValue(check));
        }

        mUpdateType = (ListPreference) findPreference(Constants.KEY_UPDATE_INFO);
        if (mUpdateType != null) {
            int type = mPrefs.getInt(Constants.UPDATE_TYPE_PREF, 0);
            mUpdateType.setValue(String.valueOf(type));
            mUpdateType.setSummary(mUpdateType.getEntries()[type]);
        }

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
        Log.d(TAG, "onResume()");
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this);
        mUpdateHandler.post(updateProgress);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
        mUpdateHandler.removeCallbacks(updateProgress);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences mPrefs, String key) {
        // TODO Auto-generated method stub

        Log.d(TAG, "Shared prefs were changes");

        if (findPreference(key) == mUpdateCheck) {
            int newValue = mPrefs.getInt(key, Constants.UPDATE_FREQ_AT_BOOT);
            mUpdateCheck.setSummary(mapCheckValue(newValue));
            scheduleUpdateService(newValue * 1000);
        } else if (findPreference(key) == mUpdateType) {
            int newValue = Integer.valueOf((String) mPrefs.getString(key,
                    Constants.UPDATE_INFO_BRANCH_NIGHTLY));
            mUpdateType.setSummary(mUpdateType.getEntries()[newValue]);
            checkForUpdates();
        } else if (Constants.CHECK_FOR_UPDATE.equals(key)) {
            if (mPrefs.getBoolean(Constants.CHECK_FOR_UPDATE, false) == true) {
                checkForUpdates();
                mPrefs.edit().putBoolean(Constants.CHECK_FOR_UPDATE, false).apply();
            }
        }
    }

    private void checkForUpdates() {
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
                    startUpdate(ui);
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
    
    // *********************************************************
    // Supporting methods
    // *********************************************************

    Runnable updateProgress = new Runnable() {
        public void run() {
            if (mDownloadingPreference != null) {
                if (mDownloadingPreference.getProgressBar() != null && mDownloading) {
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
                if (mDownloading) {
                    mUpdateHandler.postDelayed(this, 1000);
                }
            }
        }
    };

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

    private String readLogFile(String filename) {
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
            return getString(R.string.no_changelog_alert);
        }

        return text.toString();
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
                        mDownloading = true;
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
                    ui.setChanges(readLogFile(ui.getFileName()));

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
                    getActivity(),
                    MessageFormat
                            .format(getResources().getString(
                                    R.string.delete_single_update_success_message),
                                    filename), Toast.LENGTH_SHORT).show();

        } else if (!mUpdateFolder.exists()) {
            Toast.makeText(getActivity(), R.string.delete_updates_noFolder_message,
                    Toast.LENGTH_SHORT)
                    .show();

        } else {
            Toast.makeText(getActivity(), R.string.delete_updates_failure_message,
                    Toast.LENGTH_SHORT)
                    .show();
        }

        // Update the list
        updateLayout();
        return success;
    }

    private void scheduleUpdateService(int updateFrequency) {
        // Get the intent ready
        Intent i = new Intent(getActivity(), UpdateCheckService.class);
        i.putExtra(Constants.CHECK_FOR_UPDATE, true);
        PendingIntent pi = PendingIntent.getService(getActivity(), 0, i, 0);

        // Clear any old alarms before we start
        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);

        // Check if we need to schedule a new alarm
        if (updateFrequency > 0) {
            Date lastCheck = new Date(mPrefs.getLong(Constants.LAST_UPDATE_CHECK_PREF, 0));
            am.setRepeating(AlarmManager.RTC_WAKEUP, lastCheck.getTime() + updateFrequency,
                    updateFrequency, pi);
        }
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

    private boolean deleteOldUpdates() {
        boolean success;
        // mUpdateFolder: Foldername with fullpath of SDCARD
        if (mUpdateFolder.exists() && mUpdateFolder.isDirectory()) {
            deleteDir(mUpdateFolder);
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

}
