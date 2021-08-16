package algo.CostFunctions;

import algo.Schedule.Processor;
import algo.Schedule.Schedule;
import algo.Schedule.Task;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton class that dynamically assigns the critical path for all nodes in a tree. (Top-Down Recursive method.)
 * Run Time Complexity: O(n) on the number of nodes in the graph.
 */
public class CriticalPath {
    private static CriticalPath criticalPath;
    //Store results of the Critical paths for tasks in a Hashmap for O(1) retrieval time, can be changed to an array.
    private HashMap<Task, Integer> memo = new HashMap<>();
//    private Integer[] memo1;

    /**
     * Private constructor to initiate the Critical Path class, also calculates the critical path for all nodes.
     * @param graph
     */
    private CriticalPath(Graph graph) {
//        memo1 = new Integer[graph.getNodeCount()];
        for(int i = 0; i < graph.getNodeCount(); i++) {

            Node node = graph.getNode(i);
            Task task = (Task) node.getAttribute("Task");
            criticalPath(task,0);
        }
    }

    /**
     * Initialize the CriticalPath object, will throw an error if already initialized.
     * @param g Input graph from user.
     * @return CriticalPath object.
     */
    public static synchronized  CriticalPath init(Graph g) {
        if (criticalPath != null) {
            throw new AssertionError("CriticalPath class has already been instantiated");
        }
        criticalPath = new CriticalPath(g);
        return criticalPath;
    }

    /**
     * Private method to dynamically calculate the critical path of each node in the graph.
     * @param task The task to calculate the critical path.
     * @param total The current total weight of nodes in the recursive search.
     * @return The critical path of the task.
     */
    private int criticalPath(Task task, int total) {
        if (memo.containsKey(task)) {
            return memo.get(task);
        }

        Node node = task.getNode();
        List<Edge> edges = node.leavingEdges().collect(Collectors.toList());
        if (edges.isEmpty()) {
            memo.put(task,task.getDurationTime());
            return task.getDurationTime();
        }

        int max = 0;
        // Traverse through edges and pick the subtree with the maximum weight to form the critical path for this node.
        for(Edge e : edges){
            Task child = (Task)e.getNode1().getAttribute("Task");
            int temp = ((Double)node.getAttribute("Weight")).intValue() + criticalPath(child,total + task.getDurationTime());

            max = Math.max(max,temp);
        }

        memo.put(task,max);
        return memo.get(task);
    }

    /**
     * Returns the value of the Critical Path given a task.
     * @param task
     * @return The critical path for the task.
     */
    public int getCriticalPath(Task task) {
        int path = memo.get(task);
        return path;
    }

    public static void  clearObject() {
        criticalPath = null;
    }
}
