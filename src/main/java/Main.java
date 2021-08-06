import algo.Processor;
import algo.Task;
import io.InputReader;
import io.OutputWriter;
import org.graphstream.graph.Graph;

import java.util.*;

import algo.ValidScheduler;

public class Main  {
    public static void main(String[] args) {

        List<String> commands = Arrays.asList(args);

        String fileName = args[0];
        int numberOfProcessors = Integer.parseInt(args[1]);
        int numberOfCores = 1;
        String outputFileName = fileName + "-output";

        /**
         * Parallel code
         * TODO Implement Parallel Code by Milestone 2
         */
        if (commands.contains("-p")){
            int index = commands.indexOf("-p");
            numberOfCores = Integer.parseInt(commands.get(index+1));
        }

        /**
         * Visualization Code
         * TODO Implement Visualization by Milestone 2
         */
        if (commands.contains("-v")){
            //Visualize option
        }

        if (commands.contains("-o")){
            int index = commands.indexOf("-o");
            outputFileName = commands.get(index+1);
        }


        // Initialize processor pool
        Processor[] processorPool  = new Processor[numberOfProcessors];
        for (int i = 0; i < numberOfProcessors; i ++) {
            processorPool[i] = new Processor(i+1);
        }

        // Read and perform valid sorting of graph.
        InputReader reader = new InputReader(fileName);
        Graph inputGraph = reader.read();

        ValidScheduler v = new ValidScheduler();

        v.setProcessorList(processorPool);
        v.topologicalorder(inputGraph);
        v.scheduleTasks();

        // Write the scheduled graph to a file.
        OutputWriter writer = new OutputWriter();
        writer.write(inputGraph, "outputtry.dot");

    }
}