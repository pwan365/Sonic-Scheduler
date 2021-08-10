package algo;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;
import java.util.stream.Collectors;

public class dfs {

    private Schedule schedule;
    private Schedule bestSchedule;
    private Graph graph;
    private int numProcessors;
    public dfs(int processors,Graph inputGraph) {
        schedule = new Schedule(processors);
        bestSchedule = new Schedule(processors);
        graph = inputGraph;
        bestSchedule.setLatestScheduleTime(Integer.MAX_VALUE);
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

    public void branchBound(Task task, int processor) throws CloneNotSupportedException {
        if(task != null){
            schedule.scheduleTask(task,processor);
        }
        System.out.print("Processor: ");
        System.out.print(processor);
        System.out.print("    Task: ");
        if(task != null){
            System.out.println(task.getNode().getIndex());
        } else{
            System.out.println("NULL");
        }
        Stack scheduledTasks = schedule.getScheduledTasks();
        ArrayList<Task> sT = new ArrayList<>(scheduledTasks);
        ArrayList<Task> allPossibilities = validOrder(sT);

        System.out.print("All possiblities:    ");
        for(Task temp: allPossibilities){
            System.out.print(temp.getNode().getIndex());
        }
        System.out.println("");
        System.out.print("st:    ");
        for(Task temp: sT){
            System.out.print(temp.getNode().getIndex());
        }
        System.out.println("");

        if (allPossibilities.isEmpty()) {
            int currentBest = bestSchedule.getLatestScheduleTime();
            int candidateBest = schedule.getLatestScheduleTime();
            if (candidateBest < currentBest) {
                bestSchedule = (Schedule)schedule.clone();
            }
        }

        for(int i = 0; i < allPossibilities.size(); i++) {
            scheduledTasks.push(allPossibilities.get(i));
            for (int j = 0; j < numProcessors; j++) {
                branchBound(allPossibilities.get(i),j);
            }
            schedule.removeTasks();
        }
    }

    public Schedule getBestSchedule() {
        return bestSchedule;
    }
}
