package algo;

import java.util.HashSet;

public class Schedule {

    private Processor[] processorList;
    private int latestScheduleTime = 0;
    private HashSet<Task> scheduledSet = new HashSet<>();

    public Schedule(int numProcessors) {
        //Initialize Processor Pool
        processorList  = new Processor[numProcessors];
        for (int i = 0; i < numProcessors; i ++) {
            processorList[i] = new Processor(i+1);
        }
    }

    public int getLatestScheduleTime() {
        int time = 0;
        for (int i =0; i < processorList.length; i++) {
            time = Math.max(time,processorList[i].getLatestTime());
        }
        return time;
    }

    public void scheduleTask(Task task, int processorID) {
        processorList[processorID].addTask(task);
        scheduledSet.add(task);
        int candidateScheduleTime = processorList[processorID].getLatestTime();
        if (candidateScheduleTime > latestScheduleTime) {
            latestScheduleTime = candidateScheduleTime;
        }
    }
    public void removeTasks(Task task) {
        scheduledSet.remove(task);
        task.unSchedule();
    }

    public HashSet<Task> getScheduledTasks() {
        return this.scheduledSet;
    }


}
