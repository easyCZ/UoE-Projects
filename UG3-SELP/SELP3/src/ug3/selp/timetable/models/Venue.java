package ug3.selp.timetable.models;

public class Venue {
	
	private String name, description;
	private String map;
	
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
	public String getMap() {return map;}
	public void setMap(String map) {this.map = map;}
	
	@Override
	public String toString() {
		return String.format("%s - %s - %s", name, description, map);
	}

}
