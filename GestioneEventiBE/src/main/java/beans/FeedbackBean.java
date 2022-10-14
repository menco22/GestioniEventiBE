package beans;

public class FeedbackBean {	
	private int idFeedback;
	private int idCreator;
	private int idBooking;
	private int evaluation;
	private String description;
	
	public FeedbackBean(int idFeedback, int idCreator, int idBooking, int evaluation, String description) {
		super();
		this.idCreator = idCreator;
		this.idBooking = idBooking;
		this.evaluation = evaluation;
		this.description = description;
	}
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



}
