package algo;


import algo.helpers.comparators.DLS;
import algo.helpers.pruning.FixedTaskOrder;
import org.graphstream.graph.Graph;

import java.util.PriorityQueue;

/**
 * Class to run a sequential search using DFS branch and bound.
 *
 * @author Luxman Jeyarajah, Wayne Yao
 */
public class SequentialSearch extends RecursiveSearch implements VisualiseSearch{

    //Number of states examined.
    private int states = 0;
    private BestSchedule bestSchedule;
    private Graph inputGraph;
    private ScheduleState schedule;

    public SequentialSearch(Graph input, IntGraph graph, int processors) {
        schedule = new ScheduleState(graph,processors,true);
        bestSchedule = new BestSchedule();
        inputGraph = input;
    }

    /**
     * Initialize the recursive search.
     */
    public void run() {
        boolean[] candidateTasks = schedule.getOrder();
        // Check if there is a fixed task order.
        candidateTasks = FixedTaskOrder.checkFTO(candidateTasks,schedule.taskProcessors,
                schedule.taskInformation, schedule.intGraph.outEdges,
                schedule.intGraph.inEdges, schedule.numTasks);

        // Run all candidate tasks on first processor(other processors are duplicate states.)
        for (int i = 0; i < schedule.numTasks; i++) {
            if (candidateTasks[i]) {
                int commCost = schedule.commCost(i,0);
                branchBound(i, 0, commCost);
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
        //Update State count.
        states += 1;

        //Check whether this state can be pruned.
        boolean pruned = prune(schedule,task,cost,processor,bestSchedule);
        if (pruned) {
            return;
        }

        //Add the task to the current schedule state.
        schedule.addTask(task, processor, cost);

        //Check if this is a duplicate state and prune accordingly.
        boolean seen = checkSeen(schedule,task,processor,cost);
        if (seen) {
            return;
        }

        //Check whether we can update the bestSchedule
        updateBestSchedule(bestSchedule, schedule);

        // Get a list of the candidate tasks, sorted to be recursively called.
        PriorityQueue<DLS> lowestCost = getCandidateTasks(schedule,bestSchedule);

        // Recursively call the child tasks.
        recursiveCall(lowestCost,task,processor,cost);

    }

    /**
     * Method to recursively call the BranchBound method.
     * @param lowestCost The priority Queue containing the tasks to be recursively called.
     * @param task The current task that was scheduled.
     * @param processor The current processor that the task was scheduled.
     * @param cost The cost to schedule the task.
     */
    protected void recursiveCall(PriorityQueue<DLS> lowestCost,int task, int processor,int cost) {
        while (!lowestCost.isEmpty()) {
            DLS candidate = lowestCost.poll();
            int candidateTask = candidate.getTask();
            int processorID = candidate.getProcessor();
            int candidateCost = candidate.getCost();
            branchBound(candidateTask, processorID, candidateCost);
        }
        //Backtrack
        schedule.removeTask(task,processor,cost);
    }

    /**
     * Return the states explored.
     * @return Number of states explored.
     */
    @Override
    public int getStates() {
        return states;
    }

    /**
     * Return the best time of the schedule.
     * @return Current best time seen.
     */
    @Override
    public int getBestTime() {
        return bestSchedule.bestTime;
    }

    /**
     * Return the best schedule.
     * @return Current best schedule of the search.
     */
    @Override
    public BestSchedule getBestSchedule() {
        return bestSchedule;
    }

    /**
     * Write attributes to the graph for output and return best time.
     * @return Best time.
     */
    public int done() {
        bestSchedule.setGraphAttributes(inputGraph);
        return bestSchedule.bestTime;
    }
}
