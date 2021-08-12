package algo.CostFunctions;

import algo.Processor;
import algo.Task;

public class Estimator implements Comparable<Estimator> {

    private int procNum;
    private Task candidateTask;
    public int estCost;

    public Estimator(Task task, int processor) {
        candidateTask = task;
        procNum = processor;
    }
    @Override
    public int compareTo(Estimator e) {
        return Integer.compare(this.estCost,e.estCost);
    }

    public Task getTask() {
        return this.candidateTask;
    }
    public void setEstimates(int cost) {
        estCost = cost;
    }
    public int getProcNum() {
        return procNum;
    }
}
