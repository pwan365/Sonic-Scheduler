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
    //Optional, may remove.
    private long prune = 0;
    private Graph input;
    private int numProcessors;
    private Processor[] processorList;
    private HashSet<Integer> duplicateDetector;

    public SequentialSearch(int processors, Graph inputGraph) {
        input = inputGraph;
        partialSchedule = new PartialSchedule(processors,inputGraph);
        bestSchedule = new BestSchedule();
        criticalPath = CriticalPath.init(inputGraph);
        loadBalancer = LoadBalancer.init(inputGraph,processors);
        allOrders = AllOrders.init(inputGraph);
        duplicateStart = DuplicateStart.init();
        processorList = partialSchedule.getProcessors();
        numProcessors = processors;
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
        Processor candidateProcessor = processorList[processor];
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
        boolean [] candidateProcessors = normalise();
        for(int i = 0; i < allPossibilities.size(); i++) {
            for (int j = 0; j < numProcessors; j++) {
                if (candidateProcessors[j]) {

                    CommunicationCost startTask = new CommunicationCost(allPossibilities.get(i),
                            partialSchedule.getProcessors()[j],partialSchedule,criticalPath.
                            getCriticalPath(allPossibilities.get(i)));

                    int hashCode = hashCodeGenerator(numProcessors,allPossibilities.get(i),j,startTask.startTime());
                    if (!duplicateDetector.contains(hashCode)) {
                        lowestCost.add(startTask);
                        duplicateDetector.add(hashCode);
                    }
                    else {
                        prune += 1;
                    }

                }
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

    private boolean [] normalise() {
        boolean zeroFlag = false;
        boolean [] processorStarted = new boolean[numProcessors];
        for (int i = 0; i < numProcessors; i ++) {
            if (processorList[i].getTime() == 0) {
                if (!zeroFlag) {
                    zeroFlag = true;
                    processorStarted[i] = true;
                }
                else {
                    processorStarted[i] = false;
                }
            }
            else {
                processorStarted[i] = true;
            }
        }
        return processorStarted;
    }

    public int hashCodeGenerator(int numberOfProcessors,Task candTask, int candProcessor, int start) {
        HashSet<Task> scheduledTasks = partialSchedule.getScheduledTasks();
        Set<Stack<Integer>> scheduleSet = new HashSet<>();
        Stack<Integer>[] stacks = new Stack[numberOfProcessors];

        for (int i = 0; i < stacks.length; i++){
            stacks[i] = new Stack<>();
        }

        for(Task scheduledTask: scheduledTasks){
            int startTime = scheduledTask.getStartingTime();
            int allocatedProcessor = scheduledTask.getAllocatedProcessor().getProcessNum();
                stacks[allocatedProcessor - 1].add(scheduledTask.getNode().getIndex());
                stacks[allocatedProcessor - 1].add(startTime);
        }
        stacks[candProcessor].add(candTask.getNode().getIndex());
        stacks[candProcessor].add(start);

        for(Stack<Integer> stack : stacks){
            scheduleSet.add(stack);
        }

        return scheduleSet.hashCode();

    }

    public int getBestSchedule() {
        System.out.println("PRUNED");
        System.out.println(prune);
        bestSchedule.done();
        bestSchedule.writeToGraph(input);
        System.out.println(bestSchedule.getTime());
        return bestSchedule.getTime();
    }
}
