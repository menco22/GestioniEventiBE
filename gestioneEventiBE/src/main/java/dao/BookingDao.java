package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.BookingBean;
import beans.UserBean;

public class BookingDao {
	private Connection connection;
	private String query;
	PreparedStatement statement = null;
	ResultSet result = null;
	
	public BookingDao(Connection connection) {
		super();
		this.connection = connection;
	}
	
	public ArrayList<BookingBean> getBooking () throws SQLException {
		ArrayList <BookingBean> bookingList = new ArrayList();
		
		query = "SELECT * FROM t_bookings";
		
		try {
			// A prepared statement is used here because the query contains parameters
			statement = connection.prepareStatement(query);
			// This sets the article's code as first parameter of the query
			
			result = statement.executeQuery();
			// If there is a match the entire row is returned here as a result
			while(result.next()) {
				// Here an Article object is initialized and the attributes obtained from the database are set
				int idBooking = result.getInt("id_booking");
				String code = result.getString("code");
				String bookingType = result.getString("booking_type");
				int idUser = result.getInt("id_user");
				int  idEvent = result.getInt("id_event");
				int idTable = result.getInt("id_table");
				BookingBean booking = new BookingBean( idBooking, code, bookingType, idUser, idEvent, idTable);
				bookingList.add(booking);
			}
		} catch (SQLException e) {
		    e.printStackTrace();
			throw new SQLException(e);

		} finally {
			try {
				result.close();
			} catch (Exception e1) {
				throw new SQLException(e1);
			}
			try {
				statement.close();
			} catch (Exception e2) {
				throw new SQLException(e2);
			}
		}	
		
		return bookingList;
	}
	
	public boolean addBooking( String code, String bookingType, int idUser, int idEvent, int idTable) throws SQLException
	{
			String query = "INSERT INTO t_bookings (code, booking_type, id_user, id_event, id_table) VALUES(?, ?, ?, ?, ?)";
			int r=0;
			try {
				statement = connection.prepareStatement(query);
				//statement.setInt(1, id_person);
				statement.setString(1, code);
				statement.setString(2, bookingType);
				statement.setInt(3, idUser);
				statement.setInt(4, idEvent);
				statement.setInt(5, idTable);
			    r=statement.executeUpdate();
				//result = statement.executeUpdate();
				// If there is an affected row, it means the user has been added
			    if(r>0) {
			    return true;
			    }else {
			    	return false;
			    }
			} catch (SQLException e) {
				throw new SQLException(e);
			} finally {
				try {
					statement.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
		}
}
