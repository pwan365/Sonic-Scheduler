package algo;

import org.graphstream.graph.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Task {
    private double finishing_time = -1.0;
    private double starting_time;
    private double duration_time;
    private List<Node> node_list = new ArrayList<Node>();
    private Node node;
    private Processor allocated_processor;

    public Task(Node node){
        this.node = node;
        this.duration_time = (Double)node.getAttribute("Weight");
        List<Edge> edges = node.leavingEdges().collect(Collectors.toList());
        for(Edge e : edges){
            node_list.add(e.getNode1());
        }
    }

    public double getFinishing_time() {
        return finishing_time;
    }

    public void setFinishing_time(double finishing_time) {
        this.finishing_time = finishing_time;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public List<Node> getNode_list() {
        return node_list;
    }

    public Processor getAllocated_processor() {
        return allocated_processor;
    }

    public void setAllocated_processor(Processor allocated_processor) {
        this.allocated_processor = allocated_processor;
    }

    public double getStarting_time() {
        return starting_time;
    }

    public void setStarting_time(double starting_time) {
        this.starting_time = starting_time;
    }
}
