package algo.CostFunctions;

import algo.Task;

import java.util.HashSet;
import java.util.Iterator;

public class LoadBalancer {

    public static int calculateLB(HashSet<Task> unscheduledTasks,int numProcessors) {
        int sum = 0;
        Iterator<Task> iterator = unscheduledTasks.iterator();
        while(iterator.hasNext()) {
            Task task = iterator.next();
            sum += task.getDurationTime();
        }
        return (int) Math.ceil(sum/numProcessors);
    }
}
