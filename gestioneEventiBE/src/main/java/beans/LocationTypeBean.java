package beans;

public class LocationTypeBean {
	//proprietà tipo location
	private int idLocationType;
	private String description;
	
	public LocationTypeBean (int idLocationType,String description) {
		//costruttore
		super();
		this.description = description;
		this.idLocationType = idLocationType;
	}
	
	//getters e setters delle varie proprietà
	public void setIdLocationType(int id) {
		this.idLocationType = id;
	}
	
	public int getIdLocationType() {
		return this.idLocationType;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
}
