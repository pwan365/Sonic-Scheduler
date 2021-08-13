package algo.CostFunctions;

import algo.Processor;
import com.rits.cloning.Cloner;

public class BestSchedule {
    private Processor[] processors;
    private int bestTime;

    public BestSchedule(){
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
}
