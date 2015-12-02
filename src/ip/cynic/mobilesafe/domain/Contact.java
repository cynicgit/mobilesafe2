package ip.cynic.mobilesafe.domain;

/**
 * @author cynic
 * 
 *         2015-12-1
 */
public class Contact {

	private String name;
	private String phone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Contact(String name, String phone) {
		super();
		this.name = name;
		this.phone = phone;
	}
	
	public Contact() {
	}
}