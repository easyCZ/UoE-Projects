package com.ug3.selp.timetableapp;

import java.util.List;

import com.ug3.selp.timetableapp.adapter.DrawerArrayAdapter;
import com.ug3.selp.timetableapp.db.DatabaseHelper;
import com.ug3.selp.timetableapp.models.Course;
import com.ug3.selp.timetableapp.models.DrawerFilter;
import com.ug3.selp.timetableapp.service.AsyncDownloader;
import com.ug3.selp.timetableapp.service.Preferences;

import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements DrawerListener, OnClickListener{
	
	public static final String PREFS_NAME = "UoEInfTimetable";		
	private final String TAG = "MainActivity";
	
	private DrawerLayout drawer;
	private DrawerFilter activeYears = new DrawerFilter();
	private int progressField = R.id.downloadAndParseProgressValue;
	private int progressBar = R.id.downloadAndParseSpinner;
	private int downloadAndParseDesc = R.id.downloadAndParseDescSection;
	
	private Preferences preferences;
	
	private FragmentReceiver fragmentReceiver;
	
	private DatabaseHelper db;
	private DrawerArrayAdapter adapter;
	
	private final int[] drawerFilterIDs = new int[] {
    	R.id.filtersY1, R.id.filtersY2, R.id.filtersY3, R.id.filtersY4, R.id.filtersY5};
	private final int[] drawerFilterActiveRes = new int[] {
		R.drawable.ic_year_one_active, R.drawable.ic_year_two_active, 
		R.drawable.ic_year_three_active, R.drawable.ic_year_four_active,
		R.drawable.ic_year_five_active};
	private final int[] drawerFilterInactiveRes = new int[] {
			R.drawable.ic_year_one, R.drawable.ic_year_two, R.drawable.ic_year_three, 
			R.drawable.ic_year_four, R.drawable.ic_year_five};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentReceiver = new FragmentReceiver();
        this.registerReceiver(
        	fragmentReceiver, new IntentFilter(Resources.FRAGMENT_BUNDLE_KEY));
        
        if (findViewById(R.id.timetableFragment) != null) {
        	
        	if (savedInstanceState != null)
        		return;
        	
        	TimetableListFragment fragment = new TimetableListFragment();
        	
        	fragment.setArguments(getIntent().getExtras());
        	getSupportFragmentManager().beginTransaction()
        		.add(R.id.timetableFragment, fragment).commit();
        	
        }
        

        // Init preferences
        preferences = new Preferences(this.getApplicationContext(), PREFS_NAME, Context.MODE_PRIVATE);
        handlePreferences();
        
        // Init database
        db = new DatabaseHelper(getApplicationContext());

        setUpDrawer(R.id.drawer_layout);
        setupCourseFilters();
        
        final ListView listview = (ListView) findViewById(R.id.listview);
        final List<Course> courses = db.getCoursesFiltered(activeYears.getFilters());
        
        adapter = new DrawerArrayAdapter(this, R.layout.sidebar_drawer, courses);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
            	Course c = courses.get(position);
            	adapter.remove(c);
            	c.setTracked(!c.isTracked());
            	adapter.insert(c, position);
            	db.updateCourse(c);
            	ImageView img = (ImageView) view.findViewById(R.id.sidebarIcon);
            	if (c.isTracked()) {
            		img.setImageResource(android.R.color.transparent);
            		img.setBackgroundResource(R.drawable.checkbox_checked);
            	} else {
            		img.setImageResource(android.R.color.transparent);
            		img.setBackgroundResource(R.drawable.checkbox_unchecked);
            	}
            }

          });
        
        RelativeLayout dlAndParse = (RelativeLayout) findViewById(R.id.downloadAndParse);
        dlAndParse.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(TAG, "Starting AsyncDownloader");
				AsyncDownloader aDownloader = new AsyncDownloader(
					getApplicationContext(),
					preferences,
					(TextView) findViewById(progressField),
					(ProgressBar) findViewById(progressBar),
					(LinearLayout) findViewById(downloadAndParseDesc),
					(RelativeLayout) findViewById(R.id.downloadAndParse),
					adapter);
				try {
					aDownloader.execute(
							"http://www.inf.ed.ac.uk/teaching/courses/selp/xml/venues.xml",
							"http://www.inf.ed.ac.uk/teaching/courses/selp/xml/courses.xml",
							"http://www.inf.ed.ac.uk/teaching/courses/selp/xml/timetable.xml");
				} catch (Exception e) {
					// TODO: Show error message
					Log.w(TAG, "3 URLs are required for AsyncDownloader.");
					Toast.makeText(getApplicationContext(), "Three urls are required", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
    }

	@Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, DatabaseHelper.listToString(activeYears.getFilters()));
        if (preferences != null)
        	preferences.set("drawerFilters", activeYears.getFilters());
    }
    
    private void setUpDrawer(int drawerLayout) {
    	drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerListener(this);
        
        // Attach drawer listener to menu button
        ImageView optionsBtn = (ImageView) findViewById(R.id.optionsBtn);
        optionsBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (drawer.isDrawerOpen(Gravity.LEFT))
					drawer.closeDrawer(Gravity.LEFT);
				else
					drawer.openDrawer(Gravity.LEFT);
			}
		});
	}

	private void handlePreferences() {        
        if (!preferences.getBoolean("dataAvailable", false)) {
        	RelativeLayout layout = (RelativeLayout) findViewById(R.id.downloadAndParse);
        	layout.setVisibility(View.VISIBLE);
        }
        activeYears.setFilters(preferences.getFilters("drawerFilters"));
        
        for (int i = 0; i < drawerFilterIDs.length; i++) {
        	ImageView img = (ImageView) findViewById(drawerFilterIDs[i]);
        	String tag = (String) img.getTag();
        	if (activeYears.contains(tag)) {
        		img.setImageResource(android.R.color.transparent);
        		img.setBackgroundResource(drawerFilterActiveRes[i]);
    		} else {
    			img.setImageResource(android.R.color.transparent);
    			img.setBackgroundResource(drawerFilterInactiveRes[i]);
    		}
        }
        
	}

	private void setupCourseFilters() {
    	for (int i = 0; i < drawerFilterIDs.length; i++) {
    		ImageView iv = (ImageView) findViewById(drawerFilterIDs[i]);
    		iv.setOnClickListener(this);
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	// drawerFilter listener
	@Override
	public void onClick(View v) {
		ImageView img;
		try {
			img = (ImageView) v;
		} catch (Exception e) {
			return;
		}	
		String tag = (String) img.getTag();
		int tagInt = Integer.parseInt(tag.substring(1)) - 1;
		if (activeYears.contains(tag)) {
			setDrawerFilterResource(img, drawerFilterInactiveRes[tagInt], true);
		} else {
			setDrawerFilterResource(img, drawerFilterActiveRes[tagInt], false);
		}
		List<Course> newCourses = db.getCoursesFiltered(activeYears.getFilters());
		adapter.clear();
		for (Course c : newCourses)
			adapter.add(c);
	
	}
	
	private void applyImageResource(ImageView img, int resource) {
		img.setImageResource(android.R.color.transparent);
		img.setBackgroundResource(resource);
	}
	
	private void setDrawerFilterResource(ImageView img, int res, boolean remove) {
		applyImageResource(img, res);
		if (remove)
			activeYears.remove((String) img.getTag());
		else
			activeYears.add((String) img.getTag());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (db != null)
			db.close();
		this.unregisterReceiver(fragmentReceiver);
	}		
	
	@Override
	public void onDrawerClosed(View arg0) {}
	
	@Override
	public void onDrawerOpened(View arg0) {}
	
	@Override
	public void onDrawerStateChanged(int arg0) {}
	
	@Override
	public void onDrawerSlide(View arg0, float arg1) {}   
	
	public class FragmentReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {
			int id = intent.getIntExtra(Resources.FRAGMENT_ACTION_KEY, -1);
			if (id != -1) {
				String acr = intent.getStringExtra(Resources.FRAGMENT_ACRONYM_KEY);
				LectureDetailFragment fragment = new LectureDetailFragment();
				Bundle bundle = new Bundle();
				bundle.putString(Resources.FRAGMENT_ACRONYM_KEY, acr);
				fragment.setArguments(bundle);
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.timetableFragment, fragment);
				transaction.addToBackStack(null);
				transaction.commit();
			}
		}
		
	}
}
