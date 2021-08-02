package io;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSink;
import org.graphstream.stream.file.FileSinkDOT;

import java.io.IOException;
import java.util.Random;

public class RandomGraphGenerator {

    private static int MAX_NUM_OF_TASKS = 10;
    private static int MAX_NODE_WEIGHT = 10;
    private static int MAX_EDGE_WEIGHT = 10;


    public static void main(String[] args) {
        Random randomProducer = new Random();

        int numOfTasks = randomProducer.nextInt(MAX_NUM_OF_TASKS);
        String outputName = "textingGraph.dot";
        Graph dotGraph = new DefaultGraph(outputName);

        for (int i = 0; i < numOfTasks; i++){
            Node node = dotGraph.addNode(String.valueOf(i));
            node.setAttribute("Weight",randomProducer.nextInt(MAX_NODE_WEIGHT) + 1);
        }

        for (int i = 0; i < numOfTasks; i++){
            for (int j = i + 1; j < numOfTasks; j++){
                if(randomProducer.nextBoolean()) {
                    String label = i + "-" + j;
                    Edge edge = dotGraph.addEdge(label, i, j, true);
                    edge.setAttribute("Weight", randomProducer.nextInt(MAX_EDGE_WEIGHT) + 1);
                }
            }
        }

        FileSink file = new FileSinkDOT(true);

        try {
            file.writeAll(dotGraph, outputName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}