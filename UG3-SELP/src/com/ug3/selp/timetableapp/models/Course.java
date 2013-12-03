package com.ug3.selp.timetableapp.models;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Course {
	
	private URI url, drps;
	private String name, euclid, acronym, lecturer;
	private List<String> degree;
	private int level, credit, year, semester;
	
	public Course() {
		this.url = this.drps = null;
		this.degree = new ArrayList<String>();
		this.name = this.euclid = this.acronym = this.lecturer = "";
		this.level = this.credit = this.year = this.semester = -1;
	}

	// Setters and getters
	public URI getUrl() {return url;}
	public void setUrl(URI url) {this.url = url;}
	public URI getDrps() {return drps;}
	public void setDrps(URI drps) {this.drps = drps;}
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
	public int getSemester() {return semester;}
	public void setSemester(int semester) {this.semester = semester;}

}
