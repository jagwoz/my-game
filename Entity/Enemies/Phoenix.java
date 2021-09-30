package Entity.Enemies;

import Entity.Animation;
import Entity.Enemy;
import Entity.Fireball;
import TileMap.TileMap;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Phoenix extends Enemy {
	private static final int WALKING = 0;
	private static final int FIREBALL = 1;
	private static final int ATTACKING = 2;
	private static final int DEAD = 3;

	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
			4, 3, 3, 4
	};

	public Phoenix(TileMap tm, int lvl) {
		super(tm);
		level = lvl;
		name = "Phoenix";
		hasFire = true;
		width = 120;
		height = 120;
		cwidth = 60;
		cheight = 100;

		moveSpeed = 2;
		fallSpeed = 0.4;
		maxFallSpeed = 20.0;

		maxSpeed = 3.4 + 0.01 * level;
		health = maxHealth = level * 500;
		damage = level * 100;
		fireBallDamage = level * 100;
		healthInfo = (int)(((float)health/(float)maxHealth)*100);
		attackRange = 120;
		attack = false;
		shooting = false;

		normal = maxSpeed;
		slow = maxSpeed/2;
		damageToPlayer = 0;
		fireballRange = 400;
		fireBalls = new ArrayList<>();
		hitStatistics = new ArrayList<>();

		try {
			BufferedImage spriteSheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Enemies/firemage.png"
				)
			);
			sprites = new ArrayList<>();
			for(int i = 0; i < 4; i++) {
				BufferedImage[] bi =
						new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++) {
					if(i != ATTACKING) {
						bi[j] = spriteSheet.getSubimage(
								j * width,
								i * height,
								width,
								height
						);
					}
					else {
						bi[j] = spriteSheet.getSubimage(
								j * 300,
								i * height,
								300,
								height
						);
					}
				}
				sprites.add(bi);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites.get(WALKING));
		animation.setDelay(200);
		right = true;
		facingRight = true;
	}

	public void update() {
		getNextPosition();
		checkTileMapCollision();
		healthInfo = (int)(((float)health/(float)maxHealth)*100);
		if(!dead)setPosition(xtemp, ytemp);

		if(currentAction == ATTACKING) {
			if(animation.hasPlayedOnce()) attack = false;
		}
		if(currentAction == FIREBALL) {
			if(animation.hasPlayedOnce()) {shooting = false;}
		}

		if(attack && !dead) {
			if(currentAction != ATTACKING) {
				currentAction = ATTACKING;
				width = 300;
				damage = level * 100;

				animation.setFrames(sprites.get(ATTACKING));
				animation.setDelay(100);
			}
		}
		else if(shooting && !dead) {
			if(currentAction != FIREBALL) {
				currentAction = FIREBALL;
				width = 120;
				damage = level * 100;
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(100);
				Fireball fb = new Fireball(tileMap, facingRight);
				fb.setPosition(x, y);
				fireBalls.add(fb);
			}
		}
		else if(dead){
			if(currentAction != DEAD) {
				currentAction = DEAD;
				animation.setFrames(sprites.get(DEAD));
				animation.setDelay(100);
				width = 120;
				cwidth = 0;
				cheight = 0;
			}
		}
		else{
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(100);
				damage = level * 100;
				width = 120;
			}
		}

		if(!dead)
			switch (actualEffect){
				case 0:
					autoHit(lastTakenDamage/4,1000); break;
				case 1:
					maxSpeed = slow; break;
				case 2:
					maxSpeed = 0.000001;
					animation.setFrames(sprites.get(WALKING));
					animation.setDelay(100);
					width = 120; break;
				case 3:
					maxSpeed-=0.03;
					if(maxSpeed<0) maxSpeed=0.000001;
					animation.setFrames(sprites.get(WALKING));
					animation.setDelay(100);
					width = 120; break;
				case 4:
					maxSpeed = 3*slow;
					autoHit(lastTakenDamage/3,500); break;
				case 6:
					maxSpeed-=0.06;
					if(maxSpeed<0) maxSpeed=0.000001;
					animation.setFrames(sprites.get(WALKING));
					animation.setDelay(100);
					width = 120; break;
				default:
					maxSpeed = normal; break;
			}

		for(int i = 0; i < hitStatistics.size(); i++) {
			hitStatistics.get(i).update();
			if(hitStatistics.get(i).shouldRemove()) {
				hitStatistics.remove(i);
				i--;
			}
		}

		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}

		isGoingDead();
		positionUpdate();
		isPausing();
		isFlinching();
		isAutoFlinching();
		isInEffect();
		if(actualEffect!=3 && actualEffect!=2 && actualEffect!=6 && !dead)
		animation.update2();
		else animation.update();
	}
}











