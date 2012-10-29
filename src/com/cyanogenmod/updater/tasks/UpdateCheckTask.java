/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * * Licensed under the GNU GPLv2 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl-2.0.txt
 */

package com.cyanogenmod.updater.tasks;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;

import com.cyanogenmod.updater.R;
import com.cyanogenmod.updater.UpdatesSettings;
import com.cyanogenmod.updater.interfaces.IUpdateCheckService;
import com.cyanogenmod.updater.interfaces.IUpdateCheckServiceCallback;
import com.cyanogenmod.updater.misc.Constants;

public class UpdateCheckTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "CMUpdater";

    private IUpdateCheckService mService;
    private boolean mBound;
    private Intent mServiceIntent;
    private final ProgressDialog mProgressDialog;
    private final Activity mParent;

    public UpdateCheckTask(Activity parent) {
        mParent = parent;
        mProgressDialog = new ProgressDialog(mParent.getApplicationContext());
        mProgressDialog.setTitle(R.string.checking_for_updates);
        mProgressDialog.setMessage(mParent.getResources().getString(R.string.checking_for_updates));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                if (!isCancelled()) {
                    cancel(true);
                }
            }
        });
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog.show();
        mServiceIntent = new Intent(IUpdateCheckService.class.getName());
        ComponentName comp = mParent.startService(mServiceIntent);
        if (comp == null) {
            Log.e(TAG, "startService failed");
            mBound = false;
        } else {
            mBound = mParent.bindService(mServiceIntent, mConnection, 0);
        }
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        if (mBound) {
            try {
                while (mService == null) {
                    // Wait till the Service is bound
                }
                mService.checkForUpdates();
            } catch (RemoteException e) {
                Log.e(TAG, "Exception on calling UpdateCheckService", e);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (mBound) {
            mParent.unbindService(mConnection);
            mBound = false;
        }
        mParent.stopService(mServiceIntent);
        updateLayout();
    }

    @Override
    protected void onCancelled() {
        if (mBound) {
            mParent.unbindService(mConnection);
            mBound = false;
        }
        mParent.stopService(mServiceIntent);
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        updateLayout();
        super.onCancelled();
    }

    private void updateLayout() {
        PreferenceManager.getDefaultSharedPreferences(mParent)
                .edit().putBoolean(Constants.CHECK_FOR_UPDATE, true);
    }

    /**
     * Class for interacting with the main interface of the service.
     */
    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IUpdateCheckService.Stub.asInterface(service);
            try {
                mService.registerCallback(mCallback);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException", e);
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            try {
                mService.unregisterCallback(mCallback);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException", e);
            }
            mService = null;
        }
    };

    private final IUpdateCheckServiceCallback mCallback = new IUpdateCheckServiceCallback.Stub() {
        public void updateCheckFinished() throws RemoteException {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        }
    };
}
