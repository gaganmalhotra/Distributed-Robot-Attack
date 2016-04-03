import java.io.Serializable;

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
