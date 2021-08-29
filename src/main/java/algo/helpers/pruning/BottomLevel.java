package algo.helpers.pruning;


import java.util.LinkedList;

/**
 * Bottom level class that calculates the bottom level for all tasks in a graph and stores it in an integer array.
 *
 * @author Luxman Jeyarajah
 */
public class BottomLevel {
    private static int [] bottomLevel;


    /**
     * Prevent instantiation of the class.
     */
    private BottomLevel() {

    }

    /**
     * Initiates the calculation of the bottom level, loops through all tasks, if its bottom level has not been
     * calculated already through dynamic searching of previous tasks, calculates the bottom level for that node
     * and its children.
     * @param numTasks Number of tasks in the graph.
     * @param weights The weights of each node.
     * @param outEdges The outEdges(children) of each node.
     */
    public static void initBottomLevel(int numTasks, int[] weights, LinkedList<int[]>[] outEdges) {
        bottomLevel = new int[numTasks];
        for (int i = 0; i < numTasks; i++) {
            bottomLevel[i] = -1;
        }
        for (int i = 0; i < numTasks; i++) {
            int weight = weights[i];
            calculateBottomLevel(i, weight,outEdges,weights);
        }
    }

    /**
     * Dynamically search through and calculate the bottom level by searching the subtrees of the node and selecting
     * the greatest one. Runs in O(n) (bounded by the number of nodes in the graph.)
     * @param task The task(node) we wish to calculate the bottom level for.
     * @param total The current total weights calculated during this subtree.
     * @param outEdge A list of children nodes for all nodes in the graph.
     * @param weights The weights of all nodes in the graph.
     * @return The bottom level of a given task.
     */
    private static int calculateBottomLevel(int task, int total,LinkedList<int[]>[] outEdge,int [] weights) {
        //If bottomLevel of this task is calculated, don't recalculate it.
        if (bottomLevel[task] != -1) {
            return bottomLevel[task];
        }
        LinkedList<int[]> outEdges = outEdge[task];
        if (outEdges.size() == 0) {
            bottomLevel[task] = total;
            return total;
        }

        int max = 0;
        // Traverse through edges and pick the subtree with the maximum weight to form the critical path for this node.
        for (int i = 0; i < outEdges.size(); i++) {
            int child = outEdges.get(i)[0];
            int weight = weights[child];
            int temp = total + calculateBottomLevel(child, weight,outEdge,weights);

            max = Math.max(max, temp);
        }

        bottomLevel[task] = max;
        return max;
    }

    /**
     * Used for pruning purposes and thus requires the process start time of a task that will be scheduled as well
     * as the cost to schedule this task on the processor. Returns an underestimate of the schedule finish time.
     * @param task The task we wish to retrieve the bottom level.
     * @param cost The cost to schedule the task on a given processor.
     * @param processorStart The start time of the processor.
     * @return Bottom Level cost function, underestimate of the finishing time of the graph.
     */
    public static int pruneBLevel(int task, int cost, int processorStart) {
        return (bottomLevel[task] + cost + processorStart);
    }

    /**
     * Returns the Bottom level of a task, used to calculate the DLS of a candidate Task.
     * @param task The task we wish to retrieve the bottom level for.
     * @return The bottom level of the task.
     */
    public static int returnBLevel(int task) {
        return bottomLevel[task];
    }
}
