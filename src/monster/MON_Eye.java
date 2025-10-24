package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Stone;

public class MON_Eye extends Entity{

    GamePanel gp;

    public MON_Eye(GamePanel gp) {
        super(gp);

        this.gp = gp;
    
        type = 2;
        name = "Slime";
        speed = 1;
        maxLife = 6;
        life = maxLife;
        direction = "up";
        projectile = new OBJ_Stone(gp);


        solidArea.x = 5;
        solidArea.y = 5;
        solidArea.width = 38;
        solidArea.height = 38;
        solidAreaDefaultX = solidArea.x;
        solidaAreaDefaultY = solidArea.y;

        actionCounter = 120;

        getImage();
    }
    public void getImage() {
        up1 = setup("/res/monster/eye1", gp.tileSize, gp.tileSize);
        up2 = setup("/res/monster/eye2", gp.tileSize, gp.tileSize);
        down1 = setup("/res/monster/eye1", gp.tileSize, gp.tileSize);
        down2 = setup("/res/monster/eye2", gp.tileSize, gp.tileSize);
        left1 = setup("/res/monster/eye1", gp.tileSize, gp.tileSize);
        left2 = setup("/res/monster/eye2", gp.tileSize, gp.tileSize);
        right1 = setup("/res/monster/eye1", gp.tileSize, gp.tileSize);
        right2 = setup("/res/monster/eye2", gp.tileSize, gp.tileSize);
    }

    //override the update method so if player gets close, monsters get aggro
    public void update() {
        super.update();
        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);
        int tileDistance = (xDistance + yDistance) / gp.tileSize;

        //agro distance
        if (!onPath && tileDistance < 50) {
            onPath = true;
        }
        if (onPath && tileDistance > 50) {
            onPath = false;
        }
    }
    public void setAction() {

        if (onPath) {
            int goalCol = ((gp.player.worldX + gp.player.solidArea.x) / gp.tileSize);
            int goalRow = ((gp.player.worldY + gp.player.solidArea.y) / gp.tileSize);

            searchPath(goalCol, goalRow);
            int i = new Random().nextInt(100) + 1;
            if (i > 75 && !projectile.alive && shotAvailableCounter >= 30) {
                projectile.setMonsterProjectile(worldX, worldY, direction, true, this);
                gp.projectileList.add(projectile);
                shotAvailableCounter = 0;
            }
        } else {
            actionCounter++;
            if (actionCounter >= 120) {
                Random random = new Random();
                int i = random.nextInt(100) + 1; // i = random number 1-100

                if (i <= 25) {
                    direction = "up";
                }
                if (i > 25 && i <= 50) {
                    direction = "down";
                }
                if (i > 50 && i <= 75) {
                    direction = "left";
                }
                if (i > 75 && i <= 100) {
                    direction = "right";
                }
                actionCounter = 0;
            }
        } 
    }
}
