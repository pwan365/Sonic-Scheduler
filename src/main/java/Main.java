import algo.Processor;
import algo.Task;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import algo.ValidScheduler;

import java.util.Scanner;

public class Main  {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the processor number: ");
        int numberOfProcessors = scanner.nextInt();
        while (numberOfProcessors > 6 || numberOfProcessors <= 0 ) {
            System.out.print("Invalid processor number! Please enter a new number: ");
            numberOfProcessors = scanner.nextInt();
        }

        Processor[] processorPool  = new Processor[numberOfProcessors];
      
      
        ValidScheduler v = new ValidScheduler();
        Queue<Task> taskQueue = v.topologicalorder();
    }
}