package beans;

import java.sql.Date;
import java.util.*;
import java.time.LocalDateTime;

public class EventBean {	
	//proprietà di ogni evento
	private int idEvent;
	private int idCreator;
	private int idLocation;
	private String eventName;
	private String date;
	private String locationName;
	private String address;
	
	public EventBean(int idEvent, int idCreator, int idLocation, String locationName, String address, String eventName, String date) {
		//costruttore
		super();
		this.idEvent = idEvent;
		this.idCreator = idCreator;
		this.idLocation = idLocation;
		this.locationName = locationName;
		this.address = address;
		this.eventName = eventName;
		this.date = date;
	}

	//getters e setters per le varie proprietà
	public int getIdEvent() {
		return idEvent;
	}
	public void setIdEvent(int idEvent) {
		this.idEvent = idEvent;
	}
	public int getIdCreator() {
		return idCreator;
	}
	public void setIdCreator(int idCreator) {
		this.idCreator = idCreator;
	}
	public int getIdLocation() {
		return idLocation;
	}
	public void setIdLocation(int idLocation) {
		this.idLocation = idLocation;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
	

}
