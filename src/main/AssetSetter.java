package main;

import entity.NPC_Reaper;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;

public class AssetSetter {

    GamePanel gp;

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
        gp.npc[0] = new NPC_Reaper(gp);
        gp.npc[0].worldX = gp.tileSize * 47;
        gp.npc[0].worldY = gp.tileSize * 47;
    }

}
