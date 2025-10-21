package main;

import java.awt.Rectangle;

public class EventHandler {
    GamePanel gp;
    Rectangle eventRect;
    int eventRectDefaultX;
    int eventRectDefaultY;
    int dPitCounter = 0;
    int healCounter = 0;
    int healTriggerCounter = 0;

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

    public void checkEvent() {
        //COUNTERS
        if (dPitCounter <= 120) {
            dPitCounter++;
        }
        if (healCounter <= 600) {
            dPitCounter++;
        }
        if (healTriggerCounter <= 60) {
            dPitCounter++;
        }

        //EVENTS
        if (dPitCounter >= 120 && hit(32, 34, "any")) {
            damagePit(gp.playState);
            dPitCounter = 0;
        }
        if (dPitCounter >= 120 && hit(45, 43, "any")) {
            damagePit(gp.playState);
            dPitCounter = 0;
        }
        if (dPitCounter >= 120 && hit(77, 45, "any")) {
            damagePit(gp.playState);
            dPitCounter = 0;
        }
        if (hit(32, 26, "any") || hit(31, 27, "any")) {
            if (healTriggerCounter >= 60) {
                if (healCounter >= 600) {
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
    public void damagePit(int gameState) {
        gp.gameState = gameState;
        //gp.ui.currentDialogue = "You fell into a pit!";
        gp.ui.showMessage("You fell into a pit!");
        gp.player.life--;
    }
    public void healing(int gameState) {
        gp.gameState = gameState;
        // gp.ui.currentDialogue = "You healed";
        gp.ui.showMessage("The healing cauldron healed you!");
        gp.player.life++;
    }
}
