package algo;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * All the initial graph information is stored in IntGraph class. It parses all the information needed
 * from GraphStream.Graph to arrays of integers or LinkedList.
 *
 * @author Luxman Jeyarajah, Wayne Yao
 */
public class IntGraph {

    protected int [] tasks;
    protected int [] weights;
    protected  LinkedList<int[]>[] outEdges;
    protected  LinkedList<int[]>[] inEdges;
    protected int [] inDegrees;

    /**
     * Constructor of IntGraph
     * @param g GraphStream.Graph object
     */
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
        inDegrees = new int [numTasks];
        initEdges(g);
    }

    /**
     * Initialize the edges in the graph.
     * @param g GraphStream.Graph object
     */
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
                int[] outEdge = new int[]{childNode, edgeWeight};
                int[] inEdge = new int[]{i,edgeWeight};
                outEdges[i].add(outEdge);
                inEdges[childNode].add(inEdge);
                
                inDegrees[childNode] += 1;
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
//        for (int i =0;i < inEdges.length; i++) {
//            for (int j =0; j < inEdges[i].size(); j++) {
//                System.out.println("Task");
//                System.out.println(inEdges[i].get(j)[0]);
//                System.out.println(i);
//                System.out.println("Weight");
//                System.out.println(inEdges[i].get(j)[1]);
//            }

//            for (int j =0;j < inDegrees.length; j++) {
//                System.out.println("Task");
//                System.out.println(j);
//                System.out.println(inDegrees[j]);
//            }
//        }
//
//        for (int i=0; i< weights.length; i++) {
//            System.out.println(weights[i]);
//            System.out.println(i);
//        }
    }
}
