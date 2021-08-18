package algo.Schedule;

import com.rits.cloning.Cloner;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * Represents the Best Schedule currently found, contains the processors with their tasks and the time of the schedule.
 *
 * @author Wayne Yao, Luxman Jeyarajah
 */
public class BestSchedule implements Schedule {
    private Processor[] processors;
    private int bestTime;

    public BestSchedule(){
        bestTime = Integer.MAX_VALUE;
    }

    public void makeCopy(int candidateBest, Processor[] processors) {
        Cloner cloner = new Cloner();
        int numOfProcessors = processors.length;
        this.processors = new Processor[numOfProcessors];
        for(int i = 0; i < numOfProcessors; i++){
            this.processors[i] = cloner.deepClone(processors[i]);
        }
        this.bestTime = candidateBest;
    }

    public int getTime() {
        return bestTime;
    }

    public Processor[] getProcessors() {
        return processors;
    }

    public void done(){
        for(Processor processor:processors){
            for(Task task: processor.getTasks()){
                System.out.println("PROCESSOR: ");
                System.out.println(task.getAllocatedProcessor().getProcessNum());
                System.out.println("TASKLAESTTIME: " + task.getFinishingTime());
            }
        }
    }

    public void writeToGraph(Graph input){
        for(Processor processor: processors){
            for(Task task: processor.getTasks()){
                int nodeIndex = task.getNode().getIndex();
                Node node = input.getNode(nodeIndex);
                node.setAttribute("Task", task);
            }
        }
    }

}
