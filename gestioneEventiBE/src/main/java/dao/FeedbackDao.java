package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.FeedbackBean;
import beans.UserBean;

public class FeedbackDao {
	private Connection connection;
	private String query;
	PreparedStatement statement = null;
	ResultSet result = null;
	
	public FeedbackDao(Connection connection) {
	super();
	this.connection = connection;
	}
	
	public ArrayList<FeedbackBean> getFeedback () throws SQLException {
		ArrayList <FeedbackBean> feedbackList = new ArrayList();
		
		query = "SELECT * FROM t_feedbacks";
		
		try {
			// A prepared statement is used here because the query contains parameters
			statement = connection.prepareStatement(query);
			// This sets the article's code as first parameter of the query
			
			result = statement.executeQuery();
			// If there is a match the entire row is returned here as a result
			while(result.next()) {
				// Here an Article object is initialized and the attributes obtained from the database are set
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
	
	public boolean addFeedback(int idCreator, int idBooking, int evaluation, String description) throws SQLException
	{
			String query = "INSERT INTO t_feedbacks (id_creator, id_booking, evaluation, description) VALUES(?, ?, ?, ?)";
			int r=0;
			try {
				statement = connection.prepareStatement(query);
				//statement.setInt(1, id_person);
				statement.setInt(1, idCreator);
				statement.setInt(2,idBooking);
				statement.setInt(3, evaluation);
				statement.setString(4, description);
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
