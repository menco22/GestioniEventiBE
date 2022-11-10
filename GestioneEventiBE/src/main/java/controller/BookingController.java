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

import beans.BookingBean;
import beans.NewBookingBean;
import beans.NewLocationBean;
import dao.BookingDao;

/**
 * Servlet implementation class BookingController
 */
public class BookingController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookingController() {
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
		AuthenticationController auth = new AuthenticationController (request);
		if(auth.checkToken(request)==true) {
			BookingDao bookingDao = new BookingDao(this.connection);
			String bookingResponse = "";
			try {
				ArrayList <BookingBean> bookingList = bookingDao.getBooking();
				bookingResponse = new Gson().toJson(bookingList);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		response.getWriter().append(bookingResponse);
		}else {
			response.sendError(401,"Effettuare login");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		AuthenticationController auth = new AuthenticationController (request);
		if(auth.checkToken(request)==true) {
			connectToDb();
			StringBuilder buffer = new StringBuilder();
		    BufferedReader reader = request.getReader();
		    String line;
		    while ((line = reader.readLine()) != null) {
		        buffer.append(line);
		        buffer.append(System.lineSeparator());
		    }
		    String data = buffer.toString();
		    BookingDao newBookingDao = new BookingDao(this.connection);
		    NewBookingBean newBooking = null;
		    boolean addedBooking = false;
		    Gson datas = new Gson();
		    try {
		    	newBooking = datas.fromJson(data, NewBookingBean.class);
		    	addedBooking = newBookingDao.addBooking(newBooking.getCode(), newBooking.getBookingType(), 
		    			                newBooking.getIdUser(), newBooking.getIdEvent(), newBooking.getIdTable());
		    	if(addedBooking == true) {
		    		System.out.println("Booking aggiunto con successo");
		    	}
		    }catch(JsonSyntaxException | SQLException e) {
				e.printStackTrace();
		    }
		//doGet(request, response);
		}else {
			response.sendError(401, "Effettuare il login");
		}
	}
}
