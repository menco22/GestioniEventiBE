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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import beans.EventBean;
import beans.NewEventBean;
import beans.RegistrationBean;
import dao.EventDao;
import dao.UserDao;

/**
 * Servlet implementation class EventController
 */
public class EventController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EventController() {
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
		EventDao eventDao = new EventDao(this.connection);
		String eventResponse ="";
		try {
			ArrayList <EventBean> eventList = eventDao.getEvents();
			//System.out.println(eventList);
			   eventResponse = new Gson().toJson(eventList);
				
				
		}catch (SQLException e) {
			e.printStackTrace();
		}
		response.getWriter().append(eventResponse);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		connectToDb();
		StringBuilder buffer = new StringBuilder();
	    BufferedReader reader = request.getReader();
	    String line;
	    while ((line = reader.readLine()) != null) {
	        buffer.append(line);
	        buffer.append(System.lineSeparator());
	    }
	    String data = buffer.toString();
		EventDao newEventDao = new EventDao(this.connection);
		NewEventBean newEvent = null;
		boolean addedEvent = false;
		Gson datas = new Gson();
		try {
			newEvent = datas.fromJson(data, NewEventBean.class );
			addedEvent = newEventDao.addEvent(newEvent.getIdCreator(), newEvent.getIdLocation(),newEvent.getEventName(),newEvent.getDate());
			if(addedEvent == true ) {
				System.out.println("Evento aggiunto con successo");
				}
			}catch(JsonSyntaxException | SQLException e) {
				e.printStackTrace();
			}
		//doGet(request, response);
	}

}
