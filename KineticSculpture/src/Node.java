
/*
 * This class define a node with "id", "x", "y", an ArrayList of input marbles,
 * and an ArrayList of output marbles..
 */
import java.util.ArrayList;

import javafx.scene.shape.Circle;

public class Node {
    private int id;
    private int x;
    private int y;
    protected ArrayList<Circle> input;
    protected ArrayList<Circle> output;

    public Node(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        input = new ArrayList<Circle>();
        output = new ArrayList<Circle>();
    }

    // Return y
    public int getY() {
        return y;
    }

    // Return x
    public int getX() {
        return x;
    }

    public int getID() {
        return id;
    }

    // Define this for convenience to call its subclass' function
    public int get_total() {
        return 0;
    }

    // Define this for convenience to call its subclass' function
    public void process() {

    }
}
