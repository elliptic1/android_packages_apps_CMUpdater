package com.cyanogenmod.updater.preferences;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.cyanogenmod.updater.R;
import com.cyanogenmod.updater.R.xml;
import com.cyanogenmod.updater.interfaces.IActivityMessenger;

public class AvailableUpdatesFragment extends PreferenceFragment 
    implements OnSharedPreferenceChangeListener {
    
    private IActivityMessenger messenger;

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("TAG", "created au fragment");
        PreferenceManager.setDefaultValues(getActivity(), R.xml.available_updates, false);
            
        addPreferencesFromResource(R.xml.available_updates);
        
    }
    
    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
        .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
        .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // TODO Auto-generated method stub
        
    }

}
