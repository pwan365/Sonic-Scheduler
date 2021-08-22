package gui;

import algo.ScheduleThread;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Visualiser class responsible for displaying GUI for the application
 * It passes input arguments into the Controller class to set up the GUI
 *
 * @author Samuel Chen, Kayla Kautai
 */
public class Visualiser extends Application {

     private Controller c;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader  = new FXMLLoader(Visualiser.class.getResource("/visualisation.fxml"));
        Parent root = loader.load();
        this.c = loader.getController();
        stage.setScene(new Scene(root,1440,700));
        stage.show();
    }

    /**
     * Pass input into the controller class, the scheduleThread is passed into controller for the GUI to control
     * the start of the scheduling
     * @param scheduleThread the thread that runs the scheduling algorithm
     * @param inputName the name of the input file
     * @param numProc number of processor that the tasks are assigned to
     */
    public void loadData(ScheduleThread scheduleThread,String inputName, int numProc,int threadCount){
        c.passInput(scheduleThread, inputName,numProc,threadCount);
    }
}