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
		int expeted = 3;
		assertEquals(expeted, eventList.size());
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
		assertEquals(4, location.getIdLocation());
		assertEquals("chillout", location.getLocationName());
		assertEquals("via fiorentina 10", location.getAddress());
		
		// caso id=0
		EventBean event2 = eventDao.getEventById(0);
		assertNull(event2);
		
		// caso id non presente
		EventBean event3 = eventDao.getEventById(10);
		assertNull(event3);
	}

}
