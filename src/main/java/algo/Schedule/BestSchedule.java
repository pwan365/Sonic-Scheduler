package algo.Schedule;

import com.rits.cloning.Cloner;

public class BestSchedule {
    private Processor[] processors;
    private int bestTime = Integer.MAX_VALUE;

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

    public Processor[] getBestProcessors(){
        return processors;
    }


    public int getBestTime() {
        return bestTime;
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
}
