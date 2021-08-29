package algo;

import algo.helpers.comparators.DLS;
import algo.helpers.pruning.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Abstract class that defines methods that both the Sequential and Parallel Search will use.
 *
 * @author Luxman Jeyarajah, Wayne Yao
 */
public abstract class RecursiveSearch {

    /**
     * Intitialize the recursive search.
     */
    protected abstract void run();

    /**
     * Check whether we can prune the state before we add it.
     * @param schedule The current schedule.
     * @param task The task to be scheduled
     * @param cost The cost to schedule the task.
     * @param processor The processor to schedule the task.
     * @param bestSchedule The current best schedule
     * @return Boolean value to tell the recursive caller whether the task was pruned, if so we stop the recursive
     * call for this state.
     */
    protected boolean prune(ScheduleState schedule,int task, int cost, int processor,BestSchedule bestSchedule) {
        int bWeight = BottomLevel.pruneBLevel(task,cost,schedule.processorTimes[processor]);
        int loadBalance = LoadBalancer.calculateLoadBalance(schedule.idle,cost,schedule.numProcessors);
        int candidateTime = Math.max(schedule.time.peek(), Math.max(bWeight, loadBalance));

        // If the candidate time is less than best time, then exit.
        if (bestSchedule.bestTime <= candidateTime) {
            return true;
        }
        return false;

    }

    /**
     * Checks whether we have seen a state before and prunes if so.
     * @param schedule The current schedule state.
     * @param task The current task that has been scheduled.
     * @param processor The current processor the task was scheduled.
     * @param cost The cost incurred when scheduling the task.
     * @return A boolean value telling the recursive caller whether this state was pruned, if so the state should stop
     * the recursive call.
     */
    protected boolean checkSeen(ScheduleState schedule,int task, int processor, int cost) {
        // Check whether the Current schedule has been visited before.
        boolean seen = HashCodeStorage.checkIfSeen(schedule.taskInformation,schedule.taskProcessors,schedule.numProcessors,schedule.numTasks);

        // If so, exit.
        if (seen) {
            schedule.removeTask(task,processor,cost);
            return true;
        }
        return false;
    }

    /**
     * Update the best schedule if all tasks are scheduled and the time of the schedule is better than the one we have
     * seen.
     * @param bestSchedule The current best schedule.
     * @param schedule The candidate schedule(current schedule state.)
     */
    protected void updateBestSchedule(BestSchedule bestSchedule, ScheduleState schedule) {
        if (schedule.scheduled == schedule.numTasks) {
            int candidateBest = schedule.time.peek();
            if (candidateBest < bestSchedule.bestTime) {
                bestSchedule.makeCopy(candidateBest,schedule.taskProcessors,schedule.taskInformation);
            }
        }
    }

    /**
     * Gets the candidate tasks to be called in the next recursive call.
     * @param schedule The current schedule
     * @return A Priority Queue of tasks to be recursively called, ordered by their DLS level non-increasingly.
     */
    protected PriorityQueue<DLS>  getCandidateTasks(ScheduleState schedule,BestSchedule bestSchedule) {
        PriorityQueue<DLS> lowestCost = new PriorityQueue<>();
        HashSet<Integer> seenTasks = new HashSet<>();

        boolean[] candidateTasks = schedule.getOrder();
        candidateTasks = FixedTaskOrder.checkFTO(candidateTasks,schedule.taskProcessors,schedule.taskInformation,
                schedule.intGraph.outEdges,
                schedule.intGraph.inEdges,schedule.numTasks);

        for (int i = 0; i < schedule.numTasks; i++) {
            if (candidateTasks[i]) {
                // If this is a duplicate node then don't run an additional permutation.
                if(seenTasks.contains(i)){
                    continue;

                }
                else
                {
                    LinkedList<Integer> sameStates = DuplicateStates.getDuplicateNodes(i);
                    seenTasks.addAll(sameStates);
                }

                //Normalise processors, if they are on 0, schedule on 1 and skip the others.
                boolean zero = false;
                for (int j = 0; j < schedule.numProcessors; j++) {
                    if (schedule.processorTimes[j] == 0) {
                        if (zero) {
                            continue;
                        }
                        else {
                            zero = true;
                        }
                    }
                    int commCost = schedule.commCost(i,j);
                    int bottomLevel = BottomLevel.returnBLevel(i);
                    boolean pruned = prune(schedule,i,commCost,j,bestSchedule);
                    if (pruned) {
                        continue;
                    }
                    DLS DLS = new DLS(bottomLevel,commCost,schedule.processorTimes[j],i,j);
                    lowestCost.add(DLS);
                }
            }
        }

        return lowestCost;
    }

}
