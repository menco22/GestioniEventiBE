package dao;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import beans.FeedbackBean;

public class FeedbackDaoTest {

	@Test
	public void testGetFeedback() throws SQLException {
		FeedbackDao feedbackDao = new FeedbackDao ();
		ArrayList <FeedbackBean> feedbackList = new ArrayList<>();
		String orderBy = "id_feedback";
		String orderDirection = "asc";
		feedbackList = feedbackDao.getFeedback(orderBy, orderDirection);
		assertEquals(1, feedbackList.size());
	}

	@Test
	public void testGetFeedbackById() throws SQLException {
		FeedbackDao feedbackDao = new FeedbackDao ();
		// caso id = 0
		FeedbackBean feedback1 = feedbackDao.getFeedbackById(0);
		assertNull(feedback1);
		
		//caso id non presente
		FeedbackBean feedback2 = feedbackDao.getFeedbackById(10);
		assertNull(feedback2);
		
		//caso id presente
		FeedbackBean feedback3 = feedbackDao.getFeedbackById(1);
		assertEquals(1, feedback3.getIdFeedback());
		assertEquals(9, feedback3.getIdCreator());
		assertEquals(7, feedback3.getIdBooking());
		assertEquals(5, feedback3.getEvaluation());
		assertEquals("tutto molto bello",feedback3.getDescription());
	}

}
