
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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import com.cyanogenmod.updater.tasks.FileIO;
import com.cyanogenmod.updater.tasks.UpdateCheckTask;
import com.cyanogenmod.updater.utils.UpdateFilter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class UpdateFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    private static final String TAG = "UpdateFragment";

    private ListPreference mUpdateCheck;
    private ListPreference mUpdateType;

    private SharedPreferences mPrefs;

    private IActivityMessenger messenger;
    private FileIO fileIO;

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

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_cat_update_options, false);
        addPreferencesFromResource(R.xml.pref_cat_update_options);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPrefs == null) {
            mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        }
        mPrefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPrefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences mPrefs, String key) {

        Log.d(TAG, "Shared prefs were changed, key = " + key);

        if (findPreference(key) == mUpdateCheck) {

            int newValue = Integer.valueOf(mPrefs
                    .getString(key, "" + Constants.UPDATE_FREQ_AT_BOOT));
            mUpdateCheck.setSummary(fileIO.mapCheckValue(newValue));
            scheduleUpdateService(newValue * 1000);

        } else if (findPreference(key) == mUpdateType) {

//            String u = mPrefs.getString(key,
//                    "" + Constants.UPDATE_INFO_BRANCH_NIGHTLY);
//            int newValue = Integer.valueOf(u);
//            mUpdateType.setSummary(mUpdateType.getEntries()[newValue]);

        }
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

 
}
