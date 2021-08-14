package algo.Schedule;

import algo.Solution.CommunicationCost;
import org.graphstream.graph.Edge;

import java.util.HashSet;
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
    private HashSet<Task> tasks;

    /**
     * The constructor of Processor.
     * @param processNumber The given id to this processor.
     */
    public Processor(int processNumber) {
        this.processNum = processNumber;
        tasks = new HashSet<>();
    }

    /**
     * This method returns the latest time this processor is free.
     *
     * @return The latest time this processor is free.
     */
    public int getTime() {
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
    public void addTask(Task task,int start) {
//        int communicationCost = communicationCost(task);
//        int latestProcessorTime = latestTime;
//
//        int taskStartTime = communicationCost + latestProcessorTime;
        int comm = start - latestTime;
        int taskDurationTime = task.getDurationTime();
        int taskEndTime = start + taskDurationTime;
        task.setTaskDetails(this, taskEndTime, start);
        task.setCommunicationCost(comm);
        latestTime = taskEndTime;
        tasks.add(task);
    }
    /** Removes a task from the processor, used to support backtracking in DFS.
     *
     * @param task
     */
    public void removeLatestTask(Task task) {
        int currentLatestTime = latestTime;
        int taskDuration = task.getDurationTime();
        int communicationCost = task.getCommunicationCost();
        int newLatestTime = currentLatestTime - taskDuration - communicationCost;
        setLatestTime(newLatestTime);
        tasks.remove(task);
    }

    public HashSet<Task> getTasks(){
        return tasks;
    }
}