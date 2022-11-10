package controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.*;

import org.apache.http.protocol.HttpContext;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import beans.LocationBean;
import beans.NewLocationBean;
import dao.LocationDao;

/**
 * Servlet implementation class LocationController
 */
public class LocationController extends HttpServlet implements HttpContext {
	private static final long serialVersionUID = 1L;
    private Connection connection; 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LocationController() {
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
			LocationDao locationDao = new LocationDao(this.connection);
			String locationResponse = "";
			try { 
				Cookie[] cookies = request.getCookies();
				System.out.println(cookies);
				ArrayList<LocationBean> locationList = locationDao.getLocations();
				locationResponse = new Gson().toJson(locationList);
	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.getWriter().append(locationResponse);
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
		    LocationDao newLocationDao = new LocationDao(this.connection);
		    NewLocationBean newLocation = null;
		    boolean addedLocation = false;
		    Gson datas = new Gson();
		    try {
		    	newLocation = datas.fromJson(data, NewLocationBean.class);
		    	addedLocation = newLocationDao.addLocation(newLocation.getLocationName(), newLocation.getLocationAddress(), newLocation.getLocationType());
		    	if(addedLocation == true) {
		    		System.out.println("Location aggiunta con successo");
		    	}
		    }catch(JsonSyntaxException | SQLException e) {
				e.printStackTrace();
		    }
		}else {
			response.sendError(401, "Effettuare il login");
		}
		//doGet(request, response);
	}

	@Override
	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object removeAttribute(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
