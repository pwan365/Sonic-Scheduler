package algo.Solution;

import com.rits.cloning.Cloner;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * Represents the Best Schedule currently found, contains the processors with their tasks and the time of the schedule.
 *
 * @author Wayne Yao, Luxman Jeyarajah
 */
public class BestSchedule {


    private int[][] taskInformation;
    private int[] taskProcessors;
    protected int bestTime;


    public BestSchedule(){
        bestTime = Integer.MAX_VALUE;
    }

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

    public int[][] getTaskInformation() {
        return taskInformation;
    }



    public int[] getTaskProcessors() {
        return taskProcessors;
    }
    public int getTime() {
        return bestTime;
    }

    public void writeToGraph(Graph graph){
        int nodeCount = graph.getNodeCount();
        for (int i = 0; i < nodeCount; i++){
            Node node = graph.getNode(i);
            node.setAttribute("Start", taskInformation[i][0]);
            node.setAttribute("Processor", taskProcessors[i] + 1);
        }
    }
}
