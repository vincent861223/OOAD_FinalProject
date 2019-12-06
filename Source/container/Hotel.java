package container;
import container.Room;

public class Hotel{
	public int id;
	public String locality;
	public String street;
	public Room[] rooms;

	public Hotel(){
		//Default constructor
		this.id = 0;
		this.locality = "";
		this.street = "";
	}

	public Hotel(int id, String locality, String street){
		// Parameter constructor
		this.id = id;
		this.locality = locality;
		this.street = street;
	}

	@Override 
	public String toString() {
		// return String representation of the class
        return  "-----Hotel----\n" +
        		"id: " + this.id + "\n" + 
        		"locality: " + this.locality + "\n" + 
        		"street: " +  this.street + "\n" ;
    }
}