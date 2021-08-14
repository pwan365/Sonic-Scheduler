package algo.Solution;

import algo.Schedule.BestSchedule;
import algo.CostFunctions.CriticalPath;
import algo.CostFunctions.LoadBalancer;
import algo.Schedule.PartialSchedule;
import algo.Schedule.Task;
import org.graphstream.graph.Graph;

import java.util.*;

/**
 * Class to run a sequential search using DFS branch and bound.
 *
 * @author Luxman Jeyarajah
 */
public class SequentialSearch extends BranchAndBound {

    private PartialSchedule partialSchedule;
    private BestSchedule bestSchedule;
    private LoadBalancer loadBalancer;
    private CriticalPath criticalPath;
    private AllOrders allOrders;
    //Optional, may remove.
    private long prune = 0;

    public SequentialSearch(int processors, Graph inputGraph) {
        partialSchedule = new PartialSchedule(processors,inputGraph);
        bestSchedule = new BestSchedule();
        criticalPath = CriticalPath.init(inputGraph);
        loadBalancer = LoadBalancer.init(inputGraph,processors);
        allOrders = AllOrders.init(inputGraph);
    }

    /**
     * Recursive function that goes through all possible schedules and finds the one with the earliest schedule time.
     * @param task Task to be scheduled.
     * @param processor Processor to schedule the task on.
     * @param Cost Cost to schedule the task on the processor.
     */
    public void branchBound(Task task, int processor,int Cost) {
        int numProcessors = partialSchedule.getNumProcessors();
        int bWeight = criticalPath.getCriticalPath(task) + Cost;
        int loadBalance = loadBalancer.calculateLB(partialSchedule.idle + Cost);
        int candidateTime = Math.max(loadBalance,bWeight);

        if (bestSchedule.getTime() <= candidateTime) {
            prune += 1;
            return;
        }

        partialSchedule.scheduleTask(task,processor,Cost);

        HashSet<Task> scheduledTasks = partialSchedule.getScheduledTasks();
        ArrayList<Task> allPossibilities = allOrders.getOrder(scheduledTasks);

        if (allPossibilities.isEmpty()) {
                int candidateBest = partialSchedule.getTime();
                if (candidateBest < bestSchedule.getTime()) {
                    bestSchedule.makeCopy(candidateBest, partialSchedule.getProcessors());
                }
        }

        PriorityQueue<CommunicationCost> lowestCost = new PriorityQueue<>();

        for(int i = 0; i < allPossibilities.size(); i++) {
            for (int j = 0; j < numProcessors; j++) {
                CommunicationCost start = new CommunicationCost(allPossibilities.get(i),
                        partialSchedule.getProcessors()[j]);
                lowestCost.add(start);
            }
        }

        while (!lowestCost.isEmpty()) {
            CommunicationCost candidate = lowestCost.poll();
            Task candidateTask = candidate.getTask();
            int processorID = candidate.getProcessorID();
            int startTime = candidate.startTime();
            branchBound(candidateTask,processorID,startTime);
        }
        partialSchedule.removeTasks(task);
    }

    public int getBestSchedule() {
        System.out.println(prune);
        bestSchedule.done();
        return bestSchedule.getTime();
    }
}
