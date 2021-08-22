package algo;

/**
 * Interface that contains methods that GUI is going to use to interact with algorithm.
 * For both SequentialSearch and ParallelSearch to implement.
 *
 * @author Luxman Jeyarajah
 */
public interface GUISchedule {

    public void run();
    public int getBestTime();
    public BestSchedule getBestSchedule();
    public int getStates();
    public int done();
}
