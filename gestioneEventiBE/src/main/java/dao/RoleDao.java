package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.RoleBean;
import beans.UserBean;

public class RoleDao {
	private Connection connection;
	private String query;
	PreparedStatement statement = null;
	ResultSet result = null;
	
	public RoleDao(Connection connection) {
		super();
		this.connection = connection;
	}	
	
	public ArrayList<RoleBean> getRole () throws SQLException {
		ArrayList <RoleBean> roleList = new ArrayList();
		
		query = "SELECT * FROM t_roles";
		
		try {
			// A prepared statement is used here because the query contains parameters
			statement = connection.prepareStatement(query);
			// This sets the article's code as first parameter of the query
			
			result = statement.executeQuery();
			// If there is a match the entire row is returned here as a result
			while(result.next()) {
				// Here an Article object is initialized and the attributes obtained from the database are set
				int idRole = result.getInt("id_role");
				String role = result.getString("code");
				RoleBean roles = new RoleBean( idRole, role);
				roleList.add(roles);
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
		
		return roleList;
	}
	
	public RoleBean getRoleById (int idRole) throws SQLException {
		RoleBean role =null;
		String query = "SELECT * FROM t_roles WHERE id_role = ?";
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, idRole);
			result = statement.executeQuery();
			while(result.next()) {
				String roleCode = result.getString("code");	
				role = new RoleBean(idRole, roleCode); 
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
		return role;
	}
	
	public boolean addRole( String code) throws SQLException
	{
			String query = "INSERT INTO t_roles (code) VALUES(?)";
			int r=0;
			try {
				statement = connection.prepareStatement(query);
				//statement.setInt(1, id_person);
				statement.setString(1, code);
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
	
	public boolean deleteRole (int idRole) throws SQLException {
		String query ="DELETE FROM t_roles WHERE id_roles = ?";
		int r = 0;
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, idRole);
			r = statement.executeUpdate();
			if (r>0) {
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
