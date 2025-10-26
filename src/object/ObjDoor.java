package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

/**
 * Represents a door object in the game.
 */
public class ObjDoor extends SuperObject {

    GamePanel gp;
    
    /**
     * Constructs a new door object.
     * 
     * @param gp the Gamepanel instance currently active
     */
    public ObjDoor(GamePanel gp) {
        this.gp = gp;
        name = "Door";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/objects/door.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }

}
