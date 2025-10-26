package main;

import entity.NPCReaper;
import java.util.Random;
import monster.MonsterEye;
import object.ObjChest;
import object.ObjDoor;
import object.ObjKey;

/**
 * The class is responsible for placing every asset in the game world.
 */
public class AssetSetter {

    GamePanel gp;
    int monsterCount;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Initializes all static objects.
     */
    public void setObject() {

        gp.obj[0] = new ObjKey(gp);
        gp.obj[0].worldX = 76 * gp.tileSize;
        gp.obj[0].worldY = 45 * gp.tileSize;

        gp.obj[1] = new ObjKey(gp);
        gp.obj[1].worldX = 75 * gp.tileSize;
        gp.obj[1].worldY = 45 * gp.tileSize;

        gp.obj[2] = new ObjDoor(gp);
        gp.obj[2].worldX = 47 * gp.tileSize;
        gp.obj[2].worldY = 65 * gp.tileSize;

        gp.obj[3] = new ObjDoor(gp);
        gp.obj[3].worldX = 30 * gp.tileSize;
        gp.obj[3].worldY = 28 * gp.tileSize;

        gp.obj[4] = new ObjChest(gp);
        gp.obj[4].worldX = 32 * gp.tileSize;
        gp.obj[4].worldY = 27 * gp.tileSize;

        gp.obj[5] = new ObjChest(gp);
        gp.obj[5].worldX = 61 * gp.tileSize;
        gp.obj[5].worldY = 66 * gp.tileSize;
    }

    /**
     * Initializes and places all NPCs in the world.
     */
    public void setNPC() {

        gp.npc.clear();
        NPCReaper npc1 = new NPCReaper(gp);
        npc1.worldX = gp.tileSize * 30;
        npc1.worldY = gp.tileSize * 25;

        gp.npc.add(npc1);
    }

    /**
     * Initializes and places a set of monster entities at fixed locations.
     */
    public void setMonster() {

        gp.monster.clear();

        MonsterEye eye1 = new MonsterEye(gp);
        eye1.worldX = gp.tileSize * 50;
        eye1.worldY = gp.tileSize * 45;

        MonsterEye eye2 = new MonsterEye(gp);
        eye2.worldX = gp.tileSize * 45;
        eye2.worldY = gp.tileSize * 50;

        MonsterEye eye3 = new MonsterEye(gp);
        eye3.worldX = gp.tileSize * 40;
        eye3.worldY = gp.tileSize * 40;

        MonsterEye eye4 = new MonsterEye(gp);
        eye4.worldX = gp.tileSize * 40;
        eye4.worldY = gp.tileSize * 45;

        MonsterEye eye5 = new MonsterEye(gp);
        eye5.worldX = gp.tileSize * 45;
        eye5.worldY = gp.tileSize * 40;

        gp.monster.add(eye1);
        gp.monster.add(eye2);
        gp.monster.add(eye3);
        gp.monster.add(eye4);
        gp.monster.add(eye5);
    }

    /**
     * Spawns a new monster at a randomized location.
     */
    public void spawnMonster() {

        Random random = new Random();
        int randomCol = random.nextInt(52) + 18;
        int randomRow = random.nextInt(29) + 29;
        
        for (int i = 0; i < gp.monster.size(); i++) {
            if ((Math.abs(randomCol * gp.tileSize - gp.monster.get(i).worldX) < 30)
                    || (Math.abs(randomRow * gp.tileSize - gp.monster.get(i).worldY) < 30)) {
                System.out.println("occupied by monster!");
                return;
            }
        }


        if (Math.abs(randomCol * gp.tileSize - gp.player.worldX) < 30 
            || Math.abs(randomRow * gp.tileSize - gp.player.worldY) < 30) {
            System.out.println("occupied by PLAYER!");
            return;
        }

        try {
            MonsterEye newEye = new MonsterEye(gp);
            newEye.worldX = gp.tileSize * randomCol;
            newEye.worldY = gp.tileSize * randomRow;
            gp.monster.add(newEye);
            newEye.checkCollision();
            if (newEye.collisionOn) {
                newEye.alive = false;
                gp.monster.remove(newEye);
                System.out.println("got stuck!");
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
    }

}
