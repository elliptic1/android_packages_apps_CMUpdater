package com.cyanogenmod.updater.preferences;

import android.os.Bundle;

public class FragmentMessage {
	
	// Message purposes
	public static final int updateField = 1;
	public static final int reloadContents = 2;
	
	// Message recipients
	public static final int parentActivity = 1;
	public static final int fragment = 2;

	private Bundle mBundle;
	
	FragmentMessage() {
		mBundle = new Bundle();
	}
	
	public Bundle getBundle() {
		return mBundle;
	}
	
	public int getPurpose() {
	    return mBundle.getInt("purpose");
	}
	
	public void setPurpose(int purpose) {
		mBundle.putInt("purpose", purpose);
	}
	
    public int getRecipient() {
        return mBundle.getInt("recipient");
    }

    public void setRecipient(int recipient) {
	    mBundle.putInt("recipient", recipient);
	}
	
    public int getPreferenceToUpdate() {
        return mBundle.getInt("preferenceToUpdate");
    }

    public void setPreferenceToUpdate(int preferenceId) {
	    mBundle.putInt("preferenceToUpdate", preferenceId);
	}
	
    public String getBody() {
        return mBundle.getString("body");
    }

    public void setBody(String body) {
	    mBundle.putString("body", body);
	}
	
}
