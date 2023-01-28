package beans;

public class FeedbackBean {
	//proprietà del feedback
	private int idFeedback;
	private int idCreator;
	private int idBooking;
	private int evaluation;
	private String description;
	private BookingBean booking;
	private EventBean event;
	private UserBean user;
	private String username;
	
	public FeedbackBean(int idFeedback, int idCreator, int idBooking, int evaluation, String description) {
		//costruttore
		super();
		this.idFeedback = idFeedback;
		this.idCreator = idCreator;
		this.idBooking = idBooking;
		this.evaluation = evaluation;
		this.description = description;
	}
	
	public FeedbackBean(int idFeedback,  int evaluation, String description, BookingBean booking, EventBean event, String username) {
		this.idFeedback = idFeedback;
		this.evaluation = evaluation;
		this.description = description;
		this.booking = booking;
		this.event = event;
		this.username = username;
	}
	
	//getters e setters delle varie proprietà
	public int getIdFeedback() {
		return idFeedback;
	}
	public void setIdFeedback(int idFeedback) {
		this.idFeedback = idFeedback;
	}
	public int getIdCreator() {
		return idCreator;
	}
	public void setIdCreator(int idCreator) {
		this.idCreator = idCreator;
	}
	public int getIdBooking() {
		return idBooking;
	}
	public void setIdBooking(int idBooking) {
		this.idBooking = idBooking;
	}
	public int getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(int evaluation) {
		this.evaluation = evaluation;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public BookingBean getBooking() {
		return booking;
	}

	public void setBooking(BookingBean booking) {
		this.booking = booking;
	}

	public EventBean getEvent() {
		return event;
	}

	public void setEvent(EventBean event) {
		this.event = event;
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}



}
