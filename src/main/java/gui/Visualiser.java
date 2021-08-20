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

    public void loadData(ScheduleThread scheduleThread,String inputName, String numTask, int numProc){
        c.passInput(scheduleThread, inputName,numTask,numProc);
    }
}