package controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;

import beans.FeedbackBean;
import dao.FeedbackDao;

/**
 * Servlet implementation class FeedbackController
 */
public class FeedbackController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection; 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FeedbackController() {
        super();
        // TODO Auto-generated constructor stub
    }

    private Connection connectToDb(){
		try {
			// The following lines takes the parameters used to log into the database
			ServletContext context = getServletContext();
			String driver = context.getInitParameter("dbDriver");
			String url = context.getInitParameter("dbUrl");
			String user = context.getInitParameter("dbUser");
			String password = context.getInitParameter("dbPassword");
			// This initializes the driver used to interact with the database
			Class.forName(driver);
			// This returns the connection
			return DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			return null;
		}
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		AuthenticationController auth = new AuthenticationController(request);
		FeedbackDao feedbackDao = new FeedbackDao(this.connection);
		String feedbackResponse = "";
		if(auth.checkToken(request)==true) {
			String id = request.getParameter("id");	
			if(id != null) {
				try {
					FeedbackBean feedback = feedbackDao.getFeedbackById(Integer.parseInt(id));
					feedbackResponse = new Gson().toJson(feedback);
				}catch (SQLException e) {
					e.printStackTrace();
				}
				response.getWriter().append(feedbackResponse);
			}else {
				try {
				String orderBy = request.getParameter("orderBy");
				String orderDirection = request.getParameter("orderDirection");
				if(orderBy == null) {
					orderBy = "id_feedback";
				}
				if (orderDirection == null) {
					orderDirection = "asc";
				}
				ArrayList <FeedbackBean> feedbackList = feedbackDao.getFeedback(orderBy, orderDirection);
				feedbackResponse = new Gson().toJson(feedbackList);
				}catch (SQLException e) {
					e.printStackTrace();
				}
				response.getWriter().append(feedbackResponse);
			}
		}else {
			response.sendError(401,"Effettuare il login");
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
