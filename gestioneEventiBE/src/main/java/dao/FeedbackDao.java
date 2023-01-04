package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.FeedbackBean;

public class FeedbackDao {
	private Connection connection;
	private String query;
	PreparedStatement statement = null;
	ResultSet result = null;
	
	public FeedbackDao(Connection connection) {
	super();
	this.connection = connection;
	}
	
	public FeedbackDao (){
		String dbUrl = "jdbc:mysql://localhost:3306/laurea";
		String dbClass = "com.mysql.cj.jdbc.Driver";
		try {
			Class.forName(dbClass);
			connection = DriverManager.getConnection(dbUrl, "root", "root");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// funzione per recuperare tutti i feedback contenuti nell'apposita tabella con ordine specificato dall'utente 
	//(in caso non venga specificato è previsto un ordinamento di default)
	public ArrayList<FeedbackBean> getFeedback (String orderBy, String orderDirection) throws SQLException {
		ArrayList <FeedbackBean> feedbackList = new ArrayList <>();
		
		String query = "SELECT * FROM t_feedbacks Order by " + orderBy + " " + orderDirection;
		
		try {
			statement = connection.prepareStatement(query);
			result = statement.executeQuery();

			while(result.next()) {
				int idFeedback = result.getInt("id_feedback");
				int idCreator = result.getInt("id_creator");
				int idBooking = result.getInt("id_booking");
				int evaluation = result.getInt("evaluation");
				String description = result.getString("description");
				FeedbackBean feedback = new FeedbackBean( idFeedback, idCreator, idBooking, evaluation, description);
				feedbackList.add(feedback);
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
		
		return feedbackList;
	}
	
	//funzione che restituisce il feedback corrispondente all'id passatole
	public FeedbackBean getFeedbackById(int idFeedback) throws SQLException {
		FeedbackBean feedback = null;
		String query = "SELECT * FROM t_feedbacks WHERE id_feedback=?";
		try {
			statement = connection.prepareStatement(query);//impostazione del parametro e invio della query al db
			statement.setInt(1, idFeedback);
			result = statement.executeQuery();
			while(result.next()) {
				int idCreator = result.getInt("id_creator");
				int idBooking = result.getInt("id_booking");
				int evaluation = result.getInt("evaluation");
				String description = result.getString("description");
				feedback = new FeedbackBean(idFeedback, idCreator, idBooking, evaluation, description);
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
		return feedback;
		
	}
	
	//funzione che restituisce il o i feedback/s corrispondenti a una certa prenotazione
	public ArrayList<FeedbackBean> getFeedbackByBooking (int idBooking) throws SQLException {
		String query = "SELECT * FROM t_feedbacks WHERE id_booking=?";
		ArrayList <FeedbackBean> feedbackList = new ArrayList();
		FeedbackBean feedback = null;
		try {
			statement = connection.prepareStatement(query);//impostazione del parametro e invio della query al db
			statement.setInt(1, idBooking);
			result = statement.executeQuery();
			while(result.next()) {
				int idFeedback = result.getInt("id_feedback");
				int idCreator = result.getInt("id_creator");
				idBooking = result.getInt("id_booking");
				int evaluation = result.getInt("evaluation");
				String description = result.getString("description");
				feedback = new FeedbackBean(idFeedback, idCreator, idBooking, evaluation, description);
				feedbackList.add(feedback);
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
		return feedbackList;
	}
	
	
	//funzione per l'aggiunta di  un nuovo feedback
	public boolean addFeedback(int idCreator, int idBooking, int evaluation, String description) throws SQLException
	{
			String query = "INSERT INTO t_feedbacks (id_creator, id_booking, evaluation, description) VALUES(?, ?, ?, ?)";
			int r=0;
			try {
				statement = connection.prepareStatement(query);//imposta i vari valori corrispondenti ai ? nella quey
				statement.setInt(1, idCreator);
				statement.setInt(2,idBooking);
				statement.setInt(3, evaluation);
				statement.setString(4, description);
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
	
	public boolean updateFeedback (int idFeedback, int idCreator, int idBooking, int evaluation, String description) throws SQLException {
		String query = "UPDATE t_feedbacks SET id_creator=?, id_booking=?, evaluation=?, description=? WHERE id_feedback=?";
		int r = 0;
		try {
			statement = connection.prepareStatement(query);//imposta i vari valori corrispondenti ai ? nella query e l'id del feedback da modificare
			statement.setInt(1, idCreator);
			statement.setInt(2, idBooking);
			statement.setInt(3, evaluation);
			statement.setString(4, description);
			statement.setInt(5, idFeedback);
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
	
	//funzione per l'eliminazione di un dato feedback
	public boolean deleteFeedback (int idFeedback) throws SQLException {
		String query = "DELETE FROM t_feedbacks WHERE id_feedback = ?";
		int r = 0;
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, idFeedback);//imposta l'id del feedback da cancellare
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
