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
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cyanogenmod.updater.customization.Customization;
import com.cyanogenmod.updater.interfaces.IActivityMessenger;
import com.cyanogenmod.updater.misc.Constants;
import com.cyanogenmod.updater.preferences.UpdateFragment;
import com.cyanogenmod.updater.utils.SysUtils;

import java.util.Date;
import java.util.List;

public class UpdatesSettings extends PreferenceActivity implements IActivityMessenger {
    private static final String TAG = "UpdatesSettings";
    private static final boolean DEBUG = false;

    private static final int MENU_REFRESH = 0;
    private static final int MENU_DELETE_ALL = 1;
    private static final int MENU_SYSTEM_INFO = 2;

    private String mSystemMod;
    private String mSystemRom;

    private UpdateFragment mUpdateFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set 'HomeAsUp' feature of the actionbar to fit better into Settings
        final ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        // Turn on the Options Menu
        invalidateOptionsMenu();

        Log.d("TAG", "end of on create");
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.layout.preference_headers, target);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, MENU_REFRESH, 0, R.string.menu_refresh)
                .setIcon(R.drawable.ic_menu_refresh)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS
                        | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add(0, MENU_DELETE_ALL, 0, R.string.menu_delete_all)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        menu.add(0, MENU_SYSTEM_INFO, 0, R.string.menu_system_info)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case MENU_REFRESH:
                // send update notice to updateFragment
                if (mUpdateFragment != null) {
                    mUpdateFragment.checkForUpdates();
                } else {
                    Log.e(TAG, "update Fragment was null in menu refresh");
                }
                return true;

            case MENU_DELETE_ALL:
                if (mUpdateFragment != null) {
                    mUpdateFragment.confirmDeleteAll();
                } else {
                    Log.e(TAG, "update Fragment was null in delete all");
                }
                return true;

            case MENU_SYSTEM_INFO:
                showSysInfo();
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return false;
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//
//        Log.e(TAG, "inside onNewIntent, the intent follows");
//
//        // Log.d(TAG, "bundle key set: " +
//        // intent.getExtras().keySet().toString());
//
//        Log.d(TAG, "show_fragment: " + intent.getExtras().getString(":android:show_fragment"));
//
//        FragmentManager fragmentManager = getFragmentManager();
//        if (fragmentManager != null) {
//            Log.d(TAG, fragmentManager.toString());
//            // UpdateFragment updateFragment = (UpdateFragment) fragmentManager
//            // .findFragmentById(R.id.update_fragment_id);
//            // if (updateFragment != null) {
//            // updateFragment.onNewIntent(intent);
//            // } else {
//            // Log.e(TAG, "null updateFragment in onNewIntent");
//            // }
//        } else {
//            Log.e(TAG, "null fragmentManager in onNewIntent");
//        }
//
//    }

    public void F2FMessage(Fragment recipient, String message) {

    }

    public void F2AMessage(String message) {

    }

    public void showSysInfo() {

        mSystemMod = SysUtils.getSystemProperty(Customization.BOARD);
        mSystemRom = SysUtils.getSystemProperty(Customization.SYS_PROP_MOD_VERSION);

        // Build the message
        Date lastCheck = new Date(PreferenceManager.getDefaultSharedPreferences(this)
                .getLong(Constants.LAST_UPDATE_CHECK_PREF, 0));
        String message = getString(R.string.sysinfo_device) + " " + mSystemMod + "\n\n"
                + getString(R.string.sysinfo_running) + " " + mSystemRom + "\n\n"
                + getString(R.string.sysinfo_last_check) + " " + lastCheck.toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.menu_system_info);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.dialog_ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
        ((TextView) dialog.findViewById(android.R.id.message)).setTextAppearance(this,
                android.R.style.TextAppearance_DeviceDefault_Small);
    }

}
