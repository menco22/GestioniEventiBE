package beans;

public class BookingBean {
	//proprità delle prenotazioni o booking
	private int idBooking;
	private String code;
	private String bookingType;
	private int idUser;
	private EventBean event;
	private TableBean table;
	
	public BookingBean (int idBooking, String code, String bookingType, int idUser,EventBean event, TableBean table) {
		//costruttore
		super() ;
		this.idBooking = idBooking;
		this.code = code;
		this.bookingType = bookingType;
		this.idUser = idUser;
		this.event = event;
		this.table = table;
	}
	
	//getters e setters per le varie proprietà
	public int getIdBooking() {
		return idBooking;
	}

	public void setIdBooking(int idBooking) {
		this.idBooking = idBooking;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBookingType() {
		return bookingType;
	}

	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}


	public EventBean getEvent() {
		return event;
	}

	public void setEvent(EventBean event) {
		this.event = event;
	}

	public TableBean getTable() {
		return table;
	}

	public void setTable(TableBean table) {
		this.table = table;
	}
	
	
}
