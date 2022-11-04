package beans;

import java.sql.Date;
import java.util.*;
import java.time.LocalDateTime;

public class EventBean {	
	private int idEvent;
	private int idCreator;
	private int idLocation;
	private String eventName;
	private String date;
	public EventBean(int idEvent, int idCreator, int idLocation, String eventName, String date) {
		super();
		this.idCreator = idCreator;
		this.idLocation = idLocation;
		this.eventName = eventName;
		this.date = date;
	}

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

	
	

}
