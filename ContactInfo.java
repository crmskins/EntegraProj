/*
 * Chad Milburn
 * April 1, 2018
 */

/*
 * In this class I simply create the object ContactInfo which contains
 * the name, phone, and email all contained as Strings. Included are the 
 * get functions.
 * 
 * I also made a toString function just to make printing out my results more
 * convienient.
 */
public class ContactInfo {

	private String name, phone, email; 
	public ContactInfo(String name, String phone, String email){
		this.name = name;
		this.phone = phone;
		this.email = email;
	}
	
	public String getName(){
		return name;
	}
	
	public String getPhoneNumber(){
		return phone;
	}
	
	public String getEmailAddress(){
		return email;
	}
	
	@Override
	public String toString(){
		String x = "Name: " + name + "\n"
					+ "Phone: " + phone + "\n"
					+ "Email: " + email;
		return x;
	}
}
