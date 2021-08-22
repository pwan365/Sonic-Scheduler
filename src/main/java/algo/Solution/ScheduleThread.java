package algo.Solution;

import io.InputReader;
import io.OutputWriter;
import org.graphstream.graph.Graph;

public class ScheduleThread extends Thread{
    private String inputFileName;
    private String outputFileName;
    private int numberOfProcessors;
    private SequentialSearch search;
    private ParallelSearch searchParallel;
    private boolean done = false;
    private Graph inputGraph;
    private IntGraph intGraph;

    public ScheduleThread(String inputName, String outputName, int numOfProcessors,boolean parallel){
        inputFileName = inputName;
        outputFileName = outputName;
        numberOfProcessors = numOfProcessors;
        // Read and perform valid sorting of graph.
        InputReader reader = new InputReader(inputFileName);
        inputGraph = reader.read();
        intGraph = new IntGraph(inputGraph);


//        ValidScheduler v = new ValidScheduler(1);
//        v.topologicalorder(inputGraph);
        if (!parallel) {
            search = new SequentialSearch(inputGraph,intGraph,numberOfProcessors);
//            search.run();
        }
        else {
            searchParallel = new ParallelSearch(inputGraph,intGraph,numberOfProcessors);
//            searchParallel.run();
        }

    }

    public void run(){

        search.run();
        done = true;
        search.done();
        // Write the scheduled graph to a file.

        OutputWriter writer = new OutputWriter();
        writer.write(inputGraph, outputFileName);
        // 27 seconds
    }

    public int getBestTime() {
        return search.bestSchedule.getTime();
    }

    public BestSchedule getBestSchedule() {
        return search.bestSchedule;
    }

//    public int getStates() {
//        return search.getStates();
//    }

    public boolean isDone() {
        return done;
    }
//    public boolean getBestChanged() {
//        boolean isNewBest = search.newBest;
//        search.newBest = false;
//
//        return isNewBest;
//    }
}
