package Entity;

import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HitStatistic extends MapObject {
    private boolean player;
    private boolean critical;
    private String damageToShow;
    private float ymax;
    private boolean hit;
    private boolean remove;
    private BufferedImage[] sprites;
    private BufferedImage[] hitSprites;
    private boolean pausing;
    private long pauseTimer;

    public HitStatistic(TileMap tm, String damage, boolean p, boolean c) {

        super(tm);
        player = p;
        critical = c;
        damageToShow = damage;
        if(damageToShow!="MISS") damageToShow="-"+damageToShow;
        facingRight = right;
        moveSpeed = 2;
        dy = -moveSpeed;
        width = 120;
        height = 120;
        cwidth = 0;
        cheight = 0;
        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/fireball.png"));
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
            animation.setDelay(50);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isRight(){return facingRight;}

    public void setMaxY(float y){
        ymax = y - 50;
    }

    public int getX(){return getx();}

    public void setHit() {
        if(hit) return;
        hit = true;
        animation.setFrames(hitSprites);
        animation.setDelay(70);
        dy = 0;
    }

    public double getTempX(){return xtemp;}
    public double getTempY(){return ytemp;}

    public boolean shouldRemove() { return remove; }

    public void update() {
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        if(ytemp < ymax){ setHit(); }

//        animation.update2();
        if(hit && animation.hasPlayedOnce()) {
            remove = true;
        }
    }

    public void draw(Graphics2D g) {
        setMapPosition();
//        super.draw(g);
        if(ytemp>ymax){
        if(player) {
            g.setColor(new Color(255, 255, 255));
            g.setFont(new Font("TimesRoman", Font.BOLD, 28));
        }
        else {
            g.setColor(Color.WHITE);
            if(critical){
                g.setFont(new Font("TimesRoman", Font.PLAIN, 28));
            }
            else{
                g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
            }
        }
        g.drawString(damageToShow,(float)xtemp + (float)getXMap(),(float)ytemp);}
    }
}





























