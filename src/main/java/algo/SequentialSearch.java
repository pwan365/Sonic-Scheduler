package algo;


import algo.helpers.costFunctions.BottomLevel;
import algo.helpers.costFunctions.LoadBalancer;
import org.graphstream.graph.Graph;

import java.util.*;

/**
 * Class to run a sequential search using DFS branch and bound.
 *
 * @author Luxman Jeyarajah
 */
public class SequentialSearch extends BranchAndBound implements  GUISchedule{

    private int states = 0;
    public BestSchedule bestSchedule;
    private Graph inputGraph;

    public SequentialSearch(Graph input, IntGraph graph, int processors) {
        super(graph,processors, true);
        bestSchedule = new BestSchedule();
        inputGraph = input;
    }

    public void run() {
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

        for (int i = 0; i < numTasks; i++) {
            if (candidateTasks[i]) {
                int candidateTask = i;
                int candidateProcessor = 0;
                int commCost = commCost(candidateTask, candidateProcessor);
                branchBound(candidateTask, candidateProcessor, commCost);
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
        states += 1;
        int bWeight = BottomLevel.pruneBLevel(task,cost,processorTimes[processor]);
        int loadBalance = LoadBalancer.calculateLoadBalance(idle,cost,numProcessors);
        int candidateTime = Math.max(time.peek(), Math.max(bWeight, loadBalance));

        if (bestSchedule.bestTime <= candidateTime) {
            return;
        }


        addTask(task, processor, cost);

        boolean seen = checkSeen();

        if (seen) {
            removeTask(task,processor,cost);
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
        PriorityQueue<DLS> lowestCost = new PriorityQueue<>();

        HashSet<Integer> seenTasks = new HashSet<>();



        for (int i = 0; i < numTasks; i++) {
            if (candidateTasks[i]) {
                if(seenTasks.contains(i)){
                    continue;
                }else{
                    seenTasks.addAll(equivalentList[i]);
                }
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
                    int bottomLevel = BottomLevel.returnBLevel(task);
                    DLS DLS = new DLS(bottomLevel,commCost,processorTimes[j],i,j);
                    lowestCost.add(DLS);
                }
            }
        }

            while (!lowestCost.isEmpty()) {
                DLS candidate = lowestCost.poll();
                int candidateTask = candidate.task;
                int processorID = candidate.processor;
                int candidateCost = candidate.cost;
                branchBound(candidateTask, processorID, candidateCost);
            }
            removeTask(task,processor,cost);
    }

    @Override
    public int getStates() {
        return states;
    }
    @Override
    public int getBestTime() {
        return bestSchedule.bestTime;
    }

    @Override
    public BestSchedule getBestSchedule() {
        return bestSchedule;
    }

    public int done() {
        bestSchedule.writeToGraph(inputGraph);
        return bestSchedule.bestTime;
    }
}
