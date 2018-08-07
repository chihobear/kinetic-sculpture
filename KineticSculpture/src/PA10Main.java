
/*
 *This program generates a text field for users' input which should be a file name.
 *Then the program process this file into a visual Kinetic Sculpture.
 *example of input file:
 *delay: 1
   input: RED, BLUE
   0: input, (10,10)
   1: passthrough, (60,20)
   2: passthrough, (70,80)
   3: sink, (140,100)
   0 -> 1
   0 -> 2
   1 -> 3
   2 -> 3
 * 
 * This main class mainly generates a visual sculpture on the screen according to the data in the file.
 */

import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PA10Main extends Application {
    private static final int background_width = 800;
    private static final int background_heighth = 600;

    private static final int rect_size = 30;
    private static final int rect_stroke_width = 2;

    private HashMap<Node, Rectangle> Node_Rect = new HashMap<>();
    private HashMap<Edge, Line> Edge_Line = new HashMap<>();

    private Button conduct;
    private TextField command;
    private Group root;
    
    private String fileName;
    private ProcessFile pf;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Text field to take in garden command
        command = new TextField();

        // Border pane will contain canvas for drawing and text area underneath
        BorderPane p = new BorderPane();

        // Input Area + nextMove Button
        Label cmd = new Label("Type Command in TextField");
        HBox hb = new HBox(3);

        // Input + Text Output
        VBox vb = new VBox(2);

        setupNodes(hb, cmd, vb);
        setupEventHandlers();

        // Show the group.
        p.setBottom(vb);
        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        stage.show();
    }

    // Create a new window on the screen.
    private void newStage() {
        Stage secondaryStage = new Stage();
        root = new Group();

        // ===== set up the scene with a hardcoded sculpture
        Rectangle background = new Rectangle(0, 0, background_width,
                background_heighth);
        background.setFill(Color.WHITE);
        root.getChildren().add(background);

        // Create rectangles.
        for (Node n : pf.NodeSet) {
            Rectangle rect = new Rectangle(n.getX(), n.getY(), rect_size,
                    rect_size);
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(rect_stroke_width);
            rect.setFill(Color.WHITE);
            Node_Rect.put(n, rect);
            root.getChildren().add(rect);
        }

        // Create lines.
        for (Edge e : pf.g.get()) {
            Line l = new Line(e.startX, e.startY, e.endX, e.endY);
            Edge_Line.put(e, l);
            root.getChildren()
                    .add(l);
        }
        // Connect the group into the scene and show the window.
        secondaryStage.setTitle("PA10-KineticSculpture");
        secondaryStage.setScene(new Scene(root));
        secondaryStage.show();

        playSculpture(pf, Node_Rect, Edge_Line);
    }

    // Method that will cause all of the sculpture nodes to process
    // their inputs and set up path transitions for all of their output edges.
    private void playSculpture(ProcessFile pf,
            HashMap<Node, Rectangle> Node_Rect, HashMap<Edge, Line> Edge_Line) {

        // Store all the circles in the tree.
        ArrayList<Circle> marbles = new ArrayList<Circle>();
        PauseTransition wait = new PauseTransition(Duration.seconds(1));
        wait.setOnFinished((ActionEvent e) -> {

            // Remove these circles out of the tree.
            for (Circle c : marbles) {
                root.getChildren().remove(c);
            }
            marbles.clear();

            // If not all marbles arrives the final node.
            if (pf.findSink().get_total() < pf.total_circle) {

                for (Node n : pf.NodeSet) {
                    n.process();
                }

                ArrayList<Node> nodes = getNode(pf);
                ArrayList<Edge> edges = getEdge(pf, nodes);

                // Process all the edge that should have a marble on it.
                for (Edge edge : edges) {
                    Line path = Edge_Line.get(edge);

                    // If there is no marble should be on the line, just do the
                    // next iteration.
                    if (getCircle(edge) == null) {
                        continue;
                    }

                    Circle marble = marbleClone(getCircle(edge));
                    marbles.add(marble);
                    root.getChildren().add(marble);
                    PathTransition trans = new PathTransition(
                            Duration.seconds(pf.delay), path, marble);
                    trans.play();

                    passCircle(edge);
                }
                wait.playFromStart();
            } else {
                System.out.println("Nothing more to process");
                wait.stop();
            }
        });

        // Now that the PauseTransition thread is setup, get it going.
        wait.play();
    }

    // Get all nodes that should transport a marble and return an array list of
    // those nodes.
    private ArrayList<Node> getNode(ProcessFile pf) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node n : pf.NodeSet) {
            if (n.output.size() != 0) {
                nodes.add(n);
            }
        }
        return nodes;
    }

    // Get all edges that should transport a marble and return an array list of
    // those edges.
    private ArrayList<Edge> getEdge(ProcessFile pf, ArrayList<Node> nodes) {
        ArrayList<Edge> edges = new ArrayList<Edge>();
        for (Edge e : pf.g.get()) {
            if (judgeContainNode(nodes, e.getStart())) {
                edges.add(e);
            }
        }
        return edges;
    }

    // Judge if an ArrayList of nodes contain a specific node.
    // Return true if containing, false otherwise.
    private boolean judgeContainNode(ArrayList<Node> nodes, Node node) {
        for (Node n : nodes) {
            if (n == node) {
                return true;
            }
        }
        return false;
    }

    // Return a marble that should be transport ont the edge.
    private Circle getCircle(Edge edge) {
        if (edge.getStart().output.size() == 0) {
            return null;
        }
        return edge.getStart().output.get(0);
    }

    // Clone a marble.
    private Circle marbleClone(Circle toClone) {
        Circle clone = new Circle();
        clone.setFill(toClone.getFill());
        clone.setRadius(toClone.getRadius());
        return clone;
    }

    // Update marbles information in nodes when a marble has been transport.
    private void passCircle(Edge edge) {
        Circle marble = edge.getStart().output.remove(0);
        edge.getEnd().input.add(marble);
    }

    /*
     * Sets up the TextField, label, and button to be
     * in the bottom
     */
    private void setupNodes(HBox hb, Label cmd, VBox vb) {

        conduct = new Button("Conduct");

        hb.setSpacing(400);
        hb.getChildren().add(cmd);
        hb.getChildren().add(conduct);

        vb.getChildren().add(hb);
        vb.getChildren().add(command);
    }
    
    // Set up an event for the button click
    private void setupEventHandlers() {
        conduct.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                fileName = command.getText().trim();
                pf = new ProcessFile(fileName);
                newStage();
            }
        });
    }

}