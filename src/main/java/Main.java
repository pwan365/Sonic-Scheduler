import algo.Processor;
import algo.Task;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;

import algo.ValidScheduler;

public class Main  {
    public static void main(String[] args) {
        List commands = Arrays.asList(args);

        String fileName = args[0];
        int numberOfProcessors = Integer.parseInt(args[1]);

        if (commands.contains("-p")){
            //Parallel option
        }

        if (commands.contains("-v")){
            //Visualize option
        }

        if (commands.contains("-o")){
            //Output option
        }

        /*Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the processor number: ");
        int numberOfProcessors = scanner.nextInt();
        while (numberOfProcessors > 6 || numberOfProcessors <= 0 ) {
            System.out.print("Invalid processor number! Please enter a new number: ");
            numberOfProcessors = scanner.nextInt();
        } */

        Processor[] processorPool  = new Processor[numberOfProcessors];


        ValidScheduler v = new ValidScheduler();

        v.setProcessorList(processorPool);
        Queue<Task> taskQueue = v.topologicalorder();
    }
}