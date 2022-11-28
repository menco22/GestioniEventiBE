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
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import beans.RegistrationBean;
import beans.UserBean;
import dao.UserDao;
import io.jsonwebtoken.Jwts;

/**
 * Servlet implementation class RegistrationController
 */
public class RegistrationController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int VALIDITY_TIME_MS = 0;
	private Connection connection;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrationController() {
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
	    System.out.println("i parametri sono:" +data);
	    
	    String name ="";
	    String surname ="";
	    String email ="";
	    String username ="";
	    String password = "";
	    int idRole = -1;
	    JSONObject jsonObject = null;
		UserDao registrationDao = new UserDao(this.connection);
		RegistrationBean registration = null;
		boolean addedUser = false;
		//UserBean newUser = null;
		Gson datas = new Gson();
		try {
			registration = datas.fromJson(data, RegistrationBean.class );
			addedUser = registrationDao.addUser(registration.getName(), registration.getSurname(),
								registration.getEmail(), registration.getUsername(), registration.getPassword(), registration.getIdRole());
			if(addedUser == true ) {
				System.out.println("Utente aggiunto con successo");
				String jwtToken = Jwts.builder()
						   .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME_MS*60))
						   .setSubject(registration.getUsername())
						   //.claim("id", registration.getIdUser())
						   .claim("email", registration.getEmail())
						   .claim("roles", registration.getIdRole())
						  //.signWith( SignatureAlgorithm.HS512, JwtConstants.SECRET)
						   .compact();
				System.out.println(jwtToken);		
				Cookie cookie = new Cookie("signInCookie", jwtToken);
				//cookie.setMaxAge(60*60);
				//cookie.setPath("/LocationController");
				cookie.setHttpOnly(true);
				response.addCookie(cookie);
			}
		}catch(JsonSyntaxException | SQLException e) {
			e.printStackTrace();
		}
		//doGet(request, response);
	}
   //CHECK: stampare la query
}