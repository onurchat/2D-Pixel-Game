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

/**
 * The class serves as a base class for all Entities in the game. 
 * It defines position movement hitboxes and more.
 */
public class Entity {

    GamePanel gp;

    public String name;
    public int worldX;

    public int worldY;

    public int speed;
    public BufferedImage up1;

    public BufferedImage up2;

    public BufferedImage down1;

    public BufferedImage down2;

    public BufferedImage left1;

    public BufferedImage left2;

    public BufferedImage right1;

    public BufferedImage right2;

    public BufferedImage attackUp1;

    public BufferedImage attackUp2;

    public BufferedImage attackDown1;

    public BufferedImage attackDown2;

    public BufferedImage attackLeft1;

    public BufferedImage attackLeft2;

    public BufferedImage attackRight1;

    public BufferedImage attackRight2;

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
    String[] dialogues = new String[20];
    int dialogueIndex = 0;
    public int type; //0 = player, 1= NPC, 2= monster
    public boolean isPlayerAttacking = false;
    public boolean alive = true;
    public boolean dying = false;
    int dyingCounter = 0;
    boolean hpBarOn = false;
    public int shotAvailableCounter = 0;

    public boolean onPath = false;
    
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

    /**
     * Handles entity dialogue.
     */
    public void speak() {
        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        if (dialogueIndex == 0) {
            gp.soundEffect.stop();
            gp.playSoundEffect(10);
        } else if (dialogueIndex == 1) {
            gp.soundEffect.stop();
            gp.playSoundEffect(11);
        } else if (dialogueIndex == 2) {
            gp.soundEffect.stop();
            gp.playSoundEffect(12);
        } else if (dialogueIndex == 3) {
            gp.soundEffect.stop();
            gp.playSoundEffect(13);
        }
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
            default:
                break;
        }
    }

    /**
     * Checks for collisions between this entity and other collisions. If the
     * collisions comes from an enemy player takes damage.
     */
    public void checkCollision() {
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
    }
    
    /**
     * Updates the entity's position, animation and state.
     */
    public void update() { //identical for all entities

        if (type == 2 && shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }

        setAction();
        checkCollision();


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
                default:
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

    /**
     * Renders the entity on the game screen.
     * 
     * @param g2 the {@code Graphics2D} contex used for rendering
     */
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
                    break;
                default:
                    break;
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

    /**
     * Plays the entity's dying animation and removes it from the game.
     * 
     * @param g2 the {@code Graphics2D} contex used for fading animation.
     */
    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;
        int animationInterval = 5;

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
            //gp.aSetter.spawnMonster(47, 50);
        }
    }

    public void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    /**
     * Loads and scales image from the given path.
     * 
     * @param imagePath the path to the image file
     * @param width the image width
     * @param height the image height
     * @return the loaded and scaled image
     */
    public BufferedImage setup(String imagePath, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    /**
     * Directs the entity to move toward a target position.
     * 
     * @param goalCol the goal tile column value
     * @param goalRow the goal tile row value
     */
    public void searchPath(int goalCol, int goalRow) {

        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) /  gp.tileSize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);

        //if a path is found:
        if (gp.pFinder.search()) {

            //Next world x and world y
            int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;

            //Entity's solid area positions
            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                direction = "up";
            } else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                direction = "down";
            } else if (enTopY >= nextY && enBottomY < nextY + gp.tileSize) {
                //either left or right
                if (enLeftX > nextX) {
                    direction = "left";
                } else if (enLeftX < nextX) {
                    direction = "right";
                }
            } else if (enTopY > nextY && enLeftX > nextX) {
                //either up or left
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            } else if (enTopY > nextY && enLeftX < nextX) {
                // either up or right
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            } else if (enTopY < nextY && enLeftX > nextX) {
                // down or left
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            } else if (enTopY < nextY && enLeftX < nextX) {
                // down or right
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            }

            //If the goal is reached, stop the search:
            int nextCol = gp.pFinder.pathList.get(0).col;
            int nextRow = gp.pFinder.pathList.get(0).row;
            if (nextCol == goalCol && nextRow == goalRow) {
                onPath = false;
            }
        }
    }

    //PARTICLES BLUEPRINT METHODS

    /**
     * Returns the color of particles generated by this entity.
     * 
     * @return the color of particle
     */
    public Color getParticleColor() {
        Color color = null;
        return color;
    }

    /**
     * Returns the size of particles generated by this entity.
     * 
     * @return the size of particle
     */
    public int getParticleSize() {
        int size = 0;
        return size;
    }
    
    /**
     * Returns the speed of particles generated by this entity.
     * 
     * @return the speed of particle
     */
    public int getParticleSpeed() {
        int speed = 0;
        return speed;
    }
    
    /**
     * Returns the maximum lifespan of particles.
     * 
     * @return the particle lifespan
     */
    public int getParticleMaxLife() {
        int maxLife = 20;
        return maxLife;
    }

    /**
     * Generates and spawns four particles around target entity.
     * 
     * @param generator the entity that creates the particles
     * @param target the target entity around which particles appear
     */
    public void generateParticle(Entity generator, Entity target) {

        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        Particle p1 = new Particle(gp, target, color, size, speed, maxLife, -2, -1);
        gp.particleList.add(p1);
        Particle p2 = new Particle(gp, target, color, size, speed, maxLife, 2, -1);
        gp.particleList.add(p2);
        Particle p3 = new Particle(gp, target, color, size, speed, maxLife, -2, 1);
        gp.particleList.add(p3);
        Particle p4 = new Particle(gp, target, color, size, speed, maxLife, 2, 1);
        gp.particleList.add(p4);
    }
}
