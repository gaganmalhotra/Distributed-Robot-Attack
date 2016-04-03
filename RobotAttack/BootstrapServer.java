import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BootstrapServer {
//	HashMap<String, String> mp = new HashMap<>();
	Board board;

	public BootstrapServer() {
		//board = new Board();
	}

	public Queue<Location> aroundthetarget() {
		Queue<Location> freshlist = new LinkedList<Location>();
		if (!((board.target_x + 1) >= board.board.length)) {
			Location temp = new Location(board.target_x + 1, board.target_y);
			freshlist.offer(temp);
		}
		if (!((board.target_x - 1) < 0)) {
			Location temp = new Location(board.target_x - 1, board.target_y);
			freshlist.offer(temp);
		}
		if (!((board.target_y - 1) < 0)) {
			Location temp = new Location(board.target_x, board.target_y - 1);
			freshlist.add(temp);
		}
		if (!(board.target_y + 1 >= board.board.length)) {
			Location temp = new Location(board.target_x, board.target_y + 1);
			freshlist.add(temp);
		}
		if (!(board.target_y + 1 >= board.board.length && board.target_x + 1 >= board.board.length)) {
			Location temp = new Location(board.target_x+1, board.target_y + 1);
			freshlist.add(temp);
		}
		if (!(board.target_y -1 < board.board.length && board.target_x - 1 < board.board.length)) {
			Location temp = new Location(board.target_x-1, board.target_y - 1);
			freshlist.add(temp);
		}
		return freshlist;
	}

	public void sendlist(ArrayList<String> robotlist) {
		try {
			for (String ip : robotlist) {
				Socket s = new Socket(ip, Constants.ROBOT_LISTEN);
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(new Message("sending_list", robotlist));
				oos.flush();
					}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws IOException {
		BootstrapServer server = new BootstrapServer();
		ServerListener serverlistener;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//start the UI 
		//System.out.println("Enter the dimension for the square matrix board eg: l");
		String dimen="12";
		server.board = new Board(Integer.parseInt(dimen),Integer.parseInt(dimen));
		
		server.aroundthetarget();
		Rob r= new Rob(Integer.parseInt(dimen));
		r.init(r);
		
		try {
			serverlistener = new ServerListener(server,r,Integer.parseInt(dimen));
			serverlistener.start();
			while (true) {
				System.out.println("Target Location"+ server.board.target_x + " "+ server.board.target_y);
				r.addRobot(server.board.target_x, server.board.target_y, "target");
				System.out.println("1. Move Robot to Target");
				System.out.println("2. Exit");

				int choice = Integer.parseInt(br.readLine());
				switch (choice) {
				case 1:
					server.sendlist(serverlistener.robotlist);
					break;
				case 2:
					System.exit(0);
				default:
					System.out.println("Not a good choice.. Try again!");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
