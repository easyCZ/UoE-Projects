package ug3.selp.timetable;

import ug3.selp.timetable.tab_adapters.SelectPagerAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;

public class SelectActivity extends SherlockFragmentActivity {
	
	private ViewPager tabs;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_activity);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Initialize tabs
		tabs = (ViewPager) findViewById(R.id.selectPager);
		tabs.setOnPageChangeListener(onPageChangeListener);
		tabs.setAdapter(new SelectPagerAdapter(getSupportFragmentManager()));
		addActionBarTabs();
		
		
	}
	
	// Add action tabs into the action bar
	private void addActionBarTabs() {
		actionBar = getSupportActionBar();
        String[] tabs = { "Year 1", "Year 2", "Year 3", "Year 4", "Year 5" };
        for (String tabTitle : tabs) {
            ActionBar.Tab tab = actionBar.newTab().setText(tabTitle)
                    .setTabListener(tabListener);
            actionBar.addTab(tab);
        }
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		
	}
	
	// Listener for action bar events
	private ActionBar.TabListener tabListener = new ActionBar.TabListener() {
		
    	@Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            tabs.setCurrentItem(tab.getPosition());
        }
 
        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
 
        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.select, menu);
	    
	    return super.onCreateOptionsMenu(menu);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
