package beans;

public class NewEventBean {
	//bean per la lettura del json inviato dal frontend alla crazione di un nuovo evento
	//proprietà
	private int idLocation;
	private String eventName;
	private String date;

	public int getIdLocation() {
		return idLocation;
	}
	public void setIdLocation(int idLocation) {
		this.idLocation = idLocation;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
