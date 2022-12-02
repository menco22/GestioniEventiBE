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
public class LocationController extends HttpServlet {
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
		LocationDao locationDao = new LocationDao(this.connection);
		String locationResponse = "";
		if(auth.checkToken(request)==true) {
			String id = request.getParameter("id");
			if(id == null) {
				try { 
					String orderBy = request.getParameter("orderBy");
					String orderDirection = request.getParameter("orderDirection");
					if(orderBy == null) {
						orderBy = "id_location";
					}
					if (orderDirection == null) {
						orderDirection = "asc";
					}
					ArrayList<LocationBean> locationList = locationDao.getLocations(orderBy, orderDirection);
					locationResponse = new Gson().toJson(locationList);
	
				} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
				response.getWriter().append(locationResponse);
			}else {
				try {
					LocationBean location  = locationDao.getLocationById(Integer.parseInt(id));
					locationResponse = new Gson().toJson(location);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
					response.getWriter().append(locationResponse);
			}
		
			
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
			LocationDao locationDao = new LocationDao(this.connection);
			StringBuilder buffer = new StringBuilder();
		    BufferedReader reader = request.getReader();
		    String line;
		    String id = request.getParameter("id");	
			String action = request.getParameter("action");
			System.out.println(id + " " + action);
			if(id == null && action == null) {
			    while ((line = reader.readLine()) != null) {
			    	buffer.append(line);
			    	buffer.append(System.lineSeparator());
			    }
			    String data = buffer.toString();
			    NewLocationBean newLocation = null;
			    boolean addedLocation = false;
			    Gson datas = new Gson();
			    try {
			    	newLocation = datas.fromJson(data, NewLocationBean.class);
			    	addedLocation = locationDao.addLocation(newLocation.getLocationName(), newLocation.getAddress(), newLocation.getLocationType());
			    	if(addedLocation == true) {
			    		System.out.println("Location aggiunta con successo");
			    	}else {
						System.out.println("Aggiunta fallita");
					}
			    }catch(JsonSyntaxException | SQLException e) {
			    	e.printStackTrace();
			    }  
			}else if(id != null && action.equalsIgnoreCase("update")) {
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
					buffer.append(System.lineSeparator());
				}
				String data = buffer.toString();
				NewLocationBean newDetailLocation= null;
				boolean updatedLocation = false;
				Gson datas = new Gson();
				try {
					newDetailLocation = datas.fromJson(data, NewLocationBean.class);
					updatedLocation = locationDao.updateLocation(Integer.parseInt(id), newDetailLocation.getLocationName(),
							newDetailLocation.getAddress(), newDetailLocation.getLocationType());
					if(updatedLocation == true) {
						System.out.println("Dati Location modificati con successo");
					}else {
						System.out.println("Aggiornamento non avvenuto");
					}
				}catch(JsonSyntaxException | SQLException e) {
					e.printStackTrace();
				}
			}else if(action.equalsIgnoreCase("delete") && id != null) {
				boolean deletedLocation = false;
				try {
					deletedLocation = locationDao.deleteLocation(Integer.parseInt(id));
					if(deletedLocation == true) {
						System.out.println("Location rimossa con successo");
					}else {
						System.out.println("Eliminazione non avvenuta");
					}
				}catch(JsonSyntaxException | SQLException e) {
					e.printStackTrace();
				}
			}else	if(action != null || id == null) {
				if(action.equalsIgnoreCase("delete") || action.equalsIgnoreCase("update")) {
					response.sendError(400, "Specificare la location");
				}else if(id == null){
					response.sendError(400, "Azione non valida e  location non specificata");
				}else if(id != null) {
					response.sendError(400,"Azione non valida sulla location specificata");
				}
			}		
		}else {
			response.sendError(401, "Effettuare il login");
		}
	}
}
