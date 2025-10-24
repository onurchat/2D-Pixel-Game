package main;

import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class MouseHandler extends MouseAdapter{

    GamePanel gp;

    public int mouseX;
    public int mouseY;
    public boolean mousePressed = false;
    double scaleX;
    double scaleY;

    public MouseHandler(GamePanel gp) {
        this.gp = gp;
        scaleX = (double) gp.screenWidth / gp.screenWidth2;
        scaleY = (double) gp.screenHeight / gp.screenHeight2;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gp.gameState == gp.playState) {
            mouseX = (int) (e.getX() * scaleX);
            mouseY = (int) (e.getY() * scaleY);
            System.out.println(mouseX + " " + mouseY);
            mousePressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //mousePressed = false;
    }

}
