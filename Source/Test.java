
import container.Hotel;
import container.Room;
import container.Order;
import container.Account;
import container.SearchInfo;

import database.Database;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Test{
	public static void main(String[] args){
		//Hotel hotel = new Hotel(2, 2, "Taipei",  "abc street");
		//Room room = new Room("Double", 1000, 3);
		//Order order = new Order(1, "Taipei", "abc street");
		//Account account = new Account(1, "example@gmail.com", "testAccount");
		//SearchInfo searchInfo = new SearchInfo("Taipei", 2, "2019.10.1", "2019.10.1");
		//System.out.println(hotel);
		//System.out.println(room);
		//System.out.println(order);
		//System.out.println(account);
		//System.out.println(searchInfo);


		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		Database db = new Database("Database/hotel.db", "Resource/HotelList.json");

		// Database db = new Database("../Database/hotel.db", "");
		List<Hotel> hotels = db.getAllHotel();
		for(Hotel hotel: hotels){
			System.out.println(hotel);
		}
		// Hotel hotel = db.getHotel(1);
		// System.out.println(hotel);
		// System.out.println(hotel.rooms);

		System.out.println(db.addAccount("user1", "user1@gmail.com", "password1"));
		System.out.println(db.addAccount("user2", "user2@gmail.com", "password2"));
		System.out.println(db.addAccount("user3", "user3@gmail.com", "password3"));
		System.out.println(db.addAccount("user4", "user4@gmail.com", "password4"));
		System.out.println(db.addAccount("user5", "user5@gmail.com", "password5"));
		System.out.println(db.addAccount("user6", "user6@gmail.com", "password6"));
		System.out.println(db.addAccount("user7", "user7@gmail.com", "password7"));
		System.out.println(db.verifyAccount("user1@gmail.com", "password1"));
		System.out.println(db.verifyAccount("user1@gmail.com", "password2"));
		System.out.println(db.setHotelOwner(1, 1));
		System.out.println(db.setHotelOwner(2, 2));
		System.out.println(db.setHotelOwner(3, 3));
		System.out.println(db.setHotelOwner(4, 4));
		System.out.println(db.setHotelOwner(5, 5));
		System.out.println(db.setHotelOwner(6, 6));
		System.out.println(db.setHotelOwner(7, 7));
		System.out.println(db.getAccount(0));
		System.out.println(db.getRoomsOfHotel(1));
		System.out.println(db.getHotelOwner(1));

		Account account = db.getAccount(3);
		Hotel hotel = db.getHotel(1);
		List<Room> selected_rooms = db.getRoomsOfHotel(1);
		Order order = new Order(hotel, "2019/10/23", "2019/10/24", selected_rooms);
		System.out.println(db.addCustomerOrder(account, order));
	}
}