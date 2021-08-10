package algo;

import java.util.Stack;

public class Schedule implements Cloneable {
    private Processor[] processorList;
    private int latestScheduleTime = 0;
    private Stack<Task> scheduledTasks = new Stack<>();
    public Schedule(int numProcessors) {
        //Initialize Processor Pool
        processorList  = new Processor[numProcessors];
        for (int i = 0; i < numProcessors; i ++) {
            processorList[i] = new Processor(i+1);
        }
    }

    public int getLatestScheduleTime() {
        return this.latestScheduleTime;
    }

    public void setLatestScheduleTime(int latestTime) {
        latestScheduleTime = latestTime;
    }

    public void scheduleTask(Task task, int processorID) {
        processorList[processorID].addTask(task);

        int candidateScheduleTime = processorList[processorID].getLatestTime();
        if (candidateScheduleTime > latestScheduleTime) {
            latestScheduleTime = candidateScheduleTime;
        }
    }
    public void removeTasks() {
        Task removedTask1 = scheduledTasks.pop();

        removedTask1.unSchedule();
    }
    public Stack getScheduledTasks() {
        return this.scheduledTasks;
    }
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
