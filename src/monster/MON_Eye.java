package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;

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

        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
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
    public void setAction() {
        actionCounter++;
        if (actionCounter >= 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1; //i = random number 1-100

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
