import java.io.Serializable;

/**
 * 
 * @author Sagar Shah
 * @author Gagandeep Malhotra 
 * @author Sumedha Singh
 *
 */
public class Location implements Serializable {

	/**
	 * 
	 */
	
	public String toString() {
		return "Location [x=" + x + ", y=" + y + "]";
	}


	public int x;
	public int y;

	
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	
}
