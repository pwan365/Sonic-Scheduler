import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import algo.ValidScheduler;

public class Main  {
    public static void main(String[] args) {
        ValidScheduler v = new ValidScheduler();
        Queue<Node> nodeQueue = v.topologicalorder();
        for (Node n : nodeQueue){
            System.out.println(n.getId());
        }
    }
}