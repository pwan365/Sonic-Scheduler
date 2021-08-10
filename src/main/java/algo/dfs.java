package algo;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class dfs {

    private Schedule schedule;
    private Schedule bestSchedule;
    private int numProcessors;
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
        // hashMap and return list
        Map<Node, Boolean> map = new HashMap<>();
        ArrayList<Task> result = new ArrayList<>();

        for(Task task : scheduledTasks){
            Node node = task.getNode(); // scheduled task node
            if(map.get(node)){
                result.add(task); // if node is already in valid map, then we don't need to look into it
                continue;
            }
            ArrayList<Edge> parentEdges = task.getParentEdgeList();
            boolean flag = false;
            for(Edge e: parentEdges){
                Node parentNode = e.getNode0();
                for(Task scheduledTask : scheduledTasks) { // scan through all tasks to see whether the parent node exists
                    if(scheduledTask.getNode() == parentNode){
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    break;
                }
            }
            if(flag){
                map.put(node, true);
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
