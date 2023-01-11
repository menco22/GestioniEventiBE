package dao;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import beans.EventBean;
import beans.LocationBean;

public class EventDaoTest {
	
	@Test
	public void testGetEvents() throws SQLException {
		EventDao eventDao = new EventDao ();
		ArrayList <EventBean> eventList = new ArrayList ();
		String orderBy = "id_event";
		String orderDirection = "asc";
		eventList = eventDao.getEvents(orderBy, orderDirection);
		int expeted = 2;
		assertEquals(expeted, eventList.size());
	}
	
	@Test
	public void testGetEventsByCreator() throws SQLException {
		EventDao eventDao = new EventDao ();
		ArrayList <EventBean> eventList1 = new ArrayList ();
		String orderBy = "id_event";
		String orderDirection = "asc";
		//caso id presente
		eventList1 = eventDao.getEventsByCreator(orderBy, orderDirection,19);
		assertEquals(1, eventList1.size());
		
		//caso id = 0
		ArrayList <EventBean> eventList2 = new ArrayList();
		eventList2 = eventDao.getEventsByCreator(orderBy, orderDirection,0);
		assertNull(eventList2);
		
		//caso id non presente
		ArrayList <EventBean> eventList3 = new ArrayList();
		eventList3 = eventDao.getEventsByCreator(orderBy, orderDirection,190);
		assertEquals(0,eventList3.size());
	}

	@Test
	public void testGetEventById() throws SQLException {
		// caso id presente
		EventDao eventDao = new EventDao ();
		EventBean event  = eventDao.getEventById(6);
		LocationBean location =new LocationBean(4, "chillout", "via fiorentina 10");
		//EventBean expected = new EventBean(6, 12, "evento33*33", "2022-12-30 21:30:00", location);
		assertEquals(6, event.getIdEvent());
		assertEquals("evento33*33", event.getEventName());
		assertEquals("2022-12-30 21:30", event.getDate());
		assertEquals("2022-01-09 20:30", event.getDataScadenza());
		assertEquals(100, event.getStandingPlaces());
		assertEquals(4, location.getIdLocation());
		assertEquals("chillout", location.getLocationName());
		assertEquals("via fiorentina 10", location.getAddress());
		
		// caso id=0
		EventBean event2 = eventDao.getEventById(0);
		assertNull(event2);
		
		// caso id non presente
		EventBean event3 = eventDao.getEventById(100);
		assertNull(event3);
	}

}
