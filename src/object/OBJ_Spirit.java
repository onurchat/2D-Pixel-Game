package object;

import java.awt.Color;

import entity.Projectile;
import main.GamePanel;

public class OBJ_Spirit extends Projectile{

    GamePanel gp;

    public OBJ_Spirit(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name = "Spirit";
        direction = "up";
        speed = 8;
        maxLife = 40;
        life = maxLife;
        useCost = 2;
        alive = false;
        getImage();
    }

    public void getImage() {
        up1 = setup("/res/projectile/spirit1", gp.tileSize, gp.tileSize);
        up2 = setup("/res/projectile/spirit2", gp.tileSize, gp.tileSize);
        down1 = setup("/res/projectile/spirit1", gp.tileSize, gp.tileSize);
        down2 = setup("/res/projectile/spirit2", gp.tileSize, gp.tileSize);
        left1 = setup("/res/projectile/spirit1", gp.tileSize, gp.tileSize);
        left2 = setup("/res/projectile/spirit2", gp.tileSize, gp.tileSize);
        right1 = setup("/res/projectile/spirit1", gp.tileSize, gp.tileSize);
        right2 = setup("/res/projectile/spirit2", gp.tileSize, gp.tileSize);
    }

    public Color getParticleColor() {
        Color color = new Color(0, 50, 255);
        return color;
    }

    public int getParticleSize() {
        int size = 8;
        return size;
    }

    public int getParticleSpeed() {
        int speed = 1;
        return speed;
    }

    public int getParticleMaxLife() {
        int maxLife = 20;
        return maxLife;
    }

}
