package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_Stone extends Projectile{
    GamePanel gp;

    public OBJ_Stone(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name = "Stone";
        direction = "up";
        speed = 5;
        maxLife = 100;
        life = maxLife;
        useCost = 2;
        alive = false;
        getImage();
    }

    public void getImage() {
        up1 = setup("/res/projectile/monsterP", gp.tileSize, gp.tileSize);
        up2 = setup("/res/projectile/monsterP2", gp.tileSize, gp.tileSize);
        down1 = setup("/res/projectile/monsterP", gp.tileSize, gp.tileSize);
        down2 = setup("/res/projectile/monsterP2", gp.tileSize, gp.tileSize);
        left1 = setup("/res/projectile/monsterP", gp.tileSize, gp.tileSize);
        left2 = setup("/res/projectile/monsterP2", gp.tileSize, gp.tileSize);
        right1 = setup("/res/projectile/monsterP", gp.tileSize, gp.tileSize);
        right2 = setup("/res/projectile/monsterP2", gp.tileSize, gp.tileSize);
    }
}
