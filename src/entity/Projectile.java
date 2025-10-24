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

    public void setPlayerProjectile(int worldX, int worldY, int destScreenX, int destScreenY, boolean alive, Entity user) {

        this.worldX = worldX;
        this.worldY = worldY;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;

        destWorldX = (user.worldX - (gp.screenWidth2 / 2) + destScreenX);
        destWorldY = (user.worldY - (gp.screenHeight2 / 2) + destScreenY);

        float dx = gp.player.worldX - destWorldX;
        float dy = gp.player.worldY - destWorldY;

        length = (int) Math.sqrt(dx * dx + dy * dy);

        dx /= length;
        dy /= length;

        vX = dx * speed;
        vY = dy * speed;
        System.out.println("Dest World X: " + destWorldX + "Dest World Y: " + destWorldY);
    }

    public void setMonsterProjectile(int worldX, int worldY, String direction, boolean alive, Entity user) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = maxLife;
    }

    public void update() {

        if (user == gp.player) {
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
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            if (monsterIndex != 999) {
                gp.player.damageMonster(monsterIndex, 1);
                alive = false;
            }
        }
        if (user != gp.player) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
            }
            life--;
            if (life < 0) {
                alive = false;
            }
            boolean contactPlayer = gp.cChecker.checkPlayer(this);
            if (!gp.player.invincible && contactPlayer) {
                gp.player.life--;
                gp.playSoundEffect(4);
                gp.player.invincible = true;
                alive = false;
            }
        }
    }
}
