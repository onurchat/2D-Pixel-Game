package ai;

import java.util.ArrayList;

import main.GamePanel;

public class PathFinder {
    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
    }

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
    public boolean search() {

        while (!goalReached && step < 500) {
            int col = currentNode.col;
            int row = currentNode.row;

            //check current node
            currentNode.checked = true;
            openList.remove(currentNode);

            //open the Up node
            if (row - 1 >= 0) {
                openNode(node[col][row - 1]);
            }
            //open the Left node
            if (col - 1 >= 0) {
                openNode(node[col - 1][row]);
            }
            // open the Down node
            if (row + 1 < gp.maxWorldRow) {
                openNode(node[col][row + 1]);
            }
            // open the Right node
            if (col + 1 < gp.maxWorldCol) {
                openNode(node[col + 1][row]);
            }

            //Find the best node
            int bestNodeIndex = 0;
            int bestNodeFCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                //check if this node's f cost is better
                if (openList.get(i).fCost < bestNodeFCost) {
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).fCost;
                }

                //if F Cost is equal check the G cost
                else if (openList.get(i).fCost == bestNodeFCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            //If there is no node in openList, end the loop
            if (openList.size() == 0) {
                break;
            }

            //After the loop, openList[bestNodeIndex] is the next step
            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }
        return goalReached;
    }
    public void openNode(Node node) {
        if (!node.open && !node.checked && !node.solid) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }
    public void trackThePath() {
        Node current = goalNode;
        while (current != startNode) {
            pathList.add(0, current);
            current = current.parent;
        }
    }
}
