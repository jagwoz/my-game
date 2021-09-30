package Entity;

import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Arrow extends MapObject {

    private boolean hit;
    private boolean remove;
    private BufferedImage[] sprites;
    private BufferedImage[] hitSprites;
    private boolean pausing;
    private long pauseTimer;

    public Arrow(TileMap tm, boolean right) {

        super(tm);
        facingRight = right;
        moveSpeed = 35;
        if(right) dx = moveSpeed;
        else dx = -moveSpeed;
        pausing = true;
        pauseTimer = System.nanoTime();
        width = 120;
        height = 120;
        cwidth = 60;
        cheight = 8;

        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/arrow.png"));
            sprites = new BufferedImage[4];
            for(int i = 0; i < sprites.length; i++) {
                sprites[i] = spritesheet.getSubimage(
                        i * width,
                        0,
                        width,
                        height
                );
            }
            hitSprites = new BufferedImage[3];
            for(int i = 0; i < hitSprites.length; i++) {
                hitSprites[i] = spritesheet.getSubimage(
                        i * width,
                        height,
                        width,
                        height
                );
            }
            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(120);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int getX(){return getx(); }
    public int getY(){return getx(); }

    public void setHit() {
        if(hit) return;
        hit = true;
        animation.setFrames(hitSprites);
        animation.setDelay(50);
        dx = 0;
    }

    public boolean shouldRemove() { return remove; }

    public void isPausing() {
        if (pausing) {
            long elapsed = (System.nanoTime() - pauseTimer) / 1000000;
            if (elapsed > 400) {
                pausing = false;
            }
        }
    }

    public void update() {
        checkTileMapCollision();
        isPausing();
        if(pausing) setPosition(xtemp, ytemp);
        else setPosition(xtemp, ytemp);

        if(dx == 0 && !hit){ setHit(); }

        animation.update2();
        if(hit && animation.hasPlayedOnce()) {
            remove = true;
        }

    }
    
    public void draw(Graphics2D g) {

        setMapPosition();

        super.draw(g);

    }
}


















