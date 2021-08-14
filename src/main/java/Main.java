import algo.Schedule.Task;
import algo.Solution.AllOrders;
import algo.Solution.ValidScheduler;
import algo.Solution.SequentialSearch;
import io.InputReader;
import io.OutputWriter;
import org.graphstream.graph.Graph;

import java.util.*;

public class Main  {
    public static void main(String[] args) throws CloneNotSupportedException {

        List<String> commands = Arrays.asList(args);

        String fileName = args[0];
        int numberOfProcessors = Integer.parseInt(args[1]);
        int numberOfCores = 1;
        //Formatting output name
        String outputFileName = fileName.replace(".dot", "-output.dot");

        /*
         * Parallel code
         * TODO Implement Parallel Code by Milestone 2
         */
        if (commands.contains("-p")){
            int index = commands.indexOf("-p");
            numberOfCores = Integer.parseInt(commands.get(index+1));
        }

        /*
         * Visualization Code
         * TODO Implement Visualization by Milestone 2
         */
        if (commands.contains("-v")){
            //Visualize option
        }

        /*
         * Output Code
         * Used if the user specifies an output name
         */
        if (commands.contains("-o")){
            int index = commands.indexOf("-o");
            outputFileName = commands.get(index+1);
            if (!outputFileName.contains(".dot")){
                //Add .dot if the user did not enter the file extension
                outputFileName += ".dot";
            }
        }

        // Read and perform valid sorting of graph.

        InputReader reader = new InputReader(fileName);
        Graph inputGraph = reader.read();

        ValidScheduler v = new ValidScheduler(1);
        v.topologicalorder(inputGraph);

        SequentialSearch d = new SequentialSearch(numberOfProcessors,inputGraph);
        HashSet<Task> empty = new HashSet<>();
        d.branchBound((Task)inputGraph.getNode(0).getAttribute("Task"), 0,0);
        int best = d.getBestSchedule();
        System.out.println(best);
        // Write the scheduled graph to a file.
        OutputWriter writer = new OutputWriter();
        writer.write(inputGraph, outputFileName);
        // 27 seconds

    }
}