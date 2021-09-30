package GameState;

import TileMap.*;
import Entity.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Victory extends GameState {

    private TileMap tileMap;
    private Background bg;
    private Animation animation;


    private BufferedImage youAreDead;
    private BufferedImage[] bi = new BufferedImage[4];

    private Color titleColor;
    private Font titleFont;

    public Victory(TileMap tm ,GameStateManager gsm) {
        this.gsm = gsm;
        tileMap=tm;

        this.gsm = gsm;

        try {
            youAreDead = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Backgrounds/victory.png"
                    )
            );
            bg = new Background("/Backgrounds/menubackground.png", 0);
            bg.setVector(0, 0);
            titleColor = new Color(128, 0, 0);
            titleFont = new Font(
                    "Century Gothic",
                    Font.PLAIN,
                    56);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

        for(int j = 0; j < 4; j++) {
            bi[j] = youAreDead.getSubimage(
                    j * 800,
                    0,
                    800,
                    187
            );
        }
        animation = new Animation();
        animation.setFrames(bi);
        animation.setDelay(800);
        animation.playedOnce(false);
    }

    public void init() {}

    public void update() {
        animation.update();
    }

    public void draw(Graphics2D g) {
        bg.draw(g);
        g.setColor(titleColor);
        g.setFont(titleFont);

        g.drawImage(animation.getImage(), 80, 170, null);
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent ke, int k) {
        if (animation.hasPlayedOnce()){
            select();
        }
    }

    private void select() {
        gsm.setState(tileMap,GameStateManager.MENU_STATE,null,null);
    }

    public void keyReleased(int k) {}
    public void mouseClicked(MouseEvent e) { }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseDragged(MouseEvent e) { }
    public void mouseMoved(MouseEvent e) { }
}




















