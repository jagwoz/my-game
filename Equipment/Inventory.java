package Equipment;

import Entity.MapObject;
import TileMap.TileMap;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Inventory extends MapObject {
    ArrayList<Item> items;
    ArrayList<Boolean> avaiable = new ArrayList<>(Collections.nCopies(20, true));
    private ArrayList<Rectangle> inventorySlots;
    private Point pointerInventory = new Point(0,0);
    private int currentIndex;
    private int x = 1280;
    private int y = 0;
    private BufferedImage image;
    private ArrayList<BufferedImage> itemsFills = new ArrayList<>();
    private ArrayList<BufferedImage> allPluses = new ArrayList<>();
    private BufferedImage pointer;
    private boolean isStatic;
    private boolean hiding;
    private boolean showing;
    private ArrayList<ItemInfo> itemInfoList =  new ArrayList<>();
    ArrayList<BufferedImage[]> sprites1= new ArrayList<>();
    private final int[] numItem = { 9, 9, 9, 9, 9, 9, 9, 9, 9 };
    private Item customItem;
    private float[] scalesCopy = { 1f, 1f, 1f, 0.0f };
    private ArrayList<float[]> scales;
    private ArrayList<float[]> offsets;
    private ArrayList<RescaleOp> rops;
    private Rectangle lastInfoShowed = new Rectangle(0,0,0,0);
    ArrayList<Item> equipmentItems;
    private ArrayList<float[]> scalesEq;
    private ArrayList<float[]> offsetsEq;
    private ArrayList<RescaleOp> ropsEq;
    private ArrayList<Rectangle> equipmentSlots;
    ArrayList<Boolean> avaiableEq = new ArrayList<>(Collections.nCopies(8, true));

    public Inventory(TileMap tm){
        super(tm);
        {
            width = 70;
            height = 70;
            isStatic = true;
            hiding = true;
            currentIndex = 0;
            customItem = new Item(null);
            items = new ArrayList<>(Collections.nCopies(20, customItem));
            inventorySlots = new ArrayList<>(Collections.nCopies(20, new Rectangle(0, 0, 0, 0)));
            scales = new ArrayList<>(Collections.nCopies(20, scalesCopy));
            offsets = new ArrayList<>(Collections.nCopies(20, new float[4]));
            rops = new ArrayList<>(Collections.nCopies(20, new RescaleOp(scales.get(0), offsets.get(0), null)));

            equipmentItems = new ArrayList<>(Collections.nCopies(8, customItem));
            equipmentSlots = new ArrayList<>(Collections.nCopies(8, new Rectangle(0, 0, 0, 0)));
            scalesEq = new ArrayList<>(Collections.nCopies(8, scalesCopy));
            offsetsEq = new ArrayList<>(Collections.nCopies(8, new float[4]));
            ropsEq = new ArrayList<>(Collections.nCopies(8, new RescaleOp(scales.get(0), offsets.get(0), null)));
        } // variables fill
        {
            try {
                image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/inventory.png"));
                itemsFills.add(ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/normalitem.png")));
                itemsFills.add(ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/customitem.png")));
                itemsFills.add(ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/epicitem.png")));
                pointer = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/pointer.png"));
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
        } // graphics write
    }

    public void setPointer(Point p){
        pointerInventory.setLocation(p.getX(), p.getY());
    }

    public void show(){
        x = 880;
        isStatic = true;
        hiding = false;
        showing = false;
    }

    public void hide(){
        itemInfoList.clear();
        isStatic = false;
        hiding = true;
        showing = false;
    }

    public void equipItem(){
        {
            for (int i = 0; i < 20; i++) {
                if (inventorySlots.get(i).contains(pointerInventory)) {
                    if (items.get(i).equals(customItem)) return;
                    if (avaiableEq.get(items.get(i).getType()) == true) {
                        equipmentItems.set(items.get(i).getType(), items.get(i));
                        avaiableEq.set(items.get(i).getType(), false);
                        items.set(i, customItem);
                        avaiable.set(i, true);
                    }
                    else {
                        Item tmp = items.get(i);
                        items.set(i, equipmentItems.get(tmp.getType()));
                        equipmentItems.set(tmp.getType(), tmp);
                    }
                    itemInfoList.clear();
                }
            }
        } // clicked on inventory
        {
            for (int i = 0; i < 8; i++) {
                if (equipmentSlots.get(i).contains(pointerInventory)) {
                    if (equipmentItems.get(i).equals(customItem)) return;
                    if (!avaiable.contains(true)) return;
                    for (int j = 0; j < 20; j++) {
                        if (avaiable.get(j) == true) {
                            items.set(j, equipmentItems.get(i));
                            avaiable.set(j, false);
                            equipmentItems.set(i, customItem);
                            avaiableEq.set(i, true);
                            itemInfoList.clear();
                            return;
                        }
                    }
                }
            }
        } // clicked on equipment
    }

    public void removeItem(){
        for(int i=0; i<20; i++){
            if(inventorySlots.get(i).contains(pointerInventory)){
                items.set(i, customItem);
                avaiable.set(i, true);
                itemInfoList.clear();
            }
        }
    }

    public void upgradeItem(){
        for(int i=0; i<20; i++){
            if(inventorySlots.get(i).contains(pointerInventory)){
                if(items.get(i).equals(customItem)) return;
                items.get(i).upgradeItem();
            }
        }
    }

    public void addItem(Item itm){
        for (int i = 0; i < 20; i++)
            if (avaiable.get(i) == true) {
                items.set(i,itm);
                avaiable.set(i,false);
                return;
            }
    }

    public void addItemInfo(){
        if(!lastInfoShowed.contains(pointerInventory)) itemInfoList.clear();
        if(itemInfoList.size()>0) return;
        for(int i=0; i<20; i++){
            if(inventorySlots.get(i).contains(pointerInventory)){
                if(items.get(i).equals(customItem)) return;
                itemInfoList.add(new ItemInfo(tileMap, items.get(i).getAllInfo(), inventorySlots.get(i).x, inventorySlots.get(i).y));
                lastInfoShowed = inventorySlots.get(i);
                return;
            }
        }
        for(int i=0; i<11; i++){
            if(equipmentSlots.get(i).contains(pointerInventory)){
                if(equipmentItems.get(i).equals(customItem)) return;
                itemInfoList.add(new ItemInfo(tileMap, equipmentItems.get(i).getAllInfo(), equipmentSlots.get(i).x, equipmentSlots.get(i).y));
                lastInfoShowed = equipmentSlots.get(i);
                return;
            }
        }
    }


    public String makeRandomItem(int level, ArrayList<Double> skinChance){
        Random r = new Random();
        String randomItem = "";

        {
            int random = r.nextInt(6);
            randomItem += ("0" + random);
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

    public int howAvaibles(){
        int sum = 0;
        for(int i=0; i<avaiable.size(); i++){
            if(avaiable.get(i) == true) sum++;
        }
        return sum;
    }

    public void changeInventoryMove(){
        if(isStatic){
            if(showing){
                itemInfoList.clear();
                isStatic = false;
                hiding = true;
                showing = false;
            }
            else if(hiding){
                isStatic = false;
                showing = true;
                hiding = false;
            }
        }
    }

    public void update(int playerLevel){
        for(int i = 0; i< itemInfoList.size(); i++) itemInfoList.get(i).update(playerLevel);

        if(showing && !isStatic) {
            if (x > 880) x-=40;
            else {
                x = 880;
                isStatic = true;
            }
        }
        else if(hiding && !isStatic) {
            if (x <= 1280) x+=40;
            else {
                x = 1280;
                isStatic = true;
            }
        }

        //EQ
        Rectangle itemSlotEq;

        itemSlotEq = new Rectangle(x + 101, y + 85, 70, 70);
        equipmentSlots.set(0,itemSlotEq);

        itemSlotEq = new Rectangle(x + 177, y + 47, 70, 70);
        equipmentSlots.set(1,itemSlotEq);

        itemSlotEq = new Rectangle(x + 177, y + 123, 70, 70);
        equipmentSlots.set(2,itemSlotEq);

        itemSlotEq = new Rectangle(x + 177, y + 199, 70, 70);
        equipmentSlots.set(3,itemSlotEq);

        itemSlotEq = new Rectangle(x + 253, y + 47, 70, 70);
        equipmentSlots.set(4,itemSlotEq);

        itemSlotEq = new Rectangle(x + 253, y + 123, 70, 70);
        equipmentSlots.set(5,itemSlotEq);

        itemSlotEq = new Rectangle(x + 253, y + 199, 70, 70);
        equipmentSlots.set(6,itemSlotEq);

        itemSlotEq = new Rectangle(x + 85, y + 177, 70, 70);
        equipmentSlots.set(7,itemSlotEq);


        for(int i = 0; i< itemInfoList.size(); i++) itemInfoList.get(i).update(playerLevel);
        {
            for (int i = 0; i < 20; i++) {
                Rectangle itemSlot = new Rectangle(x + 63 + i % 4 * 76, y + 305 + i / 4 * 76, 70, 70);
                inventorySlots.set(i, itemSlot);
                if (itemSlot.contains(pointerInventory)) {
                    if (scales.get(i)[3] < 1) scales.set(i, new float[]{2f, 2f, 2f, (float) (scales.get(i)[3] + 0.03)});
                } else if (scales.get(i)[3] > 0)
                    scales.set(i, new float[]{2f, 2f, 2f, (float) (scales.get(i)[3] - 0.04)});
            }

            for (int i = 0; i < 8; i++) {
                if (equipmentSlots.get(i).contains(pointerInventory)) {
                    if (scalesEq.get(i)[3] < 1)
                        scalesEq.set(i, new float[]{2f, 2f, 2f, (float) (scalesEq.get(i)[3] + 0.03)});
                } else if (scalesEq.get(i)[3] > 0)
                    scalesEq.set(i, new float[]{2f, 2f, 2f, (float) (scalesEq.get(i)[3] - 0.04)});
            }

            for (int i = 0; i < 20; i++)
                rops.set(i, new RescaleOp(scales.get(i), offsets.get(i), null));

            for (int i = 0; i < 8; i++)
                ropsEq.set(i, new RescaleOp(scalesEq.get(i), offsetsEq.get(i), null));
        } // pointer animation
    }

    public void draw(Graphics2D g){
        if(x!=1280){
            g.drawImage(image, x, y, 400, 720, null);
        }

        for(int i=0; i<items.size(); i++){
            g.drawImage(pointer, rops.get(i), inventorySlots.get(i).x, inventorySlots.get(i).y);
            if(items.get(i) != customItem){
                g.drawImage(sprites1.get(items.get(i).getAllInfo().get(1))[items.get(i).getAllInfo().get(2)],
                        inventorySlots.get(i).x, inventorySlots.get(i).y, 70, 70, null);
                g.drawImage(itemsFills.get(items.get(i).getAllInfo().get(5)),
                        inventorySlots.get(i).x, inventorySlots.get(i).y, 70, 70, null);
                g.drawImage(itemsFills.get(items.get(i).getAllInfo().get(5)), rops.get(i), inventorySlots.get(i).x, inventorySlots.get(i).y);
            }
        }

        for(int i=0; i<equipmentItems.size(); i++){
            BufferedImage pointerShow = pointer;

            g.drawImage(pointerShow, ropsEq.get(i), equipmentSlots.get(i).x, equipmentSlots.get(i).y);
            if(equipmentItems.get(i) != customItem){
                int w = 70;
                int h = 70;
                BufferedImage sprite = sprites1.get(equipmentItems.get(i).getAllInfo().get(1))[equipmentItems.get(i).getAllInfo().get(2)];
                BufferedImage itemFill = itemsFills.get(equipmentItems.get(i).getAllInfo().get(5));

                g.drawImage(sprite,
                        equipmentSlots.get(i).x, equipmentSlots.get(i).y, w, h, null);
                g.drawImage(itemFill,
                        equipmentSlots.get(i).x, equipmentSlots.get(i).y, w, h, null);
                g.drawImage(itemFill, ropsEq.get(i), equipmentSlots.get(i).x, equipmentSlots.get(i).y);
            }
        }

        for(int i = 0; i< itemInfoList.size(); i++) itemInfoList.get(i).draw(g);
    }
}