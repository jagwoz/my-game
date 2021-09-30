package Entity;

import TileMap.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Player extends MapObject {
	// graphics
	private BufferedImage hud, hudfill, hudskulls;
	private ArrayList<BufferedImage> image;
	private ArrayList<BufferedImage[]> sprites;
	private Animation lvlanimation;
	private BufferedImage levelup;
	private BufferedImage[] BI = new BufferedImage[7];

	// player attributes
	private String name;
	private int health;
	private int maxHealth;
	private int healthInfo;
	private int manaInfo;
	private int shoot;
	private int maxShoots;
	private int mana;
	private int maxMana;
	private int level;
	private double procent;

	// pausing
	private boolean flinching;
	private long flinchTimer;
	private boolean pausing;
	private long pauseTimer;

	// player attacking
	private int arrowCost;
	private int arrowDamage;
	private ArrayList<Arrow> arrows;

	// player statistics
	private int kills;
	private int hitChance;
	private int hitChanceArrow;
	private int criticalChance;
	private String currentHitEffects;
	private int hitDamage;
	private int hitRange;

	// player states
	private boolean life;
	private boolean dead;
	private boolean shooting;
	private boolean hiting;

	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int SHOOTING = 4;
	private static final int HITING = 6;
	private static final int DEADING = 7;

	// others
	private ArrayList<HitStatistic> hitStatisticsOur;
	private boolean temp;

	public Player(TileMap tm) {
		super(tm);
		// player attributes
		name = "jagwoz";
		level = 1;
		width = 120;
		height = 120;
		cwidth = 80;
		cheight = 90;
		moveSpeed = 1.2;
		stopSpeed = 1.6;
		fallSpeed = 0.6;
		maxFallSpeed = 30.0;
		jumpStart = -11.8;
		stopJumpSpeed = 1.2;
		kills = 0;
		facingRight = true;
		dead = false;
		life = true;
		arrows = new ArrayList<>();
		hitStatisticsOur = new ArrayList<>();

		// (level) attributes
		health = maxHealth = 1000 + ((level - 1) * 100);
		mana = maxMana = 1000;
		shoot = maxShoots = 300;
		maxSpeed = 10.4;
		healthInfo = (int)(((float)health/(float)maxHealth)*100);
		manaInfo = (int)(((float)mana/(float)maxMana)*100);
		arrowCost = 300;
		arrowDamage = 100;
		hitDamage = level*250;
		hitRange = 120;
		temp = false;
		// graphics load
		try {
			levelup = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Player/levelup.png"
					));
			hud = ImageIO.read(getClass().getResourceAsStream(
							"/HUD/hud.png"
					));
			hudfill = ImageIO.read(getClass().getResourceAsStream(
							"/Sprites/Enemies/fill.gif"
					));
			hudskulls = ImageIO.read(getClass().getResourceAsStream(
							"/HUD/skullshud.png"
					));
            BufferedImage levels = ImageIO.read(getClass().getResourceAsStream(
            				"/Sprites/Player/level.png"
                    ));
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(
							"/Sprites/Player/playersprites.png"
					));
            image = new ArrayList<>();
			sprites = new ArrayList<>();

            for(int i = 0; i < 100; i++) {
                        image.add(levels.getSubimage(i * 25, 0, 25, 25
                        ));
            }

			for(int i = 0; i < 7; i++) {
				BI[i] = levelup.getSubimage(
						i * 150,
						0,
						150,
						200
				);
			}

			for(int i = 0; i < 8; i++) {
				int[] numFrames = {2, 4, 1, 2, 3, 2, 6, 6};
				BufferedImage[] bi = new BufferedImage[numFrames[i]];

				for(int j = 0; j < numFrames[i]; j++) {
					if(i!=HITING && i!=WALKING && i!=SHOOTING && i!=DEADING) {
						bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
					}
					else {
						bi[j] = spritesheet.getSubimage(j * width * 2, i * height, width * 2, height);
					}
				}
				sprites.add(bi);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		lvlanimation = new Animation();
		lvlanimation.setFrames(BI);
		lvlanimation.setDelay(100);
		lvlanimation.playedOnce(true);
	}

	public String getName() { return name; }
	int getHealth() { return health; }
	public boolean getAbsolutelyDead() { return life; }
	public boolean getLifeStatuse() { return !dead; }
	double getProcent() { return procent; }
    public int getLevel(){ return level; }

    public void respawn(){
		dead = false;
		life = true;
		health = maxHealth;
		mana = maxMana;
		shoot = maxShoots;
		width = 120;
		height = 120;
		cwidth = 80;
		cheight = 90;
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		flinching = false;
		temp = false;
	}

	public void setHiting() {
		if(currentAction != SHOOTING && !shooting)
		hiting = true;
	}

	public void setShooting() {
		if((mana >= arrowCost) && currentAction != HITING && !hiting){
			shooting = true;
		}
	}

	public void checkAttack(ArrayList<Enemy> enemies) {
		for (Enemy e : enemies) {
			if (hiting) {
				if (facingRight) {
					if ( e.getx() > x &&
						 e.getx() < x + hitRange &&
						 e.gety() > y - height / 2 &&
						 e.gety() < y + height / 2
					) {
						 e.hit(hitDamage, currentHitEffects, getx(), hitChance, criticalChance);
					}
				}
				else {
					if ( e.getx() < x &&
						 e.getx() > x - hitRange &&
						 e.gety() > y - height / 2 &&
						 e.gety() < y + height / 2
					) {
						 e.hit(hitDamage, currentHitEffects, getx(), hitChance, criticalChance);
					}
				}
			}
			for (Arrow arrow : arrows) {
				if (arrow.intersects(e)) {
					e.hit(arrowDamage, currentHitEffects, getx(), hitChanceArrow, criticalChance);
					arrow.setHit();
					break;
				}
			}
		}
	}

	public void addExpience(){
		if (level == 99 || dead) return;
		Random r = new Random();
		int random = (r.nextInt(200))+100;
		procent += random;
		if(procent >= 100.0 && (level + ((int)(procent) - (((int)procent) % 100))/100 < 99)) {
			int n = ((int)procent) % 100;
			level = level + ((int)(procent) - n)/100;
			procent = n;
			lvlanimation.setFrames(BI);
			lvlanimation.setDelay(100);
			updateStatistics();
			health = maxHealth;
			mana = maxMana;
		}
		else {
			level = 99;
			procent = 100.0;
		}
	}

	void hit(int damage, boolean deadPlayer) {
		if(flinching && deadPlayer) return;
		flinching = true;
		flinchTimer = System.nanoTime();
		{
			Random r = new Random();
			int newDamage = damage + r.nextInt(101) - 50;
			HitStatistic hs = new HitStatistic(tileMap, String.valueOf(newDamage), true, false);
			hs.setMaxY((float) (y - 140));
			hs.setPosition(x, y - 120);
			hitStatisticsOur.add(hs);
			health -= newDamage;
		}
		if(health < 0) health = 0;
	}

	private void getNextPosition() {
		if(dead) { dx = 0; dy = 0; x = getx(); return; }
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}

		if((currentAction == HITING || currentAction == SHOOTING) && !(jumping || falling)) {
			dx = 0;
		}
		if(jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}
		else if(falling) {
			dy += fallSpeed;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}

	private void updateStatistics(){
		if(health!=0 && health<=maxHealth) health++;
		if(mana<=maxMana) mana++;
		if(health >= maxHealth) health = maxHealth;
		maxHealth = 1000 + (level-1) * 100;
		maxMana = 1000 + (level-1) * 100;
		hitDamage = 250*level;
		arrowDamage = 100;
		hitChance = 85;
		hitChanceArrow = 60;
		criticalChance = 40;
		currentHitEffects = "0000011111222223333344444555556666677777000001111122222333334444455555666667777700112233445566771234";
		healthInfo = (int)(((float)health/(float)maxHealth)*100);
		manaInfo = (int)(((float)mana/(float)maxMana)*100);
	}

	private void isFlinching() {
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 300) {
				flinching = false;
			}
		}
		if (pausing) {
			long elapsed = (System.nanoTime() - pauseTimer) / 1000000;
			if (elapsed > 2500) {
				pausing = false;
			}
		}
	}

	public boolean getTemp(){
		return temp;
	}

	public void update() {
		if(health == 0){
			if(!temp){
			try {
				String musicFile = "Resources/Music/dead.mp3";
				InputStream in = new FileInputStream(musicFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			temp = true;}
		}
		if(health == 0 && !falling) {
			if(!dead) {
				pausing = true; pauseTimer = System.nanoTime();
				dead = true;
			}
		}
		lvlanimation.update();

		for(int i = 0; i < hitStatisticsOur.size(); i++) {
			hitStatisticsOur.get(i).update();
			if(hitStatisticsOur.get(i).shouldRemove()) {
				hitStatisticsOur.remove(i);
				i--;
			}
		}

		for(int i = 0; i < arrows.size(); i++) {
			arrows.get(i).update();
			if(arrows.get(i).shouldRemove()) {
				arrows.remove(i);
				i--;
			}
		}

		getNextPosition();
		checkTileMapCollision();
		updateStatistics();
		isFlinching();

		if(gety() > tileMap.getHeight() - 120){ health = 0; x = getx(); y = tileMap.getHeight() - 120; hit(0, false);}
		setPosition(xtemp, ytemp);

		// check attack has stopped
		if(currentAction == DEADING) {
			if(animation.hasPlayedOnce() && !pausing) life = false;
		}
		if(currentAction == HITING || currentAction == SHOOTING) {
			if(animation.hasPlayedOnce()) {
				hiting = false;
				if(shooting)
				{
					Arrow ar = new Arrow(tileMap, facingRight);
						if(facingRight)
							ar.setPosition(x + 50, y);
						else
							ar.setPosition(x - 50, y);
					arrows.add(ar);
				}
				shooting = false;
			}
		}

		if(shoot > maxShoots) shoot = maxShoots;
		if(shooting && currentAction != SHOOTING) {
				if (mana - arrowCost >= 0){
					mana -= arrowCost;
			}
		}

		// set animation
		if(dead) {
			if(currentAction != DEADING) {
				currentAction = DEADING;
				width = 240;
				cwidth = 0;
				cheight = 0;
				animation.setFrames(sprites.get(DEADING));
				animation.setDelay(150);
			}
		}
		else if(hiting) {
			if(currentAction != HITING) {

				currentAction = HITING;
				animation.setFrames(sprites.get(HITING));
				animation.setDelay(25);
				width = 240;
			}
		}
		else if(shooting) {
			if(currentAction != SHOOTING) {
				currentAction = SHOOTING;
				animation.setFrames(sprites.get(SHOOTING));
				animation.setDelay(100);
				width = 240;
			}
		}
		else if(dy > 0) {
			currentAction = FALLING;
			animation.setFrames(sprites.get(FALLING));
			animation.setDelay(100);
			width = 120;
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 120;
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(150);
				width = 240;
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 120;
			}
		}

		if(currentAction != DEADING)animation.update2();
		else animation.update();

		// set direction
		if(currentAction != HITING && currentAction != SHOOTING) {
			if(!dead) {
				if (right) facingRight = true;
				if (left) facingRight = false;
			}
		}
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		for (Arrow arrow : arrows) {
			arrow.draw(g);
		}
		for (HitStatistic hitStatistic : hitStatisticsOur) {
			hitStatistic.draw(g);
		}

		{
			int xDraw = (int)x;
			int yDraw = (int)y;
			if (health>0) {
				g.drawImage(image.get(level),
						xDraw + (int) getXMap() - 55,
						yDraw - 88 + (int) +getYMap(),
						null);
				g.setFont(new Font("Courier", Font.PLAIN, 15));
				g.setColor(Color.WHITE);
				g.drawString(
						name,
						xDraw+ (int) getXMap() - g.getFontMetrics().stringWidth(name) / 2,
						yDraw - 70 + (int) getYMap()
				);
				g.drawImage(hud, xDraw + (int) getXMap() - 85,
						yDraw - 150 + (int) getYMap(), null);
				g.drawImage(hudfill, xDraw + (int) getXMap() + healthInfo - 50,
						yDraw - 143 + (int) getYMap(), 100 - healthInfo, 17, null, null);
				if(getProcent()<=100)
				g.drawImage(hudfill, xDraw + (int) getXMap() - 50 + (int) getProcent(),
						yDraw - 125 + (int) getYMap(), 100-(int) getProcent(), 11, null,null);
				g.drawImage(hudfill, xDraw + (int) getXMap() + manaInfo - 50,
						yDraw - 112 + (int) getYMap(), 100 - manaInfo, 6, null, null);
				g.setFont(new Font("Courier", Font.BOLD, 14));
				g.drawString(
						healthInfo + "/100",
						xDraw + (int) getXMap() - g.getFontMetrics().stringWidth(healthInfo + "/100") / 2,
						yDraw - 130 + (int) getYMap()
				);
				g.drawImage(hudskulls, xDraw + (int) getXMap() - 85,
						yDraw- 150 + (int) getYMap(), null);
				g.setFont(new Font("Courier", Font.BOLD, 10));
				g.drawString(
						(int) getProcent() + "%",
						xDraw + (int) getXMap() - g.getFontMetrics().stringWidth((int) getProcent() + "%") / 2,
						yDraw - 116 + (int) +getYMap()
				);
			}
		}

		if(!lvlanimation.hasPlayedOnce()){
			g.drawImage(lvlanimation.getImage(), getx()+ (int)getXMap() - 75, gety() - 150 +(int)getYMap(), 150, 200, null,null);
		}

		super.draw(g);
	}
}