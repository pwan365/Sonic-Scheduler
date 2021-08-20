package algo.Schedule;

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
    private int bestTime;

    public BestSchedule(){
        bestTime = Integer.MAX_VALUE;
    }

    public void makeCopy(int candidateBest, int[] allocatedProcessors, int[][] taskData) {
        taskInformation = new int[taskData.length][4];
        allocatedProcessors = new int[allocatedProcessors.length];

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

    public int getTime() {
        return bestTime;
    }

    public int[][] getTaskInformation() {
        return taskInformation;
    }
}
