package algo;

public class DLS implements Comparable<DLS> {

    private final int dsl;
    protected final int task;
    protected final int processor;
    protected final int cost;

    public DLS(int blevel, int cost, int start, int task, int processor) {
        this.dsl = -(blevel - (cost + start));
        this.task = task;
        this.processor = processor;
        this.cost = cost;
    }


    @Override
    public int compareTo(DLS d) {
        return Integer.compare(this.dsl,d.dsl);
    }
}
