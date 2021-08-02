package algo;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.algorithm.NotInitializedException;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Reference
 * https://www.javatips.net/api/gs-algo-master/src/org/graphstream/algorithm/TopologicalSort.java
 */

public class TopologicalSort implements Algorithm {
    public static class GraphHasCycleException extends IllegalStateException {}

    private final static int MARK_UNMARKED = 0;
    private final static int MARK_TEMP = 1;
    private final static int MARK_PERM = 2;

    /**
     * graph to calculate a topological ordering
     */
    private Graph graph;

    /**
     * collection containing sorted nodes after calculation
     */
    private Node[] sortedNodes;

    /**
     * Next index to populated in sortedNodes
     */
    private int index;



    @Override
    public void init(Graph theGraph) {
        sortedNodes = new Node[theGraph.getNodeCount()];
        graph = theGraph;
    }

    @Override
    public void compute() {
        index = sortedNodes.length - 1;
        computeDFS();
    }


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

    private Node getUnmarkedNode(int[] marks) {
        for (int i = 0; i < marks.length; i++) {
            if (marks[i] == MARK_UNMARKED) {
                return graph.getNode(i);
            }
        }
        return null;
    }

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

    public Queue<Task> getSortedTasks() {
        List<Task> taskList = new ArrayList<Task>();
        for (Node n : sortedNodes){
            Task t = new Task(n);
            n.setAttribute("task", t);
            taskList.add(t);
        }
        return new LinkedList<>(taskList);
    }

}
