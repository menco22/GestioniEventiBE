package dao;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import beans.LocationBean;
import beans.LocationTypeBean;

public class LocationDaoTest {

	@Test
	public void testGetLocations() throws SQLException {
		LocationDao locationDao = new LocationDao ();
		ArrayList <LocationBean> locationList = new ArrayList <>();
		String orderBy = "id_location";
		String orderDirection = "asc";
		locationList = locationDao.getLocations(orderBy, orderDirection);
		assertEquals(11, locationList.size());
	}

	@Test
	public void testGetLocationById() throws SQLException {
		LocationDao locationDao = new LocationDao ();
		//caso id = 0
		LocationBean location1 = locationDao.getLocationById(0);
		assertNull(location1);
		
		// caso id non presente
		LocationBean location2 = locationDao.getLocationById(50);
		assertNull(location2);
		
		//caso id presente
		LocationBean location3 = locationDao.getLocationById(5);
		LocationTypeBean type =new LocationTypeBean(2, "pub");
		assertEquals(5, location3.getIdLocation());
		assertEquals("newpub", location3.getLocationName());
		assertEquals("via umbria 40", location3.getAddress());	
		assertEquals(2, type.getIdLocationType());
		assertEquals("pub", type.getDescription());
		
		}

}
