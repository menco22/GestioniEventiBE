package controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.compression.*;
import dao.LocationDao;
import dao.UserDao;
import beans.CredentialBean;
import beans.LocationBean;
import beans.UserBean;

/**
 * Servlet implementation class UserController
 */
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
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
		try {
			ArrayList<UserBean> userList = userDao.getUserList();
		    userResponse = new Gson().toJson(userList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		response.getWriter().append(userResponse);
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
		//response.setContentType("text/html");
		//String password=request.getParameter("password");
		UserDao loginDao=new UserDao(this.connection);
		UserBean user = null;
		CredentialBean credential= null;
		Gson datas = new Gson();
		try {
		credential = datas.fromJson(data, CredentialBean.class);
		user = loginDao.getUser(credential.getUsername(), credential.getPassword());
			//user = loginDao.getUser(username, password);
		} catch (JsonSyntaxException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(user);
		if(user != null) {
			LocalDate now = LocalDate.now();
			String jwtToken = Jwts.builder()
			        .claim("username", user.getUsername())
			        .claim("password", user.getPassword())
			        .setSubject("jane")
			        .compact();			
			System.out.println(jwtToken);		 

		}
		//doGet(request, response);
	}

}
