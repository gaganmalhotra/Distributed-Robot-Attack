import java.io.Serializable;
import java.util.Date;


public class DateNLocation implements Serializable{
	
	Date date;
	Location loc;
	
	public DateNLocation(Date date, Location loc){
		this.date=date;
		this.loc=loc;
	}

}
