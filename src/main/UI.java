package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import object.OBJ_Key;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font font;
    BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    int msgCounter = 0;

    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");

    public UI(GamePanel gp) {
        this.gp = gp;
        font = new Font("Arial", Font.PLAIN, 40);
        OBJ_Key key = new OBJ_Key(gp);
        keyImage = key.image;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2) {
        //DONT CREATE INSTANCE IN DRAW METHODS!!!
        //BECAUSE IT GETS CALLED 60 TIMES PER SECOND
        this.g2 = g2;
        g2.setFont(font);
        g2.setColor(Color.white);
        if (gp.player.hasKey > 0) {
            g2.drawImage(keyImage, gp.tileSize / 2, gp.tileSize / 2, gp.tileSize, gp.tileSize, null);
            g2.drawString(" " + gp.player.hasKey, 74, 65);
        }

        //TIME
        if (gp.gameState == gp.playState) {
            playTime += (double) 1 / 60;
        }

        g2.drawString("Time:" + dFormat.format(playTime), gp.tileSize * 11, 65);

        //message
        if (messageOn) {
            g2.setFont(g2.getFont().deriveFont(30F));
            g2.drawString(message, gp.tileSize / 2, gp.tileSize * 5);

            msgCounter++;

            if (msgCounter > 120) {
                msgCounter = 0;
                messageOn = false;
            }
        }

        if (gp.gameState == gp.playState) {

        }

        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }


    }

    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int x = getXForCenteredText(text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);
    }

    public int getXForCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }

}
