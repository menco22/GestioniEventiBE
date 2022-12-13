package beans;

public class NewFeedbackBean {
	//bean per la lettura del json inviato dal frontend alla creazione di un nuovo feedback
	//proprietà:
	private int idBooking;
	private int evaluation;
	private String description;
	
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
