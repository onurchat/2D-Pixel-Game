package object;

import entity.Projectile;
import java.awt.Color;
import main.GamePanel;


/**
 * Represents a stone object in the game.
 */
public class ObjStone extends Projectile {
    GamePanel gp;

    /**
     * Constructs a new Stone object.
     * 
     * @param gp the Gamepanel instance currently active
     */
    public ObjStone(GamePanel gp) {
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

    /**
     * Loads and sets up images for spirit projectile.
     */
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

    /**
     * Gets the color of particles.
     */
    public Color getParticleColor() {
        Color color = new Color(25, 5, 75);
        return color;
    }

    /**
     * Gets the size of the particles.
     */
    public int getParticleSize() {
        int size = 8;
        return size;
    }

    /**
     * Gets the speed of the particles.
     */
    public int getParticleSpeed() {
        int speed = 1;
        return speed;
    }

    /**
     * Gets the lifespan of the particles.
     */
    public int getParticleMaxLife() {
        int maxLife = 20;
        return maxLife;
    }

}
