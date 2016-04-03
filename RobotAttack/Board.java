import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Board {
	final int rows = 12, columns = 12;

	int board[][] = new int[rows][columns];

	Robot robot;

	int x, y;

	final int target_x, target_y;
	HashMap<Integer, ArrayList<String>> maplevel = new HashMap<>();
	Random rand = new Random();
	int aStart = 0;
	int aEnd;

	public Board(int xLength, int yLength) {
		aEnd = xLength - 1;
		target_x = getRandomLocation();
		target_y = getRandomLocation();
		initializeboard();
	}

	public void initializeboard() {
		for (int i = 0; i <= board.length - 1; i++) {
			for (int j = 0; j <= board.length - 1; j++) {
				board[i][j] = 0;
			}
		}
	}

	public int getRandomLocation() {
		long range = (long) aEnd - (long) aStart + 1;
		long fraction = (long) (range * rand.nextDouble());
		int randomNumber = (int) (fraction + aStart);
		return randomNumber;
	}

	public void levelmap(Location target) {
		for (int i = 0; i < board.length; i++) {
			ArrayList<String> listlevel = new ArrayList<String>();
			if (!(target.x + i >= board.length)) {
				listlevel.add((target.x + i) + " " + target.y);
			}
			if (!(target.x - i < 0)) {
				listlevel.add((target.x - i) + " " + (target.y));
			}
			if (!(target.y + i >= board.length)) {
				listlevel.add((target.x) + " " + (target.y + i));
			}
			if (!(target.y - i < 0)) {
				listlevel.add(target.x + " " + (target.y - i));
			}
			maplevel.put(i, listlevel);
		}

	}
}
