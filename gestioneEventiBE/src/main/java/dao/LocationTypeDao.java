package dao;

import java.sql.Connection;
import java.sql.DriverManager;
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
		
		public LocationTypeDao() {
			String dbUrl = "jdbc:mysql://localhost:3306/laurea";
			String dbClass = "com.mysql.cj.jdbc.Driver";
			try {
				Class.forName(dbClass);
				connection = DriverManager.getConnection(dbUrl, "root", "root");
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		// funzione per recuperare tutti i tipi di ocations contenuti nell'apposita tabella con ordine specificato dall'utente 
		//(in caso non venga specificato è previsto un ordinamento di default)
		public ArrayList<LocationTypeBean> getLocationType (String orderBy, String orderDirection) throws SQLException {
			ArrayList <LocationTypeBean> typeList = new ArrayList();
			
			query = "SELECT * FROM t_location_types WHERE deleted = false Order by " + orderBy + " " + orderDirection;
			
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
		
		//funzione che restituisce il tipo di location relativo all'id passatole
		public LocationTypeBean getTypeById (int idType) throws SQLException {
			LocationTypeBean type = null;
			String query = "SELECT * FROM t_location_types WHERE id_location_type = ? AND deleted = false";
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
		
		//funzione per l'aggiunta di un nuovo tipo di location
		public boolean addType( String description) throws SQLException
		{
				String query = "INSERT INTO t_location_types (description) VALUES(?)";
				int r=0;
				try {
					statement = connection.prepareStatement(query);
					statement.setString(1, description);//imposto i valori del nuovo tipo
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
		
		//funzione per l'aggiornamento di un tipo corrispondente all'id passato
		public boolean updateLocationType (int idLocationType, String description) throws SQLException{
			String query = "UPDATE t_location_types SET description=? WHERE id_location_type=? AND deleted = false";
			int r = 0;
			try {
				statement = connection.prepareStatement(query);
				statement.setString(1, description);//imposto il nuovo valore e l'id del tipo da modificare
				statement.setInt(2, idLocationType);
				r=statement.executeUpdate();
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
		
		//funzione per l'eliminazione, anche in questo caso si procede con un'eliminazione logica ponendo un flag a true
		//se procedessimo con un'eliminazione vera e propria dovremmo eliminare tutte le location di quel tipo e,a cascata, tutti gli eventi collegati a quelle location
		public boolean deleteLocationType (int idLocationType) throws SQLException {
			String query = "UPDATE t_location_types SET deleted=true WHERE id_location_type=?";
			int r = 0;
			try {
				statement = connection.prepareStatement(query);
				statement.setInt(1, idLocationType);//imposto l'id del tipo da eliminare
				r = statement.executeUpdate();
				// se r>0 significa che almeno una riga è stata modificata, nel nostro caso ciò significa che l'eliminazione  è avvenuta con successo
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
