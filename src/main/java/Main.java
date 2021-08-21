import algo.Solution.ParallelSearch;
import algo.Solution.SequentialSearch;
import io.InputReader;
import algo.Solution.IntGraph;
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

        IntGraph graph = new IntGraph(inputGraph);
//        graph.testEdge();

/*        SequentialSearch s = new SequentialSearch(graph,numberOfProcessors);
        s.run();
        s.done();*/

        long start, end;
        start = System.currentTimeMillis();
        SequentialSearch ps = new SequentialSearch(graph, numberOfProcessors);
        ps.run();
        System.out.println(ps.bestSchedule.getTime());
        end = System.currentTimeMillis();
        System.out.println("Run time: " + (end - start));
    }
}