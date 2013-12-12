package com.ug3.selp.timetableapp;

import java.util.List;
import com.ug3.selp.timetableapp.adapter.TimetableArrayAdapter;
import com.ug3.selp.timetableapp.db.DatabaseHelper;
import com.ug3.selp.timetableapp.models.Lecture;
import com.ug3.selp.timetableapp.service.Preferences;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author s1115104
 *
 * Fragment responsible for the "Agenda" like view. Contains tabs and 
 * updates on tab click.
 */
public class TimetableListFragment extends Fragment {
	
	private final String TAG = "TimetableListFragment";
	
	private TimetableArrayAdapter timetableAdapter;
	private int adapterResource = R.layout.timetable_row;
	private DatabaseHelper db;
	private List<Lecture> lectures;
	private ListView listView;
	private TextView activeTab;
	private Context context;
	private View view;
	private String dayName = "";
	private Preferences preferences;
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = getActivity();
        // Init preferences
        preferences = new Preferences(
        		getActivity(), Resources.PREFERENCES_KEY, Context.MODE_PRIVATE);
        view = inflater.inflate(R.layout.timetable_fragment, container, false);
        
        // Assign default day, either today or Monday if it's Sat || Sun
        _setDefaultDay();
        
        // Db init
		db = new DatabaseHelper(context);
        // Given today, find lectures
		lectures = db.getLecturesByDay(
        	dayName, preferences.get(Resources.PREFERENCES_SEMESTER_KEY));
		// Attach data to adapter
		timetableAdapter = new TimetableArrayAdapter(
			context, adapterResource, lectures);
		listView = (ListView) view.findViewById(R.id.timetable_list);
		listView.setAdapter(timetableAdapter);
		
		_attachTabListener();		
		db.close();
		
		Log.d(TAG, "Fragment inflated and showing.");
		// Inflate the layout for this fragment
        return view;
    }
	
	
	// Release resources
	@Override
	public void onDestroy() {
		super.onDestroy();
		db.close();
		Log.d(TAG, "Fragment destroyed. Leaving.");
	}
	
	// Update current tab view. Preferences might have changed.
	@Override
	public void onResume() {
		super.onResume();
		_setDefaultDay();
		db = new DatabaseHelper(context);
        lectures = db.getLecturesByDay(
        	dayName, preferences.get(Resources.PREFERENCES_SEMESTER_KEY));
        timetableAdapter.clear();
        for (Lecture l: lectures)
        	timetableAdapter.add(l);
	}
	
	// Attach a listener to the tabs
	private void _attachTabListener() {
		// container should have 5 children, one for each day
		LinearLayout container = (LinearLayout) view.findViewById(R.id.timetableTabsContainer);
		for (int i = 0; i < container.getChildCount(); i++) {
			TextView tv = (TextView) container.getChildAt(i);
			final Integer intFinal = Integer.valueOf(i);
			tv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {				
					
					
					TextView t = (TextView) v;
					if (!activeTab.equals(t)) {
						// Reset background
						activeTab.setBackgroundColor(getResources().getColor(android.R.color.transparent));
						v.setBackgroundColor(getResources().getColor(R.color.timeGreen));
						// Update reference to the current
						activeTab = (TextView) v;
						// Tag is used as day name holder
						dayName = (String) t.getTag();
						lectures = db.getLecturesByDay(
							dayName, 
							preferences.get(Resources.PREFERENCES_SEMESTER_KEY));
						
						// Re-attach data
						timetableAdapter.clear();						
						for (Lecture l: lectures)
							timetableAdapter.add(l);
						// Release db.
						db.close();
						//Update preferences (.commit())
						preferences.setDay(intFinal+1);
					}
				}
			});
		}
	}

	// Auxiliary to set day
	// If preferences no set, today is picked.
	private void _setDefaultDay() {
		int weekDay = preferences.getDay();
		
		String[] days = new String[] {
			"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
		
		dayName = days[weekDay - 1];
		// Highlight the correct tab - Today, if it's Sat/Sun then Monday
		if (weekDay != 1 || weekDay != 7)
			activeTab = (TextView) (
				(LinearLayout) view.findViewById(R.id.timetableTabsContainer))
										.getChildAt(weekDay-1);
        else
        	activeTab = (TextView) view.findViewById(R.id.timetableTabsMonday);
		activeTab.setBackgroundColor(getResources().getColor(R.color.timeGreen)); 
	}

}
