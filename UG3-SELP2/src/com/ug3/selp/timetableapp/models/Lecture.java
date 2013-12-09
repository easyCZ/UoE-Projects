package com.ug3.selp.timetableapp.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.ug3.selp.timetableapp.db.DatabaseHelper;


public class Lecture {
	
	private String course;
	private List<Integer> years;
	private VenueSimple venue;
	private String comment;
	private String day;
	private TimeSlot time;
	private String semester;
	
	public Lecture() {
		this.course = this.comment = this.setDay(this.comment = this.setSemester(""));
		this.years = new ArrayList<Integer>();
		this.setVenue(new VenueSimple());
	}
		
	
	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public List<Integer> getYears() {
		return years;
	}

	public void setYears(List<Integer> years) {
		this.years = years;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public TimeSlot getTime() {
		return time;
	}

	public void setTime(TimeSlot time) {
		this.time = time;
	}


	public String getSemester() {
		return semester;
	}


	public String setSemester(String semester) {
		this.semester = semester;
		return semester;
	}

	public String getDay() {
		return day;
	}


	public String setDay(String day) {
		this.day = day;
		return day;
	}


	public VenueSimple getVenue() {
		return venue;
	}


	public void setVenue(VenueSimple venue) {
		this.venue = venue;
	}
	
	public String toString() {
		return String.format(Locale.ENGLISH,
			"%s - %s - %s - %s - %s - %s - %s - %s - %s",
			course, DatabaseHelper.listToString(years), venue.getBuilding(), 
			venue.getRoom(), comment, day, time.getStart(), time.getFinish(), semester);
	}

}
