package algo.Solution;

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
 */
public class ParallelSearch{

    public BestSchedule bestSchedule;
    private IntGraph graph;
    private int numProcessors;

    // Initial BranchAndBound class for initialization.
    private BranchAndBound bb;

    // Seen states of a BranchAndBound.
    public HashSet<Integer> seenStates = new HashSet<>();
    int graphWeight = 0;

    /**
     *
     * @param graph Input graph with all information stored in Integer
     * @param processors Number of processors.
     */
    public ParallelSearch(IntGraph graph, int processors){
        this.graph = graph;
        this.numProcessors = processors;
        bestSchedule = new BestSchedule();
        bb = new BranchAndBound(graph, processors, true);
        int [] weights = graph.weights;
        for (int i = 0; i< weights.length; i++) {
            graphWeight += weights[i];
        }
    }

    /**
     * Run the algorithm.
     */
    public void run() {
        boolean[] startTasks = bb.getOrder();
        int candidateTask = 0;
        int candidateProcessor = 0;
        int commCost = 0;
        for (int i = 0; i < bb.numTasks; i++) {
            if (startTasks[i]) {
                for (int j = 0; j < numProcessors; j++) {
                    candidateTask = i;
                    candidateProcessor = j;
                    commCost = bb.commCost(candidateTask, candidateProcessor);
                    break;
                }
                break;
            }
        }

        // Initialization of thread pool and invoking
        ForkJoinPool pool = new ForkJoinPool();
        RecursiveSearch re = new RecursiveSearch(bb, candidateTask, candidateProcessor, commCost);
        pool.invoke(re);
    }

    /**
     * Inner RecursiveSearch class. Each object of the class would contain the sub-task of the total scheduling.
     * The class extends RecursiveAction class which provides compute method for ForkJoinPool to invoke.
     */
    private class RecursiveSearch extends RecursiveAction{

        private BranchAndBound branchAndBound;
        private int task;
        private int processor;
        private int cost;

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
            int bWeight = branchAndBound.bottomLevel[task] + cost + branchAndBound.processorTimes[processor];
            int loadBalance = (int) Math.ceil((graphWeight + branchAndBound.idle + cost) / numProcessors);


            int candidateTime = Math.max(branchAndBound.time.peek(), Math.max(bWeight, loadBalance));

            if (bestSchedule.bestTime <= candidateTime) {
                return;
            } else {
                branchAndBound.addTask(task, processor, cost);
            }
            Set<Stack<Integer>> scheduleSet = new HashSet<>();
            Stack<Integer>[] stacks = new Stack[numProcessors];

            for (int i = 0; i < stacks.length; i++) {
                stacks[i] = new Stack<>();
            }

            // Check whether the Current schedule has been visited before.
            boolean seen;
            //Add tasks ids and start times to the stack which represents the processor
            for(int i = 0; i < branchAndBound.numTasks; i++){
                if(branchAndBound.taskInformation[i][0] != -1){
                    stacks[branchAndBound.taskProcessors[i]].add(i);
                    stacks[branchAndBound.taskProcessors[i]].add(branchAndBound.taskInformation[i][0]);
                }
            }
            for(Stack<Integer> stack : stacks) {
                scheduleSet.add(stack);
            }
            int id = scheduleSet.hashCode();
            synchronized (RecursiveSearch.class){
                if (seenStates.contains(id)) {
                    seen = true;
                } else {
                    seenStates.add(id);
                    seen = false;
                }
            }


            if (seen) {
                branchAndBound.removeTask(task,processor,cost);
                return;
            }

            if (branchAndBound.unscheduledTasks.isEmpty()) {
                int candidateBest = branchAndBound.time.peek();
                //synchronized (RecursiveSearch.class) {
                if (candidateBest < bestSchedule.bestTime) {
                    bestSchedule.makeCopy(candidateBest, branchAndBound.taskProcessors, branchAndBound.taskInformation);
                }
            }

            boolean[] candidateTasks = branchAndBound.getOrder();
            LinkedList<Integer> fto = branchAndBound.toFTOList(candidateTasks);
            if (fto != null) {
                int first = fto.poll();
                for (int i =0;i < branchAndBound.numTasks;i++) {
                    if (i != first) {
                        candidateTasks[i] = false;
                    }
                }
            }
            PriorityQueue<DSL> lowestCost = new PriorityQueue<>();

            for (int i = 0; i < branchAndBound.numTasks; i++) {
                if (candidateTasks[i]) {
                    boolean zero = false;
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
                        DSL dsl = new DSL(branchAndBound.bottomLevel[i],commCost,branchAndBound.processorTimes[j],i,j);
                        lowestCost.add(dsl);
                    }
                }
            }

            List<RecursiveSearch> list = new ArrayList<>();
            while (!lowestCost.isEmpty()) {
                DSL candidate = lowestCost.poll();
                int candidateTask = candidate.task;
                int processorID = candidate.processor;
                int candidateCost = candidate.cost;
                RecursiveSearch re = new RecursiveSearch(branchAndBound.deepCopy(), candidateTask, processorID, candidateCost);
                list.add(re);
            }
            invokeAll(list);
        }
    }
    public int done() {
        return bestSchedule.bestTime;
    }

}
