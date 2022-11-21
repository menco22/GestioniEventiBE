package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.TableBean;
import beans.UserBean;

public class TableDao {
	private Connection connection;
	private String query;
	PreparedStatement statement = null;
	ResultSet result = null;	
	
	public TableDao(Connection connection) {
		super();
		this.connection = connection;
	}
	
	public ArrayList<TableBean> getTables (String orderBy, String orderDirection) throws SQLException {
		ArrayList <TableBean> userList = new ArrayList();
		
		query = "SELECT * FROM t_tables Order by " + orderBy + " " + orderDirection;
		
		try {
			// A prepared statement is used here because the query contains parameters
			statement = connection.prepareStatement(query);
			// This sets the article's code as first parameter of the query
			
			result = statement.executeQuery();
			// If there is a match the entire row is returned here as a result
			while(result.next()) {
				// Here an Article object is initialized and the attributes obtained from the database are set
				int idTable = result.getInt("id_table");
				int tableCapacity = result.getInt("table_capacity");
				int eventId= result.getInt("id_event");
				TableBean table = new TableBean( idTable, tableCapacity, eventId);
				userList.add(table);
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
	
	public TableBean getTableById (int idTable) throws SQLException {
		TableBean table = null;
		String query = "SELECT * FROM t_tables WHERE id_table = ?";
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, idTable);
			result = statement.executeQuery();
			while(result.next()) {
				int tableCapacity = result.getInt("table_capacity");
				int idEvent = result.getInt("id_event");
				table = new TableBean (idTable, tableCapacity, idEvent);
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
		return table;
	}
	
	public ArrayList <TableBean> getTableByCapacity (int tableCapacity) throws SQLException {
		ArrayList <TableBean> tableList = new ArrayList();
		String query = "SELECT * FROM t_tables WHERE table_capacity = ?";
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, tableCapacity);
			result = statement.executeQuery();
			while(result.next()) {
				int idTable = result.getInt("id_table");
				int idEvent = result.getInt("id_event");
				TableBean table = new TableBean (idTable, tableCapacity, idEvent);
				tableList.add(table);
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
		return tableList;
	}
	
	public boolean addTable(int tableCapacity, int idEvent) throws SQLException
	{
			String query = "INSERT INTO t_tables (table_capacity, id_event) VALUES(?, ?)";
			int r=0;
			try {
				statement = connection.prepareStatement(query);
				//statement.setInt(1, id_person);
				statement.setInt(1, tableCapacity);
				statement.setInt(2, idEvent);
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
	
	public boolean updateTable (int idTable, int tableCapacity, int idEvent) throws SQLException {
		String query = "UPDATE t_tables SET table_capacity=?, id_event=? WHERE id_table=?";
		int r = 0;
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, tableCapacity);
			statement.setInt(2, idEvent);
			statement.setInt(3, idTable);
			r = statement.executeUpdate();
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
	
	public boolean deleteTable (int idTable) throws SQLException {
		String query = "DELETE FROM t_tables WHERE id_table = ?";
		int r = 0;
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, idTable);
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
