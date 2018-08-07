/*
 * This class store the information about the start node, end node, start position and end position.
 */
public class Edge {
    private Node node1;
    private Node node2;
    protected int startX;
    protected int startY;
    protected int endX;
    protected int endY;

    public Edge(Node n1, Node n2) {
        this.node1 = n1;
        this.node2 = n2;
        getLine();
    }

    // Return the start node.
    public Node getStart() {
        return node1;
    }

    // Return the end node.
    public Node getEnd() {
        return node2;
    }

    // According the the nodes, initialize position information of this line.
    private void getLine() {
        final int WIDTH = 30;
        final int LENGTH = 30;
        startX = node1.getX() + WIDTH;
        startY = node1.getY() + LENGTH / 2;
        endX = node2.getX();
        endY = node2.getY() + LENGTH / 2;
    }
}
