package com.ug3.selp.timetableapp.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.ug3.selp.timetableapp.Resources;
import com.ug3.selp.timetableapp.db.DatabaseHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Preferences {
	
	private final String TAG = "Preferences";
	
	private SharedPreferences preferences;
	
	public Preferences(Context context, String prefsName, int mode) {
		preferences = context.getSharedPreferences(prefsName, mode);
		Log.d(TAG, "Preferences initialized.");
	}
	
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
	
	public boolean set(String key, String value) {
		Log.d(TAG, key + " - " + value);
		try {
			Editor editor = preferences.edit();
			editor.putString(key, value);
			editor.commit();
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public String get(String key) {
		Log.d(TAG, key + " - " + preferences.getString(key, ""));
		return preferences.getString(key, "");
	}
	
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
	
	public List<String> getFilters(String name) {
		List<String> values = new ArrayList<String>();
		String s = preferences.getString(name, "");
		if (!s.equals(""))
			values = DatabaseHelper.stringToList(s);
		return values;	
	}
	
	public int getDay() {
		int s = preferences.getInt(Resources.PREFERENCES_DAY, -1);
		if (s != -1)
			return s;
		return getToday();
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
	
	private int getToday() {
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		if (day != 1 || day != 7)
			return day -1;
		return 0;
	}
	
	public boolean getBoolean(String name, boolean def) {
		return preferences.getBoolean(name, def);
	}
	
	public SharedPreferences getPreferences() {
		return preferences;
	}

}
