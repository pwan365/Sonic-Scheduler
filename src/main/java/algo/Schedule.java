package algo;

import java.util.Stack;

public class Schedule {
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
        scheduledTasks.push(task);
    }
    public void removeTasks() {
        Task removedTask = scheduledTasks.pop();
        removedTask.unSchedule();
    }
}
