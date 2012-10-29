
package com.cyanogenmod.updater.preferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;

import com.cyanogenmod.updater.R;
import com.cyanogenmod.updater.misc.Constants;

public class NotificationFragment extends PreferenceFragment implements
        OnSharedPreferenceChangeListener {

    private RingtonePreference mRingtone;
    private CheckBoxPreference mAllowNotifications;

    private SharedPreferences mPrefs;

    public NotificationFragment() {
        // Empty so the header can instantiate it
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_notification, false);

        addPreferencesFromResource(R.xml.pref_notification);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mRingtone = (RingtonePreference) findPreference(Constants.KEY_RINGTONE_PREFERENCE);
        mAllowNotifications = (CheckBoxPreference) findPreference(Constants.KEY_NOTIFICATION_PREFERENCE);

    }

    @Override
    public void onResume() {
        super.onResume();
        mPrefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPrefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences mPrefs, String key) {

        if (findPreference(key) == mAllowNotifications) {
            if (mPrefs.getBoolean(Constants.KEY_NOTIFICATION_PREFERENCE, true) == false) {
                mRingtone.setEnabled(false);
            } else {
                mRingtone.setEnabled(true);
            }
        } else if (findPreference(key) == mRingtone) {

            if (mRingtone != null) {
                Uri ringtoneUri = Uri.parse((String) mPrefs.getString(Constants.KEY_RINGTONE_PREFERENCE, ""));
                Ringtone ringtone = RingtoneManager.getRingtone(getActivity().getBaseContext(),
                        ringtoneUri);
                if (ringtone != null) {
                    if (ringtone.getTitle(getActivity().getBaseContext())
                            .equals("Unknown ringtone")) {
                        mRingtone.setSummary("Silent");
                    } else {
                        mRingtone.setSummary(ringtone.getTitle(getActivity().getBaseContext()));
                    }
                }

                // The Sound picker defaults to on because the Allow
                // Notifications box is checked by default.
                if (mPrefs.getBoolean(Constants.KEY_NOTIFICATION_PREFERENCE, true) == false) {
                    mRingtone.setEnabled(false);
                } else {
                    mRingtone.setEnabled(true);
                }
            }
        }
    }

}
