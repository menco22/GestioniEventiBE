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
		assertEquals(2, bookingList.size()); 
	}
	
	@Test
	public void testGetBookingByEvent() throws SQLException {
		BookingDao bookingDao1 = new BookingDao();
		ArrayList <BookingBean> bookingList1 = new ArrayList ();
		String orderBy = "id_booking";
		String orderDirection = "asc";
		//caso id presente
		bookingList1 = bookingDao1.getBookingByEvent(orderBy, orderDirection, 10);
		assertEquals(2, bookingList1.size()); 
		//caso id=0
		ArrayList <BookingBean> bookingList2 = new ArrayList ();
		bookingList2 = bookingDao1.getBookingByEvent(orderBy, orderDirection, 0);
		assertNull(bookingList2); 
		
		//caso id non presente
		ArrayList <BookingBean> bookingList3 = new ArrayList ();
		bookingList3 = bookingDao1.getBookingByEvent(orderBy, orderDirection, 20);
		assertEquals(0,bookingList3.size()); 
	}
	
	@Test
	public void testGetBookingByUser() throws SQLException {
		BookingDao bookingDao1 = new BookingDao();
		ArrayList <BookingBean> bookingList1 = new ArrayList ();
		String orderBy = "id_booking";
		String orderDirection = "asc";
		// caso id presente
		bookingList1 = bookingDao1.getBookingByUser(orderBy, orderDirection, 19);
		assertEquals(2, bookingList1.size()); 
		//caso id=0
		ArrayList <BookingBean> bookingList2 = new ArrayList ();
		bookingList2 = bookingDao1.getBookingByUser(orderBy, orderDirection, 0);
		assertNull(bookingList2); 
		ArrayList <BookingBean> bookingList3 = new ArrayList ();
		bookingList3 = bookingDao1.getBookingByUser(orderBy, orderDirection, 20);
		assertEquals(0,bookingList3.size()); 
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
		assertEquals("tavolo grande", booking3.getBookingType());
		assertEquals(19, booking3.getIdUser());
		assertEquals(10, booking3.getEvent().getIdEvent());
		assertEquals(7, booking3.getTable().getIdTable());
	}

}
