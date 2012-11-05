/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * * Licensed under the GNU GPLv2 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl-2.0.txt
 */

package com.cyanogenmod.updater.interfaces;

import com.cyanogenmod.updater.preferences.FragmentMessage;

public interface IActivityMessenger {

    public abstract void sendMessage(FragmentMessage message);

}
