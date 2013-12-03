package com.ug3.selp.timetableapp.models;

import java.net.URI;

public class Venue {
	
	private String name, description;
	private URI map;
	
	public Venue() {
		this.setName(null);
		this.setDescription(null);
		this.setMap(null);
	}

	// Setters and Getters
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}
	public URI getMap() {return map;}
	public void setMap(URI map) {this.map = map;}

}
