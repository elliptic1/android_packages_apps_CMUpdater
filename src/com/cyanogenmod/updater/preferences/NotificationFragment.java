
package com.cyanogenmod.updater.preferences;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyanogenmod.updater.R;
import com.cyanogenmod.updater.interfaces.IActivityMessenger;
import com.cyanogenmod.updater.misc.Constants;

public class NotificationFragment extends PreferenceFragment implements
        OnSharedPreferenceChangeListener {
    private final String TAG = "NotificationFragment";
    private IActivityMessenger messenger;

    private CheckBoxPreference mNotificationsCheckBox;
    private RingtonePreference mRingtone;
    private SharedPreferences mPrefs;

    public NotificationFragment() {
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
        PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_cat_notification, false);
        addPreferencesFromResource(R.xml.pref_cat_notification);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mNotificationsCheckBox = (CheckBoxPreference) findPreference(getString(R.string.notifications_checkbox_key));
        mRingtone = (RingtonePreference) findPreference(getString(R.string.ringtone_pref_key));

        mRingtone.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                updateRingtoneSummary((RingtonePreference) preference, Uri.parse((String) newValue));
                return true;
            }

            private void updateRingtoneSummary(RingtonePreference preference, Uri ringtoneUri) {
                Ringtone ringtone = RingtoneManager.getRingtone(getActivity().getBaseContext(),
                        ringtoneUri);
                if (ringtone != null) {
                    preference.setSummary(ringtone.getTitle(getActivity().getBaseContext()));
                } else {
                    preference.setSummary(getString(R.string.ringtone_silent));
                }

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPrefs == null) {
            mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        }
        mPrefs.registerOnSharedPreferenceChangeListener(this);
        mRingtone.setSummary(createRingtonePreferenceSummary());
        updatePreferenceHeader();
    }

    private void updatePreferenceHeader() {
        FragmentMessage message = new FragmentMessage();
        message.setPurpose(FragmentMessage.updateField);
        message.setRecipient(FragmentMessage.parentActivity);
        message.setPreferenceToUpdate(R.string.notification_fragment_key);
        message.setBody(createNotificationHeaderSummary());
        messenger.sendMessage(message);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPrefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences mPrefs, String key) {

        Log.d(TAG, "onSharedPreferenceChanged, the key is " + key);

        Preference changedPreference = findPreference(key);
        
        if (changedPreference == null ) {
            Log.e(TAG, "the changedPref is null");    
        } else if (changedPreference == mRingtone) {
            Log.d(TAG, "rt changed");
            mRingtone.setSummary(createRingtonePreferenceSummary());
        } else if (changedPreference == findPreference(getString(R.string.notifications_checkbox_key))) {
            Log.d(TAG, "np check box changed to " + ((CheckBoxPreference) changedPreference).isChecked());
        }

        updatePreferenceHeader();

    }

    private String createRingtonePreferenceSummary() {
        String newSummary = "No preferences set";
        if (mRingtone != null) {
            Uri ringtoneUri = Uri.parse((String) mPrefs.getString("notifications_ringtone", ""));
            Ringtone ringtone = RingtoneManager.getRingtone(getActivity().getBaseContext(),
                    ringtoneUri);
            if (ringtone != null) {
                if (ringtone.getTitle(getActivity().getBaseContext())
                        .equals("Unknown ringtone")) {
                    mRingtone.setSummary(getString(R.string.ringtone_silent));
                    newSummary = getString(R.string.ringtone_silent);
                } else {
                    mRingtone.setSummary(ringtone.getTitle(getActivity().getBaseContext()));
                    newSummary = ringtone.getTitle(getActivity().getBaseContext());
                }
            } else {
                Log.d(TAG, "ringtone was null when creating the summary");
            }

        } else {
            Log.d(TAG, "mRingtone was null when creating the summary");
        }

        return newSummary;
    }

    private String createNotificationHeaderSummary() {
        if (mNotificationsCheckBox.isChecked() == false) {
            return "Notifications are Off";
        } else {
            return "On - " + mRingtone.getSummary();
        }
    }
}
