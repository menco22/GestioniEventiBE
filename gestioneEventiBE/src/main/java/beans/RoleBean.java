package beans;

public class RoleBean {
	private int idRole;
	private String code;
	
		public RoleBean(int idRole, String code) {
		super();
		this.idRole = idRole;
		this.code = code;
		}
		
		public int getIdRole() {
		return idRole;
	}

	public void setIdRole(int idRole) {
		this.idRole = idRole;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
		
		

}
