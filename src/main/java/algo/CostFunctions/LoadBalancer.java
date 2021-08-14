package algo.CostFunctions;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
/**
 * Singleton class to calculate a lower bound estimate of a partial schedule. Sums the cost of all nodes, plus the
 * idle time of the current schedule.
 * @author Luxman Jeyarajah
 */
public class LoadBalancer {
    private static LoadBalancer loadBalancer;
    private int weight = 0;
    private int numProcessors;

    /**
     * Private constructor to prevent the class from being initialized more than once.
     * @param g
     */
    private LoadBalancer(Graph g,int nProcessors) {
        sumWeights(g);
        numProcessors = nProcessors;
    }

    /**
     * Thread safe method to instantiate a LoadBalancer instance, will calculate the sum of all weights.
     * @param g The input graph.
     * @return LoadBalancer
     */
    public synchronized static LoadBalancer init(Graph g, int numProcessors) {
        if (loadBalancer != null) {
            //Throw an error when LoadBalancer tries to get instantiated more than once.
            throw new AssertionError("LoadBalancer has already been instantiated.");
        }
        loadBalancer = new LoadBalancer(g,numProcessors);
        return loadBalancer;
    }

    /**
     * Calculates the LoadBalance given the communication cost of a schedule.
     * @param commCost Current communication cost of the schedule.
     * @return LoadBalance of the partial schedule.
     */
    public int calculateLB(int commCost) {
        int lb = (int) Math.ceil((weight + commCost) / numProcessors);
        return lb;
    }

    /**
     * Sums all the nodes in a graph and stores them in the field weight for calculations of Load Balancing on
     * schedules.
     * @param g The input graph.
     */
    private void sumWeights(Graph g) {
        for (int i = 0; i < g.getNodeCount(); i++) {
            Node node = g.getNode(i);
            int nodeWeight = ((Double) node.getAttribute("Weight")).intValue();
            weight += nodeWeight;
        }
    }
}
