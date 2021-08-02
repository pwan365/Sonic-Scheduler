package algo;

import java.util.ArrayList;
import java.util.HashMap;


public class Processor {
    private Double latest_time = 0.0;
    private HashMap<Task, ArrayList<Double>> node_table = new HashMap<>();
    private int processNum;

    public Processor(int processNumber) {
        this.processNum = processNumber;
    }

    public Double getLatestTime() {
        return latest_time;
    }

    public void setLatestTime(Double latest_time) {
        this.latest_time = latest_time;
    }


    public void addTask(Task task, Double start, Double end, Double duration){
        ArrayList<Double> task_info = new ArrayList<>();
        task_info.add(start);
        task_info.add(end);
        task_info.add(duration);
        this.node_table.put(task,task_info);
    }

    public HashMap<Task, ArrayList<Double>> getTasks() {
        return node_table;
    }

    public int getProcessNum() {
        return processNum;
    }
}