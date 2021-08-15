package gui;

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
        stage.setScene(new Scene(root,900,600));
        stage.show();
    }

    public void loadData(String inputName, String numTask, int numProc){
        c.passInput(inputName,numTask,numProc);
    }
}