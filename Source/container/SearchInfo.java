package container;

public class SearchInfo{
	public String location;
	public int peopleNum;
	public String dateIn;
	public String dateOut;

	public SearchInfo(){
		//Default constructor
		this.location = "";
		this.peopleNum = 0;
		this.dateIn = "";
		this.dateOut = "";
	}
	

	public SearchInfo(String location, int peopleNum, String dateIn, String dateOut){
		// Parameter constructor
		this.location = location;
		this.peopleNum = peopleNum;
		this.dateIn = dateIn;
		this.dateOut = dateOut;
	}

	@Override 
	public String toString() {
		// return String representation of the class
        return "-----SearchInfo-----\n" + 
        	   "location: " + this.location + "\n" + 
        	   "peopleNum: " + this.peopleNum + "\n" + 
        	   "dateIn: " +  this.dateIn + "\n" +
        	   "dateOut: " +  this.dateOut + "\n";
    }
}