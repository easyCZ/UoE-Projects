package com.ug3.selp.timetableapp.models;

import java.util.List;

import android.text.format.Time;

public class Lecture {
	
	private Course course;
	private List<Integer> years;
	private Venue venue;
	private Time time;	// ???
	private int semester; 	// ???
	private Day day;
	private String comment;
	
	public Lecture(Course course, List<Integer> years, Venue venue, String comment) {
		this.course = course;
		this.years = years;
		this.venue = venue;
		if (!comment.isEmpty() || !comment.equals(null))
			this.comment = comment;
		else
			this.comment = "";
		
		this.time = new Time();
	}
	
	
	
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public List<Integer> getYears() {
		return years;
	}

	public void setYears(List<Integer> years) {
		this.years = years;
	}

	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

}
