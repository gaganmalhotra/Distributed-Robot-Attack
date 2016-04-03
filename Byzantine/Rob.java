import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;

/**
 * 
 * @author Sagar Shah
 * @author Gagandeep Malhotra 
 * @author Sumedha Singh
 *
 */
public class Rob {

	private final JPanel gui = new JPanel(new BorderLayout(3, 3));
	private JButton[][] boardSquares;
	private JPanel board;
	JFrame f = new JFrame("Distributed Robot Attack");
	private final JLabel message = new JLabel("Robot Attack");

	Rob(int x) {
		boardSquares = new JButton[x][x];
		initializeGui(x);
	}

	public final void initializeGui(int x) {
		// set up the main GUI
		gui.setBorder(new EmptyBorder(5, 5, 5, 5));
		JToolBar tools = new JToolBar();
		tools.setFloatable(false);
		gui.add(tools, BorderLayout.PAGE_START);
		tools.addSeparator();
		tools.addSeparator();
		tools.add(message);

		gui.add(new JLabel(""), BorderLayout.LINE_START);

		board = new JPanel(new GridLayout(0, x));
		board.setBorder(new LineBorder(Color.WHITE));
		gui.add(board);

		Insets buttonMargin = new Insets(0, 0, 0, 0);
		for (int i = 0; i < boardSquares.length; i++) {
			for (int j = 0; j < boardSquares.length; j++) {
				JButton b = new JButton();
				b.setMargin(buttonMargin);
				ImageIcon icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
				b.setIcon(icon);
				b.setBackground(Color.WHITE);
				boardSquares[i][j] = b;
			}
		}

		for (int i = 0; i < boardSquares.length; i++) {
			for (int j = 0; j < boardSquares.length; j++) {
				board.add(boardSquares[i][j]);
			}
		}
	}

	public void addRobot(int newX, int newY, String type) {

		System.out.println("Adding robot at" + newX + "," + newY);
		if (type.equals("robot")) {
			boardSquares[newX][newY].removeAll();
			boardSquares[newX][newY]
					.add(new JLabel(new String("<html><font size=26><tab><b>R</b><br></font size></html>")));
			f.repaint();
			f.revalidate();
		} else if (type.equals("target")) {
			System.out.println("Adding robot at" + newX + "," + newY);
			boardSquares[newX][newY].removeAll();
			boardSquares[newX][newY]
					.add(new JLabel(new String("<html><font size=26><tab><b>T</b><br></font size></html>")));
			f.repaint();
			f.revalidate();
		}

	}

	public void removeAll() {
		for (int i = 0; i < boardSquares.length; i++) {
			for (int j = 0; j < boardSquares.length; j++) {
				boardSquares[i][j]
						.add(new JLabel(new String("<html><font size=26><tab><b></b><br></font size></html>")));
			}
		}
		f.repaint();
		f.revalidate();
	}

	public void updateRobot(int x, int y, int newX, int newY) {
		boardSquares[x][y].removeAll();
		boardSquares[newX][newY]
				.add(new JLabel(new String("<html><font size=26><tab><b>R</b><br></font size></html>")));
		f.repaint();
		f.revalidate();
	}

	public void init(Rob cb) {
		f.add(cb.getGui());
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setLocationByPlatform(true);
		f.pack();
		// ensures the minimum size is enforced.
		f.setMinimumSize(f.getSize());
		f.setVisible(true);
		f.repaint();
	}

	public final JComponent getBoard() {
		return board;
	}

	public final JComponent getGui() {
		return gui;
	}
}