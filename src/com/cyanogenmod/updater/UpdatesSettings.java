/*
 * Copyright (C) 2012 The CyanogenMod Project (DvTonder)
 *
 * * Licensed under the GNU GPLv2 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl-2.0.txt
 */

package com.cyanogenmod.updater;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cyanogenmod.updater.customTypes.UpdateInfo;
import com.cyanogenmod.updater.fragment.SettingsFragment;
import com.cyanogenmod.updater.misc.Constants;

public class UpdatesSettings extends Activity {

    private static final String TAG = "UpdatesSettings";

    private static final int MENU_REFRESH = 0;
    private static final int MENU_DELETE_ALL = 1;
    private static final int MENU_SYSTEM_INFO = 2;

    private SettingsFragment mSettingsFragment;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettingsFragment = new SettingsFragment();

        // Load the fragment
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, mSettingsFragment).commit();

        // Set 'HomeAsUp' feature of the actionbar to fit better into Settings
        final ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        // Turn on the Options Menu
        invalidateOptionsMenu();
        
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        
        Log.e(TAG, "##########ON#####NEW#######INTENT##################");

        // Check if we need to refresh the screen to show new updates
        boolean doCheck = intent.getBooleanExtra(Constants.CHECK_FOR_UPDATE,
                false);

        if (mSettingsFragment == null) {
            mSettingsFragment = new SettingsFragment();
            Log.d(TAG, "mSettingsFragment is null in onNewIntent");
        } else {
            Log.d(TAG, "mSettingsFragment is NOT null in onNewIntent");
        }

        if (doCheck) {
            mSettingsFragment.updateLayout();
        }

        // Check if we have been asked to start an update
        boolean startUpdate = intent.getBooleanExtra(Constants.START_UPDATE,
                false);
        if (startUpdate) {
            UpdateInfo ui = (UpdateInfo) intent
                    .getSerializableExtra(Constants.KEY_UPDATE_INFO);
            if (ui != null) {
                UpdatePreference pref = mSettingsFragment
                        .findMatchingPreference(ui.getFileName());
                if (pref != null) {
                    pref.setStyle(UpdatePreference.STYLE_DOWNLOADED);
                    mSettingsFragment.startUpdate(ui);
                }
            }
        }

        // Check if we have been asked to start the 'download completed'
        // functionality
        boolean downloadCompleted = intent.getBooleanExtra(
                Constants.DOWNLOAD_COMPLETED, false);
        if (downloadCompleted) {
            long id = intent.getLongExtra(Constants.DOWNLOAD_ID, -1);
            String fullPathname = intent
                    .getStringExtra(Constants.DOWNLOAD_FULLPATH);
            if (id != -1 && fullPathname != null) {
                mSettingsFragment.downloadCompleted(id, fullPathname);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_REFRESH, 0, R.string.menu_refresh)
                .setIcon(R.drawable.ic_menu_refresh)
                .setShowAsActionFlags(
                        MenuItem.SHOW_AS_ACTION_ALWAYS
                                | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add(0, MENU_DELETE_ALL, 0, R.string.menu_delete_all)
                .setShowAsActionFlags(
                        MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        menu.add(0, MENU_SYSTEM_INFO, 0, R.string.menu_system_info)
                .setShowAsActionFlags(
                        MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_REFRESH:
            mSettingsFragment.checkForUpdates();
            return true;

        case MENU_DELETE_ALL:
            mSettingsFragment.confirmDeleteAll();
            return true;

        case MENU_SYSTEM_INFO:
            mSettingsFragment.showSysInfo();
            return true;

        case android.R.id.home:
            onBackPressed();
            return true;
        }
        return false;
    }
    
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
