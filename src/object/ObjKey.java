package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

/**
 * Represents a key object in the game.
 */
public class ObjKey extends SuperObject {

    GamePanel gp;

    /**
     * Constructs a new key object.
     * 
     * @param gp the Gamepanel instance currently active
     */
    public ObjKey(GamePanel gp) {
        this.gp = gp;
        name = "Key";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/objects/key.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
