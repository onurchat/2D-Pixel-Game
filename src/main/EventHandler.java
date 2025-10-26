package main;

import java.awt.Rectangle;

/**
 * Handles ingame events.
 */
public class EventHandler {
    GamePanel gp;
    Rectangle eventRect;
    int eventRectDefaultX;
    int eventRectDefaultY;
    int dPitCounter = 120;
    int healCounter = 0;
    int healTriggerCounter = 0;

    /**
     * Construts an event handler for managing events.
     * @param gp the gamepanel instance that manages the game
     */
    public EventHandler(GamePanel gp) {
        this.gp = gp;
        eventRect = new Rectangle();
        eventRect.x = 23;
        eventRect.y = 23;
        eventRect.width = 2;
        eventRect.height = 2;
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
    }

    /**
     * Checks and processes all ingame events.
     */
    public void checkEvent() {
        //COUNTERS
        if (dPitCounter <= 120) {
            dPitCounter++;
        }
        if (healCounter <= 600) {
            healCounter++;
        }
        if (healTriggerCounter <= 60) {
            healTriggerCounter++;
        }

        //EVENTS
        if (dPitCounter >= 120 && hit(32, 34, "any")) {
            damagePit(gp.playState);
            dPitCounter = 0;
            gp.player.invincible = true;
        }
        if (dPitCounter >= 120 && hit(45, 43, "any")) {
            damagePit(gp.playState);
            dPitCounter = 0;
            gp.player.invincible = true;
        }
        if (dPitCounter >= 120 && hit(77, 45, "any")) {
            damagePit(gp.playState);
            dPitCounter = 0;
            gp.player.invincible = true;
        }
        if (hit(25, 30, "any") || hit(24, 29, "any") || hit(26, 29, "any") || hit(25, 28, "any")) {
            if (healTriggerCounter >= 60) {
                if (gp.player.life == gp.player.maxLife) {
                    gp.ui.showMessage("You already have full health!");
                } else if (healCounter >= 600) {
                    healing(gp.playState);
                    healCounter = 0;    
                } else {
                    System.out.println(healCounter);
                    gp.ui.showMessage("Wait a while to heal again!");
                }
                healTriggerCounter = 0;
            }
        }
    }

    /**
     * Checks whether the player is currently collidign with a given event.
     * 
     * @param eventCol the column value of the event tile
     * @param eventRow the row value of the event tile
     * @param reqDirection the required player direction to triger an event
     * @return true if the player intersects the specified event tile
     */
    public boolean hit(int eventCol, int eventRow, String reqDirection) {
        boolean hit = false;
        //getting players current solid area positions
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        //getting eventRect's solid area positions
        eventRect.x = eventCol * gp.tileSize + eventRect.x;
        eventRect.y = eventRow * gp.tileSize + eventRect.y;

        //check if collide
        if (gp.player.solidArea.intersects(eventRect)) {
            if (gp.player.direction == reqDirection || reqDirection.contentEquals("any")) {
                hit = true;
            }
        }

        //resetting
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidaAreaDefaultY;
        eventRect.x = eventRectDefaultX;
        eventRect.y = eventRectDefaultY;

        return hit;
    }

    /**
     * Trigers pit damage.
     * 
     * @param gameState the current game state
     */
    public void damagePit(int gameState) {
        gp.gameState = gameState;
        //gp.ui.currentDialogue = "You fell into a pit!";
        gp.ui.showMessage("You ran into spikes!");
        gp.player.life--;
        gp.playSoundEffect(4);
    }

    /**
     * Restores the players health when they interact with a healing cauldron.
     * 
     * @param gameState the current gamestate
     */
    public void healing(int gameState) {
        gp.gameState = gameState;
        // gp.ui.currentDialogue = "You healed";
        gp.ui.showMessage("The healing cauldron healed you!");
        if (gp.player.maxLife - gp.player.life >= 3) {
            gp.player.life += 3;
        } else if (gp.player.maxLife - gp.player.life >= 2) {
            gp.player.life += 2;    
        } else {
            gp.player.life += 1;
        }
    }
}
