package algo.Solution;

public class DSL implements Comparable<DSL> {

    private final int dsl;
    protected final int task;
    protected final int processor;
    protected final int cost;

    public DSL(int blevel, int cost, int start,int task, int processor) {
        this.dsl = -(blevel - (cost + start));
        this.task = task;
        this.processor = processor;
        this.cost = cost;
    }


    @Override
    public int compareTo(DSL d) {
        return Integer.compare(this.dsl,d.dsl);
    }
}
