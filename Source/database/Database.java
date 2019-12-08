package database;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.*;
import java.sql.*;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import container.Hotel;
import container.Room;
import java.security.MessageDigest;


public class Database{
	private String dbPath;
	private String jsonPath;
	private String initSQLPath;
	private Connection db_conn;
	public Database(String dbPath, String jsonPath){
		// Pass in "" as jsonPath if the database has already established.
		// Pass in the jsonPath to clear the database and init the database by jsonPath.
		this.dbPath = dbPath;
		this.jsonPath = jsonPath;
		this.initSQLPath = "Database/init_table.sql";
		try{
			File f = new File(this.dbPath);
			if(!f.exists()){
				System.out.println("DB file not exist, establishing DB...");
				initDB();
				establishDB();
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public List<Hotel> getAllHotel(){
		// Return List of Hotel
		// Note: To save query time, rooms information will not be included(i.e, Hotel.rooms = null).
		List<Hotel> hotels = new ArrayList<Hotel>();
		List<HashMap<String, String>> results = this.selectAll("Hotel");
		for(HashMap<String, String> result: results){
			// HashMap<String, String> attr = new HashMap<>();
			// attr.put("hotel_id", result.get("id"));
			// List<HashMap<String, String>> roomResults = this.select("Room", attr);
			// List<Room> rooms = new ArrayList<Room>();
			// for(HashMap<String, String> roomResult: roomResults){
			// 	Room room = new Room(roomResult.get("roomtype"), Integer.parseInt(roomResult.get("roomprice")), Integer.parseInt(roomResult.get("number")));
			// 	rooms.add(room);
			// }
			//Hotel hotel = new Hotel(Integer.parseInt(result.get("id")), Integer.parseInt(result.get("star")), result.get("locality"), result.get("street_address"), rooms);
			Hotel hotel = new Hotel(Integer.parseInt(result.get("id")), Integer.parseInt(result.get("star")), result.get("locality"), result.get("street_address"), null);
			hotels.add(hotel);
		}
		return hotels;
	}

	public Hotel getHotel(int id){
		// Return Hotel of specified hotel id. The returned Hotel will include rooms information.
		// Return null if no such hotel found. 
		List<Hotel> hotels = new ArrayList<Hotel>();
		HashMap<String, String> hotel_attr = new HashMap<>();
		hotel_attr.put("id", Integer.toString(id));
		List<HashMap<String, String>> results = this.select("Hotel", hotel_attr);
		for(HashMap<String, String> result: results){
			HashMap<String, String> attr = new HashMap<>();
			attr.put("hotel_id", result.get("id"));
			List<HashMap<String, String>> roomResults = this.select("Room", attr);
			List<Room> rooms = new ArrayList<Room>();
			for(HashMap<String, String> roomResult: roomResults){
				Room room = new Room(roomResult.get("roomtype"), Integer.parseInt(roomResult.get("roomprice")), Integer.parseInt(roomResult.get("number")));
				rooms.add(room);
			}
			Hotel hotel = new Hotel(Integer.parseInt(result.get("id")), Integer.parseInt(result.get("star")), result.get("locality"), result.get("street_address"), rooms);
			hotels.add(hotel);
		}
		if(hotels.size() < 1){
			System.out.println("Get hotel error! hotels.size() < 1");
			return null;
		}else{
			return hotels.get(0);
		}
	}

	public Boolean addAccount(String name, String email, String password){
		// addAccount will fail if account with same email has existed.
		HashMap<String, String> attr = new HashMap<>();
		attr.put("name", name);
		attr.put("email", email);
		attr.put("password", hashPassword(password));
		if(this.insert("Account", attr)) return true;
		else return false;
	}

	public Boolean verifyAccount(String email, String password){
		HashMap<String, String> attr = new HashMap<>();
		attr.put("email", email);
		attr.put("password", hashPassword(password));
		if(this.select("Account", attr).size() > 0) return true;
		else return false;
	}

	private void initDB(){
	    try{	
	    	File f = new File(this.initSQLPath);
	    	Scanner s = new Scanner(f).useDelimiter("(;(\r)?\n)|((\r)?\n)?(--)?.*(--(\r)?\n)");
	    	Statement st = null;
	        st = this.connect().createStatement();
	        while (s.hasNext()){
	            String line = s.next();
	            if (line.startsWith("/*!") && line.endsWith("*/")){
	                int i = line.indexOf(' ');
	                line = line.substring(i + 1, line.length() - " */".length());
	            }

	            if (line.trim().length() > 0){
	                st.execute(line);
	            }
	        }
	        st.close();
	    }catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void establishDB(){
		JsonHotel[] hotels = read_hotel_list();
		if(hotels == null){
			System.out.println("No hotel loaded!\n");
			return;
		}
		int count = 1;
		for(JsonHotel hotel: hotels){
			HashMap<String, String> hotel_attr = new HashMap<>();
			hotel_attr.put("star", Integer.toString(hotel.HotelStar));
			hotel_attr.put("locality", hotel.Locality);
			hotel_attr.put("street_address", hotel.Street_Address);
			insert("Hotel", hotel_attr);
			for(JsonRoom room: hotel.Rooms){
				HashMap<String, String> room_attr = new HashMap<>();
				room_attr.put("roomtype", room.RoomType);
				room_attr.put("roomprice", Integer.toString(room.RoomPrice));
				room_attr.put("number", Integer.toString(room.Number));
				room_attr.put("hotel_id", Integer.toString(count));
				insert("Room", room_attr);
			}
			count++;
		}
	}

	private Connection connect() {
		Connection conn = null;
		try {
			String url = "jdbc:sqlite:" + this.dbPath;
			conn = DriverManager.getConnection(url);
			//System.out.println("Connection to SQLite has been established.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	private Boolean insert(String table,  HashMap<String, String> attr) {
		//String sql = "INSERT INTO JsonHotel (star, locality, street_address) VALUES (1, 'Taipei', 'abc street');";
		String columns = "", values = "";
		for(String key: attr.keySet()){
			String value = attr.get(key);
			columns += (key + ", ");
			values += ("'" + value + "'" + ", ");
		}
		columns = columns.substring(0, columns.length()-2);
		values = values.substring(0, values.length()-2);

		String sql = "INSERT INTO " + table + " ( " + columns + " ) " + " VALUES " + " ( " + values + " ) ;";
		System.out.println(sql);
		//String sql = "INSERT INTO " + table + " (star, locality, street_address) " + " VALUES " + "(1, 'Taipei', 'abc street');";

		try(Connection conn = this.connect()) {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			conn.close();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	private List<HashMap<String, String>> select(String table,  HashMap<String, String> attr) {
		//String sql = "INSERT INTO JsonHotel (star, locality, street_address) VALUES (1, 'Taipei', 'abc street');";
		String conditions = "";
		for(String key: attr.keySet()){
			String value = attr.get(key);
			conditions += key + "=" + "'" + value + "'" + " and ";
		}
		conditions = conditions.substring(0, conditions.length()-5);

		String sql = "SELECT * FROM " + table + " WHERE " + conditions + ";" ;
		System.out.println(sql);
		//String sql = "INSERT INTO " + table + " (star, locality, street_address) " + " VALUES " + "(1, 'Taipei', 'abc street');";

		try(Connection conn = this.connect()) {
			Statement stmt = conn.createStatement();
			// pstmt.executeUpdate();
			ResultSet rs = stmt.executeQuery(sql);
            
            List<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
            // loop through the result set
            while (rs.next()) {
            	ResultSetMetaData rsmd = rs.getMetaData();
            	int n_column = rsmd.getColumnCount();
            	HashMap<String, String> result = new HashMap<>();
            	for (int i = 1; i <= n_column; i++) 
            		result.put(rsmd.getColumnName(i), rs.getString(i));
            	results.add(result);
            	//System.out.println(rs.getString(1) + rs.getString(2) + rs.getString(3));
                // System.out.println(rs.getInt("id") +  "\t" + 
                //                    rs.getString("name") + "\t" +
                //                    rs.getDouble("capacity"));
            }
            conn.close();
            return results;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	private List<HashMap<String, String>> selectAll(String table){
		HashMap<String, String> attr = new HashMap<>();
		attr.put("'1'", "1");
		return this.select(table, attr);
	}

	private JsonHotel[] read_hotel_list(){
		Gson gson = new Gson();
		try {
			File f = new File(this.jsonPath);
			InputStreamReader read = new InputStreamReader(new FileInputStream(f),"big5"); 
			BufferedReader br = new BufferedReader(read);
			JsonHotel[] hotels = gson.fromJson(br, JsonHotel[].class);
			return hotels;
			//
		}catch (IOException e) {
			System.out.println("File not exist!");
			return null;
		}
	}

	private String hashPassword(String password){
		String encryptedString = "";
		try{
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(password.getBytes());
			encryptedString = new String(messageDigest.digest());
			encryptedString = Base64.getEncoder().encodeToString(encryptedString.getBytes());
		}catch(Exception e){}
		return encryptedString;
	}
}

// This class is for reading json file only, please ignore this class. 
class JsonHotel{
	public int HotelID; 
	public int HotelStar; 
	public String Locality; 
	@SerializedName("Street-Address")
	public String Street_Address; 
	public JsonRoom[] Rooms; 
	// void JsonHotel(){
	// 	rooms = new JsonRoom[3];
	// }
}

// This class is for reading json file only, please ignore this class. 
class JsonRoom{
	public String RoomType;
	public int RoomPrice;
	public int Number;
}