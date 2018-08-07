/*
 * This class is to read in a file and process the data in the file into specific data structure.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ProcessFile {
    public HashSet<Node> NodeSet = new HashSet<Node>();
    public Graph g = new Graph();
    public int delay;
    public ArrayList<String> color = new ArrayList<String>();
    public int total_circle = 0;
    private ArrayList<String> node_line = new ArrayList<>();
    private ArrayList<String> edge_line = new ArrayList<>();

    private final int Circle_Radius = 10;

    public ProcessFile(String fileName) {
        Scanner input = null;
        try {
            input = new Scanner(new File(fileName));
        } catch (Exception ex) {
            System.out.println("Cannot open the file");
            System.exit(1);
        }

        while (input.hasNextLine()) {
            String line = input.nextLine();
            //Process "delay" information
            if (line.split(":")[0].trim().toLowerCase().equals("delay")) {
                processDelay(line);
                continue;
            } 
            //Process "color" information
            else if (line.split(":")[0].trim().toLowerCase()
                    .equals("input")) {
                processInput(line);
                continue;
            } 
            //Store the lines containing node information into an ArrayList.
            else if (isNumber(line.split(":")[0].trim())) {
                node_line.add(line);
                continue;
            } 
            //Store the lines containing edge information into an ArrayList.
            else {
                edge_line.add(line);
            }
        }

        //Process node information.
        for (String str : node_line) {
            processNode(str);
        }

        //Process edge information
        for (String str : edge_line) {
            processPath(str);
        }

        //Initialize circles in the input node.
        Node n = find_inputNode();
        for (String i : color) {
            for (int j = 0; j < number_circle(); j++) {
                Circle marble = new Circle();
                marble.setFill(Color.valueOf(i));
                marble.setRadius(Circle_Radius);
                n.output.add(marble);
                total_circle++;
            }
        }

        input.close();
    }

    // Find sink node.
    public Node findSink() {
        for (Node n : NodeSet) {
            if (n instanceof SinkNode) {
                return n;
            }
        }
        return null;
    }

    // Add information about "delay" into variable "delay"
    private void processDelay(String str) {
        delay = Integer.valueOf(str.split(":")[1].trim());
    }

    // Store color information into a string array.
    private void processInput(String str) {
        String[] strs = str.split(":")[1].split(",");
        for (String s : strs) {
            color.add(s.trim().toUpperCase());
        }
    }

    // Initialize each node and store them into an ArrayList of Node.
    private void processNode(String str) {
        String[] strs = str.split(":");
        int id = Integer.valueOf(strs[0].trim());
        String[] strings = strs[1].split(",");
        int num = strings[1].indexOf("(");
        int x = Integer
                .valueOf(strings[1].substring(num + 1, strings[1].length()));
        int y = Integer
                .valueOf(strings[2].trim().substring(0,
                        strings[2].trim().length() - 1));

        if (strings[0].trim().toLowerCase().equals("input")) {
            ArrayList<Circle> inputList = new ArrayList<>();
            NodeSet.add(new InputNode(id, x, y, inputList));
        } else if (strings[0].trim().toLowerCase().equals("passthrough")) {
            NodeSet.add(new ThingNode(id, x, y));
        }

        else if (strings[0].trim().toLowerCase().equals("sink")) {
            NodeSet.add(new SinkNode(id, x, y));
        }
    }

    // Initialize each edge and store them into a created graph.
    private void processPath(String str) {
        if (str.equals("")) {
            return;
        }

        int id1 = Integer.valueOf(str.split("->")[0].trim());
        int id2 = Integer.valueOf(str.split("->")[1].trim());
        Node node1 = null;
        Node node2 = null;

        // Find node according to its id
        for (Node node : NodeSet) {
            if (node.getID() == id1) {
                node1 = node;
            } else if (node.getID() == id2) {
                node2 = node;
            }
        }

        g.add(new Edge(node1, node2));
    }

    // If the string can be transform into a number, return true. Otherwise,
    // return false.
    private boolean isNumber(String str) {
        int ASCII_0 = 48;
        int ASCII_9 = 57;
        if (str.equals("")) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            int chr = str.charAt(i);
            if (chr < ASCII_0 || chr > ASCII_9) {
                return false;
            }
        }
        return true;
    }

    // Return the input node.
    private Node find_inputNode() {
        Node node = null;
        for (Node n : NodeSet) {
            if (n instanceof InputNode) {
                node = n;
            }
        }
        return node;
    }

    // Calculate how many circles per one color should be store in input node.
    private int number_circle() {
        int total = 0;
        Node node = find_inputNode();
        for (Edge e : g.get()) {
            if (e.getStart() == node) {
                total++;
            }
        }
        return total;
    }
}
