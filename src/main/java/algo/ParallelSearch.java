package algo;

import algo.helpers.comparators.DLS;
import algo.helpers.pruning.FixedTaskOrder;
import org.graphstream.graph.Graph;

import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * ParallelSearch class runs the code of finding optimal schedule for a DAG in a multithreading environment,
 * given an input graph and number of processors.
 *
 * The level of parallelization would dependent on how many threads can the current machine provide.
 * The algorithm makes use of ScheduleState class which contains all the necessary information and methods to run the algorithm.
 * The principle of the algorithm is consistent with sequential search.
 *
 * @author Jason Wang, John Jia
 */
public class ParallelSearch extends RecursiveSearch implements VisualiseSearch {

    public BestSchedule bestSchedule;
    private final int numProcessors;

    // Initial SearchState class for initialization.
    private ScheduleState original_state;

    int graphWeight = 0;
    private final Graph inputGraph;
    private int state = 0;
    public int numOfCores;
    private final int numTasks;
    private IntGraph intGraph;

    /**
     *
     * @param graph Input graph with all information stored in Integer
     * @param processors Number of processors.
     */
    public ParallelSearch(Graph inputGraph, IntGraph graph, int processors, int numOfCores){
        this.numOfCores = numOfCores;
        this.inputGraph = inputGraph;
        this.numProcessors = processors;
        this.intGraph = graph;
        bestSchedule = new BestSchedule();
        original_state = new ScheduleState(graph, processors, true);
        int [] weights = graph.weights;
        for (int weight : weights) {
            graphWeight += weight;
        }
        numTasks = graph.tasks.length;
    }

    /**
     * Run the algorithm.
     */
    public void run() {
        boolean[] startTasks = original_state.getOrder();

        startTasks = FixedTaskOrder.checkFTO(startTasks,original_state.taskProcessors,
                original_state.taskInformation, original_state.intGraph.outEdges,
                original_state.intGraph.inEdges,numTasks);

        int candidateTask = 0;
        int candidateProcessor = 0;
        int commCost = 0;

        // Initialization of thread pool and invoking
        ForkJoinPool pool = new ForkJoinPool(numOfCores);
        for (int i = 0; i < numTasks; i++) {
            if (startTasks[i]) {
                candidateTask = i;
                commCost = original_state.commCost(candidateTask, candidateProcessor);
                ParallelRecursiveSearch re = new ParallelRecursiveSearch(original_state, candidateTask, candidateProcessor, commCost);
                pool.invoke(re);
            }
            original_state = new ScheduleState(intGraph, numProcessors, true);
        }
    }

    /**
     * Inner ParallelRecursiveSearch class. Each object of the class would contain the sub-task of the total scheduling.
     * The class extends RecursiveAction class which provides compute method for ForkJoinPool to invoke.
     */
    private class ParallelRecursiveSearch extends RecursiveAction{

        private final ScheduleState scheduleState;
        private final int task;
        private final int processor;
        private final int cost;

        /**
         *
         * @param scheduleState Deepcopy of last SearchState class for each thread to access their
         *                       individual information
         * @param task Candidate task for algorithm to run on.
         * @param processor Candidate processor for algorithm to run on the processor.
         * @param cost The cost of the task.
         */
        public ParallelRecursiveSearch(ScheduleState scheduleState, int task, int processor, int cost){
            this.scheduleState = scheduleState;
            this.task = task;
            this.processor = processor;
            this.cost = cost;
        }

        /**
         * The compute method is the overridden method from RecursiveAction class for ForkJoinPool to invoke.
         * This method contains the core algorithm from the sequential search. It searches through all the information
         * from the allocated SearchState class and recursively creates more thread to search for a deep copied
         * SearchState class to search on multiple threads.
         */
        @Override
        protected void compute() {
            synchronized (RecursiveSearch.class){
                state += 1;
            }
            //Check whether this state can be pruned.
            boolean pruned = prune(scheduleState,task,cost,processor,bestSchedule);
            if (pruned) {
                return;
            }

            //Add the task to the schedule.
            scheduleState.addTask(task, processor, cost);

            //Check whether we have seen this state.
            boolean seen = checkSeen(scheduleState,task,processor,cost);
            if (seen) {
                return;
            }

            // Update best schedule
            updateBestSchedule(bestSchedule, scheduleState);

            //Get a list of candidate tasks to be called, sorted by their DLS
            PriorityQueue<DLS> lowestCost = getCandidateTasks(scheduleState,bestSchedule);

            ArrayList<ParallelRecursiveSearch> list = new ArrayList<>();
            while (!lowestCost.isEmpty()) {
                DLS candidate = lowestCost.poll();
                int candidateTask = candidate.getTask();
                int processorID = candidate.getProcessor();
                int candidateCost = candidate.getCost();
                ParallelRecursiveSearch re = new ParallelRecursiveSearch(scheduleState.deepCopy(), candidateTask, processorID, candidateCost);
                list.add(re);
            }
            invokeAll(list);
        }
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
     * Write the information to the original input graph.
     * @return BestTime of the schedule
     */
    public int done() {
        bestSchedule.setGraphAttributes(inputGraph);
        return bestSchedule.bestTime;
    }

}
