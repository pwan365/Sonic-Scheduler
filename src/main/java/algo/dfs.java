package algo;

import java.util.ArrayList;

public class dfs {

    private Schedule schedule;
    private Schedule bestSchedule;
    private int numProcessors;
    public dfs(int processors) {
        schedule = new Schedule(processors);
        schedule.setLatestScheduleTime(Integer.MAX_VALUE);
        numProcessors = processors;
    }
    /**
     * TODO Given a list of tasks already scheduled, calculate all the possible tasks that can be scheduled. Ideally
     * TODO should memoize the solution into a hashmap so there are no repeated calculations.
     */
    public ArrayList<Task> validOrder(ArrayList<Task> scheduledTasks) {
        return null;
    }
    public void branchBound(Task task, int processor) {
        /**
         * TODO Schedule task here.
         */
        ArrayList<Task> allPossibilities = validOrder(null);

        if (allPossibilities.isEmpty()) {
            int currentBest = bestSchedule.getLatestScheduleTime();
            int candidateBest = schedule.getLatestScheduleTime();
            if (candidateBest < currentBest) {
                bestSchedule = schedule;
            }
        }
        for(int i = 0; i < allPossibilities.size(); i++) {
            for (int j = 0; j < numProcessors; j++) {
                branchBound(allPossibilities.get(i),j);
            }
            /**
             * TODO After all children have been exhausted, remove the task from the schedule.
             */
        }
    }
}
