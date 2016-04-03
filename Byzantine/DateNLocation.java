import java.io.Serializable;
import java.util.Date;


/**
 * 
 * @author Sagar Shah
 * @author Gagandeep Malhotra 
 * @author Sumedha Singh
 *
 */
public class DateNLocation implements Serializable{
	
	Date date;
	Location loc;
	
	public DateNLocation(Date date, Location loc){
		this.date=date;
		this.loc=loc;
	}

}
