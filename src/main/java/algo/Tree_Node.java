package algo;

import java.util.ArrayList;
import java.util.Vector;
import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class Tree_Node{
    private Vector<Tree_Node> child = new Vector<>();
    private List<Processor> processors = new ArrayList<>();
    private Double current_time;

    public Vector<Tree_Node> getChild() {
        return child;
    }

    public void setChild(Vector<Tree_Node> child) {
        this.child = child;
    }

    public void addChild(Tree_Node child){
        this.child.add(child);
    }

    public Double getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(Double current_time) {
        this.current_time = current_time;
    }

    public List<Processor> getProcessors() {
        return processors;
    }

    public void setProcessors(List<Processor> processors) {
        this.processors = processors;
    }

    public void addProcessors(Processor processor){
        this.processors.add(processor);
    }
}

