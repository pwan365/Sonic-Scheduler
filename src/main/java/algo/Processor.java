package algo;

import java.util.ArrayList;
import java.util.HashMap;

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
     * This method returns the id of this processor.
     * @return The id of this processor.
     */
    public int getProcessNum() {
        return processNum;
    }
}