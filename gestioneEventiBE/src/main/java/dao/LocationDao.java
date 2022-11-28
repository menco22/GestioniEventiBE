package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import beans.LocationBean;

public class LocationDao {
	private Connection connection;
	private String query;
	PreparedStatement statement = null;
	ResultSet result = null;
	
	public LocationDao (Connection connection) {
		this.connection = connection;
	}
	
	public ArrayList<LocationBean> getLocations (String orderBy, String orderDirection) throws SQLException {
		ArrayList <LocationBean> locationList = new ArrayList();
		
		query = "SELECT * FROM t_locations WHERE  deleted = false Order by " + orderBy + " " + orderDirection;
		
		try {
			// A prepared statement is used here because the query contains parameters
			statement = connection.prepareStatement(query);
			// This sets the article's code as first parameter of the query
			
			result = statement.executeQuery();
			// If there is a match the entire row is returned here as a result
			while(result.next()) {
				// Here an Article object is initialized and the attributes obtained from the database are set
				int idLocation = result.getInt("id_location");
				String locationName = result.getString("location_name");
				String locationAddress = result.getString("address");
				int locationType = result.getInt("location_type");
				LocationBean location = new LocationBean(idLocation,locationName, locationAddress, locationType);
				locationList.add(location);
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
		
		return locationList;
	}
	
	public LocationBean getLocationById(int idLocation) throws SQLException {
		LocationBean location = null;
		String query = "SELECT * FROM t_locations WHERE id_location = ? AND deleted = false";
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, idLocation);
			result = statement.executeQuery();
			while(result.next()) {
				String locationName = result.getString("location_name");
				String address = result.getString("address");
				int locationType = result.getInt("location_type");
				location = new LocationBean(idLocation, locationName, address, locationType);
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
		
		return location;
	}
	
	public ArrayList<LocationBean> getLocationsByType (int type) throws SQLException {
		ArrayList <LocationBean> locationList = new ArrayList();
		
		query = "SELECT location_name FROM t_locations WHERE location_type = ?  AND deleted = false";
		
		try {
			// A prepared statement is used here because the query contains parameters
			statement = connection.prepareStatement(query);
			// This sets the article's code as first parameter of the query
			statement.setInt(1, type);
			result = statement.executeQuery();
			// If there is a match the entire row is returned here as a result
			while(result.next()) {
				// Here an Article object is initialized and the attributes obtained from the database are set
				int locationId = result.getInt("id_location");
				String locationName = result.getString("location_name");
				String locationAddress = result.getString("address");
				int locationType = result.getInt("location_type");
				LocationBean location = new LocationBean(locationId, locationName, locationAddress, locationType);
				locationList.add(location);
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
		
		return locationList;
	}
	
	public ArrayList<LocationBean> getLocationByName (String locationName) throws SQLException{
		ArrayList <LocationBean> locationList = new ArrayList();
		String query = "SELECT * FROM t_locations WHERE location_name = ?";
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, locationName);
			result = statement.executeQuery();
			while(result.next()) {
				int idLocation = result.getInt("id_location");
				String locationAddress = result.getString("address");
				int locationType = result.getInt("location_type");
				LocationBean location = new LocationBean (idLocation, locationName, locationAddress, locationType);
				locationList.add(location);
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
		return locationList;
	}
	
	public boolean addLocation( String name, String address, int locationType) throws SQLException
	{
			String query = "INSERT INTO t_locations (location_name, address, location_type) VALUES(?, ?, ?)";
			int r=0;
			try {
				statement = connection.prepareStatement(query);
				//statement.setInt(1, id_person);
				statement.setString(1, name);
				statement.setString(2, address);
				statement.setInt(3, locationType);
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
	
	public boolean deleteLocation(int idLocation, boolean delete) throws SQLException {
		String query = "UPDATE t_locations SET deleted =? WHERE id_location=?";
		int r = 0;
		try {
			statement = connection.prepareStatement(query);
			statement.setBoolean(1, delete);
			statement.setInt(2, idLocation);
			r = statement.executeUpdate();
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
	
	public boolean updateLocation (int idLocation, String locationName, String address, int locationType) throws SQLException {
		String query = "UPDATE t_locations SET location_name = ?, address = ?, location_type = ? WHERE id_location = ?";
		int r = 0;
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, locationName);
			statement.setString(2, address);
			statement.setInt(3, locationType);
			statement.setInt(4, idLocation);
			r = statement.executeUpdate();
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
