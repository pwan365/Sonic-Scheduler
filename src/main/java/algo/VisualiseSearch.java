package algo;

/**
 * Interface that contains methods to visualise the running and results of the algorithm.
 * For both SequentialSearch and ParallelSearch to implement.
 *
 * @author Luxman Jeyarajah
 */
public interface VisualiseSearch {

    /**
     * All types of algorithms should have a run method for the GUI to call.
     */
    public void run();

    /**
     * All types of algorithms should have a getter method for current best time of a searched schedule.
     * @return The best time of a schedule that has been searched.
     */
    public int getBestTime();

    /**
     * Return the best schedule for the GUI to display.
     * @return The best schedule an algorithm has currently seen..
     */
    public BestSchedule getBestSchedule();

    /**
     * Return the number of states an algorithm has searched for the GUI to display.
     * @return The number of states examined.(Inclusive of states that are pruned mid recursive call)
     */
    public int getStates();

    /**
     * Writes the attributes of the best schedule to the graph produced by GraphStream to output to the user.
     * @return
     */
    public int done();
}
