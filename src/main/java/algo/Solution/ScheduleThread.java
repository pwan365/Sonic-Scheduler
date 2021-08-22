package algo.Solution;

import io.InputReader;
import io.OutputWriter;
import org.graphstream.graph.Graph;

public class ScheduleThread extends Thread{
    private String inputFileName;
    private String outputFileName;
    private int numberOfProcessors;
    private GUISchedule search;
    private ParallelSearch searchParallel;
    private boolean done = false;
    private Graph inputGraph;
    private IntGraph intGraph;
    private int numOfCores = 1;

    public ScheduleThread(String inputName, String outputName, int numOfProcessors,boolean parallel, int numOfCores){
        numOfCores = numOfCores;
        inputFileName = inputName;
        outputFileName = outputName;
        numberOfProcessors = numOfProcessors;
        // Read and perform valid sorting of graph.
        InputReader reader = new InputReader(inputFileName);
        inputGraph = reader.read();
        intGraph = new IntGraph(inputGraph);

        if (!parallel) {
            search = new SequentialSearch(inputGraph,intGraph,numberOfProcessors);
        }
        else {
            search = new ParallelSearch(inputGraph,intGraph,numberOfProcessors,numOfCores);
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
        return search.getBestTime();
    }

    public BestSchedule getBestSchedule() {
        return search.getBestSchedule();
    }

    public int getStates() {
        return search.getStates();
    }

    public boolean isDone() {
        return done;
    }


}
