package algo;

import io.InputReader;
import org.graphstream.algorithm.TopologicalSortKahn;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ValidScheduler {

    public Queue<Node> topologicalorder(){

        System.setProperty("org.graphstream.ui", "swing");


        InputReader reader = new InputReader("input.dot");
        Graph g = reader.read();
        g.display();
        TopologicalSortKahn tp = new TopologicalSortKahn();
        tp.init(g);
        tp.compute();
        List<Node> nodeList = tp.getSortedNodes();

        Queue<Node> nodeQueue = new LinkedList<>(nodeList);
        return nodeQueue;

    }



}
