package com.iapps.ichat.helper;

import android.content.Context;
import android.content.SharedPreferences;


import com.iapps.ichat.model.BeanLog;
import com.iapps.ichat.model.BeanServiceConfig;

import java.util.ArrayList;

public class Preference {

	private final String PREF_NAME = "isanpref";
	private final String PREF_NAME_NO_CLEAR = "isanpref_noclear";
	private final String IS_FIRST_LOAD = "isFirstLoad";

	// Info
	private final String INFO_VERSION = "infoversion";
	private final String INFO_DATA = "infodata";

    //NOTIFICATION
    private final String NOTIFICATION_SAVED = "notification_saved";

    //LOG
    private final String LOG_SAVED = "log_saved";

	//CONFIG
	private final String CONFIG_SAVED = "config_saved";

	private Context context;
	private static Preference pref;

	public static Preference getInstance(Context context) {
		if (pref == null) {
			pref = new Preference(context);
		}

		return pref;
	}

	private Preference(Context context) {
		this.context = context;
	}

	private SharedPreferences getPreference() {
		return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
	}
	
	public SharedPreferences getPreferenceNoClear() {
		return context.getSharedPreferences(PREF_NAME_NO_CLEAR, Context.MODE_PRIVATE);
	}

	private SharedPreferences.Editor editPreference() {
		return getPreference().edit();
	}


	public void setFirstAppLoad(boolean isFirstAppLoad) {
		//editPreference().putBoolean(IS_FIRST_LOAD, isFirstAppLoad).commit();
		getPreferenceNoClear().edit().putBoolean(IS_FIRST_LOAD, isFirstAppLoad).commit();
	}

	public boolean isFirstAppLoad() {
		//return getPreference().getBoolean(IS_FIRST_LOAD, true);
		return getPreferenceNoClear().getBoolean(IS_FIRST_LOAD, true);
	}

	public int getVersion() {
		return getPreference().getInt(INFO_VERSION, 0);
	}

	public String getInfo() {
		return getPreference().getString(INFO_DATA, "");
	}

	public void setVersion(int version) {
		editPreference().putInt(INFO_VERSION, version).commit();
	}

	public void setInfo(String infoData) {
		editPreference().putString(INFO_DATA, infoData).commit();
	}

	public void clear() {
		editPreference().clear().commit();
	}


	public void saveLogTrackerSaved(ArrayList<BeanLog> nsaved) {
		try {
			getPreferenceNoClear().edit().putString(LOG_SAVED, com.iapps.ichat.helper.ObjectSerializer.serialize(nsaved)).commit();
		} catch (Exception e) {}
	}

	public ArrayList<BeanLog> getLogTrackerSaved() {
		try {
			String test = getPreferenceNoClear().getString(LOG_SAVED, null);
			return ((ArrayList<BeanLog>) com.iapps.ichat.helper.ObjectSerializer.deserialize(test));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void saveConfig(ArrayList<BeanServiceConfig> nsaved) {
		try {
			getPreferenceNoClear().edit().putString(CONFIG_SAVED, com.iapps.ichat.helper.ObjectSerializer.serialize(nsaved)).commit();
		} catch (Exception e) {}
	}

	public ArrayList<BeanServiceConfig> getConfig() {
		try {
			String test = getPreferenceNoClear().getString(CONFIG_SAVED, null);
			return ((ArrayList<BeanServiceConfig>) com.iapps.ichat.helper.ObjectSerializer.deserialize(test));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
