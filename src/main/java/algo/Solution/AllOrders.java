package algo.Solution;
import algo.Schedule.Task;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
        if (allOrders != null) {
            //Throw an error when LoadBalancer tries to get instantiated more than once.
            throw new AssertionError("LoadBalancer has already been instantiated.");
        }
        allOrders = new AllOrders(g);
        return allOrders;
    }

    /**
     * Method to calculate all possible tasks that can be scheduled given a set of tasks scheduled.
     * @param scheduled
     * @return
     */
    public ArrayList<Task> getOrder(HashSet<Task> scheduled) {
        if (memo.containsKey(scheduled)) {
            return memo.get(scheduled);
        }
        ArrayList<Task> result = new ArrayList<>();

        for(int i = 0; i < g.getNodeCount(); i++){
            Node node = g.getNode(i);
            Task task = (Task)node.getAttribute("Task");
            List<Edge> parentEdges = task.getParentEdgeList();

            // if input is 0 size, find all root tasks
            if(parentEdges.size() == 0 && !scheduled.contains(task)){
                result.add(task);
                continue;
            }

            boolean flag = true;
            for(Edge edge: parentEdges){
                Node parent = edge.getNode0();
                Task parentTask = (Task)parent.getAttribute("Task");
                if(!scheduled.contains(parentTask)){
                    flag = false;
                    break;
                }
            }
            if(flag && !scheduled.contains(task)){
                result.add(task);
            }
        }
        memo.put(scheduled,result);
        return result;
    }
}
