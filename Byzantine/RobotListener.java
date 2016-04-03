
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @author Sagar Shah
 * @author Gagandeep Malhotra 
 * @author Sumedha Singh
 *
 */
public class RobotListener extends Thread {

	class MyComparator implements Comparator {

		Map map;

		public MyComparator(Map map) {
			this.map = map;
		}

		public int compare(Object o1, Object o2) {

			return ((Integer) map.get(o2)).compareTo((Integer) map.get(o1));

		}
	}

	ServerSocket ss;
	Robot robot;
	int robotcount = 0;
	Map<String, Integer> mostOccuringTargetMap = new HashMap<String, Integer>();
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
					robot.b = new Board(location[4], location[4]);
					robot.boardLength = location[4];
					robot.setmylocation(location[0], location[1]);
				} else if (m.message.equals("byzantine")) {
					int[] loc = (int[]) m.data;
					robot.settarget(loc[0], loc[1]);
					robot.checkByzantinesProblem();
				} else if (m.message.equals("ReceiveTargets")) {

					if (mostOccuringTargetMap.size() == robot.ipofrobot.size()) {
						MyComparator comp = new MyComparator(mostOccuringTargetMap);
						TreeMap<String, Integer> newMap = new TreeMap(comp);
						newMap.putAll(mostOccuringTargetMap);
						String[] loc = newMap.firstKey().split(" ");
						try {
							Socket r = new Socket(Constants.BOOTSTRAP_HOST, Constants.BOOSTRAP_LISTEN);
							ObjectOutputStream os = new ObjectOutputStream(r.getOutputStream());
							// sends its target loc and most common target loc
							// to boot strap
							os.writeObject(new Message("ByzantineConsensus", new int[] { robot.target.x, robot.target.y,
									Integer.parseInt(loc[0]), Integer.parseInt(loc[1]) }));
							os.flush();
							r.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (mostOccuringTargetMap.isEmpty()) {
						mostOccuringTargetMap.put(((Location) m.data).x + " " + ((Location) m.data).y, 1);
					} else if (null != mostOccuringTargetMap.get(((Location) m.data).x + " " + ((Location) m.data).y)) {
						mostOccuringTargetMap.put(((Location) m.data).x + " " + ((Location) m.data).y,
								mostOccuringTargetMap.get(((Location) m.data).x + " " + ((Location) m.data).y) + 1);
					} else {
						mostOccuringTargetMap.put(((Location) m.data).x + " " + ((Location) m.data).y, 1);
					}
				}

				else if (m.message.equals("sending_list")) {
					robot.ipofrobot = (ArrayList<String>) m.data;
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
					if (countmap.get(key) == (robot.ipofrobot.size() - 1)) {
						ra.releaseCS();
						countmap.put(key, 0);
						robot.moveRobot(l);
						robotcount = 0;

					}
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
