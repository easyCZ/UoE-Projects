package ug3.selp.timetable;

import java.util.Calendar;

import ug3.selp.timetable.service.Preferences;
import ug3.selp.timetable.service.Resources;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;


public class SettingsActivity extends SherlockActivity {
	
	private Preferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
		
		// Add Up button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Initialise preferences
		preferences = new Preferences(
			getApplicationContext(), Resources.PREFERENCES_KEY, Context.MODE_PRIVATE);
		
		// Get semester settings row
		final View settingsContainer = findViewById(R.id.settingsSemesterContainer);
		
		// Set switch icon based on preferences semester
		// Default is semester 1 in the style
		final ImageView icon = (ImageView) findViewById(R.id.settingsSwitchIcon);
		String semester  = _getSemester();
		if (semester.equals(Resources.PREFERENCES_SEMESTER_2)) {
			icon.setImageResource(android.R.color.transparent);
			icon.setBackgroundResource(R.drawable.s2_active);
			icon.setTag(new String("s2_active"));
		}
				
		// Attach on click listener 
		settingsContainer.setOnClickListener(changeSemesterHandler);
		
		// Set last download field
		TextView lastDL = (TextView) findViewById(R.id.settingsLastDownload);
		String date = preferences.get(Resources.PREFERENCES_DATA_DATE_DOWNLOADED);
		lastDL.setText((!date.equals("")) ? date : "Never");
		
	}
	
	View.OnClickListener changeSemesterHandler = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			ImageView switchIcon = (ImageView) findViewById(R.id.settingsSwitchIcon);
			String tag = (String) switchIcon.getTag();
			if (tag.equals("s1_active")) {
				// switch to s2
				switchIcon.setImageResource(android.R.color.transparent);
				switchIcon.setBackgroundResource(R.drawable.s2_active);
				switchIcon.setTag(new String("s2_active"));
				preferences.set(
					Resources.PREFERENCES_SEMESTER_KEY, Resources.PREFERENCES_SEMESTER_2);
			} else {
				switchIcon.setImageResource(android.R.color.transparent);
				switchIcon.setBackgroundResource(R.drawable.s1_active);
				switchIcon.setTag(new String("s1_active"));
				preferences.set(
						Resources.PREFERENCES_SEMESTER_KEY, Resources.PREFERENCES_SEMESTER_1);
			}
			
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.settings, menu);
	    
	    return super.onCreateOptionsMenu(menu);
	}
	
	private String _getSemester() {
        String sem = preferences.get(Resources.PREFERENCES_SEMESTER_KEY);
        // Determine the right semester 
        // sem is empty if it has not yet been set by another Activity
        if (sem.equals("")) {
        	Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH) + 1;
            if (month >= 7 && month <= 12) {        // semester 1
            	preferences.set(
            			Resources.PREFERENCES_SEMESTER_KEY,
                        Resources.PREFERENCES_SEMESTER_1);
            	return Resources.PREFERENCES_SEMESTER_1;
            
            } else if (month >= 1 && month <= 6) {        // semester 2
            	preferences.set(
            			Resources.PREFERENCES_SEMESTER_KEY,
                        Resources.PREFERENCES_SEMESTER_2);
            	return Resources.PREFERENCES_SEMESTER_2;
            }
        }
        return sem;
	}

}
