package com.ug3.selp.timetableapp.service;

import java.util.ArrayList;
import java.util.List;

import com.ug3.selp.timetableapp.db.DatabaseHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preferences {
	
	private SharedPreferences preferences;
	
	public Preferences(Context context, String prefsName, int mode) {
		preferences = context.getSharedPreferences(prefsName, mode);
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
	
	public boolean getBoolean(String name, boolean def) {
		return preferences.getBoolean(name, def);
	}
	
	public SharedPreferences getPreferences() {
		return preferences;
	}

}
