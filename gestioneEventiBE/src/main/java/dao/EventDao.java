package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.*;

import beans.EventBean;
import beans.LocationBean;

public class EventDao {
	private Connection connection;
	private String query;
	PreparedStatement statement = null;
	ResultSet result = null;
	
	public EventDao(Connection connection) {
		super();
		this.connection = connection;
	}
	
	//funzione che ritorna l'elenco di eventi la cui location non risulti eliminata
	public ArrayList<EventBean> getEvents (String orderBy, String orderDirection) throws SQLException {
		ArrayList <EventBean> eventList = new ArrayList();
		query = "SELECT t_events.id_event, t_events.id_creator ,t_events.name, t_events.data_time, t_events.id_location, t_locations.location_name, t_locations.address FROM t_events LEFT JOIN t_locations on t_events.id_location = t_locations.id_location WHERE t_locations.deleted = false Order by " + orderBy + " " + orderDirection;
		try {
			statement = connection.prepareStatement(query); 
			result = statement.executeQuery(); 
			while(result.next()) {
				int idEvent = result.getInt("id_event");
				int idCreator = result.getInt("id_creator");
				int idLocation = result.getInt("id_location");
				String locationName = result.getString("location_name");
				String address = result.getString("address");
				String eventName = result.getString("name");
				LocalDateTime date = (LocalDateTime) result.getObject("data_time");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				String formattedDateTime = date.format(formatter); //formatto la data da LocalDateTime a String 
				LocationBean location = new LocationBean(idLocation, locationName, address); 
				EventBean event = new EventBean( idEvent, idCreator,eventName, formattedDateTime, location);
				eventList.add(event); 
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
		
		return eventList;
	}
	
	//funzione per recuperare l'evento con un dato id
	public EventBean getEventById (int idEvent) throws SQLException {
		String query="SELECT t_events.id_event, t_events.id_creator ,t_events.name, t_events.data_time, t_events.id_location, t_locations.location_name, t_locations.address FROM t_events LEFT JOIN t_locations on t_events.id_location = t_locations.id_location WHERE t_locations.deleted = false AND t_events.id_event = ?";
		EventBean event = null;
		try {
			statement = connection.prepareStatement(query); //impostazione del parametro e invio dellla query al db
			statement.setInt(1, idEvent);
			result =statement.executeQuery(); 
			while(result.next()) {
				int idCreator = result.getInt("id_creator");
				int idLocation = result.getInt("id_location");
				String eventName = result.getString("name");
				String locationName = result.getString("location_name");
				String address = result.getString("address");
				LocalDateTime date = (LocalDateTime) result.getObject("data_time");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				String formattedDateTime = date.format(formatter); //formattazione della data, si passa da LocalDateTime a stringa
				LocationBean location = new LocationBean(idLocation, locationName, address); 
				event = new EventBean( idEvent, idCreator, eventName, formattedDateTime, location);
				
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
			return event;
			
	}
	
	/*public ArrayList<EventBean> getEventByName (String eventName) throws SQLException{
		ArrayList <EventBean> eventList = new ArrayList();
		String query = "SELECT * FROM t_event WHERE name = ?";
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, eventName);
			result = statement.executeQuery();
			while(result.next()){
				int idEvent = result.getInt("id_event");
				int idCreator = result.getInt("id_creator");
				int idLocation = result.getInt("id_location");
				String locationName = result.getString("location_name");
				String address = result.getString("address");
				LocalDateTime date = (LocalDateTime) result.getObject("data_time");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				String formattedDateTime = date.format(formatter); 
				EventBean event = new EventBean(idEvent, idCreator, idLocation, locationName, address, eventName, formattedDateTime);
				eventList.add(event);
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
		return eventList;
	}
	
	public ArrayList<EventBean> getEventByDate (String eventDate) throws SQLException{
		ArrayList <EventBean> eventList = new ArrayList();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime date = LocalDateTime.parse(eventDate, formatter);
		String query = "SELECT * FROM t_event WHERE data_time = ?";
		try {
			statement = connection.prepareStatement(query);
			statement.setObject(1, date);
			result = statement.executeQuery();
			while(result.next()){
				int idEvent = result.getInt("id_event");
				int idCreator = result.getInt("id_creator");
				int idLocation = result.getInt("id_location");
				String locationName = result.getString("location_name");
				String address = result.getString("address");
				String eventName = result.getString("name");
				EventBean event = new EventBean(idEvent, idCreator, idLocation, locationName, address, eventName, eventDate);
				eventList.add(event);
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
		return eventList;
	}*/
	
	//funzione per l'aggiunta di un evento
	public boolean addEvent( int idCreator, int idLocation, String eventName, String date) throws SQLException{
			String query = "INSERT INTO t_events (id_creator, id_location, name, data_time) VALUES(?, ?, ?, ?)";
			int r=0;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime data = LocalDateTime.parse(date, formatter); //formatto la data, si passa da stringa a LocalDateTime		
			try {
				statement = connection.prepareStatement(query); // imposto valori e invio la query a db
				statement.setInt(1, idCreator);
				statement.setInt(2, idLocation);
				statement.setString(3, eventName);
				statement.setObject(4, data);
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

	//funzione per l'eliminazione di un evento specifico
	public boolean deleteEvent ( int idEvent) throws SQLException {
		String query = "DELETE FROM t_events WHERE id_event = ?";
		int r = 0;
		try{
			statement = connection.prepareStatement(query); //imposto l'id dell'evento cercato e invio al db la query
			statement.setInt(1, idEvent);
		    r=statement.executeUpdate();
			// se r>0 significa che almeno una riga � stata modificata, nel nostro caso ci� significa che l'eliminazione � avvenuta con successo
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
	
	//funzione per l'aggiornamento di un evento
	public boolean updateEvent (int idEvent, int idCreator, int idLocation, String eventName, String date) throws SQLException {
		String query = "UPDATE t_events SET id_creator=?, id_location=?, name=?, data_time=?  WHERE id_event = ?";
		int r=0;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime data = LocalDateTime.parse(date, formatter); //formattazione data, si passa da stringa a LocalDateTime	
		try {
			statement = connection.prepareStatement(query); //setto valori e id dell'evento cercato e invio la query al db
			statement.setInt(1, idCreator);
			statement.setInt(2, idLocation);
			statement.setString(3, eventName);
			statement.setObject(4, data);
			statement.setInt(5, idEvent);		
			r = statement.executeUpdate();
			// se r>0 significa che almeno una riga � stata modificata, nel nostro caso ci� significa che la modifica � avvenuta con successo
		    if(r>0) {
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
