package beans;

public class NewTableBean {
	//bean per la lettura del json inviato dal frontend alla creazione di un nuovo tavolo
	//proprietà:
	private int tableCapacity;
	private int idEvent;
	
	public int getTableCapacity() {
		return tableCapacity;
	}
	public void setTableCapacity(int tableCapacity) {
		this.tableCapacity = tableCapacity;
	}
	public int getIdEvent() {
		return idEvent;
	}
	public void setIdEvent(int idEvent) {
		this.idEvent = idEvent;
	}
}
