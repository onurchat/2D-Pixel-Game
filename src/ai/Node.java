package ai;

/** The {@code Node} class represents a cell in a grid based
 * pathfinding system.
 */
public class Node {
    Node parent;
    public int col;
    public int row;
    int gCost;
    int hCost;
    int fCost;
    boolean solid;
    boolean open;
    boolean checked;

    /**
     * Constructs a new {@code Node} at the given grid 
     * position.
     * @param col the column index of the node 
     * @param row the row index of the node
     */
    public Node(int col, int row) {
        this.col = col;
        this.row = row;
    }

}
