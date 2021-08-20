package algo.Solution;

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

    public SequentialSearch(IntGraph graph, int processors) {
        super(graph,processors);
        bestSchedule = new BestSchedule();
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
        PriorityQueue<DSL> lowestCost = new PriorityQueue<>();
//        boolean[] candidateProcessors = normalise();
        for (int i = 0; i < numTasks; i++) {
            if (candidateTasks[i]) {
                for (int j = 0; j < numProcessors; j++) {
                        int commCost = commCost(i,j);
                        DSL dsl = new DSL(bottomLevel[i],commCost,processorTimes[j],i,j);
                        lowestCost.add(dsl);
                }
            }
        }

//
//                    CommunicationCost startTask = new CommunicationCost(allPossibilities.get(i),
//                            partialSchedule.getProcessors()[j], partialSchedule, criticalPath.
//                            getCriticalPath(allPossibilities.get(i)));
//
//                    int hashCode = hashCodeGenerator(numProcessors, allPossibilities.get(i), j, startTask.startTime());
//                    if (!duplicateDetector.contains(hashCode)) {
//                        lowestCost.add(startTask);
//                        duplicateDetector.add(hashCode);
//                    } else {
//                        prune += 1;
//                    }
            while (!lowestCost.isEmpty()) {
                DSL candidate = lowestCost.poll();
                int candidateTask = candidate.task;
                int processorID = candidate.processor;
                int candidateCost = candidate.cost;
                branchBound(candidateTask, processorID, candidateCost);
            }
//            partialSchedule.removeTasks(task);
            removeTask(task,processor,cost);
            states += 1;

    }


//    public int getBestSchedule() {
//        bestSchedule.done();
//        bestSchedule.writeToGraph(input);
//        System.out.println(bestSchedule.getTime());
//        System.out.println(states);
//        System.out.println(s);
//        return bestSchedule.getTime();
//    }

    public int done() {
//        bestSchedule.printTasks();
        System.out.println(bestSchedule.bestTime);
        System.out.println(states);
//        System.out.println(prune);
        return bestSchedule.bestTime;
    }
}
