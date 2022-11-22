package controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import beans.FeedbackBean;
import beans.NewFeedbackBean;
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
    
    public void init()
    {
    	System.out.println("mi connetto al db");
    	connection = connectToDb();
    	System.out.println("connessione avvenuta con successo");
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
		AuthenticationController auth = new AuthenticationController(request);
		if(auth.checkToken(request)==true) {
			connectToDb();
			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = request.getReader();
			String line;
			String id = request.getParameter("id");	
			String action = request.getParameter("action");	
			System.out.println(id + " " + action);
			if(action == null && id == null) { 
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
					buffer.append(System.lineSeparator());
				}
				String data = buffer.toString();
				FeedbackDao newFeedbackDao = new FeedbackDao(this.connection);
				NewFeedbackBean newFeedback = null;
				boolean addedFeedback = false;
				Gson datas = new Gson();
				try {
					newFeedback = datas.fromJson(data, NewFeedbackBean.class);
					addedFeedback = newFeedbackDao.addFeedback(newFeedback.getIdCreator(),	newFeedback.getIdBooking(),
							newFeedback.getEvaluation(), newFeedback.getDescription());
					if(addedFeedback == true ) {
						System.out.println("Feedback aggiunto con successo");
					}else {
						System.out.println("Aggiunta fallita");
					}
				}catch(JsonSyntaxException | SQLException e) {
					e.printStackTrace();
				}
			}else if(action.equalsIgnoreCase("delete") && id != null) {
				FeedbackDao feedbackDao = new FeedbackDao (this.connection);
				boolean deleteFeedback = false;
				try {
					deleteFeedback = feedbackDao.deleteFeedback(Integer.parseInt(id));
					if(deleteFeedback == true) {
						System.out.println("Feedback rimosso con successo!");
					}else {
						System.out.println("Eliminazione non avvenuta");
					}
				}catch (NumberFormatException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(action.equalsIgnoreCase("update") && id!=null) {
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
					buffer.append(System.lineSeparator());
				}
				String data = buffer.toString();
				FeedbackDao updateFeedbackDao = new FeedbackDao(this.connection);
				NewFeedbackBean updateFeedback = null;
				boolean updatedFeedback = false;
				Gson datas = new Gson();
				try {
					updateFeedback = datas.fromJson(data, NewFeedbackBean.class);
					updatedFeedback = updateFeedbackDao.updateFeedback(Integer.parseInt(id),updateFeedback.getIdCreator(), updateFeedback.getIdBooking(), 
								updateFeedback.getEvaluation(), updateFeedback.getDescription());
					if(updatedFeedback == true) {
						System.out.println("Feedback aggiornato con successo");
					}else {
						System.out.println("Aggiornamento non avvenuto");
					}
				}catch(JsonSyntaxException | SQLException e) {
					e.printStackTrace();
				}
			}else	if(action != null || id == null) {
				if(action.equalsIgnoreCase("delete") || action.equalsIgnoreCase("update")) {
					response.sendError(400, "Specificare il feedback");
				}else if(id == null){
					response.sendError(400, "Azione non valida e feedback non specificato");
				}else if(id != null) {
					response.sendError(400,"Azione non valida sul feedback specificato");
				}
			}
		}else {
			response.sendError(401, "Effettuare Login");
		}
		
	}

}
