package ug3.selp.timetable.tab_adapters;

import ug3.selp.timetable.fragments.DayFragment;
import ug3.selp.timetable.service.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
	
	private final int PAGES = 5;
	private final String TAG = "ViewPagerAdapter";
	
	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getCount() {
		return PAGES;
	}

	@Override
	public Fragment getItem(int position) {
		DayFragment day = new DayFragment();
    	Bundle bundle = new Bundle();
    	// Attach the correct day to the fragment for future retriaval in the fragment
		switch (position) {
        case 0:
        	bundle.putString(Resources.BUNDLE_DAY_KEY, Resources.BUNDLE_DAY_MONDAY);
        	day.setArguments(bundle);
            return day;
        case 1:
        	bundle.putString(Resources.BUNDLE_DAY_KEY, Resources.BUNDLE_DAY_TUESDAY);
        	day.setArguments(bundle);
            return day;
        case 2:
        	bundle.putString(Resources.BUNDLE_DAY_KEY, Resources.BUNDLE_DAY_WEDNESDAY);
        	day.setArguments(bundle);
            return day;
        case 3:
        	bundle.putString(Resources.BUNDLE_DAY_KEY, Resources.BUNDLE_DAY_THURSDAY);
        	day.setArguments(bundle);
            return day;
        case 4:
        	bundle.putString(Resources.BUNDLE_DAY_KEY, Resources.BUNDLE_DAY_FRIDAY);
        	day.setArguments(bundle);
            return day;
        default:
            Log.d(TAG, "Unavailable position.");
            return null;
		}
	}

}
