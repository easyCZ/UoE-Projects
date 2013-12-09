package com.ug3.selp.timetableapp.models;

import java.util.ArrayList;
import java.util.List;

import com.ug3.selp.timetableapp.db.DatabaseHelper;

public class DrawerFilter {
	
	private List<String> tags = new ArrayList<String>();

	public void add(String tag) {
		tags.add(tag);
	}
	
	public void add(String tag, int position) {
		tags.add(position, tag);
	}
	
	public void remove(String tag) {
		if (contains(tag))
			tags.remove(tag);
	}
	
	public boolean contains(String tag) {
		return tags.contains(tag);
	}
	
	public String toString() {
		return DatabaseHelper.listToString(tags);
	}
	
	public void stringToList(String s) {
		this.tags = DatabaseHelper.stringToList(s);
	}
	
	public List<String> getFilters() {
		return tags;
	}
	
	public void setFilters(List<String> list) {
		tags = list;
	}
}
