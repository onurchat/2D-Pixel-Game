package main;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import object.OBJ_Cursor;



public class Main {
    public static JFrame window;
    public static boolean gameFocused = true;
    public static OBJ_Cursor cursor;

    public static void main(String[] args) {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Depths of Hell");
        window.setUndecorated(true);

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        //CUSTOM CURSOR
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        cursor = new OBJ_Cursor(gamePanel);
        BufferedImage cursorImage = cursor.image;
        UtilityTool uTool = new UtilityTool();
        uTool.scaleImage(cursorImage, 32, 32);
        Point hotspot = new Point(0, 0);
        Cursor customCursor = toolkit.createCustomCursor(cursorImage, hotspot, "Custom cursor");
        gamePanel.setCursor(customCursor);
        cursor = new OBJ_Cursor(gamePanel);

        
        window.addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowGainedFocus(WindowEvent e) {
                gameFocused = true;
                System.out.println("focused");
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gameFocused = false;
                System.out.println("NOT focused");
            }
            
        });

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);   

        

        gamePanel.setupGame();
        gamePanel.startGameThread();
    }


}
