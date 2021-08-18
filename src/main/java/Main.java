import algo.Processor;
import algo.Task;
import com.sun.javafx.application.PlatformImpl;
import gui.Controller;
import gui.Visualiser;
import algo.Schedule.Task;
import algo.Solution.AllOrders;
import algo.Solution.ValidScheduler;
import algo.Solution.SequentialSearch;
import io.InputReader;
import io.OutputWriter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.graphstream.graph.Graph;

import java.lang.reflect.Array;
import java.util.*;

import algo.ValidScheduler;

public class Main {
    private static Scene scene;



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
            PlatformImpl.startup(() -> {
                Visualiser v = new Visualiser();
                try{
                    v.start(new Stage());

                }catch(Exception e){

                }
                v.loadData(fileName,"20",numberOfProcessors);

            });
//            Application.launch(Visualiser.class);

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
        AllOrders a = AllOrders.init(inputGraph);

        HashSet<Task> empty = new HashSet<>();
        ArrayList<Task> tasks = a.getOrder(empty);
        for (Task task : tasks) {
            for(int i=0; i<numberOfProcessors;i++) {
                d.branchBound(task,i,0);
            }

        }
        int best = d.getBestSchedule();
        System.out.println(best);
        // Write the scheduled graph to a file.
        OutputWriter writer = new OutputWriter();
        writer.write(inputGraph, outputFileName);
        // 27 seconds

    }


}