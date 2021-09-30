package Equipment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Item{
    private String identificator;
    private int upgrade;
    private int type;
    private int skinNumber;
    private int level;
    private int name;
    private int upgradeLevel;

    private ArrayList<Integer> bonusesIndexes = new ArrayList<>();
    private ArrayList<Integer> bonusesPowers = new ArrayList<>();
    private ArrayList<Integer> bonusesNames = new ArrayList<>();
    private ArrayList<String> allBonuses = new ArrayList<>();
    private ArrayList<String> allNames = new ArrayList<>();
    private ArrayList<Integer> allInfo = new ArrayList<>();
    private ArrayList<String> allDescriptions = new ArrayList<>();

    public Item(String id){
        if(id == null){
            identificator = null;
            return;
        }

        identificator = id;
        upgrade = Integer.parseInt(identificator.substring(0,2));
        type = Integer.parseInt(identificator.substring(2,4));
        skinNumber = Integer.parseInt(identificator.substring(4,6));
        level = Integer.parseInt(identificator.substring(6,8));
        name = Integer.parseInt(identificator.substring(8,10));
        upgradeLevel = upgrade/10;

        for(int i = 10; i<identificator.length(); ){
            bonusesIndexes.add(Integer.parseInt(identificator.substring(i,i+=2)));
            bonusesPowers.add(Integer.parseInt(identificator.substring(i,i+=1)));
            bonusesNames.add(Integer.parseInt(identificator.substring(i,i+=2)));
        }

        updateAllInfo();

        try {
            String st;
            File file = new File("Bonusy.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((st = br.readLine()) != null)
                allBonuses.add(st);
        } catch (IOException e) {}
        try {
            String st;
            File file = new File("Informacje.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((st = br.readLine()) != null)
                allDescriptions.add(st);
        } catch (IOException e) {}
        try {
            String st;
            File file = new File("Nazwy.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((st = br.readLine()) != null)
                allNames.add(st);
        } catch (IOException e) {}
    }

    public int getType(){
        return type;
    }

    public void upgradeItem(){
        if(upgrade < 20) upgrade++;
        updateAllInfo();
    }

    public int getUpgrade(){
        return upgrade;
    }

    private void updateAllInfo(){
        String up = String.valueOf(upgrade);
        if(up.length()<2) up = "0" + up;
        identificator = up + identificator.substring(2);
        upgradeLevel = upgrade/10;

        allInfo.clear();
        allInfo.add(upgrade);
        allInfo.add(type);
        allInfo.add(skinNumber);
        allInfo.add(level);
        allInfo.add(name);
        allInfo.add(upgradeLevel);
    }

    public ArrayList<Integer> getAllInfo(){ return allInfo; }
}
