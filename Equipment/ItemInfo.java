package Equipment;

import Entity.Animation;
import Entity.MapObject;
import Entity.Player;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class ItemInfo extends MapObject {
    private ArrayList<Integer> allInfo;
    private BufferedImage image;
    private BufferedImage image2;
    private BufferedImage tooLowLevel;
    private int itemX;
    private int itemY;
    private int playerLevel;
    private int newx;
    private int newy;
    private ArrayList<BufferedImage> itemsFills = new ArrayList<>();
    private ArrayList<BufferedImage> allPluses = new ArrayList<>();
    private ArrayList<BufferedImage> allLevels = new ArrayList<>();
    private ArrayList<BufferedImage> allRares = new ArrayList<>();
    private ArrayList<BufferedImage> allTypes = new ArrayList<>();
    ArrayList<BufferedImage[]> sprites1= new ArrayList<>();
    private final int[] numItem = { 9, 9, 9, 9, 9, 9, 9, 9, 9 };

    public ItemInfo(TileMap tm, ArrayList<Integer> ai, int nx, int ny) {
        super(tm);
        width = 200;
        height = 300;
        cwidth = 0;
        cheight = 0;
        allInfo = ai;
        newx = nx - 250;
        newy = ny;
        if(newx + width > 1280) newx = 1280 - width;
        if(newy + height > 720) newy = 720 - height;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/itemshow.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/itemshowred.png"));
            BufferedImage pluses = ImageIO.read(getClass().getResourceAsStream(
                    "/Sprites/Player/itemplus.png"
            ));
            tooLowLevel = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/toolowlevel.png"));
            BufferedImage rares = ImageIO.read(getClass().getResourceAsStream(
                    "/Sprites/Player/itemrareinfo.png"
            ));
            BufferedImage types = ImageIO.read(getClass().getResourceAsStream(
                    "/Sprites/Player/itemtypeinfo.png"
            ));
            itemsFills.add(ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/normalitem.png")));
            itemsFills.add(ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/customitem.png")));
            itemsFills.add(ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/epicitem.png")));
            for(int i = 0; i < 21; i++) {
                allPluses.add(pluses.getSubimage(i * 60, 0, 60, 45
                ));
            }
            for(int i = 0; i < 4; i++) {
                allRares.add(rares.getSubimage(i * 70, 0, 70, 30
                ));
            }
            for(int i = 0; i < 8; i++) {
                allTypes.add(types.getSubimage(i * 70, 0, 70, 30
                ));
            }
            BufferedImage levels = ImageIO.read(getClass().getResourceAsStream(
                    "/Sprites/Player/level.png"
            ));
            for(int i = 0; i < 100; i++) {
                allLevels.add(levels.getSubimage(i * 25, 0, 25, 25
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Player/itemki.png"
                    )
            );
            for(int i = 0; i < 9; i++) {
                BufferedImage[] bi = new BufferedImage[numItem[i]];
                for(int j = 0; j < numItem[i]; j++) {
                    {
                        bi[j] = spritesheet.getSubimage(
                                j * 70,
                                i * 70,
                                70,
                                70
                        );
                    }
                }
                sprites1.add(bi);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void update(int level){
        playerLevel = level;
    }

    public void draw(Graphics2D g) {
        setMapPosition();
        g.drawImage(image2, newx, newy, width, height, null);
        g.drawString(allInfo.toString(), newx + 20, newy + 320);
    }
}





























