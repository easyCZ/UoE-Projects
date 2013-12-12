package com.ug3.selp.timetableapp;

import com.ug3.selp.timetableapp.service.Preferences;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.RadioButton;

public class SettingsActivity extends Activity {
	
	private Preferences preferences;
	private String semester;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		// Init preferences
		preferences = new Preferences(
			getApplicationContext(), 
			Resources.PREFERENCES_KEY, Context.MODE_PRIVATE);
		// Set active radio
		_setDefaultRadioButton();
	}
	
	// Auxiliary - set semester based on preferences
	private void _setDefaultRadioButton() {
		semester = preferences.get(Resources.PREFERENCES_SEMESTER_KEY);
		if (semester.equals("S1"))
			((RadioButton) findViewById(R.id.settingsSemester1Radio)).toggle();
		else if (semester.equals("S2"))
			((RadioButton) findViewById(R.id.settingsSemester2Radio)).toggle();
		else
			((RadioButton) findViewById(R.id.settingsSemesterYRRadio)).toggle();
	}
	
	// On radio change handler, updates preferences in the rest of the app
	public void onRadioButtonClicked(View view) {
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.settingsSemester1Radio:
	            if (checked) {
	            	preferences.set(
	            		Resources.PREFERENCES_SEMESTER_KEY, 
	            		Resources.PREFERENCES_SEMESTER_1);
	            }
	            break;
	        case R.id.settingsSemester2Radio:
	            if (checked) {
	            	preferences.set(
		            	Resources.PREFERENCES_SEMESTER_KEY, 
		            	Resources.PREFERENCES_SEMESTER_2);
	            }
	            break;
	        case R.id.settingsSemesterYRRadio:
	        	if (checked) {
	        		preferences.set(
	        			Resources.PREFERENCES_SEMESTER_KEY,
	        			Resources.PREFERENCES_SEMESTER_YR);
	        	}
	    }
	}

}
