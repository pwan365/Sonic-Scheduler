package algo.Solution;

import algo.Schedule.Processor;
import algo.Schedule.Schedule;
import algo.Schedule.Task;
import org.graphstream.graph.Graph;

import java.util.HashSet;

/**
 * Class to calculate whether we are starting a task on a duplicate processor, if so we do not need to schedule this task.
 */
public class DuplicateStart {

    private HashSet<Task> startZero = new HashSet<>();
    private static DuplicateStart duplicateStart;
    public int count = 0;

    private DuplicateStart() {}

    /**
     * Thread safe method to initialize a class.
     * @return The DuplicateStart Object.
     */
    public synchronized static DuplicateStart init() {
        if (duplicateStart != null) {
            //Throw an error when LoadBalancer tries to get instantiated more than once.
            throw new AssertionError("DuplicateStart has already been instantiated.");
        }
        duplicateStart = new DuplicateStart();
        return duplicateStart;
    }

    /**
     *
     * @param task
     */
    private void addZeroTask(Task task) {
        startZero.add(task);
    }

    /**
     *
     * @param task
     * @return
     */
    public boolean checkZeroTask(Processor processor, Task task,int cost) {
//        System.out.println("COST");
//        System.out.println(cost);
//        System.out.println("START");
//        System.out.println(processor.getTime());
        if ((processor.getTime() + cost) == 0) {
            if (startZero.contains(task)) {
                count += 1;
                return true;
            }
            else {
                addZeroTask(task);
            }
        }
        return false;
    }

    public static void clearObject() {
        duplicateStart = null;
    }
}
