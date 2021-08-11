package algo.CostFunctions;

import algo.Task;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class LoadBalancer {
    private static HashMap<HashSet, Integer> memo = new HashMap<>();
    public static int calculateLB(HashSet<Task> unscheduledTasks,int numProcessors) {
        if (memo.containsKey(unscheduledTasks)) {
            return memo.get(unscheduledTasks);
        }
        int sum = 0;
        Iterator<Task> iterator = unscheduledTasks.iterator();
        while(iterator.hasNext()) {
            Task task = iterator.next();
            sum += task.getDurationTime();
        }
        int lb = (int) Math.ceil(sum/numProcessors);
        memo.put(unscheduledTasks,lb);
        return lb;
    }
}
