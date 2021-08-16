package gui;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class Controller {

    private Boolean isRunning = false;
    private Boolean init = true;

    @FXML
    private Label graphName;

    @FXML
    private Text totalTask;

    @FXML
    private Text numProcess;

    @FXML
    private Button startBtn;
    @FXML
    private Text statusText;

    StatusRefresh control = new StatusRefresh();



    public void passInput(String inputGraphName, String taskNum, int procNum){
        graphName.setText(inputGraphName);
        totalTask.setText(taskNum);
        numProcess.setText(String.valueOf(procNum));
    }

    public void start(){
        toggleBtn(control);

        System.out.println("hello");
    }

    public void toggleBtn(StatusRefresh control){
        if(init){
            init = false;
            isRunning = true;
            control.start();
        }else{
            isRunning = !isRunning;
        }
            if(isRunning){
                startBtn.setText("STOP");
                startBtn.setStyle("-fx-background-color: #A30000");
                statusText.setText("SCHEDULING");
                control.start();
            }else{
                startBtn.setText("START");
                startBtn.setStyle("-fx-background-color: #56b661");
                statusText.setText("STANDBY");
                control.stop();
            }
    }

    private class StatusRefresh extends AnimationTimer{

        @Override
        public void handle(long now) {
            final long startTime = System.currentTimeMillis();
            long lastUpdate = 0;


            if (now - lastUpdate < 50_000_0000) {
                return;
            } else {
                lastUpdate = now;
                System.out.println(now);
            }
        }

    }

}