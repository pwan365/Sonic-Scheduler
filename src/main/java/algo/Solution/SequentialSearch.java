package algo.Solution;

import algo.Schedule.BestSchedule;
import algo.CostFunctions.CriticalPath;
import algo.CostFunctions.LoadBalancer;
import algo.Schedule.PartialSchedule;
import algo.Schedule.Processor;
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
    private DuplicateStart duplicateStart;
    private int numProcessors;
    //Optional, may remove.
    private long prune = 0;
    private Graph input;
    private int states = 0;

    public SequentialSearch(int processors, Graph inputGraph) {
        numProcessors = processors;
        input = inputGraph;
        partialSchedule = new PartialSchedule(processors,inputGraph);
        bestSchedule = new BestSchedule();
        criticalPath = CriticalPath.init(inputGraph);
        loadBalancer = LoadBalancer.init(inputGraph,processors);
        allOrders = AllOrders.init(inputGraph);
        duplicateStart = DuplicateStart.init();
    }

    public void schedule() {
        allOrders = AllOrders.init(input);

        HashSet<Task> empty = new HashSet<>();
        ArrayList<Task> tasks = allOrders.getOrder(empty);
        for (Task task : tasks) {
            for(int i=0; i<numProcessors;i++) {
                branchBound(task,i,0);
            }

        }
    }
    /**
     * Recursive function that goes through all possible schedules and finds the one with the earliest schedule time.
     * @param task Task to be scheduled.
     * @param processor Processor to schedule the task on.
     * @param cost Cost to schedule the task on the processor.
     */
    public void branchBound(Task task, int processor,int cost) {
//        System.out.println("Processor");
//        System.out.println(processor);
//        System.out.println("PROCESSOR TIME");
//        System.out.println(partialSchedule.getProcessors()[processor].getTime());
//        System.out.println("COST");
//        System.out.println(cost);
//        System.out.println("TASK");
//        System.out.println(task.getNode().getId());

//        System.out.println(cost);
        Processor candidateProcessor = partialSchedule.getProcessors()[processor];
        int start = partialSchedule.getTime();
        int bWeight = criticalPath.getCriticalPath(task)+ cost + candidateProcessor.getTime();
        int loadBalance = loadBalancer.calculateLB(partialSchedule.idle + cost);
        int candidateTime = Math.max(start, Math.max(bWeight, loadBalance));
        boolean checkZeroTask =  duplicateStart.checkZeroTask(candidateProcessor,task,cost);
        if (bestSchedule.getTime() <= candidateTime || checkZeroTask) {
            prune += 1;
            return;
        }

        partialSchedule.scheduleTask(task,processor,cost);

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
                CommunicationCost startTask = new CommunicationCost(allPossibilities.get(i),
                        partialSchedule.getProcessors()[j],partialSchedule);
                lowestCost.add(startTask);
            }
        }

        while (!lowestCost.isEmpty()) {
            CommunicationCost candidate = lowestCost.poll();
            Task candidateTask = candidate.getTask();
            int processorID = candidate.getProcessorID();
            int candidateCost = candidate.commCost();
            branchBound(candidateTask,processorID,candidateCost);
        }
        partialSchedule.removeTasks(task);
        states += 1;
    }

    public int getStates() {
        return states;
    }


    public int getBestSchedule() {
        System.out.println("PRUNED");
        System.out.println(prune);
        bestSchedule.done();
        bestSchedule.writeToGraph(input);
        return bestSchedule.getTime();
    }
}
