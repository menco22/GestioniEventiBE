package beans;

public class LocationTypeBean {
	private int idLocationType;
	private String description;
	
	public LocationTypeBean (int idLocationType,String description) {
		this.description = description;
		this.idLocationType = idLocationType;
	}
	
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
