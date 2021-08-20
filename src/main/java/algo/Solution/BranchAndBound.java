package algo.Solution;

import java.util.HashSet;
import java.util.Stack;

public abstract class BranchAndBound {
    private int numTasks;

    //Schedule information
    private Stack<Integer> time;
    private Integer idle;
    private HashSet<Integer> scheduledTasks;
    private HashSet<Integer> unscheduledTasks;

    //Processor information
    private int [] processorTimes;

    //Task information
    private int [] taskProcessors;

    public BranchAndBound(int tasks) {
        numTasks = tasks;
    }
}
