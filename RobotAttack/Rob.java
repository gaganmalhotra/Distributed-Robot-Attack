import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;

public class Rob {

    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JButton[][] chessBoardSquares;
    private JPanel chessBoard;
    JFrame f = new JFrame("Distributed Robot Attack");
    private final JLabel message = new JLabel(
            "Robot Attack");

    Rob(int x) {
    	chessBoardSquares= new JButton[x][x];
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

        chessBoard = new JPanel(new GridLayout(0, x));
        chessBoard.setBorder(new LineBorder(Color.WHITE));
        gui.add(chessBoard);

        Insets buttonMargin = new Insets(0,0,0,0);
        for (int i = 0; i < chessBoardSquares.length; i++) {
        	for (int j = 0; j < chessBoardSquares.length; j++) {
        		JButton b = new JButton();
                b.setMargin(buttonMargin);
                ImageIcon icon = new ImageIcon(
                        new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                b.setBackground(Color.WHITE);
                chessBoardSquares[i][j]=b;
			}
		}
        
        
        for (int i = 0; i < chessBoardSquares.length; i++) {
        	for (int j = 0; j < chessBoardSquares.length; j++) {
        		chessBoard.add(chessBoardSquares[i][j]);
        	}
        }
        
       /* // create the chess board squares
        Insets buttonMargin = new Insets(0,0,0,0);
        for (int ii = 0; ii < chessBoardSquares.length; ii++) {
            for (int jj = 0; jj < chessBoardSquares.length; jj++) {
                JButton b = new JButton();
                b.setMargin(buttonMargin);
                // our chess pieces are 64x64 px in size, so we'll
                // 'fill this in' using a transparent icon..
                ImageIcon icon = new ImageIcon(
                        new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                b.setBackground(Color.WHITE);
                chessBoardSquares[ii][jj] = b;
            }
        }*/

        //fill the chess board
       /* chessBoard.add(new JLabel(""));
        // fill the black non-pawn piece row
        for (int ii = 0; ii < x; ii++) {
            for (int jj = 0; jj < x; jj++) {
              //  switch (jj) {
                    case 0:
                        chessBoard.add(new JLabel("" + (ii + 1),
                                SwingConstants.CENTER));
                    default:
                        chessBoard.add(chessBoardSquares[ii][jj]);
               // }
            }
        }*/
    }

    
    public  void addRobot(int newX, int newY, String type){
    	
    	System.out.println("Adding robot at"+newX+","+newY);
    	if(type.equals("robot")){
    		 chessBoardSquares[newX][newY].removeAll();
    	        chessBoardSquares[newX][newY].add(new JLabel(new String("<html><font size=26><tab><b>R</b><br></font size></html>")));
    	        f.repaint();
    	        f.revalidate();
    	}else if(type.equals("target")){
    		System.out.println("Adding robot at"+newX+","+newY);
    		 chessBoardSquares[newX][newY].removeAll();
    	        chessBoardSquares[newX][newY].add(new JLabel(new String("<html><font size=26><tab><b>T</b><br></font size></html>")));
    	        f.repaint();
    	        f.revalidate();
    	}
   	
   }
    
    public  void removeAll(){
    	  for (int i = 0; i < chessBoardSquares.length; i++) {
          	for (int j = 0; j < chessBoardSquares.length; j++) {
          		chessBoardSquares[i][j].add(new JLabel(new String("<html><font size=26><tab><b></b><br></font size></html>")));
          	}
          }
    	  f.repaint();
          f.revalidate();
   }
    
    public  void updateRobot(int x, int y, int newX, int newY){
    	 chessBoardSquares[x][y].removeAll();
         chessBoardSquares[newX][newY].add(new JLabel(new String("<html><font size=26><tab><b>R</b><br></font size></html>")));
         f.repaint();
         f.revalidate();
    }
    
    public void init(Rob cb){
    	 
         f.add(cb.getGui());
         f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         f.setLocationByPlatform(true);

         // ensures the frame is the minimum size it needs to be
         // in order display the components within it
         f.pack();
         // ensures the minimum size is enforced.
         f.setMinimumSize(f.getSize());
         f.setVisible(true);
         f.repaint();
    }
    
    public final JComponent getChessBoard() {
        return chessBoard;
    }

    public final JComponent getGui() {
        return gui;
    }
}