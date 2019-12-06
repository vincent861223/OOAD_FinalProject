package database;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;

public class Database{
	private String dbPath;
	public Database(String dbPath){
		this.dbPath = dbPath;
	}

	public void read_hotel_list(){
		Gson gson = new Gson();
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.dbPath));
			Hotel[] hotels = gson.fromJson(br, Hotel[].class);
			System.out.println(hotels[0].rooms[0].RoomPrice);
		}catch (IOException e) {
			System.out.println("file not exist!");
		}
	}
}

class Hotel{
	public int HotelID; 
	public int HotelStar; 
	public String Locality; 
	public String Street_Address; 
	public Room[] rooms; 
	void Hotel(){
		rooms = new Room[3];
	}
}

class Room{
	public String RoomType;
	public int RoomPrice;
	public int Number;
}