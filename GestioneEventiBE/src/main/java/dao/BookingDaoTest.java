package dao;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import beans.BookingBean;

public class BookingDaoTest {
	
	@Test
	public void testGetBooking() throws SQLException {
		BookingDao bookingDao1 = new BookingDao();
		ArrayList <BookingBean> bookingList = new ArrayList ();
		String orderBy = "id_booking";
		String orderDirection = "asc";
		bookingList = bookingDao1.getBooking(orderBy, orderDirection);
		assertEquals(1, bookingList.size()); 
	}

	@Test
	public void testGetBookingById() throws SQLException {
		// caso id = 0
		BookingDao bookingDao = new BookingDao();
		BookingBean booking1 = bookingDao.getBookingById(0);
		assertNull(booking1);
		
		//caso id non presente
		BookingBean booking2 = bookingDao.getBookingById(10);
		assertNull(booking2);
		
		//caso id presente
		BookingBean booking3 = bookingDao.getBookingById(1);
		assertEquals(1, booking3.getIdBooking());
		assertEquals("ohciòfdx", booking3.getCode()); 
		assertEquals("doppio", booking3.getBookingType());
		assertEquals(9, booking3.getIdUser());
		assertEquals(4, booking3.getIdEvent());
		assertEquals(4, booking3.getIdTable());
	}

}
