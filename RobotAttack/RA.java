import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class RA {

	Robot robot;
	Location loc;
	DateNLocation ts;
	// Queue<String> mycsqueue= new LinkedList<String>();
	HashMap<String, Queue<String>> myqueues = new HashMap<String, Queue<String>>();

	public RA() {

	}

	public RA(Robot robot) {
		this.robot = robot;
	}

	public void requestCS() {

		for (String destIp : robot.ipofrobot) {
			try {
				// System.out.println(InetAddress.getLocalHost().getHostAddress());
				if (!destIp.equals(InetAddress.getLocalHost().getHostAddress())) {
					Socket destSocket = new Socket(destIp, Constants.ROBOT_LISTEN);
					ObjectOutputStream oos = new ObjectOutputStream(destSocket.getOutputStream());
					Date date = new Date();
					robot.myRequestDate = date;

					ts = new DateNLocation(date, robot.wanted);
					System.out.println("Requesting CS");
					oos.writeObject(new Message("Requesting CS", ts));
					oos.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void checkCS(String ip, DateNLocation dl) {
		if (robot.getmylocation().x == dl.loc.x && robot.getmylocation().y == dl.loc.y) {
			System.out.println("Inside here!!!!");
			if (myqueues == null) {
				myqueues = new HashMap<String, Queue<String>>();
			}
			if (!myqueues.containsKey(robot.wanted.x + " " + robot.wanted.y)) {
				myqueues.put(robot.wanted.x + " " + robot.wanted.y, new LinkedList<String>());

			}
			if (!myqueues.containsKey(robot.getmylocation().x + " " + robot.getmylocation().y)) {
				myqueues.put(robot.getmylocation().x + " " + robot.getmylocation().y, new LinkedList<String>());

			}
			if (!myqueues.get(dl.loc.x + " " + dl.loc.y).contains(ip)) {
				myqueues.get(dl.loc.x + " " + dl.loc.y).offer(ip);
			}

		} else {
			try {
				Socket s = new Socket(ip, Constants.ROBOT_LISTEN);
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(new Message("Reply to CS", dl.loc));
				oos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void releaseCS() {
		if(myqueues.get(robot.loc.x + " " + robot.loc.y)==null){
			return;
		}
		while (!myqueues.get(robot.loc.x + " " + robot.loc.y).isEmpty()) {
			try {
				Socket socket = new Socket(myqueues.get(loc.x + " " + loc.y).poll(), Constants.ROBOT_LISTEN);
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(new Message("Reply to CS", "OK"));

				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		myqueues.remove(loc.x + " " + loc.y);

	}

}
