package algo.Schedule;

/**
 * Interface to define commonalities between BestSchedule and PartialSchedule classes.
 *
 * @author Luxman Jeyarajah
 */
public interface Schedule {
    /**
     * Retrieves the latest time of a schedule.
     * @return Latest time of schedule.
     */
    public int getTime();

    /**
     * Getter method for the processor array.
     * @return The array of processors.
     */
    public Processor[] getProcessors();
}
