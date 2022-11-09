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

public class EventDao {
	private Connection connection;
	private String query;
	PreparedStatement statement = null;
	ResultSet result = null;
	
	public EventDao(Connection connection) {
		super();
		this.connection = connection;
	}
	
	public ArrayList<EventBean> getEvents () throws SQLException {
		ArrayList <EventBean> eventList = new ArrayList();
		
		query = "SELECT * FROM t_events";
		
		try {
			// A prepared statement is used here because the query contains parameters
			statement = connection.prepareStatement(query);
			// This sets the article's code as first parameter of the query
			
			result = statement.executeQuery();
			// If there is a match the entire row is returned here as a result
			while(result.next()) {
				// Here an Article object is initialized and the attributes obtained from the database are set
				int idEvent = result.getInt("id_event");
				int idCreator = result.getInt("id_creator");
				int idLocation = result.getInt("id_location");
				String eventName = result.getString("name");
				LocalDateTime date = (LocalDateTime) result.getObject("data_time");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				String formattedDateTime = date.format(formatter); 
				//System.out.println(idEvent);
				EventBean event = new EventBean( idEvent, idCreator, idLocation, eventName, formattedDateTime);
				//int id0 = event.getIdEvent();
				//System.out.println(id0);
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
	
	public boolean addEvent( int idCreator, int idLocation, String eventName, String date) throws SQLException
	{
			String query = "INSERT INTO t_events (id_creator, id_location, name, data_time) VALUES(?, ?, ?, ?)";
			int r=0;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime data = LocalDateTime.parse(date, formatter);		
			System.out.println(data);
			try {
				
				statement = connection.prepareStatement(query);
				//statement.setInt(1, id_person);
				statement.setInt(1, idCreator);
				statement.setInt(2, idLocation);
				statement.setString(3, eventName);
				statement.setObject(4, data);
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
