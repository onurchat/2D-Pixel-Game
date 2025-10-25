package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_Cursor extends SuperObject{
    GamePanel gp;

    public OBJ_Cursor(GamePanel gp) {
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
