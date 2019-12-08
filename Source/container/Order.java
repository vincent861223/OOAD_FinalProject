package container;
import container.Room;
import container.Hotel;
import java.util.ArrayList;
import java.util.List;

public class Order{
	public Hotel hotel;
	public String dateIn;
	public String dateOut;
	public List<Room> selected_rooms;

	public Order(){
		//Default constructor
		this.dateIn = "";
		this.dateOut = "";
	}
	

	public Order(Hotel hotel, String dateIn, String dateOut, List<Room> selected_rooms){
		// Parameter constructor
		this.hotel = hotel;
		this.dateIn = dateIn;
		this.dateOut = dateOut;
		this.selected_rooms = selected_rooms;
	}

	@Override 
	public String toString() {
		// return String representation of the class
        return "-----Order-----\n" + 
        	   "hotel: " + this.hotel + "\n" + 
        	   "dateIn: " + this.dateIn + "\n" + 
        	   "dateOut: " +  this.dateOut + "\n";
    }
}