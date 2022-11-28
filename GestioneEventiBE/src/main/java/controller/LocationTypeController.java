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

import beans.LocationTypeBean;
import beans.NewTypeBean;
import dao.LocationTypeDao;

/**
 * Servlet implementation class LocationTypeController
 */
public class LocationTypeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LocationTypeController() {
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
		LocationTypeDao typeDao = new LocationTypeDao(this.connection);
		String typeResponse = "";
		if(auth.checkToken(request)==true) {
			String id = request.getParameter("id");
			if(id != null) {
				try {
					LocationTypeBean type = typeDao.getTypeById(Integer.parseInt(id));
					typeResponse = new Gson().toJson(type);
				}catch (SQLException e) {
					e.printStackTrace();
				}
				response.getWriter().append(typeResponse);
			}else {
				try {
					String orderBy = request.getParameter("orderBy");
					String orderDirection = request.getParameter("orderDirection");
					if(orderBy == null) {
						orderBy = "id_location_type";
					}
					if (orderDirection == null) {
						orderDirection = "asc";
					}
					ArrayList <LocationTypeBean> typeList = typeDao.getLocationType(orderBy, orderDirection);
					typeResponse = new Gson().toJson(typeList);
				}catch (SQLException e) {
					e.printStackTrace();
					}
					response.getWriter().append(typeResponse);
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
			LocationTypeDao typeDao = new LocationTypeDao (this.connection);
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
				NewTypeBean newType = null;
				boolean addedType = false;
				Gson datas = new Gson();
				try {
					newType = datas.fromJson(data, NewTypeBean.class);
					addedType = typeDao.addType(newType.getDescription());
					if(addedType == true ) {
						System.out.println("Tipo aggiunto con successo");
					}else {
						System.out.println("Aggiunta fallita");
					}
				}catch(JsonSyntaxException | SQLException e) {
					e.printStackTrace();
				}
			}else if(id != null && action.equalsIgnoreCase("delete")) {
				boolean deletedType = false;
				try {
					deletedType = typeDao.deleteLocationType(Integer.parseInt(id));
					if(deletedType == true){
						System.out.println("Tipo rimosso con successo");
					}else {
						System.out.println("Eliminazione non avvenuta");
					}
				}catch(JsonSyntaxException | SQLException e) {
					e.printStackTrace();
				}
			}else if(action.equalsIgnoreCase("update") && id!=null) {
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
					buffer.append(System.lineSeparator());
				}
				String data = buffer.toString();
				NewTypeBean newDetailType = null;
				boolean updatedType = false;
				Gson datas = new Gson();
				try {
					newDetailType = datas.fromJson(data, NewTypeBean.class);
					updatedType  =typeDao.updateLocationType(Integer.parseInt(id), newDetailType.getDescription());
					if(updatedType == true) {
						System.out.println("Dati Tipo aggiornato con successo");
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