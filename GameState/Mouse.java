package GameState;

import TileMap.Background;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Mouse{
    private int x;
    private int y;
    private BufferedImage mouseOn;
    private BufferedImage mouseOff;
    private boolean isExisting;
    private boolean isOn;

    public Mouse(){
        isExisting = false;
        isOn = false;
        x = 0;
        y = 0;
        try {
            mouseOn = ImageIO.read(getClass().getResourceAsStream("/Backgrounds/curosoron.png"));
            mouseOff = ImageIO.read(getClass().getResourceAsStream("/Backgrounds/curosoroff.png"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void update(int nx, int ny){
        x = nx;
        y = ny;
        if(x > 0 && x < 1280 && y > 0 && y < 720) isExisting = true;
    }

    public void on(){
        isOn = true;
    }

    public void off(){
        isOn = false;
    }

    public void destroy(){
        isExisting = false;
    }

    public void create(){
        isExisting = true;
    }

    public void draw(Graphics2D g){
        if(!isExisting) return;
        if(!isOn)
        g.drawImage(mouseOff, x, y, null);
        else
            g.drawImage(mouseOn, x, y, null);
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}

