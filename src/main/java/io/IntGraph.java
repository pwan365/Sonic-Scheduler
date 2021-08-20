package io;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class IntGraph {

    private int [] tasks;
    private int [] weights;
    private  LinkedList<Integer[]>[] outEdges;
    private  LinkedList<Integer[]>[] inEdges;

    public IntGraph(Graph g) {
        int numTasks = g.getNodeCount();
        tasks = new int [numTasks];
        weights = new int [numTasks];


        outEdges = new LinkedList[numTasks];
        inEdges = new LinkedList[numTasks];
        initEdges(g);
    }

    private void initEdges(Graph g) {
        for(int i = 0; i < tasks.length; i++) {
            int currentTask = i;
            tasks[i] = i;
            Node currentNode = g.getNode(i);
            int weight = ((Double)currentNode.getAttribute("Weight")).intValue();
            weights[i] = weight;
            List<Edge> edges = currentNode.leavingEdges().collect(Collectors.toList());
            for(Edge e : edges){
                int childNode = e.getNode1().getIndex();
                int edgeWeight = ((Double) e.getAttribute("Weight")).intValue();
                Integer [] outEdge = new Integer[]{childNode, edgeWeight};
                Integer [] inEdge = new Integer[]{i,edgeWeight};
                outEdges[i] = new LinkedList<>();
                inEdges[i]= new LinkedList<>();
                outEdges[i].add(outEdge);
                inEdges[i].add(inEdge);
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
        for (int i =0;i < inEdges.length; i++) {
            for (int j =0; j < inEdges[i].size(); j++) {
                System.out.println("Task");
                System.out.println(inEdges[i].get(i)[0]);
                System.out.println("Weight");
                System.out.println(inEdges[i].get(i)[1]);
            }
        }
    }
}
