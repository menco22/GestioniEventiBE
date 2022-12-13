package beans;

public class TableBean {
	//prorpietà dei tavoli
	private int idTable;
	private int tableCapacity;
	private int idEvent;
	
	public TableBean(int idTable, int tableCapacity, int idEvent) {
		//costruttore
		super();
		this.idTable = idTable;
		this.tableCapacity = tableCapacity;
		this.idEvent = idEvent;
	}
	
	//getters e setters delle varie proprietà
	public int getIdTable() {
		return idTable;
	}
	public void setIdTable(int idTable) {
		this.idTable = idTable;
	}
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
