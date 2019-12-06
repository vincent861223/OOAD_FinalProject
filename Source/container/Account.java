package container;

public class Account{
	public int id;
	public String email;
	public String name;

	public Account(){
		//Default constructor
		this.id = 0;
		this.email = "";
		this.name = "";
	}
	

	public Account(int id, String email, String name){
		// Parameter constructor
		this.id = id;
		this.email = email;
		this.name = name;
	}

	@Override 
	public String toString() {
		// return String representation of the class
        return "-----Account-----\n" + 
        	   "id: " + this.id + "\n" + 
        	   "email: " + this.email + "\n" + 
        	   "name: " +  this.name + "\n";
    }
}