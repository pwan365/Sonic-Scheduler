package algo.CostFunctions;

import algo.Task;
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
    private HashMap<Task, Integer> memo = new HashMap<>();

    public CriticalPath(Graph graph) {
        for(int i = 0; i < graph.getNodeCount(); i++) {
            Node node = graph.getNode(i);
            Task task = (Task) node.getAttribute("Task");
            criticalPath(task,0);

        }
    }
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

        for(Edge e : edges){
            Task child = (Task)e.getNode1().getAttribute("Task");
            int temp = ((Double)node.getAttribute("Weight")).intValue() + criticalPath(child,total + task.getDurationTime());

            max = Math.max(max,temp);
        }

        memo.put(task,max);
        return memo.get(task);
    }

    public HashMap<Task,Integer> getMemo() {
        return memo;
    }
}
