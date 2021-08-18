package gui;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.util.List;

public class Controller {

    private Boolean isRunning = false;
    private Boolean init = true;

    @FXML
    private Label graphName;
    @FXML
    private Text timeElapsed;
    @FXML
    private Text totalTask;

    @FXML
    private Text numProcess;

    @FXML
    private Button startBtn;
    @FXML
    private Text statusText;

    StatusRefresh control = new StatusRefresh();


    @FXML
    private Text statesExamined;

    @FXML
    private Text statesMagnitude;

    @FXML
    private StackedBarChart<Number, String> barChartSchedule;


    public void passInput(String inputGraphName, String taskNum, int procNum){
        graphName.setText(inputGraphName);
        totalTask.setText(taskNum);
        numProcess.setText(String.valueOf(procNum));
    }

    public void start(){
        toggleBtn(control);
    }

    public void toggleBtn(StatusRefresh control){
        if(init){
            init = false;
            isRunning = true;
            control.start();
            control.setStartTime();
        }else{
            isRunning = !isRunning;
        }
        if(isRunning){
            startBtn.setText("STOP");
            startBtn.setStyle("-fx-background-color: #A30000");
            statusText.setText("SCHEDULING");
            control.start();
            control.setStartTime();
        }else{
            startBtn.setText("START");
            startBtn.setStyle("-fx-background-color: #56b661");
            statusText.setText("STANDBY");
            control.stop();
        }
    }

    private class StatusRefresh extends AnimationTimer {
        long startTime;
        long lastUpdate = 0;
        @Override
        public void handle(long now) {

            if (now - lastUpdate < 50_000_000) {
                return;
            } else {
                lastUpdate = now;
            }
            long elapsedMillis = System.currentTimeMillis() - startTime;
            int milliseconds = (int) ( elapsedMillis % 1000);
            int seconds = (int) ((elapsedMillis / 1000) % 60);
            int minutes = (int) ((elapsedMillis / (1000 * 60)) % 60);
            int hours = (int) (elapsedMillis / (1000 * 60 * 60));
            timeElapsed.setText(String.format("%02d : %02d : %02d.%02d",hours,minutes,seconds,milliseconds/10));
        }

        public void setStartTime(){
            startTime = System.currentTimeMillis();
        }

        /* TESTS - WILL BE REMOVED LATER */
//        formatStatesExamined(10000);
//        barChart(procNum);
    }

    /* Updates the states examined and formats large numbers into abbreviations */
    public void formatStatesExamined(long currentStates) {
        if ( currentStates < 1000 ) {
            statesExamined.setText(String.valueOf(currentStates));
            statesMagnitude.setText("");
        } else if ( currentStates < 1000000 ) {
            statesExamined.setText(String.valueOf(currentStates/1000));
            statesMagnitude.setText("thousand");
        } else if( currentStates < 1000000000 ) {
            statesExamined.setText(String.valueOf(currentStates/1000000));
            statesMagnitude.setText("million");
        } else {
            statesExamined.setText(String.valueOf(currentStates/1000000000));
            statesMagnitude.setText("billion");
        }
    }

    public void barChart(int procNum) {
        // reset the bar chart
        barChartSchedule.getData().clear();

        // prepares XYChart.Series objects by setting data
        XYChart.Series<Number, String> series1 = new XYChart.Series<>();
        series1.setName("P1");
        series1.getData().add(new XYChart.Data<>(100, "Task 1"));

        XYChart.Series<Number, String> series2 = new XYChart.Series<>();
        series2.setName("P2");
        series2.getData().add(new XYChart.Data<>(200, "Task 2"));

        //Adds the data to the chart
        barChartSchedule.getData().addAll(series1, series2);
    }

}