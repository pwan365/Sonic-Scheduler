package algo.Solution;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class IntGraph {

    protected int [] tasks;
    protected int [] weights;
    protected  LinkedList<Integer[]>[] outEdges;
    protected  LinkedList<Integer[]>[] inEdges;

    public IntGraph(Graph g) {
        int numTasks = g.getNodeCount();
        tasks = new int [numTasks];
        weights = new int [numTasks];
        outEdges = new LinkedList[numTasks];
        inEdges = new LinkedList[numTasks];
        for(int i = 0; i < numTasks; i++){
            inEdges[i] = new LinkedList<>();
            outEdges[i] = new LinkedList<>();
        }
        initEdges(g);
    }
    private void initEdges(Graph g) {
        for(int i = 0; i < tasks.length; i++) {
            tasks[i] = i;
            Node currentNode = g.getNode(i);
            int weight = ((Double)currentNode.getAttribute("Weight")).intValue();
            weights[i] = weight;
            List<Edge> edges = currentNode.leavingEdges().collect(Collectors.toList());
            for(Edge e : edges){
                int childNode = e.getNode1().getIndex();
                int edgeWeight = ((Double) e.getAttribute("Weight")).intValue();
                Integer[] outEdge = new Integer[]{childNode, edgeWeight};
                Integer[] inEdge = new Integer[]{i,edgeWeight};
                outEdges[i].add(outEdge);
                inEdges[childNode].add(inEdge);
            }
        }
    }

    public void test() {
        for (int i=0; i < tasks.length; i++) {
            System.out.println("Task");
            System.out.println(tasks[i]);
            System.out.println("Weight");
            System.out.println(weights[i]);
        }
    }

    public void testEdge() {
        for (int i =0;i < outEdges.length; i++) {
            for (int j =0; j < outEdges[i].size(); j++) {
                System.out.println("Task");
                System.out.println(outEdges[i].get(j)[0]);
                System.out.println(i);
                System.out.println("Weight");
                System.out.println(outEdges[i].get(j)[1]);
            }
        }
    }
}
