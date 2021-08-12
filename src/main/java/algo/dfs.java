package algo;

import algo.CostFunctions.CriticalPath;
import algo.CostFunctions.Estimator;
import algo.CostFunctions.LoadBalancer;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;

public class dfs {

    private Schedule schedule;
    private Schedule bestSchedule;
    private Graph graph;
    private int numProcessors;
    private int bestTime = Integer.MAX_VALUE;
    private long prune = 0;
    private HashMap<HashSet,ArrayList<Task>> memoOrder = new HashMap<>();
    private HashMap<Task ,Integer> memoCriticalPath;

    public dfs(int processors,Graph inputGraph) {
        schedule = new Schedule(processors,inputGraph);
        graph = inputGraph;
        bestSchedule = null;
        numProcessors = processors;
        memoCriticalPath = new CriticalPath(inputGraph).getMemo();
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
//        System.out.println("Task:");
//        System.out.println(task.getNode().getId());
//        System.out.println("Processor:");
//        System.out.println(processor);
        HashSet unscheduledTasks = schedule.getUnscheduledTasks();
        int numProcessors = schedule.getNumProcessors();
        int earliestTime = schedule.getEarliestProcessorTime();
        int loadBalance = LoadBalancer.calculateLB(unscheduledTasks,numProcessors) + earliestTime;
        int criticalPath = memoCriticalPath.get(task) + Cost;
        int candidateTime = Math.max(loadBalance,criticalPath);
        if (bestTime <= candidateTime) {
            prune += 1;
            return;
        }
        schedule.scheduleTask(task,processor,Cost);

        HashSet scheduledTasks = schedule.getScheduledTasks();
        ArrayList<Task> allPossibilities = validOrder(scheduledTasks);
        if (allPossibilities.isEmpty()) {
            if (bestSchedule == null) {

                bestSchedule = schedule;
                bestTime = schedule.getLatestScheduleTime();
            }
            else {
//                int currentBest = bestSchedule.getLatestScheduleTime();
                int candidateBest = schedule.getLatestScheduleTime();
                if (candidateBest < bestTime) {
                    bestSchedule = schedule;
                    bestTime = schedule.getLatestScheduleTime();
                }
            }
        }
        PriorityQueue<Estimator> lowestCost = new PriorityQueue<>();

        for(int i = 0; i < allPossibilities.size(); i++) {
            for (int j = 0; j < numProcessors; j++) {
                Estimator estimate = new Estimator(allPossibilities.get(i),j);
                schedule.getProcessorList()[j].estimateCost(estimate);
                lowestCost.add(estimate);
//                branchBound(allPossibilities.get(i),j,estimate.estCost);
            }
        }
//        System.out.println("Estimates: ");
//        for (Estimator e : lowestCost) {
//            System.out.println(e.estCost);
//        }
        while (!lowestCost.isEmpty()) {
            Estimator estimate = lowestCost.poll();
            Task candidateTask = estimate.getTask();
            branchBound(candidateTask,estimate.getProcNum(),estimate.estCost);
        }
        schedule.removeTasks(task);
    }

    public int getBestSchedule() {
        System.out.println(prune);
        return bestTime;
    }
}
