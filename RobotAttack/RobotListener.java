
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 
 * @author Sagar Shah
 *
 */
public class RobotListener extends Thread {

	ServerSocket ss;
	Robot robot;
	int robotcount = 0;
	HashMap<String, Integer> countmap = new HashMap<String, Integer>();

	/**
	 * 
	 * Constructor
	 * 
	 * @param robot
	 */
	public RobotListener(Robot robot) {
		try {
			ss = new ServerSocket(Constants.ROBOT_LISTEN);
			this.robot = robot;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// System.out.println("Robot listeners started waiting for connect");
		while (true) {
			try {

				Socket s = ss.accept();
				// System.out.println("Server sent location");
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				Message m = (Message) ois.readObject();
				RA ra = new RA(robot);
				if (m.message.equals("initlocation")) {
					int[] location = (int[]) m.data;
					robot.settarget(location[2], location[3]);
					System.out.println(location[0] + "    " + location[1]);
					robot.b = new Board(location[4], location[4]);
					robot.boardLength = location[4];
					robot.setmylocation(location[0], location[1]);
					System.out.println(" location [" + location[0] + " , " + location[1] + "]");
				} else if (m.message.equals("sending_list")) {
					robot.ipofrobot = (ArrayList<String>) m.data;
					System.out.println(robot.ipofrobot);
					// robot.printlist();
					robot.startrequest();
				} else if (m.message.equals("Requesting CS")) {
					ra.checkCS(s.getInetAddress().getHostAddress(), (DateNLocation) m.data);
				} else if (m.message.equals("Reply to CS")) {
					robotcount++;
					Location l = (Location) m.data;
					String key = l.x + " " + l.y;
					if (!countmap.containsKey(key)) {
						countmap.put(key, 0);
					}
					int count = countmap.get(key);
					countmap.put(key, count + 1);
					// System.out.println("Printing countmap "+ countmap);
					// System.out.println("printing count "+ robotcount);
					if (countmap.get(key) == (robot.ipofrobot.size()-1)) {
						ra.releaseCS();
						System.out.println("Calling move robot");
						countmap.put(key, 0);
						robot.moveRobot(l);
						// System.out.println("Sending OK");

						robotcount = 0;

					}
					System.out.println("Sending ok");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
