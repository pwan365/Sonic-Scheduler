package algo.CostFunctions;

import algo.Task;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.HashMap;

public class LoadBalancer {
    private static HashMap<Integer, Integer> memo = new HashMap<>();
    private static int weight = 0;

    public static int calculateLB(int numProcessors, int commCost) {
        int lb = (int) Math.ceil((weight + commCost) / numProcessors);
        return lb;
    }

    public static void sumWeights(Graph g) {
        for (int i = 0; i < g.getNodeCount(); i++) {
            Node node = g.getNode(i);
            int nodeWeight = ((Double) node.getAttribute("Weight")).intValue();
            weight += nodeWeight;
        }
    }
}
