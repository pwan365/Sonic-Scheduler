package algo.helpers.comparators;

/**
 * DLS(Dynamic List Scheduling) which is the Bottom Level - the earliest start time of a task. This is then sorted
 * in decreasing order. DLS is used as a heuristic to order tasks in a list of candidate tasks to be recursively
 * called.
 *
 * @author Luxman Jeyarajah
 */
public class DLS implements Comparable<DLS> {

    private final int dsl;
    private final int task;
    private final int processor;
    private final int cost;

    /**
     * Constructor to hold details of a DLS for a given task.
     * @param blevel The bottom level of the task.
     * @param cost The cost to schedule this task on the candidate processor.
     * @param start The start time of the processor to be scheduled.
     * @param task The task to be scheduled.
     * @param processor The processor in which this task will be scheduled.
     */
    public DLS(int blevel, int cost, int start, int task, int processor) {
        //Negative as we want this sorted in decreasing, not increasing order.
        this.dsl = -(blevel - (cost + start));
        this.task = task;
        this.processor = processor;
        this.cost = cost;
    }


    /**
     * Compares two DLS of tasks, returns the one that is smallest.
     * @param d The second instance of DLS
     * @return Result of comparison.
     */
    @Override
    public int compareTo(DLS d) {
        return Integer.compare(this.dsl,d.dsl);
    }

    /**
     * Getter method for thetask.
     * @return The task of the DSL.
     */
    public int getTask() {
        return task;
    }

    /**
     * Getter method for the processor
     * @return The processor of the DSL.
     */
    public int getProcessor() {
        return processor;
    }

    /**
     * Getter method for the cost
     * @return Cost to schedule the task.
     */
    public int getCost() {
        return cost;
    }


}
