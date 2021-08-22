
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
        boolean parallel = false;
        //Formatting output name
        String outputFileName = fileName.replace(".dot", "-output.dot");

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

        /*
         * Parallel code
         * TODO Implement Parallel Code by Milestone 2
         */
        if (commands.contains("-p")){
            int index = commands.indexOf("-p");
            numberOfCores = Integer.parseInt(commands.get(index+1));
            if (numberOfCores > 1) {
                parallel = true;
            }
        }

        /*
         * Visualization Code
         * TODO Implement Visualization by Milestone 2
         */
        ScheduleThread scheduleThread = new ScheduleThread(fileName,outputFileName,numberOfProcessors,parallel,numberOfCores);

        if (commands.contains("-v")){
            int finalNumberOfCores = numberOfCores;
            PlatformImpl.startup(() -> {
                Visualiser v = new Visualiser();
                try{
                    v.start(new Stage());
                }catch(Exception e){
                    e.printStackTrace();
                }
                v.loadData(scheduleThread, fileName, numberOfProcessors, finalNumberOfCores);
            });
        }else{
            scheduleThread.start();
        }
    }
}