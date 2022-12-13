package beans;

public class NewTypeBean {
	//bean per la lettura del json inviato dal frontend alla  reazione di un nuovo tipo di location
	//proprietà:
 private String description;
 
 public void setDescription (String description) {
	 this.description = description;
  }
 
 public String getDescription () {
	 return this.description;
 }
}
