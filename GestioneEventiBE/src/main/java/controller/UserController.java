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
import java.io.Serializable;
import java.lang.reflect.Type;
import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.compression.*;
import dao.LocationDao;
import dao.UserDao;
import beans.CredentialBean;
import beans.LocationBean;
import beans.UserBean;
import controller.AuthenticationController;

/**
 * Servlet implementation class UserController
 */
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final long VALIDITY_TIME_MS = 1000;
	private Connection connection;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserController() {
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
		UserDao userDao = new UserDao(this.connection);
		UserBean user = null;
		String userResponse = "";
		String id = request.getParameter("id");	
		String username = request.getParameter("username");
		if(id != null && username == null) {
			try {
				user=userDao.getUserById(Integer.parseInt(id));
				userResponse = new Gson().toJson(user);
			}catch (SQLException e) {
				e.printStackTrace();
			}
			response.getWriter().append(userResponse);
		}else if (id == null && username == null){
			try {
				String orderBy = request.getParameter("orderBy");
				String orderDirection = request.getParameter("orderDirection");
				if(orderBy == null) {
					orderBy = "id_user";
				}
				if (orderDirection == null) {
					orderDirection = "asc";
				}
				ArrayList<UserBean> userList = userDao.getUserList(orderBy, orderDirection);
				userResponse = new Gson().toJson(userList);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.getWriter().append(userResponse);
		}else if (id == null && username != null) {
			try {
				user = userDao.getUserByUsername(username);
				userResponse = new Gson().toJson(user);
			}catch (SQLException e) {
				e.printStackTrace();
			}
			response.getWriter().append(userResponse);
		}else if (id != null && username != null) {
			response.sendError(400, "Passare un solo parametro di ricerca alla volta");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//connectToDb();
		// login
		StringBuilder buffer = new StringBuilder();
	    BufferedReader reader = request.getReader();
	    String line;
	    while ((line = reader.readLine()) != null) {
	        buffer.append(line);
	        buffer.append(System.lineSeparator());
	    }
	    String data = buffer.toString();
	    System.out.println("i parametri sono:" +data);
	   
	    String username ="";
	    String password = "";
	    JSONObject jsonObject = null;
		try {
			 jsonObject = new JSONObject(data);
			username = jsonObject.getString("username");
			password = jsonObject.getString("password");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    System.out.println(username + " " + password);
		UserDao loginDao=new UserDao(this.connection);
		UserBean user = null;
		CredentialBean credential= null;
		Gson datas = new Gson();
		try {
		credential = datas.fromJson(data, CredentialBean.class);
		user = loginDao.getUser(credential.getUsername(), credential.getPassword());
		} catch (JsonSyntaxException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(user != null) {
			LocalDate now = LocalDate.now();
			String jwtToken = Jwts.builder()
					   .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME_MS*3600*5))
					   .setSubject(user.getUsername())
					   .claim("id", user.getIdUser())
					   .claim("email", user.getEmail())
					   .claim("roles", user.getIdRole())
					   .compact();			
			System.out.println(jwtToken);		
			Cookie cookie1 = new Cookie("loginCookie", jwtToken);
			cookie1.setMaxAge(5*3600);
			cookie1.setHttpOnly(true);
			response.addCookie(cookie1);
            //TODO: if user == null
		}else {
			response.sendError(403, "Utente o password errati");
		}
	}

}
