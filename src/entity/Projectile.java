package entity;

import main.GamePanel;

public class Projectile extends Entity{

    Entity user;
    public int destWorldX;
    public int destWorldY;
    public int length;

    public float vX;
    public float vY;

    public Projectile(GamePanel gp) {
        super(gp);
    }

    public void set(int worldX, int worldY, int destScreenX, int destScreenY, boolean alive, Entity user) {

        this.worldX = worldX;
        this.worldY = worldY;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;

        destWorldX = (user.worldX - (gp.screenWidth / 2) + destScreenX);
        destWorldY = (user.worldY - (gp.screenHeight / 2) + destScreenY);

        float dx = gp.player.worldX - destWorldX;
        float dy = gp.player.worldY - destWorldY;

        length = (int) Math.sqrt(dx * dx + dy * dy);

        dx /= length;
        dy /= length;

        vX = dx * speed;
        vY = dy * speed;
    }

    public void update() {

        if (user == gp.player) {
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            if (monsterIndex != 999) {
                gp.player.damageMonster(monsterIndex, 1);
                alive = false;
            }
        }
        if (user != gp.player) {

        }

        worldX -= vX;
        worldY -= vY;

        life--;
        if (life < 0) {
            alive = false;
        }

        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNumber == 1) {
                spriteNumber = 2;
            }
            if (spriteNumber == 2) {
                spriteNumber = 1;
            }
            spriteCounter = 0;
        }
    }

}
