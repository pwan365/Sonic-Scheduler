package algo;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.HashSet;

public class Schedule {

    private Processor[] processorList;
    private int latestScheduleTime = 0;
    private HashSet<Task> scheduledSet = new HashSet<>();
    private HashSet<Task> unscheduledSet = new HashSet<>();

    public Schedule(int numProcessors,Graph graph) {
        //Initialize Processor Pool
        processorList  = new Processor[numProcessors];
        for (int i = 0; i < numProcessors; i ++) {
            processorList[i] = new Processor(i+1);
        }
        //Initialize Unscheduled Tasks Set
        for(int i = 0; i < graph.getNodeCount(); i++) {
            Node node = graph.getNode(i);
            Task task = (Task) node.getAttribute("Task");
            unscheduledSet.add(task);
        }
    }

    public int getLatestScheduleTime() {
        int time = 0;
        for (int i =0; i < processorList.length; i++) {
            time = Math.max(time,processorList[i].getLatestTime());
        }
        return time;
    }

    public void scheduleTask(Task task, int processorID,int est) {
        processorList[processorID].addTask(task,est);
        scheduledSet.add(task);
        unscheduledSet.remove(task);
//        int candidateScheduleTime = processorList[processorID].getLatestTime();
//        if (candidateScheduleTime > latestScheduleTime) {
//            latestScheduleTime = candidateScheduleTime;
//        }
    }
    public void removeTasks(Task task) {
        scheduledSet.remove(task);
        unscheduledSet.add(task);
        task.unSchedule();
    }

    public HashSet<Task> getScheduledTasks() {
        return this.scheduledSet;
    }

    public int getNumProcessors() {
        return this.processorList.length;
    }

    public Processor[] getProcessorList() {
        return processorList;
    }

    public HashSet getUnscheduledTasks() {
        return this.unscheduledSet;
    }

    public int getEarliestProcessorTime() {
        int time = Integer.MAX_VALUE;
        for (int i =0; i < processorList.length; i++) {
            time = Math.min(time,processorList[i].getLatestTime());
        }
        return time;
    }
}
