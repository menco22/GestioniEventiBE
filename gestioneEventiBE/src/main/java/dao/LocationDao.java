package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import beans.LocationBean;
import beans.LocationTypeBean;

public class LocationDao {
	private Connection connection;
	private String query;
	PreparedStatement statement = null;
	ResultSet result = null;
	
	public LocationDao (Connection connection) {
		this.connection = connection;
	}
	
	public LocationDao () {
		String dbUrl = "jdbc:mysql://localhost:3306/laurea";
		String dbClass = "com.mysql.cj.jdbc.Driver";
		try {
			Class.forName(dbClass);
			connection = DriverManager.getConnection(dbUrl, "root", "root");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// funzione per recuperare tutte le locations contenute nell'apposita tabella con ordine specificato dall'utente 
	//(in caso non venga specificato è previsto un ordinamento di default)
	public ArrayList<LocationBean> getLocations (String orderBy, String orderDirection) throws SQLException {
		ArrayList <LocationBean> locationList = new ArrayList();
		query = "SELECT t_locations.id_location, t_locations.location_name, t_locations.address, t_locations.location_type,t_location_types.description FROM t_locations left join t_location_types on t_locations.location_type = t_location_types.id_location_type  WHERE t_locations.deleted = false Order by " + orderBy + " " + orderDirection;
		//recupero tutti i dati delle locations e dei loro relativi tipi
		try {
			statement = connection.prepareStatement(query);	
			result = statement.executeQuery();
			while(result.next()) {
				int idLocation = result.getInt("id_location");
				String locationName = result.getString("location_name");
				String locationAddress = result.getString("address");
				int locationType = result.getInt("location_type");
				String description = result.getString("description");
				LocationTypeBean locationTypeBean = new LocationTypeBean(locationType, description);
				LocationBean location = new LocationBean(idLocation,locationName, locationAddress, locationTypeBean);
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
	
	//funzione che recupera la location corrispondente all'id passatole 
	public LocationBean getLocationById(int idLocation) throws SQLException {
		LocationBean location = null;
		String query = "SELECT t_locations.id_location, t_locations.location_name, t_locations.address, t_locations.location_type ,t_location_types.description FROM t_locations left join t_location_types on t_locations.location_type = t_location_types.id_location_type WHERE t_locations.id_location=? AND t_locations.deleted = false";
		//query che recupera le info di una data location insieme alle info relative al tipo di location di cui si tratta
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, idLocation);
			result = statement.executeQuery();
			while(result.next()) {
				String locationName = result.getString("location_name");
				String address = result.getString("address");
				int locationType = result.getInt("location_type");
				String description = result.getString("description");
				LocationTypeBean locationTypeBean = new LocationTypeBean(locationType, description);
				location = new LocationBean(idLocation, locationName, address, locationTypeBean);
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
		
	//funzione per l'aggiunta di una nuova location 
	public boolean addLocation( String name, String address, int locationType) throws SQLException
	{
			String query = "INSERT INTO t_locations (location_name, address, location_type) VALUES(?, ?, ?)";
			int r=0;
			try {
				statement = connection.prepareStatement(query);
				statement.setString(1, name);//imposto i vari valori corrispondenti a ? nella query
				statement.setString(2, address);
				statement.setInt(3, locationType);
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
	
	//funzione per l'eliminazione logica, nell caso della location si procede con un'eliminazione logica quindi viene posto un flag a true
	//(l'eliminazione vera e propria comporterebbe l'eliminazione di tutti gli eventi associati sia futuri che passati)
	public boolean deleteLocation(int idLocation) throws SQLException {
		String query = "UPDATE t_locations SET deleted = true WHERE id_location=?";
		int r = 0;
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, idLocation);//imposta l'id della location da eliminare
			r = statement.executeUpdate();
			// se r>0 significa che almeno una riga è stata modificata, nel nostro caso ciò significa che l'eliminazione è avvenuta con successo
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
	
	//funzione per l'aggiotnamento dei dati di una determinata location
	public boolean updateLocation (int idLocation, String locationName, String address, int locationType) throws SQLException {
		String query = "UPDATE t_locations SET location_name = ?, address = ?, location_type = ? WHERE id_location = ? AND deleted = false";
		int r = 0;
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, locationName);//imposto il valore dei campi e l'id della location da modificare
			statement.setString(2, address);
			statement.setInt(3, locationType);
			statement.setInt(4, idLocation);
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
