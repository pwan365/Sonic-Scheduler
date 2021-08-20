package algo.Solution;
import java.util.HashSet;
import java.util.Stack;

public abstract class BranchAndBound {
    //Graph
    IntGraph intGraph;
    int numProcessors;
    int numTasks;

    //Schedule information
    private Stack<Integer> time;
    private Integer idle;
    private HashSet<Integer> scheduledTasks;
    private HashSet<Integer> unscheduledTasks;

    //Processor information
    private int [] processorTimes;

    //Task information
    private int [] taskProcessors;
    //Index 0 is StartTime, Index 1 is Weight, Index 2 is Finish Time, Index 3 is communication cost.
    private int [][] taskInformation;

    public BranchAndBound(IntGraph graph,int numberOfProcessors) {
        intGraph = graph;
        numProcessors = numberOfProcessors;
        numTasks = graph.tasks.length;

        time = new Stack<>();
        time.push(0);
        idle = 0;
        scheduledTasks = new HashSet<>();
        unscheduledTasks = new HashSet<>();

        addUnscheduledTasks();

        processorTimes = new int[numberOfProcessors];

        taskInformation = new int[numTasks][3];

        setDefaultTaskInfo();
        setDefaultTaskProcessor();
    }

    private void addUnscheduledTasks() {
        for (int i = 0; i < numTasks; i++) {
            unscheduledTasks.add(i);
        }
    }

    private void setDefaultTaskInfo() {
        for (int i = 0; i < numTasks; i++) {
            taskInformation[i] = new int[]{-1,-1,-1,-1};
        }
    }

    private void setDefaultTaskProcessor() {
        for (int i = 0; i < numTasks; i++) {
            taskProcessors[i] = -1;
        }
    }
}
