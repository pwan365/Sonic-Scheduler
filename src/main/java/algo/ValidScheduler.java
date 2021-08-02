package algo;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import java.util.List;
import java.util.Queue;


public class ValidScheduler {

    private Processor[] processorList;
    private Queue<Task> taskQueue;

    public Queue<Task> topologicalorder(Graph g){
        TopologicalSort ts = new TopologicalSort();
        ts.init(g);
        ts.compute();
        taskQueue = ts.getSortedTasks();
        return taskQueue;
    }

    /**
     * Schedules tasks onto processors using a greedy method of scheduling tasks, tasks are scheduled on the processor
     * in which it can be scheduled earliest.
     */
    public void scheduleTasks() {

        while (!taskQueue.isEmpty()) {
            Task candidateTask = taskQueue.remove();
            Processor candidateProcessor = processorList[0];
            Double minTime = Double.MAX_VALUE;
            Double startTime = 0.0;

            for (int i = 0; i < processorList.length; i++) {
                startTime = processorList[i].getLatestTime();
                Double communicationCost = communicationCost(candidateTask,processorList[i]);
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
    public void scheduleTask(Task task, Processor processor,Double candidateTime) {
        System.out.println(processor.getTasks());
        System.out.println(processor.getLatestTime());
        Double taskDurationTime = task.getDurationTime();
        Double startTime = candidateTime;
        Double endTime = startTime + taskDurationTime;

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
    public double communicationCost(Task candidateTask,Processor candidateProcessor) {
        List<Edge> parents = candidateTask.getParentEdgeList();
        Double cost = 0.0;
        Double candidateProcessorLatestTime = candidateProcessor.getLatestTime();

        for (Edge parentEdge : parents) {
            // Gets the parent task of the candidate task, getNode0 returns the parent of an edge.
            Task parentTask = (Task)parentEdge.getNode0().getAttribute("Task");
            Processor parentProcessor = parentTask.getAllocatedProcessor();

            if (parentProcessor != candidateProcessor) {
                Double parentEndTime = parentTask.getFinishingTime();
                Double commCost = (Double)parentEdge.getAttribute("Weight");
                Double candidateCost = parentEndTime + commCost;
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