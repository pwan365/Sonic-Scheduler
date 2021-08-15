import algo.Processor;
import algo.Task;
import com.sun.javafx.application.PlatformImpl;
import gui.Controller;
import gui.Visualiser;
import io.InputReader;
import io.OutputWriter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.graphstream.graph.Graph;

import java.util.*;

import algo.ValidScheduler;

public class Main {
    private static Scene scene;

    public static void main(String[] args) {

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

        ValidScheduler v = new ValidScheduler(numberOfProcessors);

        v.topologicalorder(inputGraph);
        v.scheduleTasks();

        // Write the scheduled graph to a file.
        OutputWriter writer = new OutputWriter();
        writer.write(inputGraph, outputFileName);

    }


}