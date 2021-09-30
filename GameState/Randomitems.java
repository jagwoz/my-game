package GameState;

import Equipment.Item;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Randomitems {
    private boolean end;
    private boolean wasAwarded;
    private BufferedImage pointerShow;
    private BufferedImage line;
    private int x = 349;
    int lastItem = 11;
    private double speed;
    private double slow;
    private ArrayList<Item> items;
    private int itemsSize = 100;
    private int width = 70;
    private int height = 70;
    private BufferedImage image;
    private ArrayList<BufferedImage> itemsFills = new ArrayList<>();
    private ArrayList<BufferedImage> allPluses = new ArrayList<>();
    ArrayList<BufferedImage[]> sprites1= new ArrayList<>();
    private final int[] numItem = { 9, 9, 9, 9, 9, 9, 9, 9, 9 };
    private ArrayList<Item> actualItems;

    private float[] scale = { 1f, 1f, 1f, 0.0f };
    private float[] offset;
    private RescaleOp rop;

    public Randomitems(){
        Random r = new Random();
        end = false;
        wasAwarded = false;
        speed = r.nextInt(10) + 20;
        slow = 0.05;
        items = new ArrayList<>();
        actualItems = new ArrayList<>();
        offset = new float[4];
        rop = new RescaleOp(scale, offset, null);

        for(int i=0; i<itemsSize; i++){
            int lvl = r.nextInt(99) + 1;
            items.add(new Item(makeRandomItem(lvl, null)));
            if(i<11) actualItems.add(items.get(i));
        }

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/inventory.png"));
            itemsFills.add(ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/normalitem.png")));
            itemsFills.add(ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/customitem.png")));
            itemsFills.add(ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/epicitem.png")));
            pointerShow = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/pointer.png"));
            BufferedImage pluses = ImageIO.read(getClass().getResourceAsStream(
                    "/Sprites/Player/itemplusmini.png"
            ));
            for (int i = 0; i < 21; i++) {
                allPluses.add(pluses.getSubimage(i * 40, 0, 40, 30
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
            for (int i = 0; i < 9; i++) {
                BufferedImage[] bi = new BufferedImage[numItem[i]];
                for (int j = 0; j < numItem[i]; j++) {
                    {
                        bi[j] = spritesheet.getSubimage(
                                j * width,
                                i * height,
                                width,
                                height
                        );
                    }
                }
                sprites1.add(bi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String makeRandomItem(int level, ArrayList<Double> skinChance){
        Random r = new Random();
        String randomItem = "";

        {
            int random = r.nextInt(21);
            if(random<10)
            randomItem += ("0" + random);
            else randomItem += random;
        } // upgrade (2 chars)
        {
            int type = r.nextInt(7); // kiedys wiecej
            if (type < 10) randomItem += ("0" + type);
            else randomItem += type;
        } // type (2 chars)

        int skin = 0;
        {
            if (skinChance == null) {
                ArrayList<Double> standardSkinChance = new ArrayList<>(Arrays.asList(40.0, 20.0, 10.0));
                int random = r.nextInt(100) + 1;
                for (int i = 0; i < standardSkinChance.size(); i++) {
                    if (random < standardSkinChance.get(i)) skin++;
                }
            }
            randomItem += ("0" + skin);
        } // skin (2 chars)
        {
            if (level < 10) randomItem += ("0" + level);
            else randomItem += level;
        } // level (2 chars)
        {
            int random = r.nextInt(10);
            randomItem += ("0" + random);
        } // name (2 chars)

        ArrayList<Integer> bonusIndexes = new ArrayList<>();
        for(int i=0; i<(skin % 4)+2; i++){
            {
                int bonusIndex = r.nextInt(30);
                while (bonusIndexes.contains(bonusIndex)) {
                    bonusIndex = r.nextInt(30);
                }
                bonusIndexes.add(bonusIndex);

                if (bonusIndex < 10) randomItem += ("0" + bonusIndex);
                else randomItem += bonusIndex;
            } // bonus index (2 chars)
            {
                int bonusPower = r.nextInt(4);
                randomItem += bonusPower;
            } // bonus power (1 char)
            {
                int name = r.nextInt(10); // kiedys wiecej
                if (name < 10) randomItem += ("0" + name);
                else randomItem += name;
            } // bonus name index (2 chars)
        }
        return randomItem;
    }

    public boolean isEnd(){
        return end;
    }
    public boolean isWasAwarded() {return wasAwarded; }

    public Item getAward(){
        wasAwarded = false;
        return actualItems.get(5);
    }

    public void update(){
        if(end && scale[3] < 1) scale = new float[]{ 2f, 2f, 2f, (float)(scale[3] + 0.02)};
        rop = new RescaleOp(scale, offset, null);
        if(speed == 0) return;
        x += speed;
        if(speed - slow < 0) {
            speed = 0;
            wasAwarded = true;
            end = true;
        }
        else speed -= slow;
    }

    public void draw(Graphics2D g) {
        for(int i=0; i<10; i++){

            if(i > 0 && i < 10){
                g.drawImage(itemsFills.get(actualItems.get(i).getAllInfo().get(5)), x+i*70, 101, 70, 70, null);
                if(i == 5 && end){
                    g.drawImage(pointerShow, rop, x+i*70, 101);
                    g.drawImage(itemsFills.get(actualItems.get(i).getAllInfo().get(5)), rop, x+i*70, 101);
                }
                g.drawImage(sprites1.get(actualItems.get(i).getAllInfo().get(1))[actualItems.get(i).getAllInfo().get(2)], x + i*70, 101, 70, 70, null);
            }

            if(x>=270) {
                x=200;
                for(int j=10; j>=1; j--){
                    actualItems.set(j, actualItems.get(j-1));
                }
                actualItems.set(0, items.get(lastItem % itemsSize));
                lastItem++;
            }
        }
    }
}
