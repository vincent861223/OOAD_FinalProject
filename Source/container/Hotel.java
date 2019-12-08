package container;
import java.util.ArrayList;
import java.util.List;
import container.Room;

public class Hotel{
	public int id;
	public int star;
	public String locality;
	public String street;
	public List<Room> rooms;

	public Hotel(){
		//Default constructor
		this.id = 0;
		this.star = 0;
		this.locality = "";
		this.street = "";
	}

	public Hotel(int id, int star, String locality, String street, List<Room> rooms){
		// Parameter constructor
		this.id = id;
		this.star = star;
		this.locality = locality;
		this.street = street;
		this.rooms = rooms;
	}

	@Override 
	public String toString() {
		// return String representation of the class
        return  "-----Hotel----\n" +
        		"id: " + this.id + "\n" + 
        		"star: " + this.star + "\n" + 
        		"locality: " + this.locality + "\n" + 
        		"street: " +  this.street + "\n" ;

    }
}