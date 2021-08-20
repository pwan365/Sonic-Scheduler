package gui;

import algo.Solution.ScheduleThread;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
     * Pass input into the controller class
     * @param scheduleThread the thread that runs the scheduling algorithm
     * @param inputName the name of the input file
     * @param numProc number of processor that the tasks are assigned to
     */
    public void loadData(ScheduleThread scheduleThread,String inputName, int numProc){
        c.passInput(scheduleThread, inputName,numProc);
    }
}