package Entity.Enemies;

import Entity.Animation;
import Entity.Enemy;
import Entity.HitStatistic;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Wizard extends Enemy {
	private static final int WALKING = 0;
	private static final int ATTACKING = 1;
    private static final int DEADING = 2;
	//fonts and music


	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
			4, 3, 4
	};

	public Wizard(TileMap tm, int lvl) {
		super(tm);
		level = lvl;
		name = "Wizard";
        hitStatistics = new ArrayList<HitStatistic>();
		width = 120;
		height = 120;
		cwidth = 60;
		cheight = 100;
		hasFire = false;
		moveSpeed = 2;
		fallSpeed = 0.4;
		maxFallSpeed = 20.0;

		maxSpeed = 2.8 + 0.01 * level;
		health = maxHealth = level * 700;
		damage = level * 100;
		healthInfo = (int)(((float)health/(float)maxHealth)*100);
		attackRange = 140;
		attack = false;
		normal = maxSpeed;
		slow = maxSpeed/2;

		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Enemies/mage.png"
				)
			);
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 3; i++) {
				BufferedImage[] bi =
						new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++) {
					if(i != ATTACKING) {
						bi[j] = spritesheet.getSubimage(
								j * width,
								i * height,
								width,
								height
						);
					}
					else {
						bi[j] = spritesheet.getSubimage(
								j * width * 2,
								i * height,
								width * 2,
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
		animation.setDelay(100);
		right = true;
		facingRight = true;
	}

	public void update() {
		getNextPosition();
		checkTileMapCollision();
		healthInfo = (int)(((float)health/(float)maxHealth)*100);
        if(!dead)setPosition(xtemp, ytemp);

		// check attack has stopped
		if(currentAction == ATTACKING) {
			if(animation.hasPlayedOnce()) attack = false;
		}

		if(attack && !dead) {
			if(currentAction != ATTACKING) {
				currentAction = ATTACKING;
				width = 240;
				damage = level * 300;
				animation.setFrames(sprites.get(ATTACKING));
				animation.setDelay(100);
			}
		}
        else if(dead){
            if(currentAction != DEADING) {
                currentAction = DEADING;
                width = 120;
                animation.setFrames(sprites.get(DEADING));
                animation.setDelay(100);
				cwidth = 0;
				cheight = 0;
            }
        }
		else if(!dead){
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(100);
				damage = level * 300;
				width = 120;
			}
		}

        // update hitsats
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











