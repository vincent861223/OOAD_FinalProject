package container;
import container.Room;

public class Order{
	public int hotelID;
	public String locality;
	public String street;
	public Room[] selected_room;

	public Order(){
		//Default constructor
		this.hotelID = 0;
		this.locality = "";
		this.street = "";
	}
	

	public Order(int hotelID, String locality, String street){
		// Parameter constructor
		this.hotelID = hotelID;
		this.locality = locality;
		this.street = street;
	}

	@Override 
	public String toString() {
		// return String representation of the class
        return "-----Order-----\n" + 
        	   "hotelID: " + this.hotelID + "\n" + 
        	   "locality: " + this.locality + "\n" + 
        	   "street: " +  this.street + "\n";
    }
}