
/*
 * This class is a subclass of Node. 
 * Sink node is where marbles stop moving.
 */
import javafx.scene.shape.Circle;

public class SinkNode extends Node {
    protected int total = 0;

    public SinkNode(int id, int x, int y) {
        super(id, x, y);
    }

    // Remove the node which arrives and add one to the variable "total"
    public void process() {
        if (input.size() != 0) {
            Circle marble = input.remove(0);
            total++;
            System.out.println("sink output = " + marble);
        }
    }

    // Return how many marbles have been arrived at this node.
    public int get_total() {
        return total;
    }
}
