package algo;

import org.graphstream.graph.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Task {
    private double finishing_time = -1.0;
    private double starting_time;
    private double duration_time;
    private List<Edge> parent_edge_list = new ArrayList<Edge>();
    private Node node;
    private Processor allocated_processor;

    public Task(Node node){
        this.node = node;
        this.duration_time = (Double)node.getAttribute("Weight");
        List<Edge> edges = node.enteringEdges().collect(Collectors.toList());
        for(Edge e : edges){
            parent_edge_list.add(e);
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

    // Parent nodes setter and getting
    public List<Node> getNode_list() {
        return parent_node_list;
    }

    // Processor getter and setter
    public Processor getAllocated_processor() {
        return allocated_processor;
    }

    public void setAllocated_processor(Processor allocated_processor) {
        this.allocated_processor = allocated_processor;
    }

    // Starting time getter and setter
    public double getStarting_time() {
        return starting_time;
    }

    public void setStarting_time(double starting_time) {
        this.starting_time = starting_time;
    }

    public double getDuration_time() {
        return duration_time;
    }
}
