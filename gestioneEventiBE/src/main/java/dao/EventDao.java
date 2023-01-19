package dao;


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.*;

import beans.EventBean;
import beans.LocationBean;
import beans.TableBean;

public class EventDao {
	public  Connection connection;
	private String query;
	PreparedStatement statement = null;
	ResultSet result = null;
	
	public EventDao(Connection connection) {
		super();
		this.connection = connection;
	}
	
	public EventDao() {
		String dbUrl = "jdbc:mysql://localhost:3306/laurea";
		String dbClass = "com.mysql.cj.jdbc.Driver";
		try {
			Class.forName(dbClass);
			connection = DriverManager.getConnection(dbUrl, "root", "root");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<EventBean> getEventsByCreator(String orderBy, String orderDirection, int idCreator) throws SQLException {
		ArrayList <EventBean> eventList = new ArrayList();
		boolean canBook;
		if(idCreator == 0) {
			return null;
		}
		query = "SELECT t_events.id_event, t_events.id_creator ,t_events.name, t_events.data_time,t_events.data_scadenza ,t_events.standing_places,t_events.id_location, t_locations.location_name, t_locations.address FROM t_events LEFT JOIN t_locations on t_events.id_location = t_locations.id_location WHERE t_locations.deleted = false AND t_events.deleted=false AND t_events.id_creator=? Order by " + orderBy + " " + orderDirection;
		try {
			statement = connection.prepareStatement(query); 
			statement.setInt(1, idCreator);
			result =statement.executeQuery(); 
			while(result.next()) {
				int idEvent = result.getInt("id_event");
				int idLocation = result.getInt("id_location");
				String locationName = result.getString("location_name");
				String address = result.getString("address");
				String eventName = result.getString("name");
				LocalDateTime date = (LocalDateTime) result.getObject("data_time");
				LocalDateTime dataScadenza = (LocalDateTime) result.getObject("data_scadenza");
				int standingPlaces = result.getInt("standing_places");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				LocalDateTime dataOdierna = LocalDateTime.now();
				String formattedDateTime = date.format(formatter); //formatto la data da LocalDateTime a String 
				String formattedDataScadenza = dataScadenza.format(formatter);
				if(dataOdierna.isBefore(dataScadenza)) {
					canBook = true;
				}else {
					canBook = false;
				}
				LocationBean location = new LocationBean(idLocation, locationName, address); 
				EventBean event = new EventBean( idEvent, idCreator,eventName, formattedDateTime, formattedDataScadenza,standingPlaces ,location, canBook);
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
	
	//funzione che ritorna l'elenco di eventi la cui location non risulti eliminata
	public ArrayList<EventBean> getEvents (String orderBy, String orderDirection) throws SQLException {
		ArrayList <EventBean> eventList = new ArrayList();
		boolean canBook;
		query = "SELECT t_events.id_event, t_events.id_creator ,t_events.name, t_events.data_time,t_events.data_scadenza,t_events.standing_places  ,t_events.id_location, t_locations.location_name, t_locations.address FROM t_events LEFT JOIN t_locations on t_events.id_location = t_locations.id_location WHERE t_locations.deleted = false AND t_events.deleted=false Order by " + orderBy + " " + orderDirection;
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
				LocalDateTime dataScadenza = (LocalDateTime) result.getObject("data_scadenza");
				LocalDateTime dataOdierna = LocalDateTime.now();
				int standingPlaces = result.getInt("standing_places");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				String formattedDateTime = date.format(formatter); //formatto la data da LocalDateTime a String 
				String formattedDataScadenza = dataScadenza.format(formatter);
				if(dataOdierna.isBefore(dataScadenza) ) {
					canBook = true;
				}else {
					canBook = false;
				}
				LocationBean location = new LocationBean(idLocation, locationName, address); 
				EventBean event = new EventBean( idEvent, idCreator,eventName, formattedDateTime, formattedDataScadenza,standingPlaces ,location,canBook);
				//System.out.println(canBook);
				if(canBook == true) {
						eventList.add(event); 
				}
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
		EventBean event = null;
		boolean canBook;
		String query="SELECT t_events.id_event, t_events.id_creator ,t_events.name, t_events.data_time, t_events.data_scadenza,t_events.standing_places ,t_events.id_location, t_locations.location_name, t_locations.address FROM t_events LEFT JOIN t_locations on t_events.id_location = t_locations.id_location WHERE t_locations.deleted = false AND t_events.deleted=false AND t_events.id_event = ?";
		try {
			statement = connection.prepareStatement(query); //impostazione del parametro e invio dellla query al db
			statement.setInt(1, idEvent);
			result =statement.executeQuery(); 
			if(result == null) {
				return null;
			} 
			while(result.next()) {
				int idCreator = result.getInt("id_creator");
				int idLocation = result.getInt("id_location");
				String eventName = result.getString("name");
				String locationName = result.getString("location_name");
				String address = result.getString("address");
				LocalDateTime date = (LocalDateTime) result.getObject("data_time");
				LocalDateTime dataScadenza = (LocalDateTime) result.getObject("data_scadenza");
				int standingPlaces = result.getInt("standing_places");
				LocalDateTime dataOdierna = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				String formattedDateTime = date.format(formatter); //formattazione della data, si passa da LocalDateTime a stringa
				String formattedDataScadenza = dataScadenza.format(formatter);
				if(dataOdierna.isBefore(dataScadenza)) {
					canBook = true;
				}else {
					canBook = false;
				}
				LocationBean location = new LocationBean(idLocation, locationName, address); 
				event = new EventBean( idEvent, idCreator, eventName, formattedDateTime,formattedDataScadenza,standingPlaces ,location, canBook);
				
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
		
	//funzione per l'aggiunta di un evento
	public boolean addEvent( int idCreator, int idLocation, String eventName, String date, String dataScadenza, int standingPlaces) throws SQLException{
			String query = "INSERT INTO t_events (id_creator, id_location, name, data_time, data_scadenza, standing_places) VALUES(?, ?, ?, ?, ?, ?)";
			int r=0;
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime data = LocalDateTime.parse(date, formatter); //formatto la data, si passa da stringa a LocalDateTime
			LocalDateTime dataScad = LocalDateTime.parse(dataScadenza, formatter);
			try {
				statement = connection.prepareStatement(query); // imposto valori e invio la query a db
				statement.setInt(1, idCreator);
				statement.setInt(2, idLocation);
				statement.setString(3, eventName);
				statement.setObject(4, data);
				statement.setObject(5, dataScad);
				statement.setInt(6, standingPlaces);
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

	//funzione per l'eliminazione di un evento specifico
	public boolean deleteEvent ( int idEvent) throws SQLException {
		String query = "UPDATE t_events SET deleted=true WHERE id_event = ?";
		int r = 0;
		try{
			statement = connection.prepareStatement(query); //imposto l'id dell'evento cercato e invio al db la query
			statement.setInt(1, idEvent);
		    r=statement.executeUpdate();
			// se r>0 significa che almeno una riga è stata modificata, nel nostro caso ciò significa che l'eliminazione è avvenuta con successo
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
	public boolean updateEvent (int idEvent, int idCreator, int idLocation, String eventName, String date, String dataScadenza, int standingPlaces) throws SQLException {
		String query = "UPDATE t_events SET id_creator=?, id_location=?, name=?, data_time=?, data_scadenza=?, standing_places=?  WHERE id_event = ?";
		int r=0;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime data = LocalDateTime.parse(date, formatter); //formattazione data, si passa da stringa a LocalDateTime	
		LocalDateTime dataScad = LocalDateTime.parse(dataScadenza, formatter);
		try {
			statement = connection.prepareStatement(query); //setto valori e id dell'evento cercato e invio la query al db
			statement.setInt(1, idCreator);
			statement.setInt(2, idLocation);
			statement.setString(3, eventName);
			statement.setObject(4, data);
			statement.setObject(5, dataScad);
			statement.setInt(6, standingPlaces);
			statement.setInt(7, idEvent);		
			r = statement.executeUpdate();
			// se r>0 significa che almeno una riga è stata modificata, nel nostro caso ciò significa che la modifica è avvenuta con successo
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
