package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import beans.BookingBean;
import beans.EventBean;
import beans.TableBean;
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
	//(in caso non venga specificato � previsto un ordinamento di default)
	public ArrayList<BookingBean> getBooking (String orderBy, String orderDirection) throws SQLException {
		ArrayList <BookingBean> bookingList = new ArrayList();
		boolean canReview;
		query = "SELECT t_bookings.id_booking, t_bookings.code, t_bookings.booking_type, t_bookings.id_user, t_events.name, t_events.data_time, t_bookings.id_table, t_bookings.id_event, t_tables.table_capacity FROM t_events right JOIN t_bookings ON t_events.id_event=t_bookings.id_event left join t_tables on t_tables.id_table = t_bookings.id_table  WHERE t_bookings.deleted = false AND t_events.deleted=false Order by " + orderBy + " " + orderDirection;
		try {
			statement = connection.prepareStatement(query); 
			result = statement.executeQuery(); 
			while(result.next()) {
				int idBooking = result.getInt("id_booking");
				String code = result.getString("code");
				String bookingType = result.getString("booking_type");
				int idUser = result.getInt("id_user");
				int  idEvent = result.getInt("id_event");
				String eventName = result.getString("name");
				LocalDateTime date = (LocalDateTime) result.getObject("data_time");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				String formattedDateTime = date.format(formatter);
				int idTable = result.getInt("id_table");
				int tableCapacity = result.getInt("table_capacity");
				TableBean table = new TableBean(idTable, tableCapacity);
				EventBean event = new EventBean(idEvent, eventName, formattedDateTime);
				LocalDateTime dataOdierna = LocalDateTime.now();
				if(dataOdierna.isBefore(date)) {
					canReview = false;
				}else {
					canReview = true;
				}
				BookingBean booking = new BookingBean( idBooking, code, bookingType, idUser, event, table, canReview);
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
		boolean canReview;
		if(idEvent == 0) {
			return null;
		}
		query = "SELECT t_bookings.id_booking, t_bookings.code, t_bookings.booking_type, t_bookings.id_user,  t_bookings.id_event, t_events.name, t_events.data_time, t_bookings.id_table, t_tables.table_capacity FROM t_events right JOIN t_bookings ON t_events.id_event=t_bookings.id_event left join t_tables on t_tables.id_table = t_bookings.id_table WHERE t_bookings.deleted = false AND t_events.deleted=? AND t_events.id_event=? Order by " + orderBy + " " + orderDirection;
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
				int tableCapacity = result.getInt("table_capacity");
				String eventName = result.getString("name");
				LocalDateTime date = (LocalDateTime) result.getObject("data_time");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				String formattedDateTime = date.format(formatter);
				LocalDateTime dataOdierna = LocalDateTime.now();
				if(dataOdierna.isBefore(date)) {
					canReview = false;
				}else {
					canReview = true;
				}
				TableBean table = new TableBean(idTable, tableCapacity);
				EventBean event = new EventBean(idEvent, eventName, formattedDateTime);
				BookingBean booking = new BookingBean( idBooking, code, bookingType, idUser, event, table, canReview);
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
		boolean canReview;
		query = "SELECT t_bookings.id_booking, t_bookings.code, t_bookings.booking_type, t_bookings.id_user, t_events.name, t_events.data_time, t_bookings.id_table,  t_bookings.id_event, t_tables.table_capacity FROM t_events right JOIN t_bookings ON t_events.id_event=t_bookings.id_event left join t_tables on t_tables.id_table = t_bookings.id_table WHERE t_bookings.deleted = false AND t_events.deleted=false AND t_bookings.id_user=? Order by " + orderBy + " " + orderDirection;
		if(idUser == 0) {
			return null;
		}
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
				int tableCapacity = result.getInt("table_capacity");
				String eventName = result.getString("name");
				LocalDateTime date = (LocalDateTime) result.getObject("data_time");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				String formattedDateTime = date.format(formatter);
				LocalDateTime dataOdierna = LocalDateTime.now();
				if(dataOdierna.isBefore(date)) {
					canReview = false;
				}else {
					canReview = true;
				}
				TableBean table = new TableBean(idTable, tableCapacity);
				EventBean event = new EventBean(idEvent, eventName, formattedDateTime); 
				BookingBean booking = new BookingBean( idBooking, code, bookingType, idUser, event, table, canReview);
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
		String query ="SELECT t_bookings.id_booking, t_bookings.code, t_bookings.booking_type, t_bookings.id_user,  t_bookings.id_event, t_events.name, t_events.data_time, t_bookings.id_table, t_tables.table_capacity FROM t_events right JOIN t_bookings ON t_events.id_event=t_bookings.id_event left join t_tables on t_tables.id_table = t_bookings.id_table WHERE t_bookings.deleted = false AND t_events.deleted=false AND t_bookings.id_booking = ?";
		BookingBean booking = null;
		boolean canReview;
		try {
			statement = connection.prepareStatement(query);//impostazione del parametro e invio della query al db
			statement.setInt(1, idBooking); 
			result=statement.executeQuery();
			while(result.next()) {
				String code = result.getString("code");
				String bookingType = result.getString("booking_type");
				int idUser = result.getInt("id_user");
				int  idEvent = result.getInt("id_event");
				String eventName = result.getString("name");
				int idTable = result.getInt("id_table");
				int tableCapacity = result.getInt("table_capacity");
				LocalDateTime date = (LocalDateTime) result.getObject("data_time");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				String formattedDateTime = date.format(formatter);
				LocalDateTime dataOdierna = LocalDateTime.now();
				if(dataOdierna.isBefore(date)) {
					canReview = false;
				}else {
					canReview = true;
				}
				TableBean table = new TableBean(idTable, tableCapacity);
				EventBean event = new EventBean (idEvent, eventName, formattedDateTime);
				booking = new BookingBean(idBooking, code, bookingType, idUser, event, table, canReview);
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
		if(idTable == 0) {
			String query = "INSERT INTO t_bookings (code, booking_type, id_user, id_event) VALUES(?, ?, ?, ?)";
			int r=0;
			try {
				statement = connection.prepareStatement(query); 
				statement.setString(1, code); //imposta i vari valori corrispondenti ai ? nella query
				statement.setString(2, bookingType);
				statement.setInt(3, idUser);
				statement.setInt(4, idEvent);
			    r=statement.executeUpdate();
				// se r>0 significa che almeno una riga � stata modificata, nel nostro caso ci� significa che l'aggiunta � avvenuta con successo
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
		}else {
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
				// se r>0 significa che almeno una riga � stata modificata, nel nostro caso ci� significa che l'aggiunta � avvenuta con successo
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
			// se r>0 significa che almeno una riga � stata modificata, nel nostro caso ci� significa che la modifica � avvenuta con successo
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
			// se r>0 significa che almeno una riga � stata modificata, nel nostro caso ci� significa che l'eliminazione � avvenuta con successo
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
