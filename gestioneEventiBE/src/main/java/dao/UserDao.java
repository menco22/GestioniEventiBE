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
	
	//funzione che recupere lo user cosrrispondente allo username passatogli
	public UserBean getUserByUsername (String username) throws SQLException{
		String query = "SELECT * FROM t_users WHERE username=?";
		UserBean user = null;
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, username);
			result=statement.executeQuery();
			while(result.next()) {
				int idUser = result.getInt("id_user");
				String name = result.getString("name");
				String surname = result.getString("surname");
				String email = result.getString("email");
				String password = result.getString("password");
				int roleId = result.getInt("role_id");
				user = new UserBean(idUser, name, surname, email, username, password, roleId);
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
		return user;
	}
	
	//funzione che restituisce lo user corrispondente all'id passatogli
	public UserBean getUserById (int idUser) throws SQLException {
		String query = "SELECT * FROM t_users WHERE id_user=?";
		UserBean user = null;
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, idUser);
			result=statement.executeQuery();
			while(result.next()) {
				String name = result.getString("name");
				String surname = result.getString("surname");
				String email = result.getString("email");
				String username = result.getString("username");
				String password = result.getString("password");
				int roleId = result.getInt("role_id");
				user = new UserBean(idUser, name, surname, email, username, password, roleId);
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
		return user;
	}
	
	//funzione che restituisce la lista di tutti gli utenti registrati con ordinamento predefinito o scelto dall'utente
	public ArrayList<UserBean> getUserList (String orderBy, String orderDirection) throws SQLException {
		ArrayList <UserBean> userList = new ArrayList();
		
		query = "SELECT * FROM t_users Order by " + orderBy + " " + orderDirection ;
		
		try {
			statement = connection.prepareStatement(query);
			result = statement.executeQuery();
		
			while(result.next()) {
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
	
	public int getUserRole(int idUser) throws SQLException {
		int role = 0;
		query = "SELECT role_id FROM t_users WHERE id_user=?";
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, idUser);
			result = statement.executeQuery();
			while(result.next()){
				role = result.getInt("role_id");		
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
		
		return role;
	}
	
	//funzione che dati username e password restituisce l'utente associato
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
				int idUser = result.getInt("id_user");
				String name = result.getString("name");
				String surname = result.getString("surname");
				String email = result.getString("email");
				int roleId = result.getInt("role_id");
				user = new UserBean(idUser, name, surname, email, username, password, roleId);	
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
	
	//funzione per la modifica dei dati di un determinato utente
	public boolean updateUser(int idUser, String name, String surname, String email, String username, String password, int roleId ) throws SQLException{
		String query = "UPDATE t_users SET name =?, surname =?, email =?, username=?, password=?, role_id =? WHERE id_user=?";
		int r=0;
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, name);
			statement.setString(2, surname);
			statement.setString(3, email);
			statement.setString(4,username);
			statement.setString(5, password);
			statement.setInt(6, roleId);
			statement.setInt(7, idUser);
			r = statement.executeUpdate();
			// se r>0 significa che almeno una riga è stata modificata, nel nostro caso ciò significa che la modifica è avvenuta con successo
			if (r>0) {
				return true;
			}else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				statement.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	if (r>0) {
			return true;
		}else {
			return false;
		}
	}
	
	//funzione per l'aggiunta di un nuovo utente
	public boolean addUser( String name, String surname, String email, String username, String password, int roleId) throws SQLException
	{
			String query = "INSERT INTO t_users (name, surname, email, username, password, role_id) VALUES(?, ?, ?, ?, ?, ?)";
			int r=0;
			try {
				statement = connection.prepareStatement(query);
				statement.setString(1, name);
				statement.setString(2, surname);
				statement.setString(3, email);
				statement.setString(4, username);
				statement.setString(5, password);
				statement.setInt(6, roleId);
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

	//funzione per l'eliminazione dell'utente corrispondente all'id passato
	public boolean deleteUser (int idUser) throws SQLException {
		String query = "DELETE FROM t_users WHERE id_user = ?";
		int r = 0;
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, idUser);
			r = statement.executeUpdate();
			// se r>0 significa che almeno una riga è stata modificata, nel nostro caso ciò significa che l'eliminazione è avvenuta con successo
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
