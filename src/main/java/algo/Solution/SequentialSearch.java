package algo.Solution;


import org.graphstream.graph.Graph;

import java.util.*;

/**
 * Class to run a sequential search using DFS branch and bound.
 *
 * @author Luxman Jeyarajah
 */
public class SequentialSearch extends BranchAndBound{

//    private PartialSchedule partialSchedule;
//    private BestSchedule bestSchedule;
//    private LoadBalancer loadBalancer;
//    private CriticalPath criticalPath;
//    private AllOrders allOrders;
//    private DuplicateStart duplicateStart;
//    //Optional, may remove.
    private long prune = 0;
//    private Graph input;
//    private int numProcessors;
//    private Processor[] processorList;
//    private HashSet<Integer> duplicateDetector;
    private int states = 0;
    private BestSchedule bestSchedule;
    private Graph inputGraph;

    public SequentialSearch(Graph input, IntGraph graph, int processors) {
        super(graph,processors, true);
        bestSchedule = new BestSchedule();
        inputGraph = input;
//        input = inputGraph;
//        partialSchedule = new PartialSchedule(processors,inputGraph);
//        bestSchedule = new BestSchedule();
//        criticalPath = CriticalPath.init(inputGraph);
//        loadBalancer = LoadBalancer.init(inputGraph,processors);
//        allOrders = AllOrders.init(inputGraph);
//        duplicateStart = DuplicateStart.init();
//        processorList = partialSchedule.getProcessors();
//        numProcessors = processors;
//        duplicateDetector = new HashSet<>();
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
        int bWeight = bottomLevel[task] + cost + processorTimes[processor];
        int loadBalance = loadBalance(cost);
        int processort = processorTimes[processor] + cost + intGraph.weights[task];
        int candidateTime = Math.max(time.peek(), Math.max(bWeight, loadBalance));
        candidateTime = Math.max(candidateTime,processort);

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
    }



    public int done() {
//        bestSchedule.printTasks();
        System.out.println(bestSchedule.bestTime);
        //System.out.println(states);
        bestSchedule.writeToGraph(inputGraph);
//        System.out.println(prune);
        return bestSchedule.bestTime;
    }
}
