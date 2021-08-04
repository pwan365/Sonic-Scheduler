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
    private Node[] inputTasks = new Node[20];
    private Node[] outputTasks = new Node[20];
    private int[][] communicationCosts = new int[40][40];
    private List<List<Integer>> startTimes = new ArrayList<>();
    private List<List<Integer>> endTimes = new ArrayList<>();
    private Graph inputGraph;
    private Graph outputGraph;

    public void initialize(Graph input, Graph output, int numOfProcessors) {
        inputGraph = input;
        outputGraph = output;
        numOfTasks = inputGraph.getNodeCount();
        numOfOutputTasks = outputGraph.getNodeCount();
        inputTasks = new Node[numOfTasks];
//        change number of tasks to numberofOutputTasks for outputTask init
        outputTasks = new Node[numOfOutputTasks];

        //John's Initialization Starts Here---------
        for (int i = 0; i < numOfProcessors; i++){
            List<Integer> stl = new ArrayList<>();
            List<Integer> sel = new ArrayList<>();
            startTimes.add(stl);
            endTimes.add(sel);
        }
        //John's initialization Ends Here------------

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

        // Set up input and output information arrays
        for (int i = 0; i < numOfTasks; i++) {
            Node inputNode = inputGraph.getNode(i);
            Node outputNode = outputGraph.getNode(i);
//            System.out.println(outputGraph);
            inputTasks[i] = inputNode;
            outputTasks[i] = outputNode;
//            System.out.println(outputNode.getOutDegree());
            if (outputNode.getOutDegree() > 0) {
                List<Edge> edges = outputNode.leavingEdges().collect(Collectors.toList());
//                System.out.println("lllllllllllllllllll");
//                System.out.println(edges.toString());
//                System.out.println("lllllllllllllllllll");
                if (!edges.isEmpty()) {
                    for (Edge edge : edges) {
                        int parent = edge.getNode0().getIndex();
                        int child = edge.getNode1().getIndex();
//                        System.out.println("----------------------");
//                        System.out.println(parent);
//                        System.out.println(child);
//                        System.out.println(((Double) edge.getAttribute("Weight")).intValue());
//                        System.out.println("----------------------");

                        communicationCosts[parent][child] = ((Double) edge.getAttribute("Weight")).intValue();
                    }
                }
            }
        }


        // Check scheduling against the constraints
        for (int i = 0; i < numOfTasks; i++) {
            // Check if two tasks are in the same processor at the same time

            Node task = outputTasks[i];
            System.out.println(task.getAttributeCount());
            System.out.println(outputTasks[i].getAttribute("Processor"));

            // John's Implementation: The processor number should minus 1 to fit the index of the array.
            int processorNumber = ((Double) task.getAttribute("Processor")).intValue() - 1;

            List<Integer> processorStartTimes = startTimes.get(processorNumber);
            List<Integer> processorEndTimes = endTimes.get(processorNumber);

            int startTime = ((Double) task.getAttribute("Start")).intValue();
            int finishTime = startTime + ((Double) task.getAttribute("Weight")).intValue();

            for (int j = 0; j < processorStartTimes.size(); j++) {
                if(processorStartTimes.size() == 0 || processorEndTimes.size()==0){
                    break;
                }
                int scheduledStartTime = processorStartTimes.get(j);
                int scheduledEndTime = processorEndTimes.get(j);
                if (startTime <= scheduledStartTime && finishTime >= scheduledEndTime) {
                    System.out.println("The processor has already been occupied");
                    return false; // Overlapping schedules
                }
            }
            processorStartTimes.add(startTime);
            processorEndTimes.add(finishTime);
        }

        // Check if parents are scheduled before children
        for (int parent = 0; parent < numOfTasks; parent++) {
            for (int child = parent + 1; child < numOfTasks; child++) {
                Node parentNode = outputTasks[parent];
                Node childNode = outputTasks[child];
                if (communicationCosts[parent][child] != 0) {
                    int communicationCost = 0;
                    if (((Double)parentNode.getAttribute("Processor")).intValue() != ((Double)childNode.getAttribute("Processor")).intValue()) {
                        communicationCost = communicationCosts[parent][child];
                    }

                    if (((Double) parentNode.getAttribute("Start")).intValue() + ((Double) parentNode.getAttribute("Weight")).intValue() + communicationCost > (Double) childNode.getAttribute("Start")) {
                        System.out.println("child start before parent");
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
