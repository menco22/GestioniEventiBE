package beans;

public class LocationBean {
	//proprietà della location
	private int idLocation;
	private String locationName;
	private String address;
	private int locationType;
	private String description;
	
	
	public LocationBean(int idLocation, String name, String address, int locationType, String description) {
		//costruttore
		super();
		this.idLocation = idLocation;
		this.locationName = name;
		this.address = address;
		this.locationType = locationType;
		this.description = description;
	}
	
	public int getIdLocation() {
		return idLocation;
	}

	public void setIdLocation(int idLocation) {
		this.idLocation = idLocation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	//getters e setters delle proprietà
	public String getLocationName () {
		return this.locationName;
	}
	
	public void setLocationName(String name) {
		this.locationName = name;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getLocationType() {
		return locationType;
	}

	public void setLocationType(int locationType) {
		this.locationType = locationType;
	}
}
