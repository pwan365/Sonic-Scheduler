package algo;

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
    public dfs(int processors,Graph inputGraph) {
        schedule = new Schedule(processors,inputGraph);
        graph = inputGraph;
        bestSchedule = null;
        numProcessors = processors;
    }
    /**
     * TODO Given a list of tasks already scheduled, calculate all the possible tasks that can be scheduled. Ideally
     * TODO should memoize the solution into a hashmap so there are no repeated calculations.
     */
    public ArrayList<Task> validOrder(HashSet<Task> scheduledTasks) {
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
        return result;
    }

    public void branchBound(Task task, int processor) {
        HashSet unscheduledTasks = schedule.getUnscheduledTasks();
        int numProcessors = schedule.getNumProcessors();
        int loadBalance = LoadBalancer.calculateLB(unscheduledTasks,numProcessors);
        int earliestTime = schedule.getEarliestProcessorTime();
        int test = earliestTime;
        if (bestTime <= (loadBalance + earliestTime)) {
//            System.out.println("Pruned!");
            return;
        }
        schedule.scheduleTask(task,processor);

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

        for(int i = 0; i < allPossibilities.size(); i++) {
            for (int j = 0; j < numProcessors; j++) {
                branchBound(allPossibilities.get(i),j);
            }
        }
        schedule.removeTasks(task);
    }

    public int getBestSchedule() {
        return bestTime;
    }
}
