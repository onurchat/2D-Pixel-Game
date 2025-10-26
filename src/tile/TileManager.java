package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;

/**
 * Manages all the tiles on the map.
 */
public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNumber;
    ArrayList<String> fileNames = new ArrayList<>();
    ArrayList<String> collisionStatus = new ArrayList<>();

    // DEBUG
    public boolean drawPath = false;

    /**
     * Constructs a new TileManager.
     * 
     * @param gp     the column value of the event tile
     */
    public TileManager(GamePanel gp) {
        this.gp = gp;

        // READ TILE DATA FILE
        InputStream is = getClass().getResourceAsStream("/res/maps/tiledata3.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        // GETTING TILE NAMES AND COLLISION INFO FROM THE FILE
        String line;

        try {
            while ((line = br.readLine()) != null) {
                fileNames.add(line);
                collisionStatus.add(br.readLine());
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tile = new Tile[fileNames.size()];
        getTileImage();

        // Gett maxWorldCol & Row
        is = getClass().getResourceAsStream("/res/maps/hell03.txt");
        br = new BufferedReader(new InputStreamReader(is));

        try {
            String line2 = br.readLine();
            String[] maxTile = line2.split(" ");

            gp.maxWorldCol = maxTile.length;
            gp.maxWorldRow = maxTile.length;
            mapTileNumber = new int[gp.maxWorldCol][gp.maxWorldRow];

            br.close();
        } catch (IOException e) {
            System.out.println("Exception!");
        }

        loadMap("/res/maps/hell03.txt");
    }

    /**
     * Gets the tile Image and Collision status.
     * 
     */
    public void getTileImage() {

        for (int i = 0; i < fileNames.size(); i++) {
            String fileName;
            boolean collision;

            // get file name first
            fileName = fileNames.get(i);

            // get collision status
            if (collisionStatus.get(i).equals("true")) {
                collision = true;
            } else {
                collision = false;
            }

            setup(i, fileName, collision);
        }
    }

    /**
     * Sets all the different tiles up.
     * 
     * @param index index of the tile to be setup
     * @param imageName name of the file for tile's image
     * @param collision the collision status of the tile
     */
    public void setup(int index, String imageName, boolean collision) {
        UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass()
                .getResourceAsStream("/res/tiles/" + imageName));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads and loads the data in the .txt map file.
     * 
     * @param mapFilePath file path of the .txt map file
     */
    public void loadMap(String mapFilePath) {
        try {
            InputStream is = getClass().getResourceAsStream(mapFilePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int column = 0;
            int row = 0;

            while (column < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
                while (column < gp.maxWorldCol) {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[column]);
                    mapTileNumber[column][row] = num;
                    column++;
                }
                if (column == gp.maxWorldCol) {
                    column = 0;
                    row++;
                }
            }
            br.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Draws the loaded map.
     * 
     * @param g2  Graphics 2D
     */
    public void draw(Graphics2D g2) {

        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = mapTileNumber[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize; // position of tile ON THE MAP
            int worldY = worldRow * gp.tileSize;

            // position of tile ON SCREEN
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            // Improving efficiency by not rendering tiles that are not visible:
            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
                    && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                    && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
                    && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

                g2.drawImage(tile[tileNum].image, screenX, screenY, null);

            }

            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }

        if (drawPath) {
            g2.setColor(new Color(255, 0, 0, 70));

            for (int i = 0; i < gp.pFinder.pathList.size(); i++) {
                int worldX = gp.pFinder.pathList.get(i).col * gp.tileSize; 
                int worldY = gp.pFinder.pathList.get(i).row * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
            }
        }

    }

}
