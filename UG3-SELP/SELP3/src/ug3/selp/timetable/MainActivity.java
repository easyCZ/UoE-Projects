package ug3.selp.timetable;

import java.util.Calendar;

import ug3.selp.timetable.service.Preferences;
import ug3.selp.timetable.service.Resources;
import ug3.selp.timetable.tab_adapters.ViewPagerAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class MainActivity extends SherlockFragmentActivity {
	
	private final String TAG = "MainActivity";
	
	private ActionBar actionBar;
	private ViewPager viewPager;
	private Menu menu;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		// Setup view pager
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setOnPageChangeListener(onPageChangeListener);
		viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
		addActionBarTabs();
		
		// Set default active tab
		viewPager.setCurrentItem(getDayToday());
	}
	
	// Tab change listener
	private ViewPager.SimpleOnPageChangeListener onPageChangeListener = 
		new ViewPager.SimpleOnPageChangeListener() {
	        @Override
	        public void onPageSelected(int position) {
	            super.onPageSelected(position);
	            actionBar.setSelectedNavigationItem(position);
	        }
    };
    
    // Add action tabs in the action bar and attach listener to them
    private void addActionBarTabs() {
    	actionBar = getSupportActionBar();
        String[] tabs = { "MON", "TUE", "WED", "THU", "FRI" };
        for (String tabTitle : tabs) {
            ActionBar.Tab tab = actionBar.newTab().setText(tabTitle)
                    .setTabListener(tabListener);
            actionBar.addTab(tab);
        }
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }
    
    // Change the tab
    private ActionBar.TabListener tabListener = new ActionBar.TabListener() {
		
    	@Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            viewPager.setCurrentItem(tab.getPosition());
        }
 
        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
 
        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
	};
	
	

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {		
		MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    
	    this.menu = menu;
	    // If data available, hide the icon
	    setDownloadActionVisibility();
	    
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "Action bar click made.");
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    	case R.id.action_select:
	    		openSelect();
	    		return true;
	        case R.id.action_search:
	            openSearch();
	            return true;
	        case R.id.action_settings:
	            openSettings();
	            return true;
	        case R.id.action_download:
	        	openDownload();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	// Auxiliary to start the settings activity
	private void openSettings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);		
	}

	// Auxiliary to start the search activity
	private void openSearch() {
		Intent intent = new Intent(this, SearchActivity.class);
		startActivity(intent);
	}
	
	// Auxiliary to start the select course activity
	private void openSelect() {	
		Intent intent = new Intent(this, SelectActivity.class);
	    startActivity(intent);
	}

	// Auxiliary to start the download activity
	private void openDownload() {
		Intent intent = new Intent(this, DownloadActivity.class);
	    startActivity(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// Re check data available and hide download icon if appropriate
		setDownloadActionVisibility();
	}
	
	// Auxiliary to change visibility of the download icon in the action bar
	private void setDownloadActionVisibility() {
		Preferences prefs = new Preferences(
				getApplicationContext(), Resources.PREFERENCES_KEY, Context.MODE_PRIVATE);
		boolean isDataAvailable = prefs.getBoolean(Resources.PREFERENCES_DATA);
		if (menu == null)
			return;
		if (isDataAvailable && menu.size() > 0) {
			menu.getItem(0).setEnabled(false);
			menu.getItem(0).setVisible(false);
		}
	}
	
	// Auxiliary for setting the correct active tab.
	// Set Today as the active one unless it's Sun, Sat, then set Mon
	private int getDayToday() {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK); 
		if (day != 1 || day != 7)
			return day -2;
		else return 0;
	}

}
