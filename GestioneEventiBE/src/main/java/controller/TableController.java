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

import beans.NewTableBean;
import beans.TableBean;
import dao.TableDao;

/**
 * Servlet implementation class TableController
 */
public class TableController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TableController() {
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
		System.out.println(auth.checkToken(request));
		TableDao tableDao = new TableDao(this.connection);
		String tableResponse ="";
		if(auth.checkToken(request)==true) {
			String id = request.getParameter("id");
			String idEvent = request.getParameter("idEvent");
			if(idEvent != null) { 
				if(id != null) {
					try {
						TableBean table = tableDao.getTableById(Integer.parseInt(id));
						tableResponse = new Gson().toJson(table);
					}catch (SQLException e) {
						e.printStackTrace();
					}
					response.getWriter().append(tableResponse);
				}else {
					try {
						String orderBy = request.getParameter("orderBy");
						String orderDirection = request.getParameter("orderDirection");
						if(orderBy == null) {
							orderBy = "id_table";
						}
						if (orderDirection == null) {
							orderDirection = "asc";
						}
						ArrayList <TableBean> tableList = tableDao.getTablesByEvent(orderBy, orderDirection, Integer.parseInt(idEvent));
						tableResponse = new Gson().toJson(tableList);
					}catch (SQLException e) {
						e.printStackTrace();
				}
				response.getWriter().append(tableResponse);
				}
			}else {
				response.sendError(400,"Specificare evento");
			}
		}else {
			response.sendError(401,"Effettuare login!");
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
			TableDao tableDao = new TableDao(this.connection);
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
				NewTableBean newTable = null;
				boolean addedTable = false;
				Gson datas = new Gson();
				try {
					newTable = datas.fromJson(data, NewTableBean.class);
					addedTable = tableDao.addTable(newTable.getTableCapacity(), newTable.getIdEvent());
					if(addedTable == true) {
						System.out.println("Tavolo aggiunto con successo!");
					}else {
						System.out.println("Aggiunta fallita");
					}
				}catch(JsonSyntaxException | SQLException e) {
						e.printStackTrace();
					}
				}else if(id != null && action.equalsIgnoreCase("delete")) {
					boolean deletedTable = false;
					try {
						deletedTable = tableDao.deleteTable(Integer.parseInt(id));
						if(deletedTable == true) {
							System.out.println("Tavolo rimosso con successo");
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
					NewTableBean newDetailTable = null;
					boolean updatedTable = false;
					Gson datas = new Gson();
					try {
						newDetailTable = datas.fromJson(data, NewTableBean.class);
						updatedTable = tableDao.updateTable(Integer.parseInt(id), newDetailTable.getTableCapacity(),
								newDetailTable.getIdEvent());
						if(updatedTable == true) {
							System.out.println("Dati Tavolo aggiornati con successo");
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
