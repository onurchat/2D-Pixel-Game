package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

public class Player extends Entity{

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int hasKey = 0;

    public Player (GamePanel gp, KeyHandler keyH) {

        super(gp); //calling the constructor of superclass

        this.keyH = keyH;
        
        //screen X and Y are middle of screen always for player.
        screenX = (gp.screenWidth) / 2 - (gp.tileSize / 2); 
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);; 

        solidArea = new Rectangle(); //solidArea is collision area for player
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidaAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 29;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues () {
        worldX = gp.tileSize * 45; 
        worldY = gp.tileSize * 45; 
        speed = 4;
        direction = "down";

        //PLAYER STATUS
        maxLife = 6;
        life = maxLife;
    }

    public void getPlayerImage() {

        up1 = setup("/res/player/up1");
        up2 = setup("/res/player/up2");
        down1 = setup("/res/player/down1");
        down2 = setup("/res/player/down2");
        left1 = setup("/res/player/left1");
        left2 = setup("/res/player/left2");
        right1 = setup("/res/player/right1");
        right2 = setup("/res/player/right2");
    }

    public void update () {

        if (keyH.upPressed || keyH.downPressed
            || keyH.rightPressed || keyH.leftPressed) {

            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            } 

            if (keyH.rightPressed && keyH.upPressed) {
                direction = "upright";
            } else if (keyH.leftPressed && keyH.upPressed) {
                direction = "upleft";
            } else if (keyH.rightPressed && keyH.downPressed) {
                direction = "downright";
            } else if (keyH.leftPressed && keyH.downPressed) {
                direction = "downleft";
            }

            // CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            //CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex); 

            //CHECK MONSTER COLLISION
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            //CHECK EVENT
            gp.eHandler.checkEvent();
            gp.keyH.enterPressed = false;

            //IF COLLISION IS FALSE, PLAYER CAN MOVE
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
                    case "upright":
                        worldX += 3;
                        worldY -= 3;
                        break;
                    case "upleft":
                        worldX -= 3;
                        worldY -= 3;
                        break;
                    case "downright":
                        worldX += 3;
                        worldY += 3;
                        break;
                    case "downleft":
                        worldX -= 3;
                        worldY += 3;
                        break;
                }
            }

            spriteCounter++;
            if (spriteCounter > 10) {
                if (spriteNumber == 1) {
                    spriteNumber = 2;
                } else if (spriteNumber == 2) {
                    spriteNumber = 1;
                }
                spriteCounter = 0;
            }

        }
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public void pickUpObject(int i) {
        if (i != 999) {
            String objName = gp.obj[i].name;

            switch(objName) {
                case "Key":
                    gp.playSoundEffect(1);
                    hasKey++;
                    gp.obj[i] = null;
                    gp.ui.showMessage("+1 key!");
                    break;
                case "Door":
                    if (hasKey > 0) {
                        gp.playSoundEffect(2);
                        gp.obj[i] = null;
                        hasKey--;
                        gp.ui.showMessage("Opened a door!");
                    } else {
                        gp.ui.showMessage("You need a key.");
                    }
                    break;
            }
        }
    }

    public void interactNPC(int i) {
        if (i != 999) { 
            gp.gameState = gp.dialougeState;
            gp.npc[i].speak();
       }
    }

    public void contactMonster(int i) {
        if (i != 999 && !invincible) {
            life--;
            invincible = true;
        }
    }

    public void draw (Graphics2D g2) {

        BufferedImage image = null;

        switch(direction) {
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

                break;
            case "upright":
                if (spriteNumber == 1) {
                    image = up1;
                } 
                if (spriteNumber == 2) {
                    image = up2;
                }

                break;
            case "upleft":
                if (spriteNumber == 1) {
                    image = up1;
                } 
                if (spriteNumber == 2) {
                    image = up2;
                }

                break;
            case "downright":
                if (spriteNumber == 1) {
                    image = down1;
                } 
                if (spriteNumber == 2) {
                    image = down2;
                }

                break;
            case "downleft":
                if (spriteNumber == 1) {
                    image = down1;
                } 
                if (spriteNumber == 2) {
                    image = down2;
                }

                break;

        }

        if (invincible) {
            //change the opacity if player is invincible!
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            float[] scales = { 1.5f, 0f, 0f, 1.0f }; // {R, G, B, A}
            float[] offsets = new float[4];
            RescaleOp redTint = new RescaleOp(scales, offsets, null);
            g2.drawImage(image, redTint, screenX, screenY);
            // reset alpha after drawing player so other UI stays same!
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        } else {
            g2.drawImage(image, screenX, screenY, null);
        }
    }
}
