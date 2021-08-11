package graphTests.validateTests;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Validator class is used to test if the final scheduling violates the constrains
 *
 * @author Wayne Yao, Samuel Chen, John Jia
 */
public class Validator {
    private int numOfTasks;
    private int numOfOutputTasks;
    private Node[] outputTasks = new Node[20];
    private int[][] communicationCosts = new int[40][40];
    private List<List<Integer>> startTimes = new ArrayList<>();
    private List<List<Integer>> endTimes = new ArrayList<>();
    private Graph inputGraph;
    private Graph outputGraph;

    /**
     * Initialize all the fields by reading the input and output graph
     * @param input input graph object
     * @param output output graph object after scheduling
     * @param numOfProcessors number of processors needed
     */
    public void initialize(Graph input, Graph output, int numOfProcessors) {
        inputGraph = input;
        outputGraph = output;
        numOfTasks = inputGraph.getNodeCount();
        numOfOutputTasks = outputGraph.getNodeCount();
        outputTasks = new Node[numOfOutputTasks];

        for (int i = 0; i < numOfProcessors; i++){
            List<Integer> stl = new ArrayList<>();
            List<Integer> sel = new ArrayList<>();
            startTimes.add(stl);
            endTimes.add(sel);
        }

    }

    /**
     * test if the output graph violates the two main constrains: overlapping tasks
     * and the sequence of parent and children tasks.
     * @return a boolean value, true if the graph passes the test, false otherwise
     */
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

        popularizeCommunicationCost();

        // Check scheduling against the constraints
        if(!validateProcessor()){
            return false;
        }

        // Check if parents are scheduled before children
        return validateParentChild();
    }

    /**
     * Popularize the communication costs array so it can be taken out easier later
     */
    public void popularizeCommunicationCost() {
        // Set up input and output information arrays
        for (int i = 0; i < numOfTasks; i++) {
            Node outputNode = outputGraph.getNode(i);
            outputTasks[i] = outputNode;
            if (outputNode.getOutDegree() > 0) {
                List<Edge> edges = outputNode.leavingEdges().collect(Collectors.toList());
                if (!edges.isEmpty()) {
                    for (Edge edge : edges) {
                        int parent = edge.getNode0().getIndex();
                        int child = edge.getNode1().getIndex();
                        int edgeWeight = ((Double) edge.getAttribute("Weight")).intValue();
                        communicationCosts[parent][child] = edgeWeight;
                    }
                }
            }
        }
    }

    /**
     * test the scheduling so that no two tasks are scheduled at the same time in the same processor
     * @return a boolean value, true if it passes the test, false otherwise
     */
    public boolean validateProcessor() {
        for (int i = 0; i < numOfTasks; i++) {
            // Check if two tasks are in the same processor at the same time
            Node task = outputTasks[i];

            int processorNumber = ((Double) task.getAttribute("Processor")).intValue() - 1;

            List<Integer> processorStartTimes = startTimes.get(processorNumber);
            List<Integer> processorEndTimes = endTimes.get(processorNumber);

            int startTime = ((Double) task.getAttribute("Start")).intValue();
            int finishTime = startTime + ((Double) task.getAttribute("Weight")).intValue();

            for (int j = 0; j < processorStartTimes.size(); j++) {
                int scheduledStartTime = processorStartTimes.get(j);
                int scheduledEndTime = processorEndTimes.get(j);
                if ((startTime >= scheduledStartTime && startTime < scheduledEndTime) ||
                        (scheduledStartTime >= startTime && scheduledStartTime < finishTime)) {
                    System.out.println("The processor has already been occupied");
                    return false; // Overlapping schedules
                }
            }
            processorStartTimes.add(startTime);
            processorEndTimes.add(finishTime);
        }
        return true;
    }

    /**
     * test the scheduling to find if there are child tasks scheduled before parent tasks.
     * @return a boolean value, true if it passes the test, false otherwise.
     */
    public boolean validateParentChild(){
        for (int parent = 0; parent < numOfTasks; parent++) {
            for (int child = parent + 1; child < numOfTasks; child++) {
                Node parentNode = outputTasks[parent];
                Node childNode = outputTasks[child];
                if (communicationCosts[parent][child] != 0) {
                    int communicationCost = 0;

                    int parentProcessor = ((Double)parentNode.getAttribute("Processor")).intValue();
                    int childProcessor = ((Double)childNode.getAttribute("Processor")).intValue();
                    if (parentProcessor != childProcessor) {
                        communicationCost = communicationCosts[parent][child];
                    }

                    int parentStartTime = ((Double) parentNode.getAttribute("Start")).intValue();
                    int parentDuration = ((Double) parentNode.getAttribute("Weight")).intValue();
                    int childStartTime = ((Double)childNode.getAttribute("Start")).intValue();
                    if (parentStartTime + parentDuration + communicationCost > childStartTime) {
                        System.out.println("child start before parent");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

