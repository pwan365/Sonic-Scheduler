package io;

import algo.Processor;
import algo.Task;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.List;
import java.util.stream.Collectors;

public class Validator {
    private int numOfTasks;
    private int numOfOutputTasks;
    private double[] inputDurations;
    private double[] outputDurations;
    private double[][] communicationCosts;
    private double[] startTimes;
    private double[] allocatedProcessors;

    public void initialize(Graph inputGraph, Graph outputGraph, int numOfProcessors) {
        numOfTasks = inputGraph.getNodeCount();
        numOfOutputTasks = outputGraph.getNodeCount();
        // Set up input and output information arrays
        inputDurations = new double[numOfTasks];
        outputDurations = new double[numOfTasks];
        startTimes = new double[numOfTasks];
        communicationCosts = new double[numOfTasks][numOfTasks];
        allocatedProcessors = new double[numOfTasks];

        for (int i = 0; i < numOfTasks; i++) {
            Node inputNode = inputGraph.getNode(String.valueOf(i));
            Node outputNode = outputGraph.getNode(String.valueOf(i));


            inputDurations[i] = ((Double)inputNode.getAttribute("Weight"));
            outputDurations[i] = ((Double)outputNode.getAttribute("Weight"));
            startTimes[i] = ((Double)outputNode.getAttribute("Start"));
            allocatedProcessors[i] = ((Double)outputNode.getAttribute("Processor"));

            List<Edge> edges = outputNode.leavingEdges().collect(Collectors.toList());
            for (Edge edge : edges){
                int parent = edge.getNode0().getIndex();
                int child = edge.getNode1().getIndex();
                communicationCosts[parent][child] = ((Double) edge.getAttribute("Weight")).intValue();
            }
        }


    }
}
