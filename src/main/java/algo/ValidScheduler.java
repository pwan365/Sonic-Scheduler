package algo;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import java.util.List;
import java.util.Queue;


public class ValidScheduler {

    private Processor[] processorList;
    private Queue<Task> taskQueue;

    /**
     * Generates a topological order of task queue to support valid scheduling.
     * @param g Input Graph
     */
    public void topologicalorder(Graph g){
        TopologicalSort ts = new TopologicalSort();
        ts.init(g);
        ts.compute();
        taskQueue = ts.getSortedTasks();
    }

    /**
     * Schedules tasks onto processors using a greedy method of scheduling tasks, tasks are scheduled on the processor
     * in which it can be scheduled earliest.
     */
    public void scheduleTasks() {

        while (!taskQueue.isEmpty()) {
            Task candidateTask = taskQueue.remove();
            Processor candidateProcessor = processorList[0];
            int minTime = Integer.MAX_VALUE;
            int startTime = 0;

            for (int i = 0; i < processorList.length; i++) {
                startTime = processorList[i].getLatestTime();
                int communicationCost = communicationCost(candidateTask,processorList[i]);
                startTime += communicationCost;

                if (startTime < minTime) {
                    minTime = startTime;
                    candidateProcessor = processorList[i];
                }
            }
            scheduleTask(candidateTask,candidateProcessor,minTime);
        }
    }

    /**
     * Adds a task to a processor, as well start and finishing times to the task.
     *
     * @param task
     * @param processor
     * @param candidateTime
     */
    public void scheduleTask(Task task, Processor processor,int candidateTime) {
        int taskDurationTime = task.getDurationTime();
        int startTime = candidateTime;
        int endTime = startTime + taskDurationTime;

        task.setTaskDetails(processor, endTime, startTime);
        processor.setLatestTime(endTime);
        processor.addTask(task, startTime, endTime,endTime);
    }

    /**
     * Calculates any extra time a Processor has to wait before scheduling a given task due to communication costs
     * of dependent tasks.
     *
     * @param candidateTask Task that we wish to calculate the communication cost for.
     * @param candidateProcessor Processor that we wish to calculate the communication cost for.
     * @return cost Extra communication cost that the processor must wait before scheduling the task.
     */
    public int communicationCost(Task candidateTask,Processor candidateProcessor) {
        List<Edge> parents = candidateTask.getParentEdgeList();
        int cost = 0;
        int candidateProcessorLatestTime = candidateProcessor.getLatestTime();

        for (Edge parentEdge : parents) {
            // Gets the parent task of the candidate task, getNode0 returns the parent of an edge.
            Task parentTask = (Task)parentEdge.getNode0().getAttribute("Task");
            Processor parentProcessor = parentTask.getAllocatedProcessor();

            if (parentProcessor != candidateProcessor) {
                int parentEndTime = parentTask.getFinishingTime();
                int commCost = ((Double)parentEdge.getAttribute("Weight")).intValue();
                int candidateCost = parentEndTime + commCost;
                cost = Math.max(cost,candidateCost - candidateProcessorLatestTime);
            }
        }
        return cost;
    }

    /**
     * For testing purposes.
     * @param processorList
     */
    public void setProcessorList(Processor[] processorList) {
        this.processorList = processorList;
    }

    /**
     * For testing purposes.
     * @return
     */
    public Processor[] getProcessorList() {
        return processorList;
    }
}