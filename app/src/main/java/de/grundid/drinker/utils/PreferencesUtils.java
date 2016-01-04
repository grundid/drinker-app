package de.grundid.drinker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.UUID;

public class PreferencesUtils {

	public static final String KEY_RESET_NAME = "reset-name";
	public static final String KEY_RESET_BRAND = "reset-brand";
	private static final String KEY_INTERNAL_UUID = "internal_uuid";

	public static String getUuid(Context context) {
		String uuid = getPreference(context, KEY_INTERNAL_UUID, null);
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
			setPreference(context, KEY_INTERNAL_UUID, uuid);
		}
		return uuid;
	}

	public static String getPreference(Context context, String preference, String defaultValue) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences.getString(preference, defaultValue);
	}

	public static void setPreference(Context context, String preference, String value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(preference, value);
		editor.commit();
	}

	public static boolean getBoolPreference(Context context, String preference, boolean defaultValue) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences.getBoolean(preference, defaultValue);
	}

	public static void setBoolPreference(Context context, String preference, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(preference, value);
		editor.commit();
	}


}
