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

    public Processor[] getProcessorList() {
        return processorList;
    }

    public void setProcessorList(Processor[] processorList) {
        this.processorList = processorList;
    }
}
