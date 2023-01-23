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
import beans.EventBean;
import beans.FeedbackBean;
import beans.NewBookingBean;
import beans.NewLocationBean;
import dao.BookingDao;
import dao.EventDao;
import dao.FeedbackDao;
import dao.TableDao;

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
					// id diverso da null si procede con la ricerca della prenotazione specifica
					BookingBean booking = bookingDao.getBookingById(Integer.parseInt(id));
					bookingResponse = new Gson().toJson(booking);
				}catch (SQLException e) {
					e.printStackTrace();
				}
				response.getWriter().append(bookingResponse);
			}else {
				// id null quindi si vuole la lista completa di prenotazioni 
				if(auth.isAdmin(request) == true) {
					// se l'utente è user si restituiscono le prenotazioni relative ad un dato evento
					String orderBy = request.getParameter("orderBy");
					String orderDirection = request.getParameter("orderDirection");
					String idEvent = request.getParameter("idEvent");
					if(orderBy == null) {
						orderBy = "id_event";
					}
					if (orderDirection == null) {
						orderDirection = "asc";
					}
						try {
							ArrayList <BookingBean> bookingList = bookingDao.getBooking(orderBy, orderDirection);
							bookingResponse = new Gson().toJson(bookingList);
					  } catch (SQLException e) {
						  // TODO Auto-generated catch block
						  e.printStackTrace();
					  }
					response.getWriter().append(bookingResponse);
			
				}else {
					// utente non admin si restituiscono le prenotazioni effettuate dall'utente loggato
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
						System.out.println(bookingList.size());
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
		// controllo se l'utente è loggato
		if(auth.checkToken(request)==true) {
			//connectToDb();
			TableDao table = new TableDao (this.connection);
			BookingDao bookingDao = new BookingDao(this.connection);
			EventDao eventDao = new EventDao(this.connection);
			StringBuilder buffer = new StringBuilder();
		    BufferedReader reader = request.getReader();
		    String line;
			String id = request.getParameter("id");	
			String action = request.getParameter("action");	
			if(action == null && id == null) {
				// action e id nulli si procede con l'aggiunta di una nuova prenotazione
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
					System.out.println(newBooking.getBookingType());
					addedBooking = bookingDao.addBooking(newBooking.getCode(), newBooking.getBookingType(), 
		    			               auth.getIdUser(request), newBooking.getIdEvent(), newBooking.getIdTable());
					if(addedBooking == true) {
						if(newBooking.getIdTable() != 0) {
							table.bookTable(newBooking.getIdTable());
							}
							System.out.println("Booking aggiunto con successo");
							if(newBooking.getBookingType().equalsIgnoreCase("S") || newBooking.getBookingType().equalsIgnoreCase("singolo")) {
								EventBean event = eventDao.getEventById(newBooking.getIdEvent());
								if(event.getStandingPlaces() > 0) {
									event.setStandingPlaces(event.getStandingPlaces()-1);	
									eventDao.updateEvent(event.getIdEvent(), event.getIdCreator(), event.getLocationBean().getIdLocation(),
											event.getEventName(), event.getDate(), event.getDataScadenza(), event.getStandingPlaces());
								}
							}
						
					}else {
						System.out.println("Aggiunta fallita");
					}
				}catch(JsonSyntaxException | SQLException e) {
					e.printStackTrace();
				}
			}else if(id != null && action.equalsIgnoreCase("delete")) {
				// eliminazione prenotazione data
				 boolean deleteBooking = false;  
				 try {
					 	BookingBean booking = bookingDao.getBookingById(Integer.parseInt(id));
						deleteBooking = bookingDao.deleteBooking(Integer.parseInt(id));
						if(deleteBooking == true) {
							if(booking.getTable().getIdTable() != 0) {
								table.unbookTable(booking.getTable().getIdTable());
							}else {
								EventBean event = eventDao.getEventById(booking.getEvent().getIdEvent());
								event.setStandingPlaces(event.getStandingPlaces() +1);
								eventDao.updateEvent(event.getIdEvent(), event.getIdCreator(), event.getLocationBean().getIdLocation(),
										event.getEventName(), event.getDate(), event.getDataScadenza(), event.getStandingPlaces());
							}
							    
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
				   // agggiornamento prenotazione data
					while ((line = reader.readLine()) != null) {
				    	buffer.append(line);
				    	buffer.append(System.lineSeparator());
				    }
					String data = buffer.toString();
					NewBookingBean newBookingDetail = null;
					boolean updatedBooking = false;
					Gson datas = new Gson();
					try {
						BookingBean booking = bookingDao.getBookingById(Integer.parseInt(id));
						newBookingDetail = datas.fromJson(data, NewBookingBean.class);
						updatedBooking = bookingDao.updateBooking(Integer.parseInt(id), newBookingDetail.getCode(),
								newBookingDetail.getBookingType(), auth.getIdUser(request), newBookingDetail.getIdEvent(), newBookingDetail.getIdTable());
						if(booking.getTable().getIdTable() != newBookingDetail.getIdTable()) {
							if(booking.getTable().getIdTable() != 0 && newBookingDetail.getIdTable() != 0) {
								table.unbookTable(booking.getTable().getIdTable());
								table.bookTable(newBookingDetail.getIdTable());
							}else if (booking.getTable().getIdTable() == 0 && newBookingDetail.getIdTable() != 0) {
								EventBean event = eventDao.getEventById(booking.getIdBooking());
								event.setStandingPlaces(event.getStandingPlaces()+1);
								eventDao.updateEvent(event.getIdEvent(), event.getIdCreator(), event.getLocationBean().getIdLocation(),
										event.getEventName(), event.getDate(), event.getDataScadenza(), event.getStandingPlaces());
								table.bookTable(newBookingDetail.getIdTable());
							}else if (booking.getTable().getIdTable() != 0 && newBookingDetail.getIdTable() == 0) {
								EventBean event = eventDao.getEventById(booking.getIdBooking());
								event.setStandingPlaces(event.getStandingPlaces()-1);
								eventDao.updateEvent(event.getIdEvent(), event.getIdCreator(), event.getLocationBean().getIdLocation(),
										event.getEventName(), event.getDate(), event.getDataScadenza(), event.getStandingPlaces());
							}
						
						}
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
