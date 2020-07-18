package model;

public class Contact {
	String name;
	String phone;
	
	public Contact() {
		super();
	}
	public Contact(String name, String phone) {
		this.name = name;
		this.phone = phone;
	}
	
	public void setContactName(String name) {
		this.name = name;
	}
	
	public String getContactName() {
		return this.name;
	}
	
	public void setContactPhone(String phone) {
		this.phone = phone;
	}
	
	public String getContactPhone() {
		return this.phone;
	}
}
