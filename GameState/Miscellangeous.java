package GameState;

import Entity.Enemy;
import Entity.Explosion;
import Entity.Player;
import Equipment.Inventory;
import TileMap.Background;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Miscellangeous extends GameState {
    private BufferedImage button;
	private ArrayList<BufferedImage> buttonsOn = new ArrayList<>();
	private boolean pausing;
	private TileMap tileMap;
	private Background bg;
	private boolean pressed = false;
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	private long pauseTimer;
	private Inventory inventory;
	private Mouse mouse;
	private float[] scalesCopy = { 1f, 1f, 1f, 0.0f };
	private ArrayList<float[]> scales;
	private ArrayList<float[]> offsets;
	private ArrayList<RescaleOp> rops;
	int pause=0;
	private Point pointM = new Point(0,0);
	private ArrayList<Rectangle> buttons = new ArrayList<>();

	public void setPause(){
		pausing = true;
		pauseTimer = System.nanoTime();
	}

	public void isPausing() {
		if (pausing) {
			long elapsed = (System.nanoTime() - pauseTimer) / 1000000;
			if (elapsed > 350) {
				pausing = false;
			}
		}
	}

	public Miscellangeous(TileMap tm , GameStateManager gsm, Player v, Inventory i) {
		super();
		this.gsm = gsm;
		tileMap = tm;
		player = v;
		inventory = i;
		pausing = false;
		enemies = new ArrayList<Enemy>();
		if(tm == null) { tileMap = new TileMap(120); }
		if(v == null) { player = new Player(tileMap); }
		if(i == null){ inventory = new Inventory(tileMap); }
		inventory.show();
		mouse = new Mouse();
		init();
        try {
			button = ImageIO.read(getClass().getResourceAsStream("/Backgrounds/button2.png"));
			BufferedImage buttonOn = ImageIO.read(getClass().getResourceAsStream("/Backgrounds/buttons.png"));
            String musicFile = "Resources/Music/music.mp3";
            InputStream in = new FileInputStream(musicFile);

			for(int j = 0; j < 7; j++) {
				buttonsOn.add(buttonOn.getSubimage(0, j*80, 270, 80));
			}

        } catch (Exception e) {
            e.printStackTrace();
        }
        scales = new ArrayList<>(Collections.nCopies(7, scalesCopy));
        offsets = new ArrayList<>(Collections.nCopies(7, new float[4]));
		rops = new ArrayList<>(Collections.nCopies(7, new RescaleOp(scales.get(0), offsets.get(0), null)));

		for(int j=0; j<7; j++){
			Rectangle tmp = new Rectangle(30, 135 + j*80, 280, 60);
			buttons.add(tmp);
		}
	}

	public void init() {
		if(pause==0)
		{
			tileMap.loadTiles("/Tilesets/grasstileset.gif");
			tileMap.loadMap("/Maps/level1-1.map");
			tileMap.setPosition(0, 0);
			tileMap.setTween(1);
			player.setPosition(200, 200);
		}
		bg = new Background("/Backgrounds/realmenu.png", 0);
	}

	public void update() {
		isPausing();
		inventory.update(player.getLevel());

		for(int i=0; i<7; i++){
			if(buttons.get(i).contains(pointM)){
				if(scales.get(i)[3]<1) scales.set(i,new float[]{ 2f, 2f, 2f, (float)(scales.get(i)[3] + 0.02)});
			}
			else if(scales.get(i)[3]>0) scales.set(i,new float[]{ 2f, 2f, 2f, (float)(scales.get(i)[3] - 0.03)});
			if(i == 3) {scales.set(i,new float[]{ 2f, 2f, 2f, 1f});}
		}

		for(int i=0; i<7; i++)
			rops.set(i,new RescaleOp(scales.get(i), offsets.get(i), null));

		inventory.addItemInfo();
//		System.out.println(MouseInfo.getPointerInfo().getLocation().x + " " + MouseInfo.getPointerInfo().getLocation().y);
	}

	public void draw(Graphics2D g) {

		bg.draw(g);
		for(int i=0; i<7; i++) g.drawImage(button, 40, i*80 + 138, null);
		inventory.draw(g);

		for(int i=0; i<7; i++)
			g.drawImage(buttonsOn.get(i), rops.get(i), 30, i*80 + 128);

		mouse.draw(g);
	}

	public void keyPressed(KeyEvent ke, int k) {
		if (k == KeyEvent.VK_X){
			inventory.removeItem();
		}

			Random r = new Random();
//		if (k == KeyEvent.VK_M) inventory.addItem((r.nextInt(19)+1)*5);
	}

	public void keyReleased(int k) {
	}
	public void mouseClicked(MouseEvent e) {

	}
	public void mousePressed(MouseEvent e) { mouse.on();
		if(SwingUtilities.isLeftMouseButton(e)){
			for(int i=0; i<7; i++)
				if(buttons.get(i).contains(pointM)){

					gsm.setState(tileMap, i+3, player, inventory); }
			inventory.upgradeItem();
		}
		else if(SwingUtilities.isRightMouseButton(e)){
			inventory.equipItem();
		}
	}
	public void mouseReleased(MouseEvent e) { mouse.off(); }
	public void mouseEntered(MouseEvent e) { mouse.create(); }
	public void mouseExited(MouseEvent e) { mouse.destroy(); }
	public void mouseDragged(MouseEvent e) { mouse.update(e.getX(),e.getY()); pointM.setLocation(e.getX(),e.getY()); inventory.setPointer(pointM); }
	public void mouseMoved(MouseEvent e) {
		mouse.update(e.getX(),e.getY()); pointM.setLocation(e.getX(), e.getY()); inventory.setPointer(pointM);
	}
}