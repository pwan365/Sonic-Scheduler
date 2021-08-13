package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Visualiser extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader  = new FXMLLoader(Visualiser.class.getResource("/visualisation.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root,800,600));
        stage.show();
    }
}