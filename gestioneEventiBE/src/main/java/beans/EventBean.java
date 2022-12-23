package beans;

import java.sql.Date;
import java.util.*;
import java.time.LocalDateTime;

public class EventBean {	
	//proprietà di ogni evento
	private int idEvent;
	private int idCreator;
	//private int idLocation;
	private String eventName;
	private String date;
	//private String locationName;
	//private String address;
	private LocationBean locationBean;
	
	public EventBean(int idEvent, int idCreator, String eventName, String date, LocationBean locationBean) {
		//costruttore
		super();
		this.idEvent = idEvent;
		this.idCreator = idCreator;
		this.eventName = eventName;
		this.date = date;
		this.locationBean = locationBean;
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

	public LocationBean getLocationBean() {
		return locationBean;
	}

	public void setLocationBean(LocationBean locationBean) {
		this.locationBean = locationBean;
	}
}
