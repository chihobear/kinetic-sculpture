
/*
 * This class is a subclass of Node
 * Input node is where marbles start moving.
 */

import java.util.ArrayList;

import javafx.scene.shape.Circle;

public class InputNode extends Node {
    private ArrayList<Circle> inputList;

    public InputNode(int id, int x, int y, ArrayList<Circle> inputList) {
        super(id, x, y);
        this.inputList = inputList;
    }

    // Remove a marble from "inputList" and add it to output.
    public void process() {
        if (inputList.size() != 0) {
            output.add(inputList.remove(0));
        }
    }
}
