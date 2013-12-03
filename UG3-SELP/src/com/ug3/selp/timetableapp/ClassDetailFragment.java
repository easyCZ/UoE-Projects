package com.ug3.selp.timetableapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ClassDetailFragment extends Fragment {
	
	public View onCreate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_class_detail_fragment, container, false);
		return view;
	}
	
	public void setText(String item) {
		TextView view = (TextView) getView().findViewById(R.id.detailsText);
		view.setText(item);
	}

}
