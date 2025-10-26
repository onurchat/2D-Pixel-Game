package main;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The class manages mouse input for the game.
 */
public class MouseHandler extends MouseAdapter {

    GamePanel gp;

    public int mouseX;
    public int mouseY;
    public boolean mousePressed = false;
    double scaleX;
    double scaleY;
    
    /**
     *  Constructs a new MouseHandler linked to the game panel.
     * @param gp the active GamePanel instance
     */
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
            mousePressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //mousePressed = false;
    }

}
