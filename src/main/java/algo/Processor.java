package algo;

import java.util.List;
import java.util.ArrayList;
import org.graphstream.graph.Node;
import java.util.HashMap;


public class Processor {
    private Double earliest_time = 0.0;
    private Double latest_time = 0.0;
    private ArrayList<Task> task_list = new ArrayList<>();
    private HashMap<String, ArrayList<Double>> node_table = new HashMap<>();

    public Double getEarliest_time() {
        return earliest_time;
    }

    public void setEarliest_time(Double earliest_time) {
        this.earliest_time = earliest_time;
    }

    public Double getLatest_time() {
        return latest_time;
    }

    public void setLatest_time(Double additional_time) {
        this.latest_time += additional_time;
    }

    public HashMap<String, ArrayList<Double>> getNode_table() {
        return node_table;
    }

    public ArrayList<Task> getTask_list() {
        return task_list;
    }

    public void setTask_list(ArrayList<Task> task_list) {
        this.task_list = task_list;
    }

    public void addTask(Task task){
        this.task_list.add(task);
    }

    public boolean isEmpty(){
        if (this.task_list.isEmpty()){
            return true;
        }
        return false;
    }



}
