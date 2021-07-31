package algo;

import java.util.List;
import java.util.ArrayList;
import org.graphstream.graph.Node;
import java.util.HashMap;


public class Processor {
    private Double latest_time = 0.0;
    private ArrayList<Task> task_list = new ArrayList<>();
    private HashMap<Task, ArrayList<Double>> node_table = new HashMap<>();

    public Double getLatest_time() {
        return latest_time;
    }

    public void setLatest_time(Double additional_time) {
        this.latest_time += additional_time;
    }


    public void addTask(Task task, Double start, Double end, Double duration){
        this.task_list.add(task);
        ArrayList<Double> task_info = new ArrayList<>();
        task_info.add(start);
        task_info.add(end);
        task_info.add(duration);
        this.node_table.put(task,task_info);
    }

    public Double getTaskLatestTime(Task task) {
        ArrayList<Double> task_info = node_table.get(task);
        return task_info.get(1);
    }

}
