package algo.Solution;

import algo.Schedule.*;
import algo.CostFunctions.CriticalPath;
import algo.CostFunctions.LoadBalancer;
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
    //Optional, may remove.
    private long prune = 0;
    private Graph input;
    private HashSet<Integer> duplicateDetector;

    public SequentialSearch(int processors, Graph inputGraph) {
        input = inputGraph;
        partialSchedule = new PartialSchedule(processors,inputGraph);
        bestSchedule = new BestSchedule();
        criticalPath = CriticalPath.init(inputGraph);
        loadBalancer = LoadBalancer.init(inputGraph,processors);
        allOrders = AllOrders.init(inputGraph);
        duplicateStart = DuplicateStart.init();
        duplicateDetector = new HashSet<>();
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
        int numProcessors = partialSchedule.getNumProcessors();
        int bWeight = criticalPath.getCriticalPath(task)+ cost + candidateProcessor.getTime();
        int loadBalance = loadBalancer.calculateLB(partialSchedule.idle + cost);
        int candidateTime = Math.max(start, Math.max(bWeight, loadBalance));
        boolean checkZeroTask =  duplicateStart.checkZeroTask(candidateProcessor,task,cost);
        if (bestSchedule.getTime() <= candidateTime || checkZeroTask) {
            prune += 1;
            return;
        }

        int hashCode = hashCodeGenerator(numProcessors);

        for (Task scheduledTask: partialSchedule.getScheduledTasks()){
            System.out.println("Task start time " + scheduledTask.getStartingTime());
            System.out.println("Task processor " + scheduledTask.getAllocatedProcessor().getProcessNum());
        }

        System.out.println(hashCode);
        if (duplicateDetector.contains(hashCode)){
            prune += 1;
            return;
        } else {
            duplicateDetector.add(hashCode);
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
    }

    public int getBestSchedule() {
        System.out.println("PRUNED");
        System.out.println(prune);
        bestSchedule.done();
        bestSchedule.writeToGraph(input);
        return bestSchedule.getTime();
    }

    public int hashCodeGenerator(int numberOfProcessors) {
        HashSet<Task> scheduledTasks = partialSchedule.getScheduledTasks();
        Set<Stack<Integer>> scheduleSet = new HashSet<>();
        Stack<Integer>[] stacks = new Stack[numberOfProcessors];

        for (int i = 0; i < stacks.length; i++){
            stacks[i] = new Stack<>();
        }

        for(Task scheduledTask: scheduledTasks){
            int startTime = scheduledTask.getStartingTime();
            int allocatedProcessor = scheduledTask.getAllocatedProcessor().getProcessNum();
            if(startTime!=-1){
                stacks[allocatedProcessor - 1].add(scheduledTask.getNode().getIndex());
                stacks[allocatedProcessor - 1].add(startTime);
            }
        }

        for(Stack<Integer> stack : stacks){
            scheduleSet.add(stack);
        }

        return scheduleSet.hashCode();

    }
}
