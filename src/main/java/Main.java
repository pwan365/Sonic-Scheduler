
import algo.Solution.ScheduleThread;
import com.sun.javafx.application.PlatformImpl;
import gui.Visualiser;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;



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
        boolean parallel = false;
        ScheduleThread scheduleThread = new ScheduleThread(fileName,outputFileName,numberOfProcessors,parallel);

        if (commands.contains("-v")){
            PlatformImpl.startup(() -> {
                Visualiser v = new Visualiser();
                try{
                    v.start(new Stage());

                }catch(Exception e){

                }
                v.loadData(scheduleThread, fileName, numberOfProcessors);

            });

        }else{
            scheduleThread.start();
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



    }


}