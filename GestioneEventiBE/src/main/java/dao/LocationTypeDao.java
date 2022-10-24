package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.LocationTypeBean;
import beans.UserBean;

public class LocationTypeDao {
	private Connection connection;
	private String query;
	PreparedStatement statement = null;
	ResultSet result = null;
	
		public LocationTypeDao(Connection connection) {
		super();
		this.connection = connection;
	}
		
		public ArrayList<LocationTypeBean> getLocationType () throws SQLException {
			ArrayList <LocationTypeBean> typeList = new ArrayList();
			
			query = "SELECT * FROM t_location_types";
			
			try {
				// A prepared statement is used here because the query contains parameters
				statement = connection.prepareStatement(query);
				// This sets the article's code as first parameter of the query
				
				result = statement.executeQuery();
				// If there is a match the entire row is returned here as a result
				while(result.next()) {
					// Here an Article object is initialized and the attributes obtained from the database are set
					int idType = result.getInt("id_location_type");
					String description = result.getString("description");
					LocationTypeBean type = new LocationTypeBean( idType, description);
					typeList.add(type);
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
			
			return typeList;
		}
		
		public boolean addType( String description) throws SQLException
		{
				String query = "INSERT INTO t_location_types (description) VALUES(?)";
				int r=0;
				try {
					statement = connection.prepareStatement(query);
					//statement.setInt(1, id_person);
					statement.setString(1, description);
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
