package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Controller {

    @FXML
    private Label graphName;


    public void passInput(String inputGraphName){
        graphName.setText(inputGraphName);
    }

}