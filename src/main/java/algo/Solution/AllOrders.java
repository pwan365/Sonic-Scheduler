package algo.Solution;
import algo.Schedule.Task;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;

/**
 * Singleton class to calculate all possible tasks a partial schedule can add at that point in time.
 *
 * @author Wayne Yao, Luxman Jeyarajah
 */
public class AllOrders {
    private static AllOrders allOrders;
    private Graph g;
    private HashMap<HashSet, ArrayList<Task>> memo = new HashMap<>();
    private AllOrders(Graph inputGraph) {
        g = inputGraph;
    }

    /**
     * Thread safe method to initialize a class.
     * @param g The input graph.
     * @return The AllOrders Object.
     */
    public synchronized static AllOrders init(Graph g) {
//        if (allOrders != null) {
//            //Throw an error when LoadBalancer tries to get instantiated more than once.
//            throw new AssertionError("LoadBalancer has already been instantiated.");
//        }
        allOrders = new AllOrders(g);
        return allOrders;
    }

    /**
     * Method to calculate all possible tasks that can be scheduled given a set of tasks scheduled.
     * @param scheduled
     * @return
     */
    public ArrayList<Integer> getOrder(HashSet<Integer> scheduled, int numTask, LinkedList<Integer[]> inEdges) {
        ArrayList<Integer> result = new ArrayList<>();

        for(int i = 0; i < numTask; i++){
            int nodeIndex = i;

            // if input is 0 size, find all root tasks
            if(inEdges.size() == 0 && !scheduled.contains(nodeIndex)){
                result.add(nodeIndex);
                continue;
            }

            boolean flag = true;
            for(int j = 0; j < inEdges.size(); j++){
                int parent = inEdges.get(j)[0];
                if(!scheduled.contains(parent)){
                    flag = false;
                    break;
                }
            }
            if(flag && !scheduled.contains(nodeIndex)){
                result.add(nodeIndex);
            }
        }
        return result;
    }
}
