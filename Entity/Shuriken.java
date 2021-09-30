package Entity;

import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Shuriken {
	private Animation animation;
	private int currentAction;
	private int previousAction;
	private boolean facingRight;
	private int moveSpeed;
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;
	private boolean pausing;
	private long pauseTimer;
	private double x;
	private double y;
	private int vx;
	private int vy;
	private double t;
	private double dx;
	private double dy;
	private int width;
	private int height;
	private BufferedImage shurikens;
	private BufferedImage[] BI = new BufferedImage[4];

	public Shuriken(boolean right) {
		remove = false;
		facingRight = right;
		moveSpeed = 24;
		width = 120;
		height = 120;
		Random r = new Random();
		x = 0;
		y = 0;
		t = 0;
		vx = (r.nextInt(10)+5)*5;
		vy = (r.nextInt(10)+2)*5;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		pausing = true;
		pauseTimer = System.nanoTime();
		try {
			shurikens = ImageIO.read(
					getClass().getResourceAsStream(
							"/Backgrounds/shurikens.png"
					));
			for(int i = 0; i < 4; i++) {
				BI[i] = shurikens.getSubimage(
						i * 40,
						0,
						40,
						40
				);
			}
			animation = new Animation();
			animation.setFrames(BI);
			animation.setDelay(30);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public int getY(){return (int)y; }
	public int getX(){return (int)x; }

	public void setHit() {
		if(hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(30);
		dx = 0;
	}

	public boolean shouldRemove() { return remove; }

	public void update() {
		t+=0.1;
		if(facingRight) x=t*vx;
		else x=-(t*vx);
		y=0-(t*vy-(10*t*t)/2);
		animation.update2();
	}

	public void draw(Graphics2D g) {
		if(y<760 || x<0 || x>1280)
		{
			if(facingRight) g.drawImage(animation.getImage(), (int)x, (int)y-40, null);
			else g.drawImage(animation.getImage(), (int)x + 1280, (int)y-40, null);
		}

	}
}

