package GameState;

        import Equipment.Inventory;
        import TileMap.*;
        import Entity.*;

        import javax.imageio.ImageIO;
        import java.awt.*;
        import java.awt.event.KeyEvent;
        import java.awt.event.MouseEvent;
        import java.awt.image.BufferedImage;
        import java.io.FileInputStream;
        import java.io.InputStream;
        import java.util.Random;

public class TheEnd extends GameState {
    private TileMap tileMap;
    private Background bg;
    private Animation animation;
    private Inventory inventory;
    private Player player;
    private BufferedImage youAreDead;
    private BufferedImage[] bi = new BufferedImage[4];

    private Color titleColor;
    private Font titleFont;

    public TheEnd(TileMap tm , GameStateManager gsm, Player p, Inventory i) {
        this.gsm = gsm;
        tileMap=tm;
        player = p;
        inventory = i;
        player.respawn();
        this.gsm = gsm;


        try {
            {
                Random r = new Random();
                int nr = r.nextInt(3) + 1;
                youAreDead = ImageIO.read(
                        getClass().getResourceAsStream(
                                "/Backgrounds/endgame" + nr + ".png"
                        )
                );
            }
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
                    j * 1280,
                    0,
                    1280,
                    720
            );
        }
        animation = new Animation();
        animation.setFrames(bi);
        animation.setDelay(100);
        animation.playedOnce(false);
        try {
            String musicFile = "Resources/Music/endgame.mp3";
            InputStream in = new FileInputStream(musicFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {}

    public void update() {
        animation.update();
    }

    public void draw(Graphics2D g) {
        bg.draw(g);
        g.setColor(titleColor);
        g.setFont(titleFont);

        g.drawImage(animation.getImage(), 0, 0, null);
    }

    private void select() {

        gsm.setState(tileMap,GameStateManager.MENU_STATE,player,inventory);
    }

    public void keyPressed(KeyEvent ke, int k) {
        if (animation.hasPlayedOnce()){
            select();
        }
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




















