import container.Hotel;
import container.Room;
import container.Order;
import container.Account;
import container.SearchInfo;

import database.Database;

public class Test{
	public static void main(String[] args){
		Hotel hotel = new Hotel(2, "Taipei",  "abc street");
		Room room = new Room("Double", 1000, 3);
		Order order = new Order(1, "Taipei", "abc street");
		Account account = new Account(1, "example@gmail.com", "testAccount");
		SearchInfo searchInfo = new SearchInfo("Taipei", 2, "2019.10.1", "2019.10.1");
		System.out.println(hotel);
		System.out.println(room);
		System.out.println(order);
		System.out.println(account);
		System.out.println(searchInfo);

		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		Database db = new Database("../Resource/HotelList.json");
		db.read_hotel_list();
	}
}