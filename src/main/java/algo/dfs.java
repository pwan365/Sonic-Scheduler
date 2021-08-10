package algo;

import java.util.ArrayList;

public class dfs {

    private Schedule schedule;
    private int numProcessors;
    public dfs(int processors) {
        schedule = new Schedule(processors);
        numProcessors = processors;
    }
    /**
     * TODO Given a list of tasks already scheduled, calculate all the possible tasks that can be scheduled. Ideally
     * TODO should memoize the solution into a hashmap so there are no repeated calculations.
     */
    public ArrayList<Task> validOrder(ArrayList<Task> scheduledTasks) {
        return null;
    }
    public void branchBound(Task task, int processor,int scheduledTasks) {
        /**
         * TODO Schedule task here.
         */

        /**
         * TODO If condition, if scheduledTasks == the number of tasks(all tasks have been scheduled), then check whether
         * this schedule is better than the current best schedule, if it is update it.
         */

        ArrayList<Task> allPossibilities = validOrder(null);
        for(int i = 0; i < allPossibilities.size(); i++) {
            for (int j = 0; j < numProcessors; j++) {
                branchBound(allPossibilities.get(i),j,scheduledTasks + 1);
            }
            /**
             * TODO After all children have been exhausted, remove the task from the schedule, subtract 1 from scheduled
             * tasks.
             */
        }
    }
}
