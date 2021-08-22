package algo;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * Represents the Best Schedule currently found, contains the processors with their tasks and the time of the schedule.
 *
 * @author Wayne Yao
 */
public class BestSchedule {
    private int[][] taskInformation; // copy of task information
    private int[] taskProcessors; // copy of allocated processor list
    protected int bestTime; // best time

    /**
     * Constructor for the class, set best time to MAX integer value so that it can be compared
     */
    public BestSchedule(){
        bestTime = Integer.MAX_VALUE;
    }

    /**
     * The main method of this class, it will make copies of the task info, task processor and the best time
     * @param candidateBest best time
     * @param allocatedProcessors task allocation list
     * @param taskData task information list
     */
    public void makeCopy(int candidateBest, int[] allocatedProcessors, int[][] taskData) {
        taskInformation = new int[taskData.length][4];
        taskProcessors = new int[allocatedProcessors.length];

        for(int i = 0; i < allocatedProcessors.length; i++){
            taskProcessors[i] = allocatedProcessors[i];
        }
        for(int i = 0; i < taskData.length; i++){
            for(int j = 0; j < taskData[i].length; j++){
                taskInformation[i][j] = taskData[i][j];
            }
        }
        this.bestTime = candidateBest;
    }

    /**
     * Getter for task information array
     * @return an array storing task information
     */
    public int[][] getTaskInformation() {
        return taskInformation;
    }

    /**
     * Getter for task allocation processor list
     * @return an array storing allocated processor for each task
     */
    public int[] getTaskProcessors() {
        return taskProcessors;
    }

    /**
     * Getter for best time
     * @return best time
     */
    public int getTime() {
        return bestTime;
    }

    /**
     * Will be called after everything is finished
     * Start time and allocated processor will be added to the graph nodes
     * @param graph the input graph object
     */
    public void setGraphAttributes(Graph graph){
        int nodeCount = graph.getNodeCount();
        for (int i = 0; i < nodeCount; i++){
            Node node = graph.getNode(i);
            node.setAttribute("Start", taskInformation[i][0]);
            node.setAttribute("Processor", taskProcessors[i] + 1);
        }
    }
}
