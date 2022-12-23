package beans;

public class LocationBean {
	//proprietà della location
	private int idLocation;
	private String locationName;
	private String address;
	private LocationTypeBean locationType;
	
	
	public LocationBean(int idLocation, String name, String address, LocationTypeBean locationType) {
		//costruttore
		super();
		this.idLocation = idLocation;
		this.locationName = name;
		this.address = address;
		this.locationType = locationType;
	}
	
	public LocationBean (int idLocation, String name, String address) {
		super();
		this.idLocation = idLocation;
		this.locationName = name;
		this.address = address;
	}
	
	public int getIdLocation() {
		return idLocation;
	}

	public void setIdLocation(int idLocation) {
		this.idLocation = idLocation;
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

	public LocationTypeBean getLocationType() {
		return locationType;
	}

	public void setLocationType(LocationTypeBean locationType) {
		this.locationType = locationType;
	}
}
