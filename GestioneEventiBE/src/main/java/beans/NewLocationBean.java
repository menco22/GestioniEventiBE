package beans;

public class NewLocationBean {
	//bean per la lettura del json inviato dal frontend alla creazione di una nuova location
	//proprietà:
	private String locationName;
	 private String address;
	 private int locationType;
	 public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
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
