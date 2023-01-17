package controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.GenericServlet;

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
import controller.AuthenticationController;

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
		AuthenticationController auth = new AuthenticationController(request);
		EventDao eventDao = new EventDao(this.connection);
		String eventResponse ="";
		// controllo se l'utente è loggato
		if(auth.checkToken(request)==true) {
			System.out.println(auth.getIdUser(request));
			System.out.println(auth.isAdmin(request));
			String id = request.getParameter("id");	
			if(id != null) {
				// get dell'evento corrispondente all'id dato
				try {
					EventBean event = eventDao.getEventById(Integer.parseInt(id));
					eventResponse = new Gson().toJson(event);
				}catch (SQLException e) {
					e.printStackTrace();
				}
				response.getWriter().append(eventResponse);
				
			}else {
				if(auth.isAdmin(request) == true) {
					// utente è admin -> lista degli eventi da lui creati
					try {
						String orderBy = request.getParameter("orderBy");
						String orderDirection = request.getParameter("orderDirection");
						if(orderBy == null) {
							orderBy = "id_event";
						}
						if (orderDirection == null) {
							orderDirection = "asc";
						}
						ArrayList <EventBean> eventList = eventDao.getEventsByCreator(orderBy, orderDirection, auth.getIdUser(request));
						eventResponse = new Gson().toJson(eventList);
					}catch (SQLException e) {
						e.printStackTrace();
					}
					response.getWriter().append(eventResponse);
				}else {
					// utente non admin -> lista completa di tutti gli eventi
					try {
						String orderBy = request.getParameter("orderBy");
						String orderDirection = request.getParameter("orderDirection");
						if(orderBy == null) {
							orderBy = "id_event";
						}
						if (orderDirection == null) {
							orderDirection = "asc";
						}
						ArrayList <EventBean> eventList = eventDao.getEvents(orderBy, orderDirection);
						eventResponse = new Gson().toJson(eventList);
					}catch (SQLException e) {
						e.printStackTrace();
					}
					response.getWriter().append(eventResponse);
				}
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
			System.out.println(auth.getIdUser(request));
			//connectToDb();
			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = request.getReader();
			String line;
			String id = request.getParameter("id");	
			String action = request.getParameter("action");	
			EventDao eventDao = new EventDao(this.connection);
			if(action == null && id == null) { 
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
					buffer.append(System.lineSeparator());
				}
				String data = buffer.toString();
				NewEventBean newEvent = null;
				boolean addedEvent = false;
				Gson datas = new Gson();
				try {
					newEvent = datas.fromJson(data, NewEventBean.class );
					addedEvent = eventDao.addEvent(auth.getIdUser(request), newEvent.getIdLocation(),newEvent.getEventName(),newEvent.getDate(), newEvent.getDataScadenza(), newEvent.getStandingPlaces());
					if(addedEvent == true ) {
						System.out.println("Evento aggiunto con successo");
					}else {
						System.out.println("Aggiunta fallita");
					}
				}catch(JsonSyntaxException | SQLException e) {
					e.printStackTrace();
				}
			}else if(id != null && action.equalsIgnoreCase("delete")){
    			boolean deleteEvent = false;
				try {
					deleteEvent = eventDao.deleteEvent(Integer.parseInt(id));
					if(deleteEvent == true) {
						System.out.println("Evento rimosso con successo");
					}else {
						System.out.println("Eliminazione non avvenuta");
					}
				} catch (NumberFormatException e) {
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
				NewEventBean newDetailEvent = null;
				boolean updatedEvent = false;
				Gson datas = new Gson();
				try {
					newDetailEvent = datas.fromJson(data, NewEventBean.class );
					updatedEvent = eventDao.updateEvent(Integer.parseInt(id), auth.getIdUser(request),
					newDetailEvent.getIdLocation(), newDetailEvent.getEventName(), newDetailEvent.getDate(),
					newDetailEvent.getDataScadenza(), newDetailEvent.getStandingPlaces());
					if(updatedEvent == true) {
						System.out.println("Dati Evento aggiornati con successo");
					}else {
						System.out.println("Aggiornamento non avvenuto");
					}
				}catch(JsonSyntaxException | SQLException e) {
					e.printStackTrace();
				}
            }else	if(action != null || id == null) {
				if(action.equalsIgnoreCase("delete") || action.equalsIgnoreCase("update")) {
					response.sendError(400, "Specificare evento");
				}else if(id == null){
					response.sendError(400, "Azione non valida e evento non specificato");
				}else if(id != null) {
					response.sendError(400,"Azione non valida su evento specificato");
				}
			}
	}else {
		response.sendError(401, "Effettuare Login");
	}
 }
}
