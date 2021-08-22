package algo;

import algo.helpers.pruning.BottomLevel;
import algo.helpers.pruning.LoadBalancer;
import algo.helpers.hashCode.HashCodeStorage;
import algo.helpers.pruning.FixedTaskOrder;
import org.graphstream.graph.Graph;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * ParallelSearch class runs the code of finding optimal schedule for a DAG in a multithreading environment,
 * given an input graph and number of processors.
 *
 * The level of parallelization would dependent on how many threads can the current machine provide.
 * The algorithm makes use of BranchAndBound class which contains all the necessary information and methods to run the algorithm.
 * The principle of the algorithm is consistent with sequential search.
 *
 * @author Jason Wang, John Jia
 */
public class ParallelSearch implements GUISchedule{

    public BestSchedule bestSchedule;
    private final int numProcessors;

    // Initial BranchAndBound class for initialization.
    private final BranchAndBound original_state;

    // Seen states of a BranchAndBound.
    public HashSet<Integer> seenStates = new HashSet<>();
    public LinkedList<Integer>[] equivalentList;
    int graphWeight = 0;
    private final Graph inputGraph;
    private int state = 0;
    public int numOfCores;
    private final int numTasks;

    /**
     *
     * @param graph Input graph with all information stored in Integer
     * @param processors Number of processors.
     */
    public ParallelSearch(Graph inputGraph, IntGraph graph, int processors, int numOfCores){
        this.numOfCores = numOfCores;
        this.inputGraph = inputGraph;
        this.numProcessors = processors;
        bestSchedule = new BestSchedule();
        original_state = new BranchAndBound(graph, processors, true);
        int [] weights = graph.weights;
        for (int weight : weights) {
            graphWeight += weight;
        }
        equivalentList = original_state.getEquivalentNodes();
        numTasks = graph.tasks.length;
    }

    /**
     * Run the algorithm.
     */
    public void run() {
        boolean[] startTasks = original_state.getOrder();
        LinkedList<Integer> fto = FixedTaskOrder.getFTO(startTasks,original_state.taskProcessors,
                original_state.taskInformation, original_state.intGraph.outEdges, original_state.intGraph.inEdges);
        if (fto != null){
            int first = fto.poll();
            for (int i = 0; i < numTasks; i++){
                if (i != first){
                    startTasks[i] = false;
                }
            }
        }
        int candidateTask = 0;
        int candidateProcessor = 0;
        int commCost = 0;
        for (int i = 0; i < numTasks; i++) {
            if (startTasks[i]) {
                candidateTask = i;
                commCost = original_state.commCost(candidateTask, candidateProcessor);
                break;
            }
        }

        // Initialization of thread pool and invoking
        ForkJoinPool pool = new ForkJoinPool(numOfCores);
        RecursiveSearch re = new RecursiveSearch(original_state, candidateTask, candidateProcessor, commCost);
        pool.invoke(re);
    }

    @Override
    public int getBestTime() {
        return bestSchedule.bestTime;
    }

    @Override
    public BestSchedule getBestSchedule() {
        return bestSchedule;
    }

    @Override
    public int getStates() {
        return state;
    }

    /**
     * Inner RecursiveSearch class. Each object of the class would contain the sub-task of the total scheduling.
     * The class extends RecursiveAction class which provides compute method for ForkJoinPool to invoke.
     */
    private class RecursiveSearch extends RecursiveAction{

        private final BranchAndBound branchAndBound;
        private final int task;
        private final int processor;
        private final int cost;

        /**
         *
         * @param branchAndBound Deepcopy of last BranchAndBound class for each thread to access their
         *                       individual information
         * @param task Candidate task for algorithm to run on.
         * @param processor Candidate processor for algorithm to run on the processor.
         * @param cost The cost of the task.
         */
        public RecursiveSearch(BranchAndBound branchAndBound, int task, int processor, int cost){
            this.branchAndBound = branchAndBound;
            this.task = task;
            this.processor = processor;
            this.cost = cost;
        }

        /**
         * The compute method is the overridden method from RecursiveAction class for ForkJoinPool to invoke.
         * This method contains the core algorithm from the sequential search. It searches through all the information
         * from the allocated BranchAndBound class and recursively creates more thread to search for a deep copied
         * BranchAndBound class to search on multiple threads.
         */
        @Override
        protected void compute() {
            synchronized (RecursiveSearch.class){
                state += 1;
            }
            int bWeight = BottomLevel.pruneBLevel(task,cost,branchAndBound.processorTimes[processor]);
            int loadBalance = LoadBalancer.calculateLoadBalance(branchAndBound.idle,cost,numProcessors);
            int candidateTime = Math.max(branchAndBound.time.peek(), Math.max(bWeight, loadBalance));

            // If the candidate time is less than best time, then exit.
            if (bestSchedule.bestTime <= candidateTime) {
                return;
            } else {
                branchAndBound.addTask(task, processor, cost);
            }



            // Check whether the Current schedule has been visited before.
            boolean seen = HashCodeStorage.checkIfSeen(branchAndBound.taskInformation, branchAndBound.taskProcessors,
                    numProcessors,numTasks);

            // If so, exit.
            if (seen) {
                branchAndBound.removeTask(task,processor,cost);
                return;
            }

            // Update best schedule
            if (branchAndBound.scheduled == branchAndBound.numTasks) {
                int candidateBest = branchAndBound.time.peek();
                if (candidateBest < bestSchedule.bestTime) {
                    bestSchedule.makeCopy(candidateBest,branchAndBound.taskProcessors,branchAndBound.taskInformation);
                }
            }

            boolean[] candidateTasks = branchAndBound.getOrder();
            LinkedList<Integer> fto = FixedTaskOrder.getFTO(candidateTasks,branchAndBound.taskProcessors,
                    branchAndBound.taskInformation, branchAndBound.intGraph.outEdges, branchAndBound.intGraph.inEdges);

            if (fto != null) {
                int first = fto.poll();
                for (int i =0;i < branchAndBound.numTasks;i++) {
                    if (i != first) {
                        candidateTasks[i] = false;
                    }
                }
            }
            PriorityQueue<DLS> lowestCost = new PriorityQueue<>();

            HashSet<Integer> seenTasks = new HashSet<>();
            for (int i = 0; i < branchAndBound.numTasks; i++) {
                if (candidateTasks[i]) {
                    boolean zero = false;
                    if(seenTasks.contains(i)){
                        continue;
                    }else{
                        seenTasks.addAll(equivalentList[i]);
                    }
                    for (int j = 0; j < numProcessors; j++) {
                        if (branchAndBound.processorTimes[j] == 0) {
                            if (zero) {
                                continue;
                            }
                            else {
                                zero = true;
                            }
                        }
                        int commCost = branchAndBound.commCost(i,j);
                        int bottomLevel = BottomLevel.returnBLevel(task);
                        DLS DLS = new DLS(bottomLevel,commCost,branchAndBound.processorTimes[j],i,j);
                        lowestCost.add(DLS);
                    }
                }
            }

            List<RecursiveSearch> list = new ArrayList<>();
            while (!lowestCost.isEmpty()) {
                DLS candidate = lowestCost.poll();
                int candidateTask = candidate.task;
                int processorID = candidate.processor;
                int candidateCost = candidate.cost;
                RecursiveSearch re = new RecursiveSearch(branchAndBound.deepCopy(), candidateTask, processorID, candidateCost);
                list.add(re);
            }
            invokeAll(list);
        }
    }

    /**
     * Write the information to the original input graph.
     * @return BestTime of the schedule
     */
    public int done() {
        bestSchedule.writeToGraph(inputGraph);
        return bestSchedule.bestTime;
    }

}
