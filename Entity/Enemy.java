package Entity;

import TileMap.TileMap;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Enemy extends MapObject {
	protected String name;
	protected int level;
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	protected int attackRange;
	protected int healthInfo;
	protected double normal;
	protected double slow;
	protected boolean absolutelyDead;
	protected int lastTakenDamage;

	protected int damageToPlayer;
	protected BufferedImage enemyHud;
	protected BufferedImage poisonEnemyHud;
	protected BufferedImage hudFill;
	protected BufferedImage hudSkulls;

	protected boolean hasFire;
	protected boolean awarded;
	protected boolean attack;
	protected boolean shooting;
	protected int fireBallDamage;
	protected int fireballRange;
	protected ArrayList<Fireball> fireBalls;
	protected ArrayList<HitStatistic> hitStatistics;
	protected int actualEffect;

	protected boolean autoFlinching;
	protected boolean flinching;
	protected boolean pausing;
	protected boolean pausingFire;

	protected boolean normalEffect;

	protected boolean playerDamage;
	protected int autoFlinchTime;
	protected long deadTimer;
	protected long pauseFireTimer;
	protected long flinchTimer;
	protected long autoFlinchTimer;
	protected long pauseTimer;

	protected long effectTimer;

	protected ArrayList<BufferedImage> image;
	protected ArrayList<BufferedImage[]> effects;
	protected final int[] numFramesEffects = {
			4, 4, 4, 4, 4, 4, 4, 4
	};
	protected Animation effect;

	public Enemy(TileMap tm) {
		super(tm);
		hasFire = false;
		awarded = true;
		absolutelyDead = false;
		normalEffect = true;
		actualEffect = 10;
		fireBalls = new ArrayList<>();
		hitStatistics = new ArrayList<>();
		playerDamage = false;

		try{
			BufferedImage levels = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Player/level.png"
					)
			);
			image = new ArrayList<>();
			for(int i = 0; i < 100; i++) {
				image.add(levels.getSubimage(
						i * 25,
						0,
						25,
						25
				));
			}
			enemyHud = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Enemies/enemyhud.png"
					)
			);
			hudFill = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Enemies/fill.gif"
					)
			);
			poisonEnemyHud = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Enemies/poisonenemyhud.png"
					)
			);
			hudSkulls = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Enemies/enemyhudskulls.png"
					)
			);
			BufferedImage spriteseffects = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Enemies/effects.png"
					)
			);
			effects = new ArrayList<>();
			for(int i = 0; i < 8; i++) {
				BufferedImage[] bi =
						new BufferedImage[numFramesEffects[i]];
				for(int j = 0; j < numFramesEffects[i]; j++) {
					bi[j] = spriteseffects.getSubimage(
							j * 120,
							i * 160,
							120,
							120
					);
				}
				effects.add(bi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		effect = new Animation();
		effect.setFrames(effects.get(0));
		effect.setDelay(400);
	}

	public void hit(int damage, String effects, double position, int chance, int critic) {
		if(dead || flinching) return;
		Random r = new Random();
		int rand = r.nextInt(100) + 1;

		if(rand > chance){
			HitStatistic hs = new HitStatistic(tileMap, "MISS", false, false);
			hs.setMaxY((float)(y - 80));
			hs.setPosition(x, y - 80);
			hitStatistics.add(hs);
			flinching = true;
			flinchTimer = System.nanoTime();
			return;
		}

		boolean critical = false;
		lastTakenDamage = damage + r.nextInt(51) - 25;
		{rand = r.nextInt(100) + 1;
		if(rand <= critic){
			lastTakenDamage *=3;
			critical = true;
		}}
		HitStatistic hs = new HitStatistic(tileMap, String.valueOf(lastTakenDamage), false, critical);
		hs.setMaxY((float)(y - 80));
		hs.setPosition(x, y - 80);
		hitStatistics.add(hs);
		if((health - lastTakenDamage) > 0) health -= lastTakenDamage;
		else health = 0;
		if(health == 0){
			if(!dead) deadTimer = System.nanoTime();
			dead = true;
		}
		flinching = true;
		flinchTimer = System.nanoTime();
		if(actualEffect==3) {
			if(position>getx()) {facingRight = false; left = true; right = false; moveSpeed = 4; maxSpeed = 4;}
			else {facingRight = true; left = false; right = true; moveSpeed = 4; maxSpeed = 4;}
		}
		if(!dead){
			if(actualEffect == 6){
				normalEffect = true;
				actualEffect = 10;
			}
			else {
				Random p = new Random();
				int random = p.nextInt(100);
				String switchEffect = effects.substring(random, random + 1);
				giveEffect(Integer.parseInt(switchEffect));
			}
		}
	}

	public void autoHit(int damage, int time) {
		if(dead || autoFlinching) return;
		autoFlinchTime = time;
		HitStatistic hs = new HitStatistic(tileMap, String.valueOf(damage), false, false);
		hs.setMaxY((float)(y - 80));
		hs.setPosition(x, y - 100);
		hitStatistics.add(hs);
		if((health - damage) > 0) health -= damage;
		else health = 0;
		if(health == 0) {
			if(!dead) deadTimer = System.nanoTime();
			dead = true;
		}
		autoFlinching = true;
		autoFlinchTimer = System.nanoTime();
	}

	public boolean isInPause(){
		return pausing;
	}

	public void setPause(){
		pausing = true;
		pauseTimer = System.nanoTime();
	}

	public void setPauseFire(){
		pausingFire = true;
		pauseFireTimer = System.nanoTime();
	}

	public void checkAttack(Player p) {
		if (!dead && actualEffect != 2 && actualEffect != 3 && actualEffect != 6 && actualEffect != 5 && !p.getTemp()) {
			if (facingRight) {
				if (
						p.getx() > x &&
								p.getx() < x + attackRange &&
								p.gety() > y - height / 2 &&
								p.gety() < y + height / 2 && !pausing()
				) {
					attack(true);
					p.hit(damage, false);
				} else if (
						p.getx() > x &&
								p.getx() < x + fireballRange &&
								p.gety() > y - height / 2 &&
								p.gety() < y + height / 2 && !pausingFire()
				) {
					firing(true);
				}
			} else {
				if (
						p.getx() < x &&
								p.getx() > x - attackRange &&
								p.gety() > y - height / 2 &&
								p.gety() < y + height / 2 && !pausing()
				) {
					attack(true);
					p.hit(damage, false);
				} else if (
						p.getx() < x &&
								p.getx() > x - fireballRange &&
								p.gety() > y - height / 2 &&
								p.gety() < y + height / 2 && !pausingFire()
				) {
					firing(true);
				}
			}

			for (int j = 0; j < fireBalls.size(); j++) {
				if (fireBalls.get(j).intersects(p)) {
					p.hit(fireBallDamage, true);
					fireBalls.get(j).setHit();
					break;
				}
			}
		}
	}


	public int getHealthInfo(){ return healthInfo; }

	public void takePlayerPosition(int playerx, int playery){
		px = playerx;
		py = playery;
	}

	public void changeAwarded(){ awarded = !awarded; }
	public boolean getAwarded(){ return awarded; }

	public void getNextPosition() {
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
		else{
			dx = 0;
		}
		if(falling) {
			dy += fallSpeed;
		}
	}

	public void attack(boolean x){attack = x; setPause();}
	public void firing(boolean x){shooting = x; setPauseFire();}
	public boolean pausing(){return pausing;}
	public boolean pausingFire(){return pausingFire;}
	public boolean isDead() { return dead; }

	public void positionUpdate(){
		if (right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		}
		else if (left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}
	}

	public void isFlinching() {
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 400) {
				flinching = false;
			}
		}
	}

	public void isAutoFlinching() {
		if (autoFlinching) {
			long elapsed = (System.nanoTime() - autoFlinchTimer) / 1000000;
			if (elapsed > autoFlinchTime) {
				autoFlinching = false;
			}
		}
	}

	public void isInEffect(){
		int time;
		switch(actualEffect){
			case 0: time = 10000; break;
			case 3: time = 7000; break;
			case 6: time = 50000; break;
			default: time = 4000; break;
		}
		long elapsed = (System.nanoTime() - effectTimer) / 1000000;
		if (elapsed > time) {
			normalEffect = true;
			actualEffect = 10;
		}
	}

	public void isPausing() {
		if (pausing) {
			long elapsed = (System.nanoTime() - pauseTimer) / 1000000;
			if (elapsed > 1000) {
				pausing = false;
			}
		}
		if (pausingFire) {
			long elapsed = (System.nanoTime() - pauseFireTimer) / 1000000;
			if (elapsed > 2500) {
				pausingFire = false;
			}
		}
	}

	public void isGoingDead() {
		if (dead) {
			long elapsed = (System.nanoTime() - deadTimer) / 1000000;
			if (elapsed > 1000) {
				actualEffect = 10;
				normalEffect = true;
				absolutelyDead = true;
			}
		}
	}

	public boolean getAbsoluteDead(){
		return absolutelyDead;
	}

	public void update() {
	}

	public void giveEffect(int numberOfEffect){
		if(!normalEffect) return;
		actualEffect = numberOfEffect;
		normalEffect = false;
		effect.setFrames(effects.get(numberOfEffect));
		effect.setDelay(200);
		effectTimer = System.nanoTime();
	}

	public boolean hasStop(){
		if(actualEffect==2 || actualEffect==3 || actualEffect==5 ) return true;
		return false;
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);

		if(!normalEffect && !dead)
		g.drawImage(effect.getImage(), getx() + (int)getXMap() - 60, gety() + (int)getYMap() - 60, 120, 120, null,null);

		if(!dead) {
			g.drawImage(enemyHud, getx() + (int) getXMap() - 70, gety() - 92 + (int) +getYMap(), 140, 30, null, null);
			g.drawImage(hudFill, getx() + (int) getXMap() + healthInfo*4/5 - 40, gety() - 86 + (int) +getYMap(), 80 - healthInfo*4/5, 18, null, null);
			g.drawImage(hudSkulls, getx() + (int) getXMap() - 70, gety() - 92 + (int) +getYMap(), 140, 30, null, null);

			g.drawImage(image.get(level),
					getx() + (int) getXMap() - 60,
					gety() - 117 + (int) +getYMap(),
					null);

			g.setFont(new Font("TimesRoman", Font.PLAIN, 16));
			g.setColor(Color.WHITE);
			g.drawString(
					name,
					getx() + (int) getXMap() - g.getFontMetrics().stringWidth(name) / 2,
					gety() - 100 + (int) +getYMap()
			);
			g.setFont(new Font("Courier", Font.BOLD, 13));
			g.drawString(
					getHealthInfo() + "/100",
					getx() + (int) getXMap() - g.getFontMetrics().stringWidth(getHealthInfo() + "/100") / 2,
					gety() - 72 + (int) +getYMap()
			);
		}

		for(int i = 0; i < hitStatistics.size(); i++) {
			hitStatistics.get(i).draw(g);
		}

		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).draw(g);
		}

		if(!dead)effect.update2();
	}
}














