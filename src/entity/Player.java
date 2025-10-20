package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

public class Player extends Entity{

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int hasKey = 0;

    public Player (GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
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
        worldX = gp.tileSize * 45; //23rd tile
        worldY = gp.tileSize * 45; //21st tile
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {

        up1 = setup("up1");
        up2 = setup("up2");
        down1 = setup("down1");
        down2 = setup("down2");
        left1 = setup("left1");
        left2 = setup("left2");
        right1 = setup("right1");
        right2 = setup("right2");
    }

    public BufferedImage setup(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/res/player/" + imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return image;
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
                        gp.ui.showMessage("opened a door!");
                    } else {
                        gp.ui.showMessage("you need a key...");
                    }
                    break;
            }
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

        g2.drawImage(image, screenX, screenY, null);

    }

}
