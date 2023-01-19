package beans;

import java.sql.Date;
import java.util.*;
import java.time.LocalDateTime;

public class EventBean {	
	//proprietà di ogni evento
	private int idEvent;
	private int idCreator;
	private String eventName;
	private String date;
	private String dataScadenza;
	private int standingPlaces;
	private LocationBean locationBean;
	private boolean canBook;
	
	public EventBean(int idEvent, int idCreator, String eventName, String date, String dataScadenza,int standingPlaces ,LocationBean locationBean, boolean canBook) {
		//costruttore
		super();
		this.idEvent = idEvent;
		this.idCreator = idCreator;
		this.eventName = eventName;
		this.date = date;
		this.dataScadenza = dataScadenza;
		this.standingPlaces = standingPlaces;
		this.locationBean = locationBean;
		this.canBook = canBook;
	}
	
	public EventBean (int idEvent, String eventName) {
		this.idEvent = idEvent;
		this.eventName = eventName;
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

	public String getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	
	public int getStandingPlaces() {
		return standingPlaces;
	}

	public void setStandingPlaces(int standingPlaces) {
		this.standingPlaces = standingPlaces;
	}

	public LocationBean getLocationBean() {
		return locationBean;
	}

	public void setLocationBean(LocationBean locationBean) {
		this.locationBean = locationBean;
	}
	
	public boolean isCanBook() {
		return canBook;
	}

	public void setCanBook(boolean canBook) {
		this.canBook = canBook;
	}
}
