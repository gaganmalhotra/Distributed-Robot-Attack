import java.io.Serializable;

/**
 * 
 * @author Sagar Shah
 * @author Gagandeep Malhotra 
 * @author Sumedha Singh
 *
 */
public class Message implements Serializable {

	String message;
	Object data;
	public Message(String message, Object data){
		this.message= message;
		this.data=data;
	}
	@Override
	public String toString() {
		return "Message [message=" + message + ", data=" + data + "]";
	}
	
	
}
