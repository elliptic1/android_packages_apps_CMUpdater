/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * * Licensed under the GNU GPLv2 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl-2.0.txt
 */

package com.cyanogenmod.updater.interfaces;

import android.app.Fragment;

public interface IActivityMessenger {

    public abstract void F2FMessage(Fragment recipient, String message);

    public abstract void F2AMessage(String message);

}
