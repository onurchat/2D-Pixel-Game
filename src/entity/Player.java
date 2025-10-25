package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.plaf.basic.BasicTabbedPaneUI.MouseHandler;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.OBJ_Spirit;

public class Player extends Entity{

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int hasKey = 0;
    public int eyesKilled = 0;

    int soundEffectTimer = 30;
    int projectileTimer = 40;
    int regenTimer = 0;

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

        attackArea.width = 36;
        attackArea.height = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
    }

    public void setDefaultValues () {
        invincible = false;
        worldX = gp.tileSize * 45; 
        worldY = gp.tileSize * 45; 
        speed = 4;
        direction = "down";

        //PLAYER STATUS
        maxLife = 6;
        life = maxLife;

        projectile = new OBJ_Spirit(gp);
    }

    public void getPlayerImage() {

        up1 = setup("/res/player/up1", gp.tileSize, gp.tileSize);
        up2 = setup("/res/player/up2", gp.tileSize, gp.tileSize);
        down1 = setup("/res/player/down1", gp.tileSize, gp.tileSize);
        down2 = setup("/res/player/down2", gp.tileSize, gp.tileSize);
        left1 = setup("/res/player/left1", gp.tileSize, gp.tileSize);
        left2 = setup("/res/player/left2", gp.tileSize, gp.tileSize);
        right1 = setup("/res/player/right1", gp.tileSize, gp.tileSize);
        right2 = setup("/res/player/right2", gp.tileSize, gp.tileSize);
    }

    public void getPlayerAttackImage() {

        attackUp1 = setup("/res/player/attackup1", gp.tileSize, gp.tileSize * 2);
        attackUp2 = setup("/res/player/attackup2", gp.tileSize, gp.tileSize * 2);
        attackDown1 = setup("/res/player/attackdown1", gp.tileSize, gp.tileSize * 2);
        attackDown2 = setup("/res/player/attackdown2", gp.tileSize, gp.tileSize * 2);
        attackLeft1 = setup("/res/player/attackleft1", gp.tileSize * 2, gp.tileSize);
        attackLeft2 = setup("/res/player/attackleft2", gp.tileSize * 2, gp.tileSize);
        attackRight1 = setup("/res/player/attackright1", gp.tileSize * 2, gp.tileSize);
        attackRight2 = setup("/res/player/attackright2", gp.tileSize * 2, gp.tileSize);
    }

    public void update () {
        if (gp.keyH.ePressed) {
            isPlayerAttacking = true;
            if (soundEffectTimer >= 30) {
                gp.playSoundEffect(5);
                soundEffectTimer = 0;
            }
        }
        if (gp.keyH.ePressTimer < 30) {
            gp.keyH.ePressTimer++;
        }
        if (soundEffectTimer < 30) {
            soundEffectTimer++;
        }
        if (projectileTimer < 40) {
            projectileTimer++;
        }
        if (isPlayerAttacking) {
            attacking();
        }
        if (regenTimer < 600) {
            regenTimer++;
        } else if (regenTimer >= 600 && (maxLife - life >= 1)) {
            life++;
            regenTimer = 0;
        } 

        if (keyH.upPressed || keyH.downPressed
            || keyH.rightPressed || keyH.leftPressed) {
            isPlayerAttacking = false;

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

        } else if (!isPlayerAttacking) {
            //spriteCounter = 0;
        }
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 30) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (projectileTimer >= 40) {
            if (gp.mouseH.mousePressed) {
                projectile.setPlayerProjectile(worldX, worldY, gp.mouseH.mouseX, gp.mouseH.mouseY, true, this);

                gp.projectileList.add(projectile);
                gp.playSoundEffect(6);
                gp.mouseH.mousePressed = false;
            }
            projectileTimer = 0;
        }
        if (life <= 0 && gp.gameState == gp.playState) {
            gp.gameState = gp.gameOverState;
            gp.stopMusic();
            gp.playSoundEffect(7);
        }
    }

    public void attacking() {
        spriteCounter++;

        if (spriteCounter <= 5) {
            spriteNumber = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 25) {
            spriteNumber = 2;

            //save the current values
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            //adjust player's world x, y for attack area
            switch(direction) {
                case "up": 
                    worldY -= attackArea.height;
                    break;
                case "down":
                    worldY += attackArea.height;
                    break;
                case "left":
                    worldX -= attackArea.width;
                    break;
                case "right":
                    worldX += attackArea.width;
                    break;
                case "upright":
                    worldY -= attackArea.height;
                    break;
                case "upleft":
                    worldY -= attackArea.height;
                    break;
                case "downright":
                    worldY += attackArea.height;
                    break;
                case "downleft":
                    worldY += attackArea.height;
                    break;
            }

            //attack area becomes solid area
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            //check monster collision with the updated world X, Y and solid area
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex, 4);

            //restore values
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        if (spriteCounter > 25) {
            spriteNumber = 1;
            spriteCounter = 0;
            isPlayerAttacking = false;
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
                case "Chest":
                    gp.playSoundEffect(8);
                    maxLife += 2;
                    gp.obj[i] = null;
                    gp.ui.showMessage("You gained a heart!");
                    break;
            }
        }
    }

    public void interactNPC(int i) {
        if (i != 999) { 
            gp.gameState = gp.dialougeState;
            gp.npc.get(i).speak();
       }
    }

    public void contactMonster(int i) {
        if (i != 999 && !invincible && !gp.monster.get(i).dying && gp.monster.get(i).alive) {
            gp.playSoundEffect(4);
            life--;
            invincible = true;
        }
    }

    public void damageMonster(int i, int damage) {
        if (i != 999) {
            if (!gp.monster.get(i).invincible) {
                gp.playSoundEffect(3);
                gp.monster.get(i).life -= damage;
                gp.monster.get(i).invincible = true;
                if (gp.monster.get(i).life <= 0) {
                    gp.monster.get(i).dying = true;
                    gp.monster.get(i).setMonsterIndex(i);
                    eyesKilled++;
                }
            }
        }
    }

    public void draw (Graphics2D g2) {

        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch(direction) {
            case "up":
                if (!isPlayerAttacking) {
                    if (spriteNumber == 1) {
                        image = up1;
                    }
                    if (spriteNumber == 2) {
                        image = up2;
                    }
                }
                if (isPlayerAttacking) {
                    tempScreenY -= gp.tileSize;
                    if (spriteNumber == 1) {
                        image = attackUp1;
                    }
                    if (spriteNumber == 2) {
                        image = attackUp2;
                    }
                }
                break;
            case "down":
                if (!isPlayerAttacking) {
                    if (spriteNumber == 1) {
                        image = down1;
                    }
                    if (spriteNumber == 2) {
                        image = down2;
                    }
                }
                if (isPlayerAttacking) {
                    if (spriteNumber == 1) {
                        image = attackDown1;
                    }
                    if (spriteNumber == 2) {
                        image = attackDown2;
                    }
                }
                break;
            case "right":
                if (!isPlayerAttacking) {
                    if (spriteNumber == 1) {
                        image = right1;
                    }
                    if (spriteNumber == 2) {
                        image = right2;
                    }
                }
                if (isPlayerAttacking) {
                    if (spriteNumber == 1) {
                        image = attackRight1;
                    }
                    if (spriteNumber == 2) {
                        image = attackRight2;
                    }
                }
                break;
            case "left":
                if (!isPlayerAttacking) {
                    if (spriteNumber == 1) {
                        image = left1;
                    }
                    if (spriteNumber == 2) {
                        image = left2;
                    }
                }
                if (isPlayerAttacking) {
                    tempScreenX -= gp.tileSize;
                    if (spriteNumber == 1) {
                        image = attackLeft1;
                    }
                    if (spriteNumber == 2) {
                        image = attackLeft2;
                    }
                }
                break;
            case "upright":
                if (!isPlayerAttacking) {
                    if (spriteNumber == 1) {
                        image = up1;
                    }
                    if (spriteNumber == 2) {
                        image = up2;
                    }
                }
                if (isPlayerAttacking) {
                    tempScreenY -= gp.tileSize;
                    if (spriteNumber == 1) {
                        image = attackUp1;
                    }
                    if (spriteNumber == 2) {
                        image = attackUp2;
                    }
                }
                break;
            case "upleft":
                if (!isPlayerAttacking) {
                    if (spriteNumber == 1) {
                        image = up1;
                    }
                    if (spriteNumber == 2) {
                        image = up2;
                    }
                }
                if (isPlayerAttacking) {
                    tempScreenY -= gp.tileSize;
                    if (spriteNumber == 1) {
                        image = attackUp1;
                    }
                    if (spriteNumber == 2) {
                        image = attackUp2;
                    }
                }
                break;
            case "downright":
                if (!isPlayerAttacking) {
                    if (spriteNumber == 1) {
                        image = down1;
                    }
                    if (spriteNumber == 2) {
                        image = down2;
                    }
                }
                if (isPlayerAttacking) {
                    if (spriteNumber == 1) {
                        image = attackDown1;
                    }
                    if (spriteNumber == 2) {
                        image = attackDown2;
                    }
                }
                break;
            case "downleft":
                if (!isPlayerAttacking) {
                    if (spriteNumber == 1) {
                        image = down1;
                    }
                    if (spriteNumber == 2) {
                        image = down2;
                    }
                }
                if (isPlayerAttacking) {
                    if (spriteNumber == 1) {
                        image = attackDown1;
                    }
                    if (spriteNumber == 2) {
                        image = attackDown2;
                    }
                }
                break;

        }

        if (invincible) {
            //change the opacity + make him red if player is invincible!
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            float[] scales = { 1.5f, 0f, 0f, 1.0f }; // {R, G, B, A}
            float[] offsets = new float[4];
            RescaleOp redTint = new RescaleOp(scales, offsets, null);
            g2.drawImage(image, redTint, tempScreenX, tempScreenY);
            // reset alpha after drawing player so other UI stays same!
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        } else {
            g2.drawImage(image, tempScreenX, tempScreenY, null);
        }
    }
}
