package GameState;

import Equipment.Inventory;
import Main.GamePanel;
import TileMap.*;
import Entity.*;
import Entity.Enemies.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;

public class Level1State extends GameState {
	private Mouse mouse;
	private Point pointM = new Point(0,0);
	private boolean pausing;
	private TileMap tileMap;
	private Background bg;
	private boolean pressed = false;
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	private HUD hud;
	private long pauseTimer;
	private Inventory inventory;
	private Point[] points = new Point[]{
//			new Point(600, 200),
			new Point(200, 200),
			new Point(2000, 200),
			new Point(2000, 200),
	};
	int pause=0;

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

	public Level1State(TileMap tm ,GameStateManager gsm, Player v, Inventory i) {
		this.gsm = gsm;
		mouse = new Mouse();
		tileMap=tm;
		player=v;
		player.respawn();
		inventory=i;
		pausing = false;
		enemies = new ArrayList<Enemy>();
		if(tm==null) { tileMap = new TileMap(120); }
		if(v==null) { player = new Player(tileMap); }
		if (i==null){ inventory=new Inventory(tileMap); }
		populateEnemies(enemies,points);
		init();

        try {
            String musicFile = "Resources/Music/music.mp3";
            InputStream in = new FileInputStream(musicFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
		inventory.hide();
	}

	public Level1State(TileMap tm ,GameStateManager gsm,Player v, Inventory i,int p,ArrayList<Enemy> en) {
		this.gsm = gsm;
		mouse = new Mouse();
		tileMap=tm;
		player=v;
		pause = p;
		inventory=i;
		enemies = en;
		if(tm==null) {tileMap = new TileMap(120); }
		if(v==null) { player = new Player(tileMap); }
		if (i==null){ inventory=new Inventory(tileMap); }
		if (en==null){ enemies = new ArrayList<Enemy>(); }
		init();

		try {
			String musicFile = "Resources/Music/music.mp3";
			InputStream in = new FileInputStream(musicFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		inventory.hide();
	}

	public void init() {
		if(pause!=0){}
		else {
			tileMap.loadTiles("/Tilesets/grasstileset.gif");
			tileMap.loadMap("/Maps/level1-1.map");
			tileMap.setPosition(0, 0);
			tileMap.setTween(1);
			player.setPosition(200, 200);
		}
		bg = new Background("/Backgrounds/level1background.jpg", 0);
		explosions = new ArrayList<Explosion>();
		hud = new HUD(player, tileMap);

	}

	private void populateEnemies(ArrayList<Enemy> enemies ,Point[] points) {
		Skeleton s;
		Wizard w;
		Phoenix p;
		for(int i = 0; i < points.length; i++) {
			if(i%3==0){
			s = new Skeleton(tileMap, 1);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);}
			else if(i%3==1){
			w = new Wizard(tileMap, 5);
			w.setPosition(points[i].x, points[i].y);
			enemies.add(w);}
			else{
			p = new Phoenix(tileMap, 3);
			p.setPosition(points[i].x, points[i].y);
			enemies.add(p);}
			}
		}

	public void update() {
		isPausing();
		hud.update();
		player.update();
		inventory.update(player.getLevel());
		tileMap.setPosition(
			GamePanel.WIDTH / 2 - player.getx(),
			GamePanel.HEIGHT / 2 - player.gety()
		);
		bg.setPosition(tileMap.getx(), tileMap.gety());

		if(!player.getLifeStatuse()){

        }
		if(!player.getAbsolutelyDead()){
//			gsm.setState(tileMap,GameStateManager.THEENDSTATE,player, inventory);
        }

		if(player.getLifeStatuse())
		player.checkAttack(enemies);


		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if (player.getLifeStatuse()){

			e.checkAttack(player);
			}
			e.update();
			if(e.isDead()){
				if(e.getAwarded()){
					player.addExpience();
					e.changeAwarded();
				}
			}
			if(e.getAbsoluteDead()) {
				enemies.remove(i);
				i--;
			}
		}
		hud.update();

		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
	}

	public void draw(Graphics2D g) {
		bg.draw(g);
		tileMap.draw(g);
		hud.draw(g);
		for(int i = 0; i < enemies.size(); i++) {
			if(abs(player.getx()-enemies.get(i).getx())<3*GamePanel.WIDTH/2)
			enemies.get(i).draw(g);
		}
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition(
					(int) tileMap.getx(), (int) tileMap.gety());
			explosions.get(i).draw(g);
		}
		player.draw(g);
		if(player.getLifeStatuse())
			inventory.draw(g);

		mouse.draw(g);
	}

	public void keyPressed(KeyEvent ke, int k) {
		if(player.getLifeStatuse()) {
			if (player.getx() > 6200 && player.getx() < 6300 && k == KeyEvent.VK_P) {
//				gsm.setState(tileMap, GameStateManager.VICTORYSTATE, player, inventory);
			}
			if (k == KeyEvent.VK_ESCAPE) {
				pause = 1;

				gsm.setState(tileMap, GameStateManager.ESCAPE, player, inventory, pause, enemies);
			}

			Random r = new Random();
//			if (k == KeyEvent.VK_M) inventory.addItem((r.nextInt(19)+1)*5);
//			if (k == KeyEvent.VK_M) inventory.addItem(1);
			if (k == KeyEvent.VK_X) inventory.removeItem();
			if (k == KeyEvent.VK_U) inventory.upgradeItem();

			if (k == KeyEvent.VK_A) player.setLeft(true);
			if (k == KeyEvent.VK_D) player.setRight(true);
			if (k == KeyEvent.VK_W) player.setJumping(true);
			if (k == KeyEvent.VK_R) {
					player.setHiting();
			}
			if (k == KeyEvent.VK_F) {
					player.setShooting();
			}
			if (k == KeyEvent.VK_I) {
				inventory.changeInventoryMove();
			}
		}
	}

	public void keyReleased(int k) {
		if(k == KeyEvent.VK_A) player.setLeft(false);
		if(k == KeyEvent.VK_D) player.setRight(false);
		if(k == KeyEvent.VK_W) player.setJumping(false);
		if(k == KeyEvent.VK_R) {
			pressed = false;
		}
		if(k == KeyEvent.VK_F) {
			pressed = false;
		}
	}
	public void mouseClicked(MouseEvent e) {

	}
	public void mousePressed(MouseEvent e) { mouse.on(); }
	public void mouseReleased(MouseEvent e) { mouse.off(); }
	public void mouseEntered(MouseEvent e) { mouse.create(); }
	public void mouseExited(MouseEvent e) { mouse.destroy(); }
	public void mouseDragged(MouseEvent e) { mouse.update(e.getX(),e.getY()); pointM.setLocation(e.getX(),e.getY()); inventory.setPointer(pointM); }
	public void mouseMoved(MouseEvent e) {
		mouse.update(e.getX(),e.getY()); pointM.setLocation(e.getX(), e.getY()); inventory.setPointer(pointM);
	}
}