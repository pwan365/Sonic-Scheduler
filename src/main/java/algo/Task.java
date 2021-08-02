package algo;

import org.graphstream.graph.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Task {
    private double finishingTime = -1.0;
    private double startingTime;
    private double durationTime;
    private ArrayList<Edge> parentEdgeList = new ArrayList<Edge>();
    private Node node;
    private Processor allocatedProcessor;

    public Task(Node node){
        this.node = node;
        this.durationTime = (Double)node.getAttribute("Weight");
        List<Edge> edges = node.enteringEdges().collect(Collectors.toList());
        for(Edge e : edges){
            parentEdgeList.add(e);
        }
    }

    public double getFinishingTime() {
        return finishingTime;
    }

    public Node getNode() {
        return node;
    }


    public void setNode(Node node) {
        this.node = node;
    }

    // Parent nodes setter and getting
    public ArrayList<Edge> getParentEdgeList() {
        return parentEdgeList;
    }

    // Processor getter and setter
    public Processor getAllocatedProcessor() {
        return allocatedProcessor;
    }

    public void setTaskDetails(Processor allocated_processor,Double finishing_time,Double starting_time) {
        this.allocatedProcessor = allocated_processor;
        this.finishingTime = finishing_time;
        this.startingTime = starting_time;
    }

    // Starting time getter and setter
    public double getStartingTime() {
        return startingTime;
    }

    public double getDurationTime() {
        return durationTime;
    }
}
