package algo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Processor class is used to represent a processor that can run Tasks.
 *
 * @author
 */
public class Processor {
    //The time when the processor is free.
    private int latestTime = 0;
    //A map to store the nodes running on this process and their information.
    private HashMap<Task, ArrayList<Integer>> nodeTable = new HashMap<>();
    //The id of the processor.
    private int processNum;

    /**
     * The constructor of Processor.
     * @param processNumber The given id to this processor.
     */
    public Processor(int processNumber) {
        this.processNum = processNumber;
    }

    /**
     * This method returns the latest time this processor is free.
     * @return The latest time this processor is free.
     */
    public int getLatestTime() {
        return latestTime;
    }

    /**
     * This method sets the latest time this processor is free.
     * @param latestTime The new latest time.
     */
    public void setLatestTime(int latestTime) {
        this.latestTime = latestTime;
    }

    /**
     * This method is used to add a task to this processor.
     * @param task The task object to be added to the processor.
     * @param start The start time of the task.
     * @param end The end time of the task.
     * @param duration The duration of the task.
     */
    public void addTask(Task task, int start, int end, int duration){
        ArrayList<Integer> task_info = new ArrayList<>();
        task_info.add(start);
        task_info.add(end);
        task_info.add(duration);
        this.nodeTable.put(task,task_info);
    }

    /**
     * This method returns all the tasks that run on this processor and their information.
     * @return A hashmap containing the task and their timings.
     */
    public HashMap<Task, ArrayList<Integer>> getTasks() {
        return nodeTable;
    }

    /**
     * This method returns the id of this processor.
     * @return The id of this processor.
     */
    public int getProcessNum() {
        return processNum;
    }
}