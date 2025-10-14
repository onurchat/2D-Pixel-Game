package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public Player (GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        
        //screen X and Y are middle of screen always for player.
        screenX = (gp.screenWidth) / 2 - (gp.tileSize / 2); 
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);; 

        solidArea = new Rectangle(); //solidArea is collision area for player
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 29;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues () {
        worldX = gp.tileSize * 23; //23rd tile
        worldY = gp.tileSize * 21; //21st tile
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/res/player/up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/res/player/up2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/res/player/down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/res/player/down2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/res/player/left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/res/player/left2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/res/player/right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/res/player/right2.png"));



        } catch (IOException e) {
            e.printStackTrace();
        }
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

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

    }

}
