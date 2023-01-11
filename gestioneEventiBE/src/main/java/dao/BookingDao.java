package dao;

import java.sql.Connection;
import java.sql.DriverManager;
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
	
	public BookingDao() {
		String dbUrl = "jdbc:mysql://localhost:3306/laurea";
		String dbClass = "com.mysql.cj.jdbc.Driver";
		try {
			Class.forName(dbClass);
			connection = DriverManager.getConnection(dbUrl, "root", "root");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// funzione per recuperare tutte le prenotazionni contenute nell'apposita tabella con ordine specificato dall'utente 
	//(in caso non venga specificato è previsto un ordinamento di default)
	public ArrayList<BookingBean> getBooking (String orderBy, String orderDirection) throws SQLException {
		ArrayList <BookingBean> bookingList = new ArrayList();
		query = "SELECT * FROM t_bookings WHERE deleted = false Order by " + orderBy + " " + orderDirection;
		try {
			statement = connection.prepareStatement(query); 
			result = statement.executeQuery(); 
			while(result.next()) {
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
	
	public ArrayList<BookingBean> getBookingByEvent (String orderBy, String orderDirection, int idEvent) throws SQLException {
		ArrayList <BookingBean> bookingList = new ArrayList();
		query = "SELECT * FROM t_bookings WHERE deleted = false AND id_event=? Order by " + orderBy + " " + orderDirection;
		try {
			statement = connection.prepareStatement(query);//impostazione del parametro e invio della query al db
			statement.setInt(1, idEvent); 
			result=statement.executeQuery();
			while(result.next()) {
				int idBooking = result.getInt("id_booking");
				String code = result.getString("code");
				String bookingType = result.getString("booking_type");
				int idUser = result.getInt("id_user");
				int idTable = result.getInt("id_table");
				BookingBean booking = new BookingBean( idBooking, code, bookingType, idUser, idEvent, idTable);
				bookingList.add(booking);
			}
		}catch (SQLException e) {
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
	
	public ArrayList<BookingBean> getBookingByUser (String orderBy, String orderDirection, int idUser) throws SQLException {
		ArrayList <BookingBean> bookingList = new ArrayList();
		query = "SELECT * FROM t_bookings WHERE deleted = false AND id_user=? Order by " + orderBy + " " + orderDirection;
		try {
			statement = connection.prepareStatement(query);//impostazione del parametro e invio della query al db
			statement.setInt(1, idUser); 
			result=statement.executeQuery();
			while(result.next()) {
				int idBooking = result.getInt("id_booking");
				String code = result.getString("code");
				String bookingType = result.getString("booking_type");
				int idEvent = result.getInt("id_event");
				int idTable = result.getInt("id_table");
				BookingBean booking = new BookingBean( idBooking, code, bookingType, idUser, idEvent, idTable);
				bookingList.add(booking);
			}
		}catch (SQLException e) {
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
	
	//funzione che restituisce una prenotazione specifica in base all'id passatole
	public BookingBean getBookingById(int idBooking) throws SQLException {
		String query ="SELECT * FROM t_bookings WHERE id_booking = ? AND deleted = false";
		BookingBean booking = null;
		try {
			statement = connection.prepareStatement(query);//impostazione del parametro e invio della query al db
			statement.setInt(1, idBooking); 
			result=statement.executeQuery();
			while(result.next()) {
				String code = result.getString("code");
				String bookingType = result.getString("booking_type");
				int idUser = result.getInt("id_user");
				int  idEvent = result.getInt("id_event");
				int idTable = result.getInt("id_table");
				booking = new BookingBean(idBooking, code, bookingType, idUser, idEvent, idTable);
			}
		}catch(SQLException e) {
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
		return booking;
	}
	
	//funzione per l'aggiunta delle prenotazioni
	public boolean addBooking( String code, String bookingType, int idUser, int idEvent, int idTable) throws SQLException{
			String query = "INSERT INTO t_bookings (code, booking_type, id_user, id_event, id_table) VALUES(?, ?, ?, ?, ?)";
			int r=0;
			try {
				statement = connection.prepareStatement(query); 
				statement.setString(1, code); //imposta i vari valori corrispondenti ai ? nella query
				statement.setString(2, bookingType);
				statement.setInt(3, idUser);
				statement.setInt(4, idEvent);
				statement.setInt(5, idTable);
			    r=statement.executeUpdate();
				// se r>0 significa che almeno una riga è stata modificata, nel nostro caso ciò significa che l'aggiunta è avvenuta con successo
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
	
 //funzione per l'aggiornamento della prenotazione	
	public boolean updateBooking (int idBooking, String code, String bookingType, int idUser, int idEvent, int idTable) throws SQLException {
		String query = "UPDATE t_bookings SET code=?, booking_type=?, id_user=?, id_event=?, id_table=?  WHERE id_booking=?";
		int r = 0;
		try {
			statement = connection.prepareStatement(query); 
			statement.setString(1, code);//imposta i vari valori da settare
			statement.setString(2, bookingType);
			statement.setInt(3, idUser);
			statement.setInt(4, idEvent);
			statement.setInt(5, idTable);
			statement.setInt(6, idBooking); //imposta il parametro del where
			r = statement.executeUpdate();
			// se r>0 significa che almeno una riga è stata modificata, nel nostro caso ciò significa che la modifica è avvenuta con successo
			if (r>0) {
				return true;
			}else {
				return false;
			}
		}catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				statement.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//funzione per l'eliminazione di una prenotazione
	public boolean deleteBooking(int idBooking) throws SQLException {
		String query = "UPDATE t_bookings SET deleted = true WHERE id_booking = ?";
		int r = 0;
		try {
			statement = connection.prepareStatement(query); 
			statement.setInt(1, idBooking); //imposta l'id della prenotazione desiderata
			r = statement.executeUpdate();
			// se r>0 significa che almeno una riga è stata modificata, nel nostro caso ciò significa che l'eliminazione è avvenuta con successo
			if (r>0) {
				return true;
			}else {
				return false;
			}
		}catch (SQLException e) {
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
