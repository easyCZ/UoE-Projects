package com.ug3.selp.timetableapp;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	private final String TAG = "MainActivity";
	private String[] mPlanetTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mPlanetTitles = new String[] {"Earth", "Mars"};
        List<String> mPlanetTitlesList = new ArrayList<String>();
        mPlanetTitlesList.addAll(mPlanetTitlesList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
     // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, mPlanetTitlesList));
        // Set the list's click listener
        
        
        
        ImageView optionsMenu = (ImageView) findViewById(R.id.optionsBtn);
        optionsMenu.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundColor(0x00000000);	// Transparent
					Log.i(TAG, "OptionsMenu released");
					return true;
				} else {
					v.setBackgroundResource(R.color.titleBarOnClick);
					Log.i(TAG, "OptionsMenu clicked");
				}
				return false;
			}
		});
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
