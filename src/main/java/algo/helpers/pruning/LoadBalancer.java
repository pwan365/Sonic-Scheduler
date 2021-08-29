package algo.helpers.pruning;

/**
 * Class that calculates the Load Balance given the current idle time of a schedule and the cost to place the next task
 * on the schedule.
 *
 * @author Luxman Jeyarajah.
 */
public class LoadBalancer {

    //Total weight of all nodes.
    private static int graphWeight ;


    /**
     * Prevent instantiation of the class.
     */
    private LoadBalancer() {

    }

    /**
     * Initializes the Load Balancer by creating a field that holds the weight of all the nodes in the graph.
     * @param weights An int array(length is the length of the graph) indicating the weight of each node in the graph.
     */
    public static void initLoadBalancer(int[] weights) {
        graphWeight = 0;
        for (int i = 0; i < weights.length; i++) {
            graphWeight += weights[i];
        }
    }

    /**
     * Returns the load balance cost given the current idle time of the schedule as well as the cost to schedule
     * the next upcoming task.
     * @param idle
     * @param currentCost
     * @param numProcessors
     * @return An integer value representing the earliest time that the current schedule can finish in.
     */
    public static int calculateLoadBalance(int idle, int currentCost, int numProcessors) {
        return (int) Math.ceil((graphWeight + idle + currentCost) / numProcessors);
    }
}
