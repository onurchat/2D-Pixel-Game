package main;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;



public class Main {
    public static JFrame window;
    public static boolean gameFocused = true;

    public static void main(String[] args) {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Game");
        window.setUndecorated(true);

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

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
