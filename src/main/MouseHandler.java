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

    public MouseHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gp.gameState == gp.playState) {
            mouseX = e.getX();
            mouseY = e.getY();
            mousePressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //mousePressed = false;
    }

}
