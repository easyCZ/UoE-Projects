package ug3.selp.timetable.models;


public class VenueSimple {
	
	public VenueSimple(String room, String building) {
		this.room = room;
		this.building = building;
	}
	
	public VenueSimple() {
		this.room = "";
		this.building = "";
	}
	
	private String room;
	private String building;
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	
	

}
