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
		
		public ArrayList<LocationTypeBean> getLocationType (String orderBy, String orderDirection) throws SQLException {
			ArrayList <LocationTypeBean> typeList = new ArrayList();
			
			query = "SELECT * FROM t_location_types Order by " + orderBy + " " + orderDirection;
			
			try {
				statement = connection.prepareStatement(query);
				result = statement.executeQuery();
				while(result.next()) {
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
		
		public LocationTypeBean getTypeById (int idType) throws SQLException {
			LocationTypeBean type = null;
			String query = "SELECT * FROM t_location_types WHERE id_location_type = ?";
			try {
				statement = connection.prepareStatement(query);
				statement.setInt(1, idType);
				result = statement.executeQuery();
				while(result.next()) {
					String description = result.getString("description");
					type = new LocationTypeBean(idType, description); 
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
			return type;
		}
		
		public boolean addType( String description) throws SQLException
		{
				String query = "INSERT INTO t_location_types (description) VALUES(?)";
				int r=0;
				try {
					statement = connection.prepareStatement(query);
					statement.setString(1, description);
				    r=statement.executeUpdate();
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
		
		public boolean updateLocationType (int idLocationType, String description) throws SQLException{
			String query = "UPDATE t_location_types SET description=? WHERE id_location_type=?";
			int r = 0;
			try {
				statement = connection.prepareStatement(query);
				statement.setString(1, description);
				statement.setInt(2, idLocationType);
				r=statement.executeUpdate();
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
		
		public boolean deleteLocationType (int idLocationType) throws SQLException {
			String query = "DELETE FROM t_location_types WHERE id_location_type = ?";
			int r = 0;
			try {
				statement = connection.prepareStatement(query);
				statement.setInt(1, idLocationType);
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
