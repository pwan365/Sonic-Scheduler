package algo;

import io.InputReader;
import io.OutputWriter;
import org.graphstream.graph.Graph;

/**
 * Schedule Thread extends Thread class which represents the thread that the GUI will be running on.
 *
 * @author Luxman Jeyarajah, Samuel Chen
 */
public class ScheduleThread extends Thread{
    private final String outputFileName;
    private final VisualiseSearch search;
    private boolean done = false;
    private final Graph inputGraph;

    /**
     *
     * @param inputName input file name
     * @param outputName output file name
     * @param numOfProcessors number of processors
     * @param parallel Whether the algorithm is running in parallel or sequential
     * @param numOfCores number of cores. Default to be 1
     */
    public ScheduleThread(String inputName, String outputName, int numOfProcessors,boolean parallel, int numOfCores){
        outputFileName = outputName;
        // Read and perform valid sorting of graph.
        InputReader reader = new InputReader(inputName);
        inputGraph = reader.read();
        IntGraph intGraph = new IntGraph(inputGraph);

        if (!parallel || numOfCores == 1) {
            search = new SequentialSearch(inputGraph, intGraph, numOfProcessors);
        }
        else {
            search = new ParallelSearch(inputGraph, intGraph, numOfProcessors,numOfCores);
        }

    }

    /**
     * Run the algorithm.
     */
    public void run(){

        search.run();
        done = true;
        search.done();
        // Write the scheduled graph to a file.
        OutputWriter writer = new OutputWriter();
        writer.write(inputGraph, outputFileName);

        //notified user the output has been produced
        System.out.println("Optimal schedule has been produced in "+ outputFileName);
    }

    /**
     * Get best time of search
     */
    public int getBestTime() {
        return search.getBestTime();
    }

    /**
     * Get best schedule of generated
     */
    public BestSchedule getBestSchedule() {
        return search.getBestSchedule();
    }

    /**
     * Get states that has been searched
     */
    public int getStates() {
        return search.getStates();
    }

    /**
     * Check whether the thread is finished running
     */
    public boolean isDone() {
        return done;
    }


}
