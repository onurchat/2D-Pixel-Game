package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_Reaper extends Entity{

    public NPC_Reaper(GamePanel gp) {
        super(gp);

        direction = "down";
        speed = 1;

        getImage();
        setDialogue();
    }

    public void getImage() {

        up1 = setup("/res/npc/reaper_up1", gp.tileSize, gp.tileSize);
        up2 = setup("/res/npc/reaper_up2", gp.tileSize, gp.tileSize);
        down1 = setup("/res/npc/reaper_down1", gp.tileSize, gp.tileSize);
        down2 = setup("/res/npc/reaper_down2", gp.tileSize, gp.tileSize);
        left1 = setup("/res/npc/reaper_left1", gp.tileSize, gp.tileSize);
        left2 = setup("/res/npc/reaper_left2", gp.tileSize, gp.tileSize);
        right1 = setup("/res/npc/reaper_right1", gp.tileSize, gp.tileSize);
        right2 = setup("/res/npc/reaper_right2", gp.tileSize, gp.tileSize);
    }

    public void setDialogue() {
        dialogues[0] = "It's always hot down \nhere you know...";
        dialogues[1] = "You think you can \nsurvive with \nthese temperatures??";
        dialogues[2] = "Just like you, I \nwanted to survive \nas long as possible here \nback then...";
        dialogues[3] = "But then I learned, \ndeath is inevitable...";
    }

    public void setAction() {

        actionCounter++;
        if (actionCounter >= 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1; //i = random number 1-100

            if (i <= 25) {
                direction = "up";
            }
            if (i > 25 && i <= 50) {
                direction = "down";
            }
            if (i > 50 && i <= 75) {
                direction = "left";
            }
            if (i > 75 && i <= 100) {
                direction = "right";
            }
            actionCounter = 0;
        }
    }

    public void speak() {
        super.speak();
    }
}
