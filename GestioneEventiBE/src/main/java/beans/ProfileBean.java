package beans;

public class ProfileBean {
	private String username;
	private int idRole;
	
	public ProfileBean(String username, int idRole) {
		super();
		this.username = username;
		this.idRole = idRole;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getIdRole() {
		return idRole;
	}

	public void setIdRole(int idRole) {
		this.idRole = idRole;
	}
	
	
}
