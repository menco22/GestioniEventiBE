package beans;

public class BookingBean {
	//proprità delle prenotazioni o booking
	private int idBooking;
	private String code;
	private String bookingType;
	private int idUser;
	private int idTable;
	private EventBean event;
	
	public BookingBean (int idBooking, String code, String bookingType, int idUser, int idTable, EventBean event) {
		//costruttore
		super() ;
		this.idBooking = idBooking;
		this.code = code;
		this.bookingType = bookingType;
		this.idUser = idUser;
		this.idTable = idTable;
		this.event = event;
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

	public int getIdTable() {
		return idTable;
	}

	public void setIdTable(int idTable) {
		this.idTable = idTable;
	}

	public EventBean getEvent() {
		return event;
	}

	public void setEvent(EventBean event) {
		this.event = event;
	}
}
