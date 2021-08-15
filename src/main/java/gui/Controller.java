package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class Controller {

    @FXML
    private Label graphName;

    @FXML
    private Text totalTask;

    @FXML
    private Text numProcess;


    public void passInput(String inputGraphName, String taskNum, int procNum){
        graphName.setText(inputGraphName);
        totalTask.setText(taskNum);
        numProcess.setText(String.valueOf(procNum));

    }

}