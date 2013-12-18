package ug3.selp.timetable.service;

import java.util.ArrayList;
import java.util.List;
import ug3.selp.timetable.db.DatabaseHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

// Class wrapper for SharedPreferences
public class Preferences {

	private final String TAG = "Preferences";

	private SharedPreferences preferences;

	public Preferences(Context context, String prefsName, int mode) {
		preferences = context.getSharedPreferences(prefsName, mode);
		Log.d(TAG, "Preferences init.");
	}

	// Setter
	public boolean set(String name, boolean value) {
		try {
			Editor editor = preferences.edit();
			editor.putBoolean(name, value);
			editor.commit();
		} catch(Exception e) {
			return false;
		}
		return true;
	}

	// Setter
	public boolean set(String key, String value) {
		//		Log.d(TAG, key + " - " + value);
		try {
			Editor editor = preferences.edit();
			editor.putString(key, value);
			editor.commit();
		} catch(Exception e) {
			return false;
		}
		return true;
	}

	// Getter
	public String get(String key) {
		//		Log.d(TAG, key + " - " + preferences.getString(key, ""));
		return preferences.getString(key, "");
	}

	// Getter
	public boolean getBoolean(String key) {
		return preferences.getBoolean(key, false);
	}

	// Setter
	public boolean set(String name, List<String> values) {
		String value = DatabaseHelper.listToString(values);
		try {
			Editor editor = preferences.edit();
			editor.putString(name, value);
			editor.commit();
		} catch(Exception e) {
			return false;
		}
		return true;
	}

	// Getter
	public List<String> getFilters(String name) {
		List<String> values = new ArrayList<String>();
		String s = preferences.getString(name, "");
		if (!s.equals(""))
			values = DatabaseHelper.stringToList(s);
		return values;	
	}

	public boolean setDay(int day) {
		try {
			Editor editor = preferences.edit();
			editor.putInt(Resources.PREFERENCES_DAY, day);
			editor.commit();
		} catch(Exception e) {
			return false;
		}
		return true;
	}

	public boolean getBoolean(String name, boolean def) {
		return preferences.getBoolean(name, def);
	}

	public SharedPreferences getPreferences() {
		return preferences;
	}

}
