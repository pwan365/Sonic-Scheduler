package algo;

import io.InputReader;
//import javafx.util.Pair;
import org.apache.commons.math3.util.Pair;
import org.graphstream.algorithm.TopologicalSortKahn;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ValidScheduler {

    private Processor[] processorList;
    private Queue<Task> taskQueue;

    public Queue<Task> topologicalorder(Graph g){


        TopologicalSortKahn tp = new TopologicalSortKahn();
        tp.init(g);
        tp.compute();
        List<Node> nodeList = tp.getSortedNodes();
        List<Task> taskList = new ArrayList<Task>();
        for (Node n : nodeList){
            for (int i = 0; i < g.getNodeCount(); i++){
                if (g.getNode(i).getId() == n.getId()){
                    n = g.getNode(i);
                }
            }
            Task task = new Task(n);
            taskList.add(task);
            n.setAttribute("task",task);
        }
        taskQueue = new LinkedList<>(taskList);
        return taskQueue;

    }

    public void scheduleTasks() {
        while (!taskQueue.isEmpty()) {
            Task candidateTask = taskQueue.remove();
            Processor candidateProcessor = processorList[0];
            Double min_time = Double.MAX_VALUE;
            Double candidateTime = 0.0;
            for (int i = 0; i < processorList.length; i++) {
                candidateTime = processorList[i].getLatest_time();
                Double communicationCost = communicationCost(candidateTask,candidateProcessor);
                candidateTime = candidateTime + communicationCost;
                if (candidateTime < min_time) {
                    min_time = candidateTime;
                    candidateProcessor = processorList[i];
                }
            }
            addTaskToProcessor(candidateTask,candidateProcessor,candidateTime);
        }
    }

    public void addTaskToProcessor(Task task, Processor processor,Double candidateTime) {
        task.setAllocated_processor(processor);
        processor.setLatest_time(task.getDuration_time());
        processor.addTask(task, candidateTime, candidateTime + task.getDuration_time(),task.getDuration_time());
    }

    public double communicationCost(Task candidateTask,Processor candidateProcessor) {
        List<Edge> parents = candidateTask.getParent_edge_list();
        Double cost = 0.0;
        for (Edge parentEdge : parents) {
            Task parentTask = (Task)parentEdge.getAttribute("task");
            Processor parent_processor = parentTask.getAllocated_processor();
            if (parent_processor != candidateProcessor) {
                double candidateCost = (Double)parentEdge.getAttribute("Weight") + parent_processor.getTaskLatestTime(candidateTask);
                cost = Math.max(cost,candidateCost - candidateProcessor.getLatest_time());
            }
        }
        return cost;
    }


    public Processor[] getProcessorList() {
        return processorList;
    }

    public void setProcessorList(Processor[] processorList) {
        this.processorList = processorList;
    }
}
