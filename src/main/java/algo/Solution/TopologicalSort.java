package algo.Solution;

import algo.Schedule.Task;
import org.graphstream.algorithm.Algorithm;
import org.graphstream.algorithm.NotInitializedException;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The helper Class that performs topological sort of a graph and generates a Queue of Tasks
 * @author John Jia
 *
 * Reference:
 * https://www.javatips.net/api/gs-algo-master/src/org/graphstream/algorithm/TopologicalSort.java
 */

public class TopologicalSort implements Algorithm {
    public static class GraphHasCycleException extends IllegalStateException {}

    private final static int MARK_UNMARKED = 0;
    private final static int MARK_TEMP = 1;
    private final static int MARK_PERM = 2;


    // graph to calculate a topological ordering
    private Graph graph;


    // collection containing sorted nodes after calculation
    private Node[] sortedNodes;

    // Next index to populated in sortedNodes
    private int index;


    /**
     * Override method from Algorithm. Initialize the algorithm
     * @param theGraph Input Graph
     */
    @Override
    public void init(Graph theGraph) {
        sortedNodes = new Node[theGraph.getNodeCount()];
        graph = theGraph;
    }

    /**
     * Override method from Algorithm. Compute the algorithm
     */
    @Override
    public void compute() {
        index = sortedNodes.length - 1;
        computeDFS();
    }


    /**
     * Compute a DFS topological ordering.
     */
    private void computeDFS() {
        if (graph == null) {
            throw new NotInitializedException(this);
        }

        int[] marks = new int[graph.getNodeCount()];
        Node n;

        while ((n = getUnmarkedNode(marks)) != null) {
            visitNode(n, marks);
        }
    }

    /**
     * Get the first unmarked node.
     * @param marks list of marks
     * @return An unmarked node
     */
    private Node getUnmarkedNode(int[] marks) {
        for (int i = 0; i < marks.length; i++) {
            if (marks[i] == MARK_UNMARKED) {
                return graph.getNode(i);
            }
        }
        return null;
    }

    /**
     * Visit Nodes.
     * @param node Node that needs to be visited
     * @param marks list of marks
     */
    private void visitNode(Node node, int[] marks) {
        int mark = marks[node.getIndex()];

        if (mark == MARK_TEMP) {
            throw new GraphHasCycleException();
        } else if (mark == MARK_UNMARKED) {
            marks[node.getIndex()] = MARK_TEMP;

            for (Edge edge : node.leavingEdges().collect(Collectors.toList())) {
                visitNode(edge.getOpposite(node), marks);
            }

            marks[node.getIndex()] = MARK_PERM;

            sortedNodes[index] = node;
            index--;
        }
    }

    /**
     *
     * Generate a queue of sorted tasks.
     * @return A Queue of tasks that has has been sorted using sorted Nodes.
     */
    public Queue<Task> getSortedTasks() {
        List<Task> taskList = new ArrayList<>();
        for (Node n : sortedNodes){
            System.out.println(n.getId());
            System.out.println(n.getIndex());
            Task t = new Task(n);
            n.setAttribute("Task", t);
            taskList.add(t);
        }
        return new LinkedList<>(taskList);
    }

}
