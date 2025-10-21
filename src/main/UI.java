package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import object.OBJ_Heart;
import object.OBJ_Key;
import object.SuperObject;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font font_dogica;
    BufferedImage heartFull;
    BufferedImage heartHalf;
    BufferedImage heartBlank;
    BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    int msgCounter = 0;
    public String currentDialogue = "";
    public int commandNum = 0;

    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");

    public UI(GamePanel gp) {
        this.gp = gp;

        try {
            InputStream is = getClass().getResourceAsStream("/res/font/dogica.otf");
            font_dogica = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        OBJ_Key key = new OBJ_Key(gp);
        keyImage = key.image;

        //CREATE HUD OBJECT
        SuperObject heart = new OBJ_Heart(gp);
        heartFull = heart.image;
        heartHalf = heart.image2;
        heartBlank = heart.image3;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
        msgCounter = 0;
    }

    public void draw(Graphics2D g2) {
        //DONT CREATE INSTANCE IN DRAW METHODS!!!
        //BECAUSE IT GETS CALLED 60 TIMES PER SECOND
        this.g2 = g2;
        g2.setFont(font_dogica);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        g2.setColor(Color.white);
        if (gp.player.hasKey > 0) {
            g2.drawImage(keyImage, gp.tileSize / 2, gp.tileSize * 2, gp.tileSize, gp.tileSize, null);
            g2.drawString(" " + gp.player.hasKey, 74, 137);
        }

        //TIME
        if (gp.gameState == gp.playState) {
            playTime += (double) 1 / 60;
        }

        if (gp.gameState != gp.titleState) {
            g2.drawString("Time:" + dFormat.format(playTime), gp.tileSize * 11, 65);
            drawPlayerLife();
        }

        //message
        if (messageOn) {
            g2.setFont(g2.getFont().deriveFont(15F));
            int x = getXForCenteredText(message);
            g2.drawString(message, x, gp.tileSize * 10);

            msgCounter++;

            if (msgCounter > 120) {
                msgCounter = 0;
                messageOn = false;
            }
        }

        //TITLE STATE
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        } 

        if (gp.gameState == gp.playState) {
            //do stuff later
        }

        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }

        if (gp.gameState == gp.dialougeState) {
            drawDialougeScreen();
        }
    }

    public void drawPlayerLife() {

        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        //DRAW MAX LIFE
        while (i < gp.player.maxLife / 2) {
            g2.drawImage(heartBlank, x, y, null);
            i++;
            x += gp.tileSize + 5;
        }
        x = gp.tileSize / 2;
        y = gp.tileSize / 2;
        i = 0;

        //DRAW CURRENT LIFE
        while (i < gp.player.life) {
            g2.drawImage(heartHalf, x, y, null);  
            i++;
            if (i < gp.player.life) {
                g2.drawImage(heartFull, x, y, null);
            }
            i++;
            x += gp.tileSize + 5;
        }
    }

    public void drawTitleScreen() {

        g2.setColor(new Color(148, 59, 59));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        //TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 45F));
        String text = "Depths of Hell";
        int x = getXForCenteredText(text);
        int y = gp.tileSize * 3;

        //SHADOW
        g2.setColor(Color.black);
        g2.drawString(text, x + 5, y + 5);
        //MAIN COLOR
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        //IMAGE
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/skel1.png"));
            x = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
            y += gp.tileSize * 2;
            g2.drawImage(image, x, y, gp.tileSize * 2, gp.tileSize * 2, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22F));
        text = "NEW GAME";
        x = getXForCenteredText(text);
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "QUIT";
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x - gp.tileSize, y);
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

    public void drawDialougeScreen() {
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 1;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;
        
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(30, 0, 0, 240); //last number = opacity
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 30, 30); //30 = how round it is

        //OUTLINE
        c = new Color(255, 204, 204);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 20, 20);

    }

}
