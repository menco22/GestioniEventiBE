package beans;

public class RoleBean {
	//proprietà del ruolo dell'utente
	private int idRole;
	private String code;
	
	public RoleBean(int idRole, String code) {
		//costruttore
		super();
		this.idRole = idRole;
		this.code = code;
	}
		
	//getters e setters delle prorpietà
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
