package beans;

public class BookingBean {
	//proprità delle prenotazioni o booking
	private int idBooking;
	private String code;
	private String bookingType;
	private String username;
	private EventBean event;
	private TableBean table;
	private boolean canReview;
	
	public BookingBean (int idBooking, String code, String bookingType, String username,EventBean event, TableBean table, boolean canReview) {
		//costruttore
		super() ;
		this.idBooking = idBooking;
		this.code = code;
		this.bookingType = bookingType;
		this.username = username;
		this.event = event;
		this.table = table;
		this.canReview = canReview;
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

	public String getUsername() {
		return username;
	}

	public void setIdUser(String username) {
		this.username = username;
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

	public boolean isCanReview() {
		return canReview;
	}

	public void setCanReview(boolean canReview) {
		this.canReview = canReview;
	}
	
}
