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

public class Database{
	private String dbPath;
	private String jsonPath;
	private Connection db_conn;
	public Database(String dbPath, String jsonPath){
		// Pass in "" as jsonPath if the db has already established.
		// Pass in the jsonPath to clear the database and init the database by jsonPath.
		this.dbPath = dbPath;
		this.jsonPath = jsonPath;
		if(!this.jsonPath.equals("")){
			initDB();
			establishDB();
		}
		HashMap<String, String> hotel_attr = new HashMap<>();
		hotel_attr.put("star", "2");
		List<HashMap<String, String>> results = this.select("Hotel", hotel_attr);
		for(HashMap<String, String> result: results){
			for(String key: result.keySet()){
				String value = result.get(key);
				System.out.println(key + ": " + value);
			}
		}
	}

	private void initDB(){
	    try{	
	    	File f = new File("../Database/init_table.sql");
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
		Hotel[] hotels = read_hotel_list();
		if(hotels == null){
			System.out.println("No hotel loaded!\n");
			return;
		}
		int count = 0;
		for(Hotel hotel: hotels){
			HashMap<String, String> hotel_attr = new HashMap<>();
			hotel_attr.put("star", Integer.toString(hotel.HotelStar));
			hotel_attr.put("locality", hotel.Locality);
			hotel_attr.put("street_address", hotel.Street_Address);
			insert("Hotel", hotel_attr);
			for(Room room: hotel.Rooms){
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

	private void insert(String table,  HashMap<String, String> attr) {
		//String sql = "INSERT INTO Hotel (star, locality, street_address) VALUES (1, 'Taipei', 'abc street');";
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
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private List<HashMap<String, String>> select(String table,  HashMap<String, String> attr) {
		//String sql = "INSERT INTO Hotel (star, locality, street_address) VALUES (1, 'Taipei', 'abc street');";
		String conditions = "";
		for(String key: attr.keySet()){
			String value = attr.get(key);
			conditions += key + "=" + "'" + value + "'" + ", ";
		}
		conditions = conditions.substring(0, conditions.length()-2);

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

	private Hotel[] read_hotel_list(){
		Gson gson = new Gson();
		try {
			File f = new File(this.jsonPath);
			InputStreamReader read = new InputStreamReader(new FileInputStream(f),"big5"); 
			BufferedReader br = new BufferedReader(read);
			Hotel[] hotels = gson.fromJson(br, Hotel[].class);
			return hotels;
			//
		}catch (IOException e) {
			System.out.println("File not exist!");
			return null;
		}
	}
}

// This class is for reading json file only
class Hotel{
	public int HotelID; 
	public int HotelStar; 
	public String Locality; 
	@SerializedName("Street-Address")
	public String Street_Address; 
	public Room[] Rooms; 
	// void Hotel(){
	// 	rooms = new Room[3];
	// }
}

// This class is for reading json file only
class Room{
	public String RoomType;
	public int RoomPrice;
	public int Number;
}