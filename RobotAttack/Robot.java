import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;

public class Robot {
	int id;
	String hostAddress;
	boolean targetReached;
	// int location[];
	// int target[];
	ArrayList<String> ipofrobot = new ArrayList<>();
	Board b;
	int boardLength;
	Location loc, target;
	Date myRequestDate;
	Queue<String> waitingrobots = new LinkedList<String>();
	RA r;
	ArrayList<Location> explored = new ArrayList<Location>();
	Location wanted;

	Queue<String> aroundthetarget = new LinkedList<>();


	public Robot() {
		this.hostAddress = hostAddress;
		r = new RA(this);
		wanted = new Location(-1, -1);	
	}
	public int getId() {
		return this.id;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public void setmylocation(int x, int y) {
		loc = new Location(x, y);
		explored.add(loc);
	
		wanted = getminNeighbour();
		r.myqueues.put(loc.x+" "+loc.y, new LinkedList<String>());
		r.myqueues.put(wanted.x+" "+wanted.y, new LinkedList<String>());

	}

	public void settarget(int x, int y) {
		target = new Location(x, y);
	}

	public Location getmylocation() {
		return loc;
	}

	public Location gettarget() {
		return target;
	}

	public int manhattanDist(Location l1, Location l2) {
		return Math.abs(l1.x - l2.x) + Math.abs(l1.y - l2.y);
	}

	public Location getminNeighbour() {
		ArrayList<Location> list = getneighbour();
		Collections.sort(list, new Comparator<Location>() {
			public int compare(Location l1, Location l2) {
				return manhattanDist(l1, target) - manhattanDist(l2, target);
			}
		});

		return list.get(0);
	}

	public ArrayList<Location> getneighbour() {
		ArrayList<Location> neighbors = new ArrayList<Location>();
		if (!((getmylocation().x + 1) >= boardLength)) {
			Location temp = new Location(getmylocation().x + 1, getmylocation().y);
			neighbors.add(temp);
		}
		if (!((getmylocation().x - 1) < 0)) {
			Location temp = new Location(getmylocation().x - 1, getmylocation().y);
			neighbors.add(temp);

		}
		if (!((getmylocation().y - 1) < 0)) {
			Location temp = new Location(getmylocation().x, getmylocation().y - 1);
			neighbors.add(temp);

		}
		if (!(getmylocation().y + 1 >= boardLength)) {
			Location temp = new Location(getmylocation().x, getmylocation().y + 1);
			neighbors.add(temp);

		}
		
		for(Location n: explored)
		{
			if(neighbors.contains(n))
			{
				neighbors.remove(n);
			}
		}

		return neighbors;

	}
	

	public void connect() {
		try {
			Socket r = new Socket(Constants.BOOTSTRAP_HOST, Constants.BOOSTRAP_LISTEN);
			ObjectOutputStream os = new ObjectOutputStream(r.getOutputStream());
			os.writeObject(new Message("init", ""));
			os.flush();
			// System.out.println("message sent");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printlist() {
		//System.out.println("Printing the robot's ip in network -  ");
		for (String ip : ipofrobot) {
			System.out.println(ip);
		}
	}

	public boolean checkdistance(Location loc, Location target) {
		if (manhattanDist(loc, target) == 0) {
			return true;
		}
		return false;
	}

	public void moveRobot(Location l) {
		//send message to server to print updated location
		try {
			Socket r = new Socket(Constants.BOOTSTRAP_HOST, Constants.BOOSTRAP_LISTEN);
			ObjectOutputStream os = new ObjectOutputStream(r.getOutputStream());
			Location[] location=new Location[2];
			location[0]=getmylocation();
			location[1]=new Location(l.x, l.y);
			os.writeObject(new Message("updateUI", location));
			os.flush();
			r.close();
			// System.out.println("message sent");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		System.out.println(" trying to Move");
		setmylocation(l.x, l.y);
		System.out.println("Location updated!" + loc.x + " " + loc.y);
		
		
		if (checkdistance(loc,  this.gettarget())) {
			this.targetReached=true;
//			System.out.println("Target Location is [" + this.gettarget().x + "," + this.gettarget().y + " ]");
			System.out.println("Robot location is [" + loc.x + " , " + loc.y + " ]");
			System.out.println("REACHED TARGET");
		} else {
			startrequest();
		}
		// }

	}
	


	public void startrequest() {
		
		//System.out.println("Other wanted          "+ wanted);
		r.requestCS();
	}

	public static void main(String args[]) {
		Robot robot = new Robot();
		RobotListener robotlistener = new RobotListener(robot);
		robotlistener.start();
		robot.connect();

	}
}
