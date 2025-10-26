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
import object.ObjHearth;
import object.ObjKey;
import object.SuperObject;

/**
 * The class handles all on-screen user interface elements in the game.
 */
public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font fontDogica;
    BufferedImage heartFull;
    BufferedImage heartHalf;
    BufferedImage heartBlank;
    BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    int msgCounter = 0;
    public String currentDialogue = "";
    public int commandNum = 0;
    int subState = 0;

    int minutes = 0;
    int seconds = 0;
    String timeString;
    Image deadEye;

    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");

    /**
     * Constructs a new UI ibject and loads required images.
     * 
     * @param gp the current gamepanel instance
     */
    public UI(GamePanel gp) {
        this.gp = gp;

        try {
            InputStream is = getClass().getResourceAsStream("/res/font/dogica.otf");
            fontDogica = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjKey key = new ObjKey(gp);
        keyImage = key.image;

        // CREATE HUD OBJECT
        SuperObject heart = new ObjHearth(gp);
        heartFull = heart.image;
        heartHalf = heart.image2;
        heartBlank = heart.image3;
        try {
            deadEye = ImageIO.read(getClass().getResourceAsStream("/res/monster/deadEye.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a text message to the player.
     * 
     * @param text the message text to display
     */
    public void showMessage(String text) {
        message = text;
        messageOn = true;
        msgCounter = 0;
    }

    /**
     * Draws the entire UI.
     * 
     * @param g2 the Graphics2D object used for drawing
     */
    public void draw(Graphics2D g2) {
        // DONT CREATE INSTANCE IN DRAW METHODS!!!
        // BECAUSE IT GETS CALLED 60 TIMES PER SECOND
        this.g2 = g2;
        g2.setFont(fontDogica);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        g2.setColor(Color.white);
        if (gp.player.hasKey > 0) {
            g2.drawImage(keyImage, gp.tileSize,
                gp.tileSize * 2 + 10, gp.tileSize, gp.tileSize, null);
            g2.drawString(" " + gp.player.hasKey, 84, 137);
        }

        // TIME
        if (gp.gameState == gp.playState) {
            playTime += (double) 1 / 60;
            minutes = (int) playTime / 60;
            seconds = (int) playTime % 60;
            timeString = String.format("%d:%02d", minutes, seconds);
        }

        if (gp.gameState != gp.titleState) {
            g2.drawString(timeString, gp.screenWidth - 140, 65);
            g2.drawImage(deadEye, gp.screenWidth - 140, 90, gp.tileSize, gp.tileSize, null);
            g2.drawString(" " + gp.player.eyesKilled, gp.screenWidth - 100, 120);
            drawPlayerLife();
        }

        // message
        if (messageOn) {
            g2.setColor(new Color(0, 0, 0));
            g2.setFont(g2.getFont().deriveFont(15F));
            int x = getXForCenteredText(message);
            g2.drawString(message, x + 2, gp.tileSize * 14 + 2);
            g2.setColor(new Color(255, 255, 255));
            g2.drawString(message, x, gp.tileSize * 14);

            msgCounter++;

            if (msgCounter > 120) {
                msgCounter = 0;
                messageOn = false;
            }
        }

        // TITLE STATE
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }

        if (gp.gameState == gp.playState) {
            // do stuff later
        }

        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }

        if (gp.gameState == gp.dialougeState) {
            drawDialougeScreen();
        }

        // OPTIONS STATE
        if (gp.gameState == gp.optionsState) {
            drawOptionsScreen();
        }

        // GAME OVER STATE
        if (gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }
    }

    /**
     * Draws the players life bar.
     */
    public void drawPlayerLife() {

        int x = gp.tileSize;
        int y = gp.tileSize;
        int i = 0;

        // DRAW MAX LIFE
        while (i < gp.player.maxLife / 2) {
            g2.drawImage(heartBlank, x, y, null);
            i++;
            x += gp.tileSize + 5;
        }
        x = gp.tileSize;
        y = gp.tileSize;
        i = 0;

        // DRAW CURRENT LIFE
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

    /**
     * Draws the title screen.
     */
    public void drawTitleScreen() {

        g2.setColor(new Color(148, 59, 59));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 45F));
        String text = "Depths of Hell";
        int x = getXForCenteredText(text);
        int y = gp.tileSize * 3;

        // SHADOW
        g2.setColor(Color.black);
        g2.drawString(text, x + 5, y + 5);
        // MAIN COLOR
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        // IMAGE
        try {
            BufferedImage image = ImageIO.read(getClass()
                .getResourceAsStream("/res/tiles/skel1.png"));
            x = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
            y += gp.tileSize * 2;
            g2.drawImage(image, x, y, gp.tileSize * 2, gp.tileSize * 2, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // MENU
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

    /**
     * Draws the pause screen.
     */
    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int x = getXForCenteredText(text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);
    }

    /**
     * Returns the X coordinate to center the text.
     * 
     * @param text the text string to center.
     * @return The X coordinate for centered text
     */
    public int getXForCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }

    /**
     * Draw Options screen.
     */
    public void drawOptionsScreen() {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(15f));

        // SUB WINDOW
        int frameWidth = gp.tileSize * 10;
        int frameHeight = gp.tileSize * 12;
        //int frameX = gp.screenWidth2 / 3 - gp.tileSize - 15;
        // int frameY = gp.tileSize * 3;

        //int frameX = (gp.screenWidth - frameWidth) / 2 + (gp.screenWidth2 - gp.screenWidth) / 2;
        int frameX = getXForCenteredText("AAAAAAAAAAAAAAAAAAAAAAAAA");
        int frameY = (gp.screenHeight - frameHeight) / 2;

        drawSubWindow(frameX - gp.tileSize, frameY - gp.tileSize, frameWidth, frameHeight);

        switch (subState) {
            case 0:
                optionsTop(frameX, frameY);
                break;
            case 1:
                optionsControls(frameX, frameY);
                break;
            case 2:
                break;
            default:
                break;
        }

        gp.keyH.enterPressed = false;
    }

    /**
     * Draws the top level options menu.
     * 
     * @param frameX X position of the frame
     * @param frameY y position of the Frame
     */
    public void optionsTop(int frameX, int frameY) {
        int textX;
        int textY;

        // TITLE
        String text = "Options";
        textX = getXForCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        // FULL SCREEN ON-OFF
        textX = frameX + gp.tileSize / 2;
        textY += gp.tileSize * 2;

        // MUSIC
        textY += gp.tileSize;
        g2.drawString("Music", textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 20, textY);
        }

        // SE
        textY += gp.tileSize;
        g2.drawString("SE", textX, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX - 20, textY);
        }

        // CONTROL
        textY += gp.tileSize;
        g2.drawString("Controls", textX, textY);
        if (commandNum == 2) {
            g2.drawString(">", textX - 20, textY);
            if (gp.keyH.enterPressed) {
                subState = 1;
                commandNum = 0;
            }
        }

        // END GAME
        textY += gp.tileSize;
        g2.drawString("Close Game", textX, textY);
        if (commandNum == 3) {
            g2.drawString(">", textX - 20, textY);
            if (gp.keyH.enterPressed) {
                System.exit(0);
            }
        }

        // BACK
        textY += gp.tileSize * 2;
        g2.drawString("Back", textX, textY);
        if (commandNum == 4) {
            g2.drawString(">", textX - 20, textY);
            if (gp.keyH.enterPressed) {
                gp.music.play();
                commandNum = 0;
                subState = 0;
                gp.gameState = gp.playState;
            }
        }

        // FULLSCREEN CHECKBOX
        textX = frameX + gp.tileSize * 6;
        textY = frameY + gp.tileSize * 3 - 20;

        // MUSIC VOLUME
        textY += gp.tileSize;
        g2.drawRect(textX - gp.tileSize, textY, 120, 20);
        int volumeWidth = 24 * gp.music.volumeScale;
        g2.fillRect(textX - gp.tileSize, textY, volumeWidth, 20);

        // SE
        textY += gp.tileSize;
        g2.drawRect(textX - gp.tileSize, textY, 120, 20);
        volumeWidth = 24 * gp.soundEffect.volumeScale;
        g2.fillRect(textX - gp.tileSize, textY, volumeWidth, 20);
    }

    /**
     * Draw Dialogue Screen.
     */
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

    /**
     * Draw a semi transplant subwindow for menu/dialogue.
     * 
     * @param x X position of the window
     * @param y Y position of the window
     * @param width Width of the window
     * @param height height of the window
     */
    public void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(30, 0, 0, 240); // last number = opacity
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 30, 30); // 30 = how round it is

        // OUTLINE
        c = new Color(255, 204, 204);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 20, 20);

    }

    /**
     * Draw game over screen.
     */
    public void drawGameOverScreen() {
        g2.setColor(new Color(10, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth2, gp.screenHeight2);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80f));

        // SHADOW
        text = "GAME OVER";
        g2.setColor(Color.black);
        x = getXForCenteredText(text) + 5;
        y = gp.tileSize * 6 + 5;
        g2.drawString(text, x, y);

        // MAIN
        text = "GAME OVER";
        g2.setColor(Color.white);
        x = getXForCenteredText(text);
        y = gp.tileSize * 6;
        g2.drawString(text, x, y);

        // Back to Title Screen
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20));
        text = "Back to Title Screen";
        x = getXForCenteredText(text);
        y += gp.tileSize * 8;
        g2.drawString(text, x, y);
        g2.drawString(">", x - 40, y);
    }   

    /**
     * Deaws the controls options menu.
     * 
     * @param frameX X position of the frame.
     * @param frameY Y position of the frame.
     */
    public void optionsControls(int frameX, int frameY) {
        int textX;
        int textY;

        // TITLE
        String text = "Controls";
        textX = getXForCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        textX = frameX - 20;
        textY += gp.tileSize;

        g2.drawString("Move", textX, textY);
        textY += gp.tileSize;
        g2.drawString("Sword Attack", textX, textY);
        textY += gp.tileSize;
        g2.drawString("Shoot", textX, textY);
        textY += gp.tileSize;
        g2.drawString("Pause", textX, textY);
        textY += gp.tileSize;
        g2.drawString("Options", textX, textY);
        textY += gp.tileSize;
        g2.drawString("Debug", textX, textY);
        textY += gp.tileSize;

        textX = frameX + gp.tileSize * 5;
        textY = frameY + gp.tileSize * 2;

        g2.drawString("WASD", textX, textY);
        textY += gp.tileSize;
        g2.drawString("E", textX, textY);
        textY += gp.tileSize;
        g2.drawString("Left Click", textX, textY);
        textY += gp.tileSize;
        g2.drawString("Space", textX, textY);
        textY += gp.tileSize;
        g2.drawString("ESC", textX, textY);
        textY += gp.tileSize;
        g2.drawString("T", textX, textY);
        textY += gp.tileSize;

        // BACK
        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize * 9;

        g2.drawString("Back", textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 20, textY);
            if (gp.keyH.enterPressed) {
                subState = 0;
            }
        }
    }

}
