package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import beans.UserBean;

public class UserDao {
	private Connection connection;
	private String query;
	PreparedStatement statement = null;
	ResultSet result = null;
	
	public UserDao(Connection connection) {
		super();
		this.connection = connection;
	}
	
	public ArrayList<UserBean> getUserList () throws SQLException {
		ArrayList <UserBean> userList = new ArrayList();
		
		query = "SELECT * FROM t_users";
		
		try {
			// A prepared statement is used here because the query contains parameters
			statement = connection.prepareStatement(query);
			// This sets the article's code as first parameter of the query
			
			result = statement.executeQuery();
			// If there is a match the entire row is returned here as a result
			while(result.next()) {
				// Here an Article object is initialized and the attributes obtained from the database are set
				int idUser = result.getInt("id_user");
				String name = result.getString("name");
				String surname = result.getString("surname");
				String email = result.getString("email");
				String username = result.getString("username");
				String password = result.getString("password");
				int roleId = result.getInt("role_id");
				UserBean user = new UserBean( idUser, name, surname, email, username, password, roleId);
				userList.add(user);
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
		
		return userList;
	}
	
	public UserBean getUser(String username, String password) throws SQLException {
		UserBean user = null;
		query = "SELECT * FROM t_users WHERE username=? and password=?";
		int r=0;
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, username);
			statement.setString(2, password);
			result = statement.executeQuery();
			while(result.next()) {
				user = new UserBean(result.getInt("id_user"), result.getString("name"), result.getString("surname"), result.getString("email"),result.getString("username"), result.getString("password"), result.getInt("role_id"));
				/*int idUser = result.getInt("id_user");
				String name = result.getString("name");
				String surname = result.getString("surname");
				String email = result.getString("email");
				username = result.getString("username");
				password = result.getString("password");
				int roleId = result.getInt("role_id");
				user = new UserBean( idUser, name, surname, email, username, password, roleId);*/
				
				
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
		return user;
	}
	
	public boolean addUser( String name, String surname, String email, String username, String password, int roleId) throws SQLException
	{
			String query = "INSERT INTO t_users (name, surname, email, username, password, role_id) VALUES(?, ?, ?, ?, ?, ?)";
			int r=0;
			try {
				statement = connection.prepareStatement(query);
				//statement.setInt(1, id_person);
				statement.setString(1, name);
				statement.setString(2, surname);
				statement.setString(3, email);
				statement.setString(4, username);
				statement.setString(5, password);
				statement.setInt(6, roleId);
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
