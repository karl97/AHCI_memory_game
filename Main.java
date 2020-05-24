
import javax.swing.JFrame;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.ALMemoryHelper;


public class Main {
	public static MemoryGame game;
	public static JFrame frame;
	public static Application application;
	
	public static void main(String[] args) {
		application = new Application(args, "tcp://localhost:43691");
		//application = new Application(args, "tcp://localhost:31343");
		frame = new JFrame("Game 1: Jumper");
		game = new MemoryGame(350, 350, 2);
		frame.add(game);
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.Start();
	}
}
