package io;

import algo.Processor;
import algo.Task;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Validator {
    private int numOfTasks;
    private int numOfOutputTasks;
    private Node[] inputTasks;
    private Node[] outputTasks;
    private int[][] communicationCosts;
    private List<List<Integer>> startTimes = new ArrayList<>();
    private List<List<Integer>> endTimes = new ArrayList<>();

    public void initialize(Graph inputGraph, Graph outputGraph, int numOfProcessors) {
        numOfTasks = inputGraph.getNodeCount();
        numOfOutputTasks = outputGraph.getNodeCount();
        inputTasks = new Node[numOfTasks];
        outputTasks = new Node[numOfTasks];

        // Set up input and output information arrays
        for (int i = 0; i < numOfTasks; i++) {
            Node inputNode = inputGraph.getNode(String.valueOf(i));
            Node outputNode = outputGraph.getNode(String.valueOf(i));
            inputTasks[i] = inputNode;
            outputTasks[i] = outputNode;

            List<Edge> edges = outputNode.leavingEdges().collect(Collectors.toList());
            for (Edge edge : edges){
                int parent = edge.getNode0().getIndex();
                int child = edge.getNode1().getIndex();
                communicationCosts[parent][child] = ((Double) edge.getAttribute("Weight")).intValue();
            }
        }
    }

    public boolean validate() {
        // Check whether the number of nodes is different in input and output
        if (numOfTasks != numOfOutputTasks) {
            System.out.println("The number of tasks is not equal in input and output graphs");
            return false;
        }

        // If there is no nodes in input graph, the schedule should be valid by default
        if (numOfTasks == 0) {
            return true;
        }

        // Check scheduling against the constraints
        for (int i = 0; i < numOfTasks; i++){
            // Check if two tasks are in the same processor at the same time
            Node task = outputTasks[i];
            int processorNumber = ((Double)task.getAttribute("Processor")).intValue();
            List<Integer> processorStartTimes = startTimes.get(processorNumber);
            List<Integer> processorEndTimes = endTimes.get(processorNumber);

            int startTime = ((Double)task.getAttribute("Start")).intValue();
            int finishTime = startTime + ((Double)task.getAttribute("Weight")).intValue();

            for (int j = 0; j < startTimes.size(); j++){
                int scheduledStartTime = processorStartTimes.get(i);
                int scheduledEndTime = processorEndTimes.get(i);
                if(startTime < scheduledStartTime && finishTime > scheduledEndTime) {
                    System.out.println("The processor has already been occupied");
                    return false; // Overlapping schedules
                }
            }
            processorStartTimes.add(startTime);
            processorEndTimes.add(finishTime);
        }

        // Check if parents are scheduled before children
        for (int parent = 0; parent < numOfTasks; parent++){
            for (int child = parent + 1; child < numOfTasks; child++){
                Node parentNode = outputTasks[parent];
                Node childNode = outputTasks[child];
                if (communicationCosts[parent][child] != 0) {
                    int communicationCost = 0;
                    if ((Double)parentNode.getAttribute("Processor") != (Double)childNode.getAttribute("Processor")) {
                        communicationCost = communicationCosts[parent][child];
                    }

                    if(((Double)parentNode.getAttribute("Start")).intValue() + ((Double)parentNode.getAttribute("Weight")).intValue() + communicationCost > (Double)childNode.getAttribute("Start")){
                        System.out.println("child start before parent");
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
