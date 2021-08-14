package algo.Solution;

import algo.Schedule.BestSchedule;
import algo.CostFunctions.CriticalPath;
import algo.CostFunctions.CostCalculator;
import algo.CostFunctions.LoadBalancer;
import algo.Schedule.PartialSchedule;
import algo.Schedule.Task;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;

public class dfs {

    private PartialSchedule partialSchedule;
    private BestSchedule bestSchedule;
    private Graph graph;
    private HashMap<HashSet,ArrayList<Task>> memoOrder = new HashMap<>();
    private LoadBalancer loadBalancer;
    private CriticalPath criticalPath;

    //Optional, may remove.
    private long prune = 0;

    public dfs(int processors,Graph inputGraph) {
        graph = inputGraph;
        loadBalancer = LoadBalancer.init(inputGraph,processors);
        partialSchedule = new PartialSchedule(processors,inputGraph);
        bestSchedule = new BestSchedule();
        criticalPath = CriticalPath.init(inputGraph);
    }

    public ArrayList<Task> validOrder(HashSet<Task> scheduledTasks) {
        if (memoOrder.containsKey(scheduledTasks)) {
            return memoOrder.get(scheduledTasks);
        }
        ArrayList<Task> result = new ArrayList<>();

        for(int i = 0; i < graph.getNodeCount(); i++){
            Node node = graph.getNode(i);
            Task task = (Task)node.getAttribute("Task");
            List<Edge> parentEdges = task.getParentEdgeList();

            // if input is 0 size, find all root tasks
            if(parentEdges.size() == 0 && !scheduledTasks.contains(task)){
                result.add(task);
                continue;
            }

            boolean flag = true;
            for(Edge edge: parentEdges){
                Node parent = edge.getNode0();
                Task parentTask = (Task)parent.getAttribute("Task");
                if(!scheduledTasks.contains(parentTask)){
                    flag = false;
                    break;
                }
            }
            if(flag && !scheduledTasks.contains(task)){
                result.add(task);
            }
        }
        memoOrder.put(scheduledTasks,result);
        return result;
    }

    public void branchBound(Task task, int processor,int Cost) {
        int numProcessors = partialSchedule.getNumProcessors();
        int bWeight = criticalPath.getCriticalPath(task) + Cost;
        int loadBalance = loadBalancer.calculateLB(partialSchedule.idle + Cost);
        int candidateTime = Math.max(loadBalance,bWeight);

        if (bestSchedule.getBestTime() <= candidateTime) {
            prune += 1;
            return;
        }

        partialSchedule.scheduleTask(task,processor,Cost);

        HashSet scheduledTasks = partialSchedule.getScheduledTasks();
        ArrayList<Task> allPossibilities = validOrder(scheduledTasks);

        if (allPossibilities.isEmpty()) {
                int candidateBest = partialSchedule.getLatestScheduleTime();
                if (candidateBest < bestSchedule.getBestTime()) {
                    bestSchedule.makeCopy(candidateBest, partialSchedule.getProcessorList());
                }
        }

        PriorityQueue<CostCalculator> lowestCost = new PriorityQueue<>();

        for(int i = 0; i < allPossibilities.size(); i++) {
            for (int j = 0; j < numProcessors; j++) {
                CostCalculator estimate = new CostCalculator(allPossibilities.get(i),j);
                partialSchedule.getProcessorList()[j].estimateCost(estimate);
                lowestCost.add(estimate);
            }
        }

        while (!lowestCost.isEmpty()) {
            CostCalculator estimate = lowestCost.poll();
            Task candidateTask = estimate.getTask();
            branchBound(candidateTask,estimate.getProcNum(),estimate.estCost);
        }
        partialSchedule.removeTasks(task);
    }

    public int getBestSchedule() {
        System.out.println(prune);
        bestSchedule.done();
        return bestSchedule.getBestTime();
    }
}
