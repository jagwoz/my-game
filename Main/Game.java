package Main;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Game {
	public static void main(String[] args) {
		
		JFrame window = new JFrame("Call of Ninja");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				cursorImg, new Point(0, 0), "blank cursor");

		window.getContentPane().setCursor(blankCursor);
	}
	
}
