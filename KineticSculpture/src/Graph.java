
/*
 * This class stores information of all the edges.
 */

import java.util.ArrayList;

public class Graph {
    private ArrayList<Edge> allEdges;

    public Graph() {
        allEdges = new ArrayList<>();
    }

    // Add a edge into this graph.
    public void add(Edge edge) {
        allEdges.add(edge);
    }

    // Return an ArrayList of edges.
    public ArrayList<Edge> get() {
        return this.allEdges;
    }
}
