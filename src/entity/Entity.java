package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {

    GamePanel gp;

    public String name;
    public int worldX, worldY;
    public int speed;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, 
        attackLeft1, attackLeft2, attackRight1, attackRight2;
    public String direction;
    public int spriteCounter = 0;
    public int spriteNumber = 1;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX;
    public int solidaAreaDefaultY;
    public boolean collisionOn = false;
    public int actionCounter = 0;
    public boolean invincible = false;
    public int invincibleCounter = 0;
    String dialogues[] = new String[20];
    int dialogueIndex = 0;
    public int type; //0 = player, 1= NPC, 2= monster
    public boolean isPlayerAttacking = false;
    public boolean alive = true;
    public boolean dying = false;
    int dyingCounter = 0;
    boolean hpBarOn = false;
    
    public Projectile projectile;
    public int useCost;

    //CHARACTER STATUS
    public int maxLife;
    public int life;

    public int mIndex;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction() { //unique for all NPCs
        //empty BUT subclass has same method and takes priority
    }

    public void speak() {
        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction) {
            case "up": 
                direction = "down";
                break; 
            case "down": 
                direction = "up";
                break;
            case "left": 
                direction = "right";
                break;
            case "right": 
                direction = "left";
                break;
            case "upright": 
                direction = "down";
                break;
            case "upleft": 
                direction = "down";
                break;
            case "downright": 
                direction = "up";
                break;
            case "downleft": 
                direction = "up";
                break;
        }
    }

    public void update() { //identical for all entities

        setAction();

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if (this.type == 2 && contactPlayer && !gp.player.invincible && !this.dying) {
            gp.player.life--;
            gp.playSoundEffect(4);
            gp.player.invincible = true;
        }

        if (!collisionOn) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 30) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        //position of NPC ON SCREEN
        int screenX = worldX - gp.player.worldX + gp.player.screenX; 
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        //Improving efficiency by not rendering NPC that are not visible:
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX 
            && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
            && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
            && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            switch (direction) {
                case "up":
                    if (spriteNumber == 1) {
                        image = up1;
                    } 
                    if (spriteNumber == 2) {
                        image = up2;
                    }
                    break;
                case "down":
                    if (spriteNumber == 1) {
                        image = down1;
                    } 
                    if (spriteNumber == 2) {
                        image = down2;
                    }
                    break;
                case "right":
                    if (spriteNumber == 1) {
                        image = right1;
                    } 
                    if (spriteNumber == 2) {
                        image = right2;
                    }

                    break;
                case "left":
                    if (spriteNumber == 1) {
                        image = left1;
                    } 
                    if (spriteNumber == 2) {
                        image = left2;
                    }
            }

            //Monster HP BAR
            if (type == 2 && !dying && hpBarOn) {

                double oneScale = (double) gp.tileSize / maxLife;
                double hpBarValue = oneScale * life;

                g2.setColor(new Color(60, 0, 0));
                g2.fillRect(screenX - 3, screenY - 18, gp.tileSize + 6, 16);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);
            }
            
            if (invincible) {
                hpBarOn = true;
                changeAlpha(g2, 0.4f);
            }

            if (dying) {
                dyingAnimation(g2);
            }

            if (gp.gameState == gp.playState) {
                spriteCounter++;
            }


            if (spriteCounter > 10) {
                if (spriteNumber == 1) {
                    spriteNumber = 2;
                } else if (spriteNumber == 2) {
                    spriteNumber = 1;
                }
                spriteCounter = 0;
            }
            
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            changeAlpha(g2, 1f);

        }
    }

    public void setMonsterIndex(int i) {
        this.mIndex = i;
    }

    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;
        int animationInterval = 5;

        gp.aSetter.spawnMonster(47, 47);

        if (dyingCounter <= animationInterval) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > animationInterval && dyingCounter <= animationInterval * 2) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > animationInterval * 2 && dyingCounter <= animationInterval * 3) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > animationInterval * 3 && dyingCounter <= animationInterval * 4) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > animationInterval * 4 && dyingCounter <= animationInterval * 5) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > animationInterval * 5 && dyingCounter <= animationInterval * 6) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > animationInterval * 6 && dyingCounter <= animationInterval * 7) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > animationInterval * 7 && dyingCounter <= animationInterval * 8) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter >= 40) {
            dying = false;
            alive = false;
            dyingCounter = 0;

            //removing monster
            gp.monster.remove(mIndex);

            //SPAWNING
            gp.aSetter.spawnMonster(47, 50);
        }
    }

    public void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    public BufferedImage setup(String imagePath, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return image;
    }

}
