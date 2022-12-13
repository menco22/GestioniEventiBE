package beans;

public class BookingBean {
	//proprità delle prenotazioni o booking
	private int idBooking;
	private String code;
	private String bookingType;
	private int idUser;
	private int idEvent;
	private int idTable;
	
	public BookingBean (int idBooking, String code, String bookingType, int idUser, int idEvent, int idTable) {
		//costruttore
		super() ;
		this.idBooking = idBooking;
		this.code = code;
		this.bookingType = bookingType;
		this.idUser = idUser;
		this.idEvent = idEvent;
		this.idTable = idTable;
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

	public int getIdEvent() {
		return idEvent;
	}

	public void setIdEvent(int idEvent) {
		this.idEvent = idEvent;
	}

	public int getIdTable() {
		return idTable;
	}

	public void setIdTable(int idTable) {
		this.idTable = idTable;
	}
}
