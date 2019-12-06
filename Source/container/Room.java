package container;

public class Room{
	public String type;
	public int price;
	public int quantity;

	public Room(){
		//Default constructor
		this.type = "None";
		this.price = 0;
		this.quantity = 0;
	}
	

	public Room(String type, int price, int quantity){
		// Parameter constructor
		this.type = type;
		this.price = price;
		this.quantity = quantity;
	}

	@Override 
	public String toString() {
		// return String representation of the class
        return "-----Room-----\n" + 
        	   "type: " + this.type + "\n" + 
        	   "price: " + this.price + "\n" + 
        	   "quantity: " +  this.quantity + "\n";
    }
}