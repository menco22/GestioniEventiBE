package controller;

import jakarta.servlet.ServletConfig;
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

import beans.RoleBean;
import dao.RoleDao;

/**
 * Servlet implementation class RoleController
 */
public class RoleController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection; 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RoleController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(){
		// TODO Auto-generated method stub
    	System.out.println("mi connetto al db");
    	connection = connectToDb();
    	System.out.println("connessione avvenuta con successo");
	}
	
    private Connection connectToDb(){
		try {
			// parametri per connettersi al db
			ServletContext context = getServletContext();
			String driver = context.getInitParameter("dbDriver");
			String url = context.getInitParameter("dbUrl");
			String user = context.getInitParameter("dbUser");
			String password = context.getInitParameter("dbPassword");
			// inizializzazione del driver usato per la connessione
			Class.forName(driver);
			// ritorna la connessione
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
		// controllo se l'utente è loggato
		if(auth.checkToken(request)==true) {
			RoleDao roleDao = new RoleDao(this.connection);
			String roleResponse = "";
			String id = request.getParameter("id");
			if(id != null) {
				// get del ruolo specifico
				try {
					RoleBean role = roleDao.getRoleById(Integer.parseInt(id));
					roleResponse = new Gson().toJson(role);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				response.getWriter().append(roleResponse);
			}else {
				// get lista ruoli
				String orderBy = request.getParameter("orderBy");
				String orderDirection = request.getParameter("orderDirection");
				if(orderBy == null) {
					orderBy = "id_role";
				}
				if (orderDirection == null) {
					orderDirection = "asc";
				}
				
				try {
					ArrayList <RoleBean> roleList = roleDao.getRole(orderBy, orderDirection);
					roleResponse = new Gson().toJson(roleList);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				response.getWriter().append(roleResponse);
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
		// i ruoli sono predefiniti non possono essere né aggiunti né eliminati
		doGet(request, response);
	}

}
