package main;

import entity.NPC_Reaper;
import monster.MON_Eye;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;

public class AssetSetter {

    GamePanel gp;
    int monsterCount;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        gp.obj[0] = new OBJ_Key(gp);
        gp.obj[0].worldX = 74 * gp.tileSize;
        gp.obj[0].worldY = 47 * gp.tileSize;

        gp.obj[1] = new OBJ_Key(gp);
        gp.obj[1].worldX = 61 * gp.tileSize;
        gp.obj[1].worldY = 72 * gp.tileSize;

        gp.obj[2] = new OBJ_Door(gp);
        gp.obj[2].worldX = 47 * gp.tileSize;
        gp.obj[2].worldY = 65 * gp.tileSize;

        gp.obj[3] = new OBJ_Door(gp);
        gp.obj[3].worldX = 30 * gp.tileSize;
        gp.obj[3].worldY = 28 * gp.tileSize;

        gp.obj[4] = new OBJ_Chest(gp);
        gp.obj[4].worldX = 28 * gp.tileSize;
        gp.obj[4].worldY = 23 * gp.tileSize;

        gp.obj[5] = new OBJ_Chest(gp);
        gp.obj[5].worldX = 55 * gp.tileSize;
        gp.obj[5].worldY = 70 * gp.tileSize;
    }

    public void setNPC() {

        gp.npc.clear();
        NPC_Reaper npc1 = new NPC_Reaper(gp);
        npc1.worldX = gp.tileSize * 30;
        npc1.worldY = gp.tileSize * 25;

        gp.npc.add(npc1);
    }

    public void setMonster() {

        gp.monster.clear();

        MON_Eye eye1 = new MON_Eye(gp);
        eye1.worldX = gp.tileSize * 50;
        eye1.worldY = gp.tileSize * 45;

        MON_Eye eye2 = new MON_Eye(gp);
        eye2.worldX = gp.tileSize * 45;
        eye2.worldY = gp.tileSize * 50;

        MON_Eye eye3 = new MON_Eye(gp);
        eye3.worldX = gp.tileSize * 40;
        eye3.worldY = gp.tileSize * 40;

        MON_Eye eye4 = new MON_Eye(gp);
        eye4.worldX = gp.tileSize * 40;
        eye4.worldY = gp.tileSize * 45;

        MON_Eye eye5 = new MON_Eye(gp);
        eye5.worldX = gp.tileSize * 45;
        eye5.worldY = gp.tileSize * 40;

        gp.monster.add(eye1);
        gp.monster.add(eye2);
        gp.monster.add(eye3);
        gp.monster.add(eye4);
        gp.monster.add(eye5);
    }

    public void spawnMonster(int col, int row) {
        
        for (int i = 0; i < gp.monster.size(); i++) {
            if ((Math.abs(col * gp.tileSize - gp.monster.get(i).worldX) < 30)
                    || (Math.abs(row * gp.tileSize - gp.monster.get(i).worldY) < 30)) {
                System.out.println("occupied by monster!");
                return;
            }
        }


        if (Math.abs(col * gp.tileSize - gp.player.worldX) < 30 
            || Math.abs(row * gp.tileSize - gp.player.worldY) < 30) {
            System.out.println("occupied by PLAYER!");
            return;
        }

        try {
            MON_Eye newEye = new MON_Eye(gp);
            newEye.worldX = gp.tileSize * col;
            newEye.worldY = gp.tileSize * row;
            gp.monster.add(newEye);
            System.out.println(gp.monster.size());

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
    }

}
