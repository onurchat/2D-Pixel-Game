package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

/**
 * Represents a chest object in the game.
 */
public class ObjChest extends SuperObject {

    GamePanel gp;

    /**
     * Constructs a new chest object.
     * 
     * @param gp the Gamepanel instance currently active
     */
    public ObjChest(GamePanel gp) {
        this.gp = gp;
        name = "Chest";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/objects/chest.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }

}
