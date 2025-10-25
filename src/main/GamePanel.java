package main;

import javax.swing.JPanel;

import ai.PathFinder;
import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 32;
    public final int maxScreenRow = 18;
    public final int screenWidth = tileSize * maxScreenCol; // 960 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // WORLD SETTINGS
    public int maxWorldCol;
    public int maxWorldRow;

    // FOR FULLSCREEN
    public int screenWidth2 = screenWidth;
    public int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

    // FPS
    int fps = 60;

    // SYSTEM
    public TileManager tileManager = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public MouseHandler mouseH = new MouseHandler(this);
    Sound music = new Sound();
    public Sound soundEffect = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Thread gameThread;
    public PathFinder pFinder = new PathFinder(this);
    public int monsterTimer = 0;
    public int monsterMultiplyer = 1;

    // ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[10];
    // public Entity npc[] = new Entity[10];
    // public Entity monster[] = new Entity[50];
    public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> monster = new ArrayList<>();
    public ArrayList<Entity> npc = new ArrayList<>();

    // GAME STATE
    public int gameState = 3;
    public final int titleState = 3;
    public final int playState = 1;
    public final int pauseState = 0;
    public final int dialougeState = 2;
    public final int optionsState = 4;
    public final int gameOverState = 5;

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(mouseH);
        this.setFocusable(true);
    }

    public void setupGame() {
        setFullScreen();
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();

        // playMusic(0);
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();
    }

    public void restart() {
        player.setDefaultValues();
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        ui.commandNum = 0;
        ui.playTime = 0;
        player.eyesKilled = 0;
        projectileList.clear();
        player.hasKey = 0;
        player.maxLife = 6;
    }

    public void setFullScreen() {
        // GET LOCAL SCREEN DEVICE
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        // GET FULL SCREEN WIDTH AND HEIGHT
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / fps;
        double delta = 0;
        double lastTime = System.nanoTime();
        double currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            if (Main.gameFocused) {
                delta += (currentTime - lastTime) / drawInterval;
            }
            timer += currentTime - lastTime;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                // repaint();
                drawToTempScreen(); // draw everything to the buffered image
                drawToScreen(); // draw the buffered image to screen
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }

        }

    }

    public void update() {

        if (gameState == playState) {
            if (monsterTimer < 300) {
                monsterTimer++;
            } else if (monsterTimer >= 300) {
                Random random = new Random();
                int randomCol = random.nextInt(13) + 35;
                int randomRow = random.nextInt(10) + 48;
                aSetter.spawnMonster(randomCol, randomRow);      
                monsterTimer = 0;
                monsterMultiplyer++;
                System.out.println(monsterMultiplyer);
            }

            player.update(); // PLAYER

            for (int i = 0; i < npc.size(); i++) { // NPCs
                if (npc.get(i) != null) {
                    npc.get(i).update();
                }
            }
            for (int i = 0; i < monster.size(); i++) { // Monsters
                if (monster.get(i) != null) {
                    if (monster.get(i).alive && !monster.get(i).dying) {
                        monster.get(i).update();
                    }
                    if (!monster.get(i).alive) {
                        monster.remove(i);
                    }
                }
            }
            for (int i = 0; i < projectileList.size(); i++) { // projectiles
                if (projectileList.get(i) != null) {
                    if (projectileList.get(i).alive) {
                        projectileList.get(i).update();
                    }
                    if (!projectileList.get(i).alive) {
                        projectileList.remove(i);
                    }
                }
            }

        }
        if (gameState == pauseState) {

        }
    }

    public void drawToTempScreen() {
        // DEBUG
        long drawStart = 0;
        if (keyH.checkDrawTime) {
            drawStart = System.nanoTime();
        }

        // TITLE SCREEN
        if (gameState == titleState) {
            ui.draw(g2);
        } else {
            tileManager.draw(g2); // ORDER MATTERS!! tiles in background, player front!

            // DRAW OBJECTS
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }

            // DRAW NPC
            for (int i = 0; i < npc.size(); i++) {
                if (npc.get(i) != null) {
                    npc.get(i).draw(g2);
                }
            }

            // DRAW MONSTERS
            for (int i = 0; i < monster.size(); i++) {
                if (monster.get(i) != null) {
                    monster.get(i).draw(g2);
                }
            }

            // DRAW PROJECTILES
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    projectileList.get(i).draw(g2);
                }
            }

            player.draw(g2); // drawing player

            ui.draw(g2); // UI ON TOP
        }

        // DEBUG
        if (keyH.checkDrawTime) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, 10, 400);
            g2.drawString("FPS: " + fps, 10, 450);
            g2.drawString("Sprite Number: " + this.player.spriteCounter, 10, 500);
        }
    }

    public void drawToScreen() {
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }

    // CAN BE DELETED
    public void paintComponent(Graphics g) { // g represents the canvas

        super.paintComponent(g); // calls the paintComponent of superclass

        Graphics2D g2 = (Graphics2D) g; // casting g to g2 (Graphics is superclass, Graphics2D is subclass)

        // DEBUG
        long drawStart = 0;
        if (keyH.checkDrawTime) {
            drawStart = System.nanoTime();
        }

        // TITLE SCREEN
        if (gameState == titleState) {
            ui.draw(g2);
        } else {
            tileManager.draw(g2); // ORDER MATTERS!! tiles in background, player front!

            // DRAW OBJECTS
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }

            // DRAW NPC
            for (int i = 0; i < npc.size(); i++) {
                if (npc.get(i) != null) {
                    npc.get(i).draw(g2);
                }
            }

            // DRAW MONSTERS
            for (int i = 0; i < monster.size(); i++) {
                if (monster.get(i) != null) {
                    monster.get(i).draw(g2);
                }
            }

            // DRAW PROJECTILES
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    projectileList.get(i).draw(g2);
                }
            }

            player.draw(g2); // drawing player

            ui.draw(g2); // UI ON TOP
        }

        // DEBUG
        if (keyH.checkDrawTime) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, 10, 400);
            g2.drawString("FPS: " + fps, 10, 450);
            g2.drawString("Sprite Number: " + this.player.spriteCounter, 10, 500);
        }

        g2.dispose();
    }

    public void playMusic(int i) {
        music.setFile(i); 
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSoundEffect(int i) {
        soundEffect.setFile(i);
        soundEffect.play();
    }
}
