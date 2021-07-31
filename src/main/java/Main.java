import algo.Processor;
import algo.Task;
import io.InputReader;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;

import algo.ValidScheduler;

public class Main  {
    public static void main(String[] args) {


        InputReader readerr = new InputReader("input.dot");
        Graph g = readerr.read();
        System.out.println(g.getNode(0));


        List<String> commands = Arrays.asList(args);

        String fileName = args[0];
        int numberOfProcessors = Integer.parseInt(args[1]);
        int numberOfCores = 1;
        String outputFileName = fileName + "-output";

        if (commands.contains("-p")){
            int index = commands.indexOf("-p");
            numberOfCores = Integer.parseInt(commands.get(index+1));
            //Parallel code
        }

        if (commands.contains("-v")){
            //Visualize option
        }

        if (commands.contains("-o")){
            int index = commands.indexOf("-o");
            outputFileName = commands.get(index+1);
        }

        /*Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the processor number: ");
        int numberOfProcessors = scanner.nextInt();
        while (numberOfProcessors > 6 || numberOfProcessors <= 0 ) {
            System.out.print("Invalid processor number! Please enter a new number: ");
            numberOfProcessors = scanner.nextInt();
        } */

        Processor[] processorPool  = new Processor[numberOfProcessors];
        for (int i = 0; i < numberOfProcessors; i ++) {
            processorPool[i] = new Processor();
        }
        InputReader reader = new InputReader(fileName);
        Graph inputGraph = reader.read();

        ValidScheduler v = new ValidScheduler();

        v.setProcessorList(processorPool);
        Queue<Task> taskQueue = v.topologicalorder(inputGraph);

        v.scheduleTasks();
    }
}