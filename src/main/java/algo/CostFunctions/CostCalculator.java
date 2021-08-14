package algo.CostFunctions;

import algo.Schedule.Task;

public class CostCalculator implements Comparable<CostCalculator> {

    private int procNum;
    private Task candidateTask;
    public int estCost;

    public CostCalculator(Task task, int processor) {
        candidateTask = task;
        procNum = processor;
    }
    @Override
    public int compareTo(CostCalculator e) {
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
