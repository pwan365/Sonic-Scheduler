package algo.Schedule;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.HashSet;
import java.util.Stack;

/**
 * Class used to schedule/remove tasks for SequentialSearch.
 *
 * @author Luxman Jeyarajah
 */
public class PartialSchedule implements Schedule {

    private Processor[] processors;
    // Stack to record the latest schedule time when each task was scheduled.
    private Stack<Integer> time = new Stack<>();
    private HashSet<Task> scheduledSet = new HashSet<>();
    private HashSet<Task> unscheduledSet = new HashSet<>();
    public int idle = 0;

    /**
     * Constructor to initialize Partial Schedule class, initializes the processors and adds all tasks to the
     * unscheduled set.
     * @param numProcessors
     * @param graph
     */
    public PartialSchedule(int numProcessors, Graph graph) {
        //Initialize Processor Pool
        processors  = new Processor[numProcessors];
        for (int i = 0; i < numProcessors; i ++) {
            processors[i] = new Processor(i+1);
        }
        //Initialize Unscheduled Tasks Set
        for(int i = 0; i < graph.getNodeCount(); i++) {
            Node node = graph.getNode(i);
            Task task = (Task) node.getAttribute("Task");
            unscheduledSet.add(task);
        }
        // Add the starting time of 0 to the stack.
        time.push(0);
    }

    /**
     * Getter method for the schedules latest time.
     * @return Latest schedule time.
     */
    public int getTime() {
        return time.peek();
    }

    /**
     * Adds a task to the given processors, accounting for communication costs, updates the time in the stack.
     * @param task The task to be scheduled.
     * @param processorID The processor number to schedule the task.
     * @param cost The cost to schedule the task on this processor.
     */
    public void scheduleTask(Task task, int processorID,int cost) {
        idle += cost;
        processors[processorID].addTask(task,cost);
        scheduledSet.add(task);
        unscheduledSet.remove(task);

//      Add the latest time to the stack, if updating the processor increases the time, push the time accordingly.
        int candidateTime = processors[processorID].getTime();
        int currentTime = time.peek();
        time.push(Math.max(candidateTime, currentTime));
    }

    /**
     * Removes a task from a schedule to support backtracking in DFS.
     * @param task The task to be removed from the schedule.
     */
    public void removeTasks(Task task) {
        time.pop();
        idle -= task.getCommunicationCost();
        scheduledSet.remove(task);
        unscheduledSet.add(task);
        task.unSchedule();
    }

    /**
     * Getter method to return the current scheduled tasks.
     * @return
     */
    public HashSet<Task> getScheduledTasks() {
        return this.scheduledSet;
    }

    /**
     * Getter method to retrieve the number of processors in the schedule.
     * @return Number of processors.
     */
    public int getNumProcessors() {
        return this.processors.length;
    }

    /**
     * Getter method to retrieve the list of processors.
     * @return
     */
    public Processor[] getProcessors() {
        return processors;
    }

    public HashSet getUnscheduledTasks() {
        return this.unscheduledSet;
    }

}
