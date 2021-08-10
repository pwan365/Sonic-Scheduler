package algo;

public class Schedule {
    private Processor[] processorList;
    private int latestScheduleTime = 0;
    public Schedule(int numProcessors) {
        //Initialize Processor Pool
        processorList  = new Processor[numProcessors];
        for (int i = 0; i < numProcessors; i ++) {
            processorList[i] = new Processor(i+1);
        }
    }
}
