package com.ug3.selp.timetableapp.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Course {
	
	private int id;
	private String url, drps;
	private String name, euclid, acronym, lecturer, semester;
	private List<String> degree;
	private int level, credit, year;
	private boolean tracked;
	
	public Course() {
		this.url = this.drps = null;
		this.degree = new ArrayList<String>();
		this.name = this.euclid = this.acronym = this.semester = this.lecturer = "";
		this.level = this.credit = this.year = -1;
		this.tracked = false;
	}

	// Setters and getters
	public String getUrl() {return url;}
	public void setUrl(String url) {this.url = url;}
	public String getDrps() {return drps;}
	public void setDrps(String drps) {this.drps = drps;}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public String getEuclid() {return euclid;}
	public void setEuclid(String euclid) {this.euclid = euclid;}
	public String getAcronym() {return acronym;}
	public void setAcronym(String acronym) {this.acronym = acronym;}
	public String getLecturer() {return lecturer;}
	public void setLecturer(String lecturer) {this.lecturer = lecturer;}
	public List<String> getDegree() {return degree;}
	public void setDegree(List<String> degree) {this.degree = degree;}
	public int getLevel() {return level;}
	public void setLevel(int level) {this.level = level;}
	public int getCredit() {return credit;}
	public void setCredit(int credit) {this.credit = credit;}
	public int getYear() {return year;}
	public void setYear(int year) {this.year = year;}
	public String getSemester() {return semester;}
	public void setSemester(String semester) {this.semester = semester;}
	
	public void addDegree(String degree) {
		this.degree.add(degree);
	}
	
	public String getSemesterNumber() {
		return semester.substring(1);
	}
	
	public String toString() {
		return String.format(Locale.ENGLISH,
				"{\n" +
				" id: %d\n" +
				" name: %s\n" + 		// name
				" acronym: %s\n" + 		// acronym 
				" lecturer: %s\n" +		// lecturer
				" drps: %s\n" +		// drps
				" url: %s\n" +		// url
				" euclid: %s\n" +		// euclid
				" level: %d\n" +	// level
				" year: %d\n" +	// year
				" credit: %d\n" +	// credit
				" semester: %s\n" +		// semester
				" degree: %s\n" +
				" tracked: %b\n" +
				"}",			// degree - CSV
				id, name, acronym, lecturer, drps, url, euclid, 
				level, year, credit, semester, listToString(degree), tracked);	
	}
	
	public static String listToString(List<String> list) {
		StringBuilder result = new StringBuilder();
		for(String string : list) {
		    result.append(string);
		    result.append(",");
		}
		return result.length() > 0 ? result.substring(1, result.length()): "";
	}

	public boolean isTracked() {
		return tracked;
	}

	public void setTracked(boolean tracked) {
		this.tracked = tracked;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
