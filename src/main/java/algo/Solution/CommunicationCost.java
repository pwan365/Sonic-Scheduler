package algo.Solution;

import algo.Schedule.Processor;
import algo.Schedule.Schedule;
import algo.Schedule.Task;
import org.graphstream.graph.Edge;

import java.util.List;

/**
 * Class to calculate the communication cost of a schedule(Should be split up into two classes, one with the comparable
 * and one solely responsible for the communication cost calculation.
 */
public class CommunicationCost implements Comparable<CommunicationCost> {

    private final Processor processor;
    private final Task task;
    private final int start;
    private final int cost;

    public CommunicationCost(Task candidateTask, Processor candidateProcessor, Schedule schedule) {
        task = candidateTask;
        processor = candidateProcessor;
        cost = communicationCost();
        start = cost + processor.getTime();
    }
    @Override
    public int compareTo(CommunicationCost e) {
        return Integer.compare(this.start,e.start);
    }

    /**
     * Calculates any extra time the processor has to wait before scheduling a given task due to communication costs of
     * dependent tasks.
     *
     * @return Extra communication cost that the processor must wait before scheduling the task.
     */
    private int communicationCost() {
        List<Edge> parents = task.getParentEdgeList();
        int calcCost = 0;
        int processorLatestTime = processor.getTime();
        for (Edge parentEdge : parents) {
            // Gets the parent task of the candidate task, getNode0 returns the parent of an edge.
            Task parentTask = (Task) parentEdge.getNode0().getAttribute("Task");
            Processor parentProcessor = parentTask.getAllocatedProcessor();
            if (parentProcessor != processor) {
                int parentEndTime = parentTask.getFinishingTime();
                int commCost = ((Double) parentEdge.getAttribute("Weight")).intValue();
                int candidateCost = parentEndTime + commCost;
                calcCost = Math.max(calcCost, candidateCost - processorLatestTime);
            }
        }
        return calcCost;
    }

    public Task getTask() {
        return this.task;
    }
    public int getProcessorID() {
        return processor.getProcessNum()-1;
    }

    public int commCost() {
        return cost;
    }
}
