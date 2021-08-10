package algo;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class dfs {

    private Schedule schedule;
    private Schedule bestSchedule;
    private int numProcessors;
    private Graph graph;
    public dfs(int processors) {
        schedule = new Schedule(processors);
        schedule.setLatestScheduleTime(Integer.MAX_VALUE);
        numProcessors = processors;
    }
    /**
     * TODO Given a list of tasks already scheduled, calculate all the possible tasks that can be scheduled. Ideally
     * TODO should memoize the solution into a hashmap so there are no repeated calculations.
     */
    public ArrayList<Task> validOrder(ArrayList<Task> scheduledTasks) {
        // hashMap and return list, and graph
        Map<Task, Boolean> map = new HashMap<>();
        ArrayList<Task> result = new ArrayList<>();
        int numberOfTasks = graph.getNodeCount();

        // if input is 0 size, find all root tasks
        if(scheduledTasks.size() == 0){
            for(int i = 0; i < numberOfTasks; i++){
                Node node = graph.getNode(i);
                List<Edge> edges = node.enteringEdges().collect(Collectors.toList());
                if(edges.size() == 0){
                    Task nodeTask = (Task)node.getAttribute("Task");
                    result.add(nodeTask);
                }
            }
            return result;
        }

        for(int i = 0; i < graph.getNodeCount(); i++){
            Node node = graph.getNode(i);
            Task task = (Task)node.getAttribute("Task");
            List<Edge> parentEdges = task.getParentEdgeList();
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
        /**
         * TODO Schedule task here.
         */
        ArrayList<Task> allPossibilities = validOrder(null);

        if (allPossibilities.isEmpty()) {
            int currentBest = bestSchedule.getLatestScheduleTime();
            int candidateBest = schedule.getLatestScheduleTime();
            if (candidateBest < currentBest) {
                bestSchedule = schedule;
            }
        }
        for(int i = 0; i < allPossibilities.size(); i++) {
            for (int j = 0; j < numProcessors; j++) {
                branchBound(allPossibilities.get(i),j);
            }
            /**
             * TODO After all children have been exhausted, remove the task from the schedule.
             */
        }
    }
}
