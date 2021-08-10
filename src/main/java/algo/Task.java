package algo;

import org.graphstream.graph.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to record a Node from the graph and its relative information.
 *
 * @author John Jia, Wayne Yao, Luxman Jeyarajah
 */
public class Task {
    private int finishingTime = -1;
    private int startingTime;
    private int durationTime;
    private int communicationCost;
    //This is the list of edges that connects to the parent of the node of this task.
    private ArrayList<Edge> parentEdgeList = new ArrayList<Edge>();
    //The node that represents this task in a graph.
    private Node node;
    //The processor this task is allocated to.
    private Processor allocatedProcessor;

    /**
     * The constructor of Task.
     * @param node The node to be referenced with this task.
     */
    public Task(Node node){
        this.node = node;
        this.durationTime = ((Double)node.getAttribute("Weight")).intValue();
        List<Edge> edges = node.enteringEdges().collect(Collectors.toList());
        for(Edge e : edges){
            parentEdgeList.add(e);
        }
    }

    /**
     * This method returns the finishing time for a task on the assigned processor. Returns -1.0 if not set.
     * @return Task finishing time.
     */
    public int getFinishingTime() {
        return finishingTime;
    }

    /**
     * This method return the node that represents the task in a graph.
     * @return Node associated with this task.
     */
    public Node getNode() {
        return node;
    }

    /**
     * This method sets the node that represents the task in a graph.
     * @param node The node to be associated with this task.
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * Returns the arraylist of edges going into the referenced node of this task.
     * @return Arraylist of edges from parents.
     */
    public ArrayList<Edge> getParentEdgeList() {
        return parentEdgeList;
    }

    /**
     * Getter for the processor this task is allocated to.
     * @return The allocated processor.
     */
    public Processor getAllocatedProcessor() {
        return allocatedProcessor;
    }

    /**
     * This method is used to set the data of this task when it is scheduled to a processor.
     * @param allocated_processor The processor this task is scheduled in.
     * @param finishing_time The finished time of this task on the scheduled processor.
     * @param starting_time The start time of this task on the scheduled processor.
     */
    public void setTaskDetails(Processor allocated_processor,int finishing_time,int starting_time) {
        this.allocatedProcessor = allocated_processor;
        this.finishingTime = finishing_time;
        this.startingTime = starting_time;
    }

    /**
     * The getter for the starting time of this task.
     * @return Starting time of this task.
     */
    public int getStartingTime() {
        return startingTime;
    }

    /**
     * The getter for the duration of this task.
     * @return Duration of this task.
     */
    public int getDurationTime() {
        return durationTime;
    }

    /**
     *
     * @return
     */
    public int getCommunicationCost() {
        return communicationCost;
    }

    /**
     * Remove this task from a Processor.
     */
    public void unSchedule() {
        finishingTime = -1;
        startingTime = -1;
        allocatedProcessor.removeLatestTask(this);
        allocatedProcessor = null;
    }
}
