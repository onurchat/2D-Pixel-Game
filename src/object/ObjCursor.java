package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

/**
 * Represents a cursor object in the game.
 */
public class ObjCursor extends SuperObject {
    GamePanel gp;

    /**
     * Constructs a new Cursor object.
     * 
     * @param gp the Gamepanel instance currently active
     */
    public ObjCursor(GamePanel gp) {
        this.gp = gp;
        name = "Cursor";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/objects/cursor.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}
