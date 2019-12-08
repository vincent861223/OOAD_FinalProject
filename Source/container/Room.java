package container;

public class Room{
	public int id;
	public String type;
	public int price;
	public int quantity;

	public Room(){
		//Default constructor
		this.id = 0;
		this.type = "None";
		this.price = 0;
		this.quantity = 0;
	}
	

	public Room(int id, String type, int price, int quantity){
		// Parameter constructor
		this.id = id;
		this.type = type;
		this.price = price;
		this.quantity = quantity;
	}

	@Override 
	public String toString() {
		// return String representation of the class
        return "-----Room-----\n" + 
        	   "id: " + this.id + "\n" +
        	   "type: " + this.type + "\n" + 
        	   "price: " + this.price + "\n" + 
        	   "quantity: " +  this.quantity + "\n";
    }
}