package com.ug3.selp.timetableapp;

import java.util.Calendar;
import java.util.List;

import com.ug3.selp.timetableapp.adapter.DrawerArrayAdapter;
import com.ug3.selp.timetableapp.db.DatabaseHelper;
import com.ug3.selp.timetableapp.models.Course;
import com.ug3.selp.timetableapp.service.AsyncDownloader;
import com.ug3.selp.timetableapp.service.Preferences;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author s1115104
 *
 * Main starting point of the application. Widgets are set up and listeners 
 * attached. The main view has a drawer with a list view attached to it.
 * It also contains the fragment manager which handles swapping fragments in and out.
 * 
 * Sword required. Here be dragons.
 */
public class MainActivity extends FragmentActivity implements DrawerListener, OnClickListener{
			
	private static final String THREE_URLS_ARE_REQUIRED = "Three urls are required";

	private final String TAG = "MainActivity";
	
	private DrawerLayout drawer;
	private ImageView activeDrawerTag;
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
        
        // Init preferences
        preferences = new Preferences(
        		this.getApplicationContext(), 
        		Resources.PREFERENCES_KEY, Context.MODE_PRIVATE);
        handlePreferences();
        _getSemester();
        
        // Register Fragments
        fragmentReceiver = new FragmentReceiver();
        this.registerReceiver(
        	fragmentReceiver, new IntentFilter(Resources.FRAGMENT_BUNDLE_KEY));
        if (findViewById(R.id.timetableFragment) != null) {
        	if (savedInstanceState != null)
        		return;
        	// Attach default fragment
        	TimetableListFragment fragment = new TimetableListFragment();
        	fragment.setArguments(getIntent().getExtras());
        	getSupportFragmentManager().beginTransaction()
        		.add(R.id.timetableFragment, fragment, "FRAGMENT_LIST").commit();
        }
        
        // Set Y1 as default filter
        activeDrawerTag = (ImageView) findViewById(R.id.filtersY1);
        activeDrawerTag.setImageResource(android.R.color.transparent);
        activeDrawerTag.setBackgroundResource(drawerFilterActiveRes[0]);
        
        // Init database
        db = new DatabaseHelper(getApplicationContext());

        // Init sidebar drawer
        setUpDrawer(R.id.drawer_layout);
        setupCourseFilters();
        
        // Setup searching
        _handleSearchField();
        
        // Populate sidebar
        final ListView listview = (ListView) findViewById(R.id.listview);
        final List<Course> courses = db.getCoursesFiltered("Y1");
        
        // Set sidebar & listener
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
            	// Notify about the change
            	Intent intent = new Intent(Resources.FRAGMENT_BUNDLE_KEY);
        		intent.putExtra(
        			Resources.FRAGMENT_ACTION_KEY, Resources.FRAGMENT_ACTION_LIST_UPDATE);
        		getApplication().sendBroadcast(intent);
        		db.close();
            }

          });
        
        // Attach Download & Parse if data not available
        // Attach Listener
        RelativeLayout dlAndParse = (RelativeLayout) findViewById(R.id.downloadAndParse);
        dlAndParse.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!isNetworkAvailable()) {
					Toast.makeText(getApplicationContext(), "No internet connection. Try again later.", Toast.LENGTH_LONG).show();
					return;
				}
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
					Log.w(TAG, "3 URLs are required for AsyncDownloader.");
					Toast.makeText(getApplicationContext(), THREE_URLS_ARE_REQUIRED, Toast.LENGTH_SHORT).show();
				}
				
			}
		});
        
        // Attach Options onclick
        ImageView settingsIcon = (ImageView) findViewById(R.id.settingsBtn);
        settingsIcon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
				startActivity(intent);
			}
		});
    }
    
    // Test for connectivity
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Attach listeners to Search field
	private void _handleSearchField() {
		final ImageView searchButton = (ImageView) findViewById(R.id.searchBtn);
		final EditText search = (EditText) findViewById(R.id.searchField);
		
		search.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 0)
					doSearchIntent(s.toString());
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {}
		});
		searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Hide || Show search field
				if (search.getVisibility() == View.GONE) {
					search.setVisibility(View.VISIBLE);
					switchToSearchFragment();
				} else 
					hideView(search);
			}
		});
	}
	
	// Auxiliary helper to hide a View 
	private void hideView(View v) {
		v.setVisibility(View.GONE);
	}
	
	// Auxiliary - Hide View by resource ID
	private void hideView(int id) {
		findViewById(id).setVisibility(View.GONE);
	}
	
	// Handler for when search fragment is requested
	private void switchToSearchFragment() {
		SearchFragment fragment = new SearchFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.timetableFragment, fragment, "FRAGMENT_SEARCH");
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	// Pass data onto the Fragment's listener to update state
	private void doSearchIntent(String query) {
		Intent intent = new Intent(Resources.FRAGMENT_BUNDLE_KEY);
		intent.putExtra(Resources.FRAGMENT_SEARCH_QUERY, query);
		intent.putExtra(Resources.FRAGMENT_SEARCH_KEY, Resources.FRAGMENT_SEARCH_ACTION);
		getApplication().sendBroadcast(intent);
	}

	// Set up sidebar drawer, including listeners
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

    // Check if XML data is available, else show button to Downlload
	private void handlePreferences() {        
        if (!preferences.getBoolean("dataAvailable", false)) {
        	RelativeLayout layout = (RelativeLayout) findViewById(R.id.downloadAndParse);
        	layout.setVisibility(View.VISIBLE);
        }        
	}

	// Attach listeners to all 5 course filter "buttons"
	private void setupCourseFilters() {
    	for (int i = 0; i < drawerFilterIDs.length; i++) {
    		ImageView iv = (ImageView) findViewById(drawerFilterIDs[i]);
    		iv.setOnClickListener(this);
    	}
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
		
		String oldTag = (String) activeDrawerTag.getTag();
		int oldPos = Integer.parseInt(oldTag.substring(1)) -1;
		applyImageResource(activeDrawerTag, drawerFilterInactiveRes[oldPos]);
		
		String newTag = (String) img.getTag();
		// TAG in the format of "Sx", get x out
		int newPos = Integer.parseInt(newTag.substring(1)) - 1;
		// Change background & update reference
		applyImageResource(img, drawerFilterActiveRes[newPos]);
		activeDrawerTag = img;
		// Re-populate the list view
		List<Course> newCourses = db.getCoursesFiltered(newTag);
		adapter.clear();
		for (Course c : newCourses)
			adapter.add(c);
	
	}
	
	// Auxiliary - set background image
	private void applyImageResource(ImageView img, int resource) {
		img.setImageResource(android.R.color.transparent);
		img.setBackgroundResource(resource);
	}
	
	// Release resources
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (db != null)
			db.close();
		this.unregisterReceiver(fragmentReceiver);
	}		
	
	// Get current semester based on either settings or set preferences
	private void _getSemester() {
		String sem = preferences.get(Resources.PREFERENCES_SEMESTER_KEY);
		if (sem.equals("")) {
			Calendar calendar = Calendar.getInstance();
			int month = calendar.get(Calendar.MONTH) + 1;
			if (month >= 7 && month <= 12)	// semester 1
				preferences.set(
					Resources.PREFERENCES_SEMESTER_KEY,
					Resources.PREFERENCES_SEMESTER_1);
			else if (month >= 1 && month <= 6)	// semester 2
				preferences.set(
						Resources.PREFERENCES_SEMESTER_KEY,
						Resources.PREFERENCES_SEMESTER_2);
			Log.d(TAG, "Semester set.");
		}
	}
	
	// Update and re-draw semester on resume
	@Override
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    _getSemester();
	}
	
	@Override
	public void onDrawerClosed(View arg0) {}
	
	@Override
	public void onDrawerOpened(View arg0) {}
	
	@Override
	public void onDrawerStateChanged(int arg0) {}
	
	@Override
	public void onDrawerSlide(View arg0, float arg1) {}  
	
	// Hide search bar if visible
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (findViewById(R.id.searchField).getVisibility() == View.GONE)
			return super.onKeyDown(keyCode, event);
		else 
			findViewById(R.id.searchField).setVisibility(View.GONE);
		return false;	    
	}
	
	// Custom Broadcast receiver to catch broadcasts from fragments
	public class FragmentReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int id = intent.getIntExtra(Resources.FRAGMENT_ACTION_KEY, -1);
			if (id != -1) {
				if (id == Resources.FRAGMENT_ACTION_DETAIL) {	// Request detail view
					String acr = intent.getStringExtra(Resources.FRAGMENT_ACRONYM_KEY);
					LectureDetailFragment fragment = new LectureDetailFragment();
					Bundle bundle = new Bundle();
					bundle.putString(Resources.FRAGMENT_ACRONYM_KEY, acr);
					fragment.setArguments(bundle);
					FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
					transaction.replace(R.id.timetableFragment, fragment, "FRAGMENT_DETAIL");
					transaction.addToBackStack(null);
					transaction.commit();
					hideView(R.id.searchField);
				} else if (id == Resources.FRAGMENT_ACTION_LIST_UPDATE) {
					Fragment frag = getSupportFragmentManager()
							.findFragmentByTag("FRAGMENT_LIST");
					FragmentTransaction transaction = getSupportFragmentManager().
							beginTransaction();
					// Remove and Re-attach seems to be one of the ways to 
					// refresh a fragment
					transaction.detach(frag);
					transaction.attach(frag);
//					transaction.addToBackStack(null);
					transaction.commit();
					hideView(R.id.searchField);
				}
				
			}
		}
		
	}
}
