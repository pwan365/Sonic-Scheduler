package algo;

import io.InputReader;
import org.graphstream.algorithm.TopologicalSortKahn;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ValidScheduler {

    private Processor[] processorList;
    private Queue<Task> taskQueue;

    public Queue<Task> topologicalorder(){

        InputReader reader = new InputReader("input.dot");
        Graph g = reader.read();

        TopologicalSortKahn tp = new TopologicalSortKahn();
        tp.init(g);
        tp.compute();
        List<Node> nodeList = tp.getSortedNodes();
        List<Task> taskList = new ArrayList<Task>();
        for (Node n : nodeList){
            taskList.add(new Task(n));
        }
        taskQueue = new LinkedList<>(taskList);
        return taskQueue;

    }

    public void scheduleTasks() {
        while (!taskQueue.isEmpty()) {
            Task candidateTask = taskQueue.remove();
            Processor candidateProcessor = processorList[0];
            Double min_time = Double.MAX_VALUE;
            for (int i = 0; i < processorList.length; i++) {
                Double candidateTime = processorList[i].getLatest_time();
                if (candidateTime < min_time) {
                    min_time = candidateTime;
                    candidateProcessor = processorList[i];
                }
            }
            candidateTask.setAllocated_processor(candidateProcessor);
            candidateProcessor.setLatest_time(candidateTask.getFinishing_time()-candidateTask.getStarting_time());
            candidateProcessor.addTask(candidateTask);
        }

    }

    public Processor[] getProcessorList() {
        return processorList;
    }

    public void setProcessorList(Processor[] processorList) {
        this.processorList = processorList;
    }
}
