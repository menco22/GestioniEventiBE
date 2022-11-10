package beans;

public class NewLocationBean {
	 public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getLocationAddress() {
		return locationAddress;
	}
	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}
	public int getLocationType() {
		return locationType;
	}
	public void setLocationType(int locationType) {
		this.locationType = locationType;
	}
	private String locationName;
	 private String locationAddress;
	 private int locationType;
}
