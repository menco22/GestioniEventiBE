package dao;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import beans.LocationTypeBean;

public class LocationTypeDaoTest {

	@Test
	public void testGetLocationType() throws SQLException {
		LocationTypeDao typeDao = new LocationTypeDao();
		ArrayList <LocationTypeBean> types = new ArrayList <>();
		String orderBy = "id_location_type";
		String orderDirection = "asc";
		types = typeDao.getLocationType(orderBy, orderDirection);
		assertEquals(2, types.size());
	}
	@Test
	public void testGetTypeById() throws SQLException {
		LocationTypeDao typeDao = new LocationTypeDao();
		// caso id=0
		LocationTypeBean type1 = typeDao.getTypeById(0);
		assertNull(type1);
		
		//caso id non presente
		LocationTypeBean type2  = typeDao.getTypeById(10);
		assertNull(type2);
		
		//caso id presente
		LocationTypeBean type3 = typeDao.getTypeById(2);
		assertEquals(2, type3.getIdLocationType());
		assertEquals("pub", type3.getDescription());
	}

}
