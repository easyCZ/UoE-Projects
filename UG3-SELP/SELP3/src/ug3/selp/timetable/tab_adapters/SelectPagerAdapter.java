package ug3.selp.timetable.tab_adapters;

import ug3.selp.timetable.fragments.YearFragment;
import ug3.selp.timetable.service.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class SelectPagerAdapter extends FragmentStatePagerAdapter {
	
	private final int PAGES = 5;
	private final String TAG = "SelectPagerAdapter";
	
	public SelectPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getCount() {
		return PAGES;
	}

	@Override
	public Fragment getItem(int position) {
		YearFragment fragment = new YearFragment();
		Bundle bundle = new Bundle();
		// Attach the correct year to the fragment for later retrival inside the fragment
		switch (position) {
        case 0:
        	bundle.putString(Resources.BUNDLE_YEAR_KEY, Resources.BUNDLE_YEAR_1);
        	fragment.setArguments(bundle);
            return fragment;
        case 1:
        	bundle.putString(Resources.BUNDLE_YEAR_KEY, Resources.BUNDLE_YEAR_2);
        	fragment.setArguments(bundle);
            return fragment;
        case 2:
        	bundle.putString(Resources.BUNDLE_YEAR_KEY, Resources.BUNDLE_YEAR_3);
        	fragment.setArguments(bundle);
            return fragment;
        case 3:
        	bundle.putString(Resources.BUNDLE_YEAR_KEY, Resources.BUNDLE_YEAR_4);
        	fragment.setArguments(bundle);
            return fragment;
        case 4:
        	bundle.putString(Resources.BUNDLE_YEAR_KEY, Resources.BUNDLE_YEAR_5);
        	fragment.setArguments(bundle);
            return fragment;
        default:
            Log.d(TAG, "Unavailable position.");
            return null;
		}
	}


}
