package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.TableBean;

public class TableDao {
	private Connection connection;
	private String query;
	PreparedStatement statement = null;
	ResultSet result = null;	
	
	public TableDao(Connection connection) {
		super();
		this.connection = connection;
	}
	
	public TableDao() {
		String dbUrl = "jdbc:mysql://localhost:3306/laurea";
		String dbClass = "com.mysql.cj.jdbc.Driver";
		try {
			Class.forName(dbClass);
			connection = DriverManager.getConnection(dbUrl, "root", "root");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//funzione che restituisce tutti i tavoli presenti con ordinamento di default o scelto dall'utente
	public ArrayList<TableBean> getTables (String orderBy, String orderDirection) throws SQLException {
		ArrayList <TableBean> tableList = new ArrayList();
		
		query = "SELECT * FROM t_tables WHERE deleted=false AND booked=false Order by " + orderBy + " " + orderDirection;
		
		try {
			statement = connection.prepareStatement(query);
			result = statement.executeQuery();
		
			while(result.next()) {
				int idTable = result.getInt("id_table");
				int tableCapacity = result.getInt("table_capacity");
				int eventId= result.getInt("id_event");
				TableBean table = new TableBean( idTable, tableCapacity, eventId);
				tableList.add(table);
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
		
		return tableList;
	}
	
	public ArrayList<TableBean> getTablesByEvent (String orderBy, String orderDirection, int idEvent) throws SQLException {
		ArrayList <TableBean> tableList = new ArrayList();
		query = "SELECT * FROM t_tables WHERE deleted=false AND booked=false AND id_event=? Order by " + orderBy + " " + orderDirection;
		if(idEvent == 0) {
			return null;
		}
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, idEvent);
			result = statement.executeQuery();
		
			while(result.next()) {
				int idTable = result.getInt("id_table");
				int tableCapacity = result.getInt("table_capacity");
				TableBean table = new TableBean( idTable, tableCapacity, idEvent);
				tableList.add(table);
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
		
		return tableList;
	}
	
	//funzione che restituisce il tavolo corrispondente all'id passatogli
	public TableBean getTableById (int idTable) throws SQLException {
		TableBean table = null;
		String query = "SELECT * FROM t_tables WHERE id_table = ? AND deleted=false";
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, idTable);//imposta l'id del tavolo ricercato
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
	
	/*public ArrayList <TableBean> getTableByCapacity (int tableCapacity) throws SQLException {
		ArrayList <TableBean> tableList = new ArrayList();
		String query = "SELECT * FROM t_tables WHERE table_capacity = ? AND deleted=false";
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
	}*/
	
	//funzione per l'aggiunta di un nuovo tavolo
	public boolean addTable(int tableCapacity, int idEvent) throws SQLException
	{
			String query = "INSERT INTO t_tables (table_capacity, id_event) VALUES(?, ?)";
			int r=0;
			try {
				statement = connection.prepareStatement(query);
				statement.setInt(1, tableCapacity); //passo i valori corrispondenti a ? nella query
				statement.setInt(2, idEvent);
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
	
	//funzione per la modifca dei dati relativi ad un determinato tavolo
	public boolean updateTable (int idTable, int tableCapacity, int idEvent) throws SQLException {
		String query = "UPDATE t_tables SET table_capacity=?, id_event=? WHERE id_table=? AND deleted=false";
		int r = 0;
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, tableCapacity);//passo i nuovi valori e l'id del tavolo da modificare
			statement.setInt(2, idEvent);
			statement.setInt(3, idTable);
			r = statement.executeUpdate();
			// se r>0 significa che almeno una riga è stata modificata, nel nostro caso ciò significa che la modifica è avvenuta con successo
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
	
	public boolean unbookTable(int idTable) throws SQLException{
		String query = "UPDATE t_tables SET booked = false WHERE id_table=? AND deleted=false AND booked = true";
		int r=0;
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, idTable);//passo l'id del tavolo da eliminare
			r = statement.executeUpdate();
			// se r>0 significa che almeno una riga è stata modificata, nel nostro caso ciò significa che l'eliminazione è avvenuta con successo
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
	
	public boolean bookTable (int idTable) throws SQLException {
		String query = "UPDATE t_tables SET booked = true WHERE id_table=? AND deleted=false";
		int r=0;
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, idTable);//passo l'id del tavolo da eliminare
			r = statement.executeUpdate();
			// se r>0 significa che almeno una riga è stata modificata, nel nostro caso ciò significa che l'eliminazione è avvenuta con successo
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
	
	//funzione per l'eliminazione di un tavolo, anche qui eliminazione logica
	public boolean deleteTable (int idTable) throws SQLException {
		String query = "UPDATE t_tables SET deleted = true WHERE id_table = ?";
		int r = 0;
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, idTable);//passo l'id del tavolo da eliminare
			r = statement.executeUpdate();
			// se r>0 significa che almeno una riga è stata modificata, nel nostro caso ciò significa che l'eliminazione è avvenuta con successo
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
