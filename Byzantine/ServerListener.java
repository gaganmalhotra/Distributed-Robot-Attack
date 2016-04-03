import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

/**
 * 
 * @author Sagar Shah
 * @author Gagandeep Malhotra 
 * @author Sumedha Singh
 *
 */
public class ServerListener extends Thread {
	HashMap<Integer, ArrayList<Location>> levelmap = new HashMap<>();

	BootstrapServer server;
	ServerSocket serverSocket;
	Board b;
	Rob ui;
	int boardLength;
	ArrayList<String> robotlist = new ArrayList<String>();
	HashMap<String, int[]> robottolocation = new HashMap<String, int[]>();
	Queue<Location> freshens;

	/*public ServerListener(BootstrapServer server) throws IOException {
		this.server = server;
		serverSocket = new ServerSocket(Constants.BOOSTRAP_LISTEN);
		System.out.println("Waiting for Robots to connect to me ...");
		freshens = server.aroundthetarget();
	}*/
	
	// constructor when target location is not passed
		public ServerListener(BootstrapServer server, Rob ui, int boardLength) throws IOException {
			this.server = server;
			this.ui = ui;
			this.boardLength = boardLength;
			b = new Board(boardLength, boardLength);
			serverSocket = new ServerSocket(Constants.BOOSTRAP_LISTEN);
			freshens = server.aroundthetarget();
			System.out.println("Waiting for Robots to connect to me ...");
		}

		// constructor when target location is passed
		public ServerListener(BootstrapServer server, Rob ui, int boardLength, int target_x, int target_y)
				throws IOException {
			this.server = server;
			this.ui = ui;
			this.boardLength = boardLength;
			b = new Board(boardLength, boardLength);
			serverSocket = new ServerSocket(Constants.BOOSTRAP_LISTEN);
			freshens = server.aroundthetarget();
			System.out.println("Waiting for Robots to connect to me ...");
		}

	@Override
	public void run() {
		while (true) {

			try {
				Socket soc = serverSocket.accept();
				// System.out.println("Robot connected");
				ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
				Message m = (Message) ois.readObject();
				System.out.println("Message" + m);
				if (m.message.equals("init")) {
					String hostaddress = soc.getInetAddress().getHostAddress();
					if (!((m.data).equals("isFaulty"))) {
						robotlist.add(hostaddress);
					}
					int[] locationonboard = new int[5];

					int x = server.board.getRandomLocation();
					int y = server.board.getRandomLocation();

					while (true) {
						boolean isPresent = false;
						for (int[] a : robottolocation.values()) {
							if ((x == a[0] && y == a[1])) {
								isPresent = true;
								break;
							}
						}

						if (x == server.board.target_x && y == server.board.target_y) {
							isPresent = true;
						}

						if (isPresent) {
							x = server.board.getRandomLocation();
							y = server.board.getRandomLocation();
						} else {
							break;
						}
					}

					locationonboard[0] = x;
					locationonboard[1] = y;
					robottolocation.put(hostaddress, locationonboard);
					// update UI
					ui.addRobot(x, y, "robot");
					
					Location target = freshens.poll();
					locationonboard[2] = target.x;
					locationonboard[3] = target.y;
					locationonboard[4] = boardLength;
					Socket robotsocket = new Socket(hostaddress, Constants.ROBOT_LISTEN);
					ObjectOutputStream oos = new ObjectOutputStream(robotsocket.getOutputStream());
					oos.writeObject(new Message("initlocation", locationonboard));
					oos.writeObject(new Message("levellist", levelmap));

					oos.flush();
				} else if (m.message.equals("sendtoserver")) {
					Location l = (Location) m.data;
					String hostaddress = soc.getInetAddress().getHostAddress();
					int locationonboard[] = new int[2];
					locationonboard[0] = l.x;
					locationonboard[1] = l.y;
					robottolocation.put(hostaddress, locationonboard);

				}else if (m.message.equals("updateUI")) {
					Location[] loc = (Location[]) m.data;
					ui.updateRobot(loc[0].x, loc[0].y, loc[1].x, loc[1].y);
					ui.addRobot(server.board.target_x, server.board.target_y, "target");
				}else if (m.message.equals("ByzantineConsensus")) {
					int[] receivedLocs = (int[]) m.data;
					if ((receivedLocs[0] != receivedLocs[2]) && (receivedLocs[1] != receivedLocs[3])) {
						// robotlist
						for (int i = 0; i < robotlist.size(); i++) {
							if (robotlist.get(i).equals(soc.getInetAddress().getHostAddress())) {
								robotlist.remove(i);
								System.out.println("Faulty robot at>>>>>>>> "+ soc.getInetAddress().getHostAddress());
							}
						}
						//resending updated robot list
						server.sendlist(robotlist);
					}

				}

			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
