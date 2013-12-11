package com.ug3.selp.timetableapp;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.ug3.selp.timetableapp.adapter.TimetableArrayAdapter;
import com.ug3.selp.timetableapp.db.DatabaseHelper;
import com.ug3.selp.timetableapp.models.Lecture;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TimetableListFragment extends Fragment {
	
	private final String TAG = "TimetableListFragment";
	public static final String PREFS_NAME = "UoEInfTimetable";
	
	private TimetableArrayAdapter timetableAdapter;
	private int adapterResource = R.layout.timetable_row;
	private DatabaseHelper db;
	private List<Lecture> lectures;
	private ListView listView;
	private TextView activeTab;
	private Context context;
	private View view;
	private String dayName = "";
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = getActivity();
        view = inflater.inflate(R.layout.timetable_fragment, container, false);
        _setDefaultDay();
        
		db = new DatabaseHelper(context);
        lectures = db.getLecturesByDay(dayName);
		timetableAdapter = new TimetableArrayAdapter(
			context, adapterResource, lectures);
		listView = (ListView) view.findViewById(R.id.timetable_list);
		listView.setAdapter(timetableAdapter);
		
		_attachTabListener();		
		db.close();
		// Inflate the layout for this fragment
        return view;
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		db.close();
	}
	
	private void _attachTabListener() {
		LinearLayout container = (LinearLayout) view.findViewById(R.id.timetableTabsContainer);
		for (int i = 0; i < container.getChildCount(); i++) {
			TextView tv = (TextView) container.getChildAt(i);
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
						dayName = (String) t.getTag();
						
						timetableAdapter.clear();						
						for (Lecture l: lectures)
							timetableAdapter.add(l);
					}
				}
			});
		}
	}

	private void _setDefaultDay() {
		Calendar calendar = Calendar.getInstance();
		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
		dayName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
		
		if (weekDay != 1 || weekDay != 7)
			activeTab = (TextView) (
				(LinearLayout) view.findViewById(R.id.timetableTabsContainer))
										.getChildAt(weekDay-2);
        else
        	activeTab = (TextView) view.findViewById(R.id.timetableTabsMonday);
		activeTab.setBackgroundColor(getResources().getColor(R.color.timeGreen)); 
	}

}
