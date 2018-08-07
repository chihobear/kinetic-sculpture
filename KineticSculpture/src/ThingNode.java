
/*
 * This class is a subclass of Node
 */
import javafx.scene.shape.Circle;

public class ThingNode extends Node {
    public ThingNode(int id, int x, int y) {
        super(id, x, y);
    }

    // Remove a marble in the input and add it into the output.
    public void process() {
        if (this.input.size() != 0) {
            Circle c = this.input.remove(0);
            this.output.add(c);
        }
    }
}
