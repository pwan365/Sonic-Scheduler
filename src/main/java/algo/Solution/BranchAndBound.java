package algo.Solution;

public abstract class BranchAndBound {
    public int states;
    public boolean done;
    public boolean parallel;

    public int getStates() {
        return states;
    }

    public boolean isDone() {
        return done;
    }
}
