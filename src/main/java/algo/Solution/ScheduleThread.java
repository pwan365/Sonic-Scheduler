package algo.Solution;

import algo.Schedule.BestSchedule;
import algo.Schedule.Task;
import io.InputReader;
import io.OutputWriter;
import org.graphstream.graph.Graph;

import java.util.ArrayList;
import java.util.HashSet;

public class ScheduleThread extends Thread{
    private String inputFileName;
    private String outputFileName;
    private int numberOfProcessors;
    private SequentialSearch search;

    public ScheduleThread(String inputName, String outputName, int numOfProcessors){
        inputFileName = inputName;
        outputFileName = outputName;
        numberOfProcessors = numOfProcessors;
    }

    public void run(){
        // Read and perform valid sorting of graph.
        InputReader reader = new InputReader(inputFileName);
        Graph inputGraph = reader.read();

        ValidScheduler v = new ValidScheduler(1);
        v.topologicalorder(inputGraph);

        search = new SequentialSearch(numberOfProcessors,inputGraph);
        search.schedule();
        search.writeToGraph();
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

}
