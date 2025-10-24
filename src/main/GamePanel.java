package main;
import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable{
    //SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; //48x48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //768 pixels
    public final int screenHeight = tileSize * maxScreenRow; //576 pixels

    //WORLD SETTINGS
    public int maxWorldCol;
    public int maxWorldRow;

    //FPS
    int fps = 60;

    //SYSTEM
    TileManager tileManager = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public MouseHandler mouseH = new MouseHandler(this);
    Sound music = new Sound();
    Sound soundEffect = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Thread gameThread;

    //ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[10];
    //public Entity npc[] = new Entity[10];
    //public Entity monster[] = new Entity[50];
    public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> monster = new ArrayList<>();
    public ArrayList<Entity> npc = new ArrayList<>();

    //GAME STATE
    public int gameState = 3;
    public final int titleState = 3;
    public final int playState = 1;
    public final int pauseState = 0;
    public final int dialougeState = 2;

    public GamePanel () {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(mouseH);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();

        //playMusic(0);
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

            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
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

            player.update(); //PLAYER

            for (int i = 0; i < npc.size(); i++) { //NPCs
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
            for (int i = 0; i < projectileList.size(); i++) { // Monsters
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

    public void paintComponent(Graphics g) { //g represents the canvas

        super.paintComponent(g); //calls the paintComponent of superclass

        Graphics2D g2 = (Graphics2D) g; //casting g to g2 (Graphics is superclass, Graphics2D is subclass)

        //DEBUG
        long drawStart = 0;
        if (keyH.checkDrawTime) {
            drawStart = System.nanoTime();
        }

        //TITLE SCREEN
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

            //DRAW MONSTERS
            for (int i = 0; i < monster.size(); i++) {
                if (monster.get(i) != null) {
                    monster.get(i).draw(g2);
                }
            }

            //DRAW PROJECTILES
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    projectileList.get(i).draw(g2);
                }
            }

            player.draw(g2); // drawing player

            ui.draw(g2); // UI ON TOP
        }

        //DEBUG
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
        music.setVolume(-10.0f);
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
