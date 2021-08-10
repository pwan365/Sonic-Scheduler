package algo;

import org.graphstream.graph.Edge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Processor class is used to represent a processor that can run Tasks.
 *
 * @author John Jia, Luxman Jeyarajah
 */
public class Processor {
    //The time when the processor is free.
    private int latestTime = 0;
    //The id of the processor.
    private int processNum;

    /**
     * The constructor of Processor.
     *
     * @param processNumber The given id to this processor.
     */
    public Processor(int processNumber) {
        this.processNum = processNumber;
    }

    /**
     * This method returns the latest time this processor is free.
     *
     * @return The latest time this processor is free.
     */
    public int getLatestTime() {
        return latestTime;
    }

    /**
     * This method sets the latest time this processor is free.
     *
     * @param latestTime The new latest time.
     */
    public void setLatestTime(int latestTime) {
        this.latestTime = latestTime;
    }

    /**
     * This method returns the id of this processor.
     *
     * @return The id of this processor.
     */
    public int getProcessNum() {
        return processNum;
    }

    /**
     * This method will add a task to this processor at the earliest time possible, accounting for communication costs.
     */
    public void addTask(Task task) {
        int communicationCost = communicationCost(task);
        int latestProcessorTime = getLatestTime();
        int taskStartTime = communicationCost + latestProcessorTime;
        int taskDurationTime = task.getDurationTime();
        int taskEndTime = taskStartTime + taskDurationTime;
        task.setTaskDetails(this, taskEndTime, taskStartTime);
        task.setCommunicationCost(communicationCost);
        setLatestTime(taskEndTime);
        System.out.println(latestTime);
    }

    /**
     * Calculates any extra time the processor has to wait before scheduling a given task due to communication costs of
     * dependent tasks.
     *
     * @param task Task that we wish to calculate the communication cost for.
     * @return Extra communication cost that the processor must wait before scheduling the task.
     */
    private int communicationCost(Task task) {
        List<Edge> parents = task.getParentEdgeList();
        int processorLatestTime = getLatestTime();
        int cost = 0;
        for (Edge parentEdge : parents) {
            // Gets the parent task of the candidate task, getNode0 returns the parent of an edge.
            Task parentTask = (Task) parentEdge.getNode0().getAttribute("Task");
            Processor parentProcessor = parentTask.getAllocatedProcessor();
            if (parentProcessor != this) {
                int parentEndTime = parentTask.getFinishingTime();
                int commCost = ((Double) parentEdge.getAttribute("Weight")).intValue();
                int candidateCost = parentEndTime + commCost;
                cost = Math.max(cost, candidateCost - processorLatestTime);
            }
        }
        return cost;

    }

    /**
     *
     * @param task
     */
    public void removeLatestTask(Task task) {
        int currentLatestTime = getLatestTime();
        int taskDuration = task.getDurationTime();
        int communicationCost = task.getCommunicationCost();
        int newLatestTime = currentLatestTime - taskDuration - communicationCost;
        setLatestTime(newLatestTime);
    }
}