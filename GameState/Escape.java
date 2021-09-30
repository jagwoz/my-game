package GameState;

import Equipment.Inventory;
import Equipment.Item;
import TileMap.*;
import Entity.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class Escape extends GameState {

    private BufferedImage image;
    private TileMap tileMap;
    private Background bg;
    private Background bg2;
    private boolean pressed = false;
    private Player player;
    private int pointerX = 100;
    private int pointerY = 100;
    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;
    private Item item;

    private HUD hud;
    private Inventory inventory;



    private Color titleColor;
    private Font titleFont;

    private Font font;

    private int currentChoice = 0;
    private String[] options = {
            "Continue",
            "Restart game",
            "Quit"
    };

    int pause ;



    public Escape(TileMap tm ,GameStateManager gsm,Player v, Inventory i,int p,ArrayList<Enemy> e) {
        this.gsm = gsm;
        tileMap=tm;
        player=v;
        pause = p;
        inventory=i;
        enemies = e;
        this.gsm = gsm;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Backgrounds/pointer2.png"));
            bg = new Background("/Backgrounds/menubackground.png", 0);
            bg.setVector(0, 0);
            bg2 = new Background("/Backgrounds/menubackground2.png", 0);
            bg2.setVector(0, 0);
            titleColor = new Color(128, 0, 0);
            titleFont = new Font(
                    "Century Gothic",
                    Font.PLAIN,
                    56);

            font = new Font("Arial", Font.PLAIN, 24);

        }
        catch(Exception ex) {
            ex.printStackTrace();
        }



    }

    public void init() {
    }


    public void update() {
        changePointer();
    }

    public void changePointer(){
        if(currentChoice == 0) { pointerX = 290; pointerY = 105 ; }
        else if(currentChoice == 1) {pointerX = 265; pointerY = 245; }
        else if(currentChoice == 2) {pointerX = 350; pointerY = 385; }
        else return;
    }

    public void draw(Graphics2D g) {
        // draw bg
        bg.draw(g);
        bg2.draw(g);
        // draw title
        changePointer();
        if((currentChoice == 0)) g.drawImage(image,pointerX,pointerY, 385, 31,null);
        else if((currentChoice == 1)) g.drawImage(image,pointerX,pointerY, 425, 31, null);
        else g.drawImage(image,pointerX,pointerY,255,31,null);
    }

    private void select() {
        if(currentChoice == 1) {
            gsm.setState(null,GameStateManager.LEVEL1_STATE,null,null);
        }
        if(currentChoice == 0) {
            if (pause==1){
            gsm.setState(tileMap,GameStateManager.LEVEL1_STATE,player,inventory, pause, enemies);}
        }
        if(currentChoice == 2) {
            System.exit(0);
        }
    }

    public void keyPressed(KeyEvent ke, int k) {
        if (k==KeyEvent.VK_ESCAPE){
            if (pause==1){
                gsm.setState(tileMap,GameStateManager.LEVEL1_STATE,player,inventory, pause, enemies);}
            else {
                System.out.println("Error");
            }

        }

        if(k == KeyEvent.VK_ENTER){
            select();
        }
        if(k == KeyEvent.VK_UP) {
            currentChoice--;
            if(currentChoice == -1) {
                currentChoice = options.length - 1;
            }
        }
        if(k == KeyEvent.VK_DOWN) {
            currentChoice++;
            if(currentChoice == options.length) {
                currentChoice = 0;
            }
        }
    }
    public void keyReleased(int k) {}
    public void mouseClicked(MouseEvent e) { }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseDragged(MouseEvent e) { }
    public void mouseMoved(MouseEvent e) { }

}



















