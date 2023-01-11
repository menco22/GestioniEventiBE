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
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import beans.BookingBean;
import beans.FeedbackBean;
import beans.NewBookingBean;
import beans.NewLocationBean;
import dao.BookingDao;
import dao.EventDao;
import dao.FeedbackDao;

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
			String id = request.getParameter("id");
			if(id != null) {
				try {
					BookingBean booking = bookingDao.getBookingById(Integer.parseInt(id));
					bookingResponse = new Gson().toJson(booking);
				}catch (SQLException e) {
					e.printStackTrace();
				}
				response.getWriter().append(bookingResponse);
			}else {
				if(auth.isAdmin(request) == true) {
					String orderBy = request.getParameter("orderBy");
					String orderDirection = request.getParameter("orderDirection");
					String idEvent = request.getParameter("idEvent");
					if(orderBy == null) {
						orderBy = "id_booking";
					}
					if (orderDirection == null) {
						orderDirection = "asc";
					}
					if (idEvent != null) {
						try {
							ArrayList <BookingBean> bookingList = bookingDao.getBookingByEvent(orderBy, orderDirection,Integer.parseInt(idEvent) );
							bookingResponse = new Gson().toJson(bookingList);
					  } catch (SQLException e) {
						  // TODO Auto-generated catch block
						  e.printStackTrace();
					  }
					response.getWriter().append(bookingResponse);
					}else {
						response.sendError(400,"Specificare evento");
					}
				}else {
					String orderBy = request.getParameter("orderBy");
					String orderDirection = request.getParameter("orderDirection");
					if(orderBy == null) {
						orderBy = "id_booking";
					}
					if (orderDirection == null) {
						orderDirection = "asc";
					}
					try {
						ArrayList <BookingBean> bookingList = bookingDao.getBookingByUser(orderBy, orderDirection, auth.getIdUser(request));
						bookingResponse = new Gson().toJson(bookingList);
				   } catch (SQLException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				   }
				 response.getWriter().append(bookingResponse);
					
				}
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
			BookingDao bookingDao = new BookingDao(this.connection);
			StringBuilder buffer = new StringBuilder();
		    BufferedReader reader = request.getReader();
		    String line;
			String id = request.getParameter("id");	
			String action = request.getParameter("action");	
			if(action == null && id == null) {
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
					buffer.append(System.lineSeparator());
				}
				String data = buffer.toString();
				NewBookingBean newBooking = null;
				boolean addedBooking = false;
				Gson datas = new Gson();
				try {
					newBooking = datas.fromJson(data, NewBookingBean.class);
					addedBooking = bookingDao.addBooking(newBooking.getCode(), newBooking.getBookingType(), 
		    			               auth.getIdUser(request), newBooking.getIdEvent(), newBooking.getIdTable());
					if(addedBooking == true) {
						System.out.println("Booking aggiunto con successo");
					}else {
						System.out.println("Aggiunta fallita");
					}
				}catch(JsonSyntaxException | SQLException e) {
					e.printStackTrace();
				}
			}else if(id != null && action.equalsIgnoreCase("delete")) {
				 boolean deleteBooking = false;  
				 try {
						deleteBooking = bookingDao.deleteBooking(Integer.parseInt(id));
						if(deleteBooking == true) {
							System.out.println("Prenotazione rimossa con successo");
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
			   }else if(id != null && action.equalsIgnoreCase("update")) {
					while ((line = reader.readLine()) != null) {
				    	buffer.append(line);
				    	buffer.append(System.lineSeparator());
				    }
					String data = buffer.toString();
					NewBookingBean newBookingDetail = null;
					boolean updatedBooking = false;
					Gson datas = new Gson();
					try {
						newBookingDetail = datas.fromJson(data, NewBookingBean.class);
						updatedBooking = bookingDao.updateBooking(Integer.parseInt(id), newBookingDetail.getCode(),
								newBookingDetail.getBookingType(), auth.getIdUser(request), newBookingDetail.getIdEvent(), newBookingDetail.getIdTable());
						if(updatedBooking == true) {
							System.out.println("Dati prenotazione aggiornati con successo");
						}else {
							System.out.println("Aggiornamento non avvenuto");
						}
					}catch(JsonSyntaxException | SQLException | NumberFormatException e) {
						e.printStackTrace();
					}
			   }else	if(action != null || id == null) {
					if(action.equalsIgnoreCase("delete") || action.equalsIgnoreCase("update")) {
						response.sendError(400, "Specificare la prenotazione");
					}else if(id == null){
						response.sendError(400, "Azione non valida e prenotazione non specificata");
					}else if(id != null) {
						response.sendError(400,"Azione non valida sulla prenotazione specificata");
					}
				}
		}else {
			response.sendError(401, "Effettuare il login");
		}
	}	
}
