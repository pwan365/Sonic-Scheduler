package algo.Solution;

import java.util.*;

/**
 * Class to run a sequential search using DFS branch and bound.
 *
 * @author Luxman Jeyarajah
 */
public class SequentialSearch extends BranchAndBound{
    private long prune = 0;
    private int states = 0;
    public BestSchedule bestSchedule;

    public SequentialSearch(IntGraph graph, int processors) {
        super(graph,processors,true);
        bestSchedule = new BestSchedule();
    }

    public void run() {
        boolean[] startTasks = getOrder();
        for (int i = 0; i < numTasks; i++) {
            if (startTasks[i]) {
                for (int j = 0; j < numProcessors; j++) {
                    int candidateTask = i;
                    int candidateProcessor = j;
                    int commCost = commCost(candidateTask, candidateProcessor);
                    branchBound(candidateTask, candidateProcessor, commCost);
                }
            }
        }
    }

    /**
     * Recursive function that goes through all possible schedules and finds the one with the earliest schedule time.
     * @param task Task to be scheduled.
     * @param processor Processor to schedule the task on.
     * @param cost Cost to schedule the task on the processor.
     */
    public void branchBound(int task, int processor,int cost) {
        int bWeight = bottomLevel[task] + cost + processorTimes[processor];
        int loadBalance = loadBalance(cost);
        int candidateTime = Math.max(time.peek(), Math.max(bWeight, loadBalance));

        if (bestSchedule.bestTime <= candidateTime) {
            prune += 1;
            return;
        }


        addTask(task, processor, cost);

        boolean seen = checkSeen();

        if (seen) {
            removeTask(task,processor,cost);
            prune+= 1;
            return;
        }

        if (scheduled == numTasks) {
            int candidateBest = time.peek();
            if (candidateBest < bestSchedule.bestTime) {
                bestSchedule.makeCopy(candidateBest,taskProcessors,taskInformation);
            }
        }

        boolean[] candidateTasks = getOrder();
        LinkedList<Integer> fto = toFTOList(candidateTasks);
        if (fto != null) {
            int first = fto.poll();
            for (int i =0;i < numTasks;i++) {
                if (i != first) {
                    candidateTasks[i] = false;
                }
            }
        }
        PriorityQueue<DSL> lowestCost = new PriorityQueue<>();

        for (int i = 0; i < numTasks; i++) {
            if (candidateTasks[i]) {
                boolean zero = false;
                for (int j = 0; j < numProcessors; j++) {
                    if (processorTimes[j] == 0) {
                        if (zero) {
                            continue;
                        }
                        else {
                            zero = true;
                        }
                    }
                    int commCost = commCost(i,j);
                    DSL dsl = new DSL(bottomLevel[i],commCost,processorTimes[j],i,j);
                    lowestCost.add(dsl);
                }
            }
        }

        while (!lowestCost.isEmpty()) {
            DSL candidate = lowestCost.poll();
            int candidateTask = candidate.task;
            int processorID = candidate.processor;
            int candidateCost = candidate.cost;
            branchBound(candidateTask, processorID, candidateCost);
        }
        removeTask(task,processor,cost);
        states += 1;

    }

    public int done() {
//        bestSchedule.printTasks();
        System.out.println(bestSchedule.bestTime);
        System.out.println(states);
//        System.out.println(prune);
        return bestSchedule.bestTime;
    }
}
