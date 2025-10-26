package ai;

import java.util.ArrayList;
import main.GamePanel;

/**
 * The {@code PathFinder} class implements a path finding algorithm 
 * using A* algorithm.
 */
public class PathFinder {
    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode;
    Node goalNode;
    Node currentNode;
    boolean goalReached = false;
    int step = 0;

    /**
     * Construct a new {@code PathFinder} instance with the given 
     * gamepanel.
     * @param gp the gamepanel that provides the map
     */
    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
    }

    /**
     * Instantiates all {@code Node} objects for the world grid.
     */
    public void instantiateNodes() {
        node = new Node[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;

        //creating a node for every tile on the map
        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            node[col][row] = new Node(col, row);
            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Resets all node data.
     */
    public void resetNodes() {

        int col = 0;
        int row = 0;

        //reset all the node data
        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }

        //reset other settings
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    /**
     * Configures the start and goal nodes.
     * 
     * @param startCol the column value of the starting node
     * @param startRow the row value of the starting node
     * @param goalCol the column index of the goal node
     * @param goalRow the row index of the goal node
     */
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes();

        //Set start and goal nodes
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

            //set solid node
            int tileNum = gp.tileManager.mapTileNumber[col][row];
            if (gp.tileManager.tile[tileNum].collision) {
                node[col][row].solid = true;
            }

            //set cost 
            getCost(node[col][row]);

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }

        }
    }
    
    /**
     * Calculates and assigns cost values for the given node.
     * @param node {@code Node} whose cost values being calculates
     */
    public void getCost(Node node) {
        
        //G Cost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        //H Cost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        //F Cost
        node.fCost = node.gCost + node.hCost;
    }

    /**
     * Executes the pathfinding process to locate a path from the start node to the goal node.
     * 
     * @return {@code true} if a path to the goal was found; {@code false} otherwise
     */
    public boolean search() {

        while (!goalReached && step < 500) {
            int col = currentNode.col;
            int row = currentNode.row;

            //check current node
            currentNode.checked = true;
            openList.remove(currentNode);

            openNeighbor(col, row - 1); // Up
            openNeighbor(col - 1, row); // Left
            openNeighbor(col, row + 1); // Down
            openNeighbor(col + 1, row); // Right

            

            //If there is no node in openList, end the loop
            if (openList.isEmpty()) {
                break;
            }

            //After the loop, openList[bestNodeIndex] is the next step
            currentNode = selectBestNode();

            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }
        return goalReached;
    }

    private Node selectBestNode() {
        Node bestNode = openList.get(0);
        for (Node n : openList) {
            if (n.fCost < bestNode.fCost || (n.fCost == bestNode.fCost 
                && n.gCost < bestNode.gCost)) {
                bestNode = n;
            }
        }
        return bestNode;
    }

    private void openNeighbor(int col, int row) {
        if (col >= 0 && col < gp.maxWorldCol && row >= 0 && row < gp.maxWorldRow) {
            openNode(node[col][row]);
        }
    }

    /**
     * Opens a node for exploration and sets its parent to the current node.
     * @param node the {@code Node} to open
     */
    public void openNode(Node node) {
        if (!node.open && !node.checked && !node.solid) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    /**
     * Reconstructs the final path from goal to the start node by following
     * the refereces.
     */
    public void trackThePath() {
        Node current = goalNode;
        while (current != startNode) {
            pathList.add(0, current);
            current = current.parent;
        }
    }
}
