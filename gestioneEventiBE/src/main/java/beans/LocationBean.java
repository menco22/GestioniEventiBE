package beans;

public class LocationBean {
	//proprietà della location
	private int idLocation;
	private String locationName;
	private String address;
	private int locationType;
	
	
	public LocationBean(int idLocation, String name, String address, int locationType) {
		//costruttore
		super();
		this.idLocation = idLocation;
		this.locationName = name;
		this.address = address;
		this.locationType = locationType;
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
