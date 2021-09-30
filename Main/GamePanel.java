package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.JPanel;

import GameState.GameStateManager;


public class GamePanel extends JPanel
		implements Runnable, KeyListener, MouseListener, MouseMotionListener{

	// dimensions
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;

	// game thread
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;

	// image
	private BufferedImage image;
	private Graphics2D g;

	// game state manager
	private GameStateManager gsm;

	public GamePanel() {
		super();
		setPreferredSize(
				new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			addMouseListener(this);
			thread.start();
		}
	}

	private void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D)image.getGraphics();
		running = true;
		gsm = new GameStateManager();
	}

	public void run() {
		init();
		long start;
		long elapsed;
		long wait;
		while(running) {
			start = System.nanoTime();
			update();
			draw();
			drawToScreen();
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed / 1000000;
			if(wait < 0)
				wait = 5;
			try {
				Thread.sleep(wait);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void update() {
		gsm.update();
	}

	private void draw() {
		gsm.draw(g);
	}

	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
		g2.dispose();
	}

	public void keyTyped(KeyEvent key) {}
	public void keyPressed(KeyEvent key) {
		gsm.keyPressed(key, key.getKeyCode());
	}
	public void keyReleased(KeyEvent key) {
		gsm.keyReleased(key.getKeyCode());
	}
	public void mouseClicked(MouseEvent e) { gsm.mouseClicked(e); }
	public void mousePressed(MouseEvent e) { gsm.mousePressed(e); }
	public void mouseReleased(MouseEvent e) { gsm.mouseReleased(e); }
	public void mouseEntered(MouseEvent e) { try{
		gsm.mouseEntered(e);
	} catch (NullPointerException exception){}
	}
	public void mouseExited(MouseEvent e) { gsm.mouseExited(e); }
	public void mouseDragged(MouseEvent e) { gsm.mouseDragged(e); }
	public void mouseMoved(MouseEvent e) { try{
		gsm.mouseMoved(e);
	} catch (NullPointerException exception){}
	}
}
















