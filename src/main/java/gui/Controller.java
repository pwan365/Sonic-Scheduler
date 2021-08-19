package gui;

import algo.Schedule.BestSchedule;
import algo.Schedule.Processor;
import algo.Schedule.Task;
import algo.Solution.ScheduleThread;
import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.util.*;

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
    private Text bestTime;

    CategoryAxis yAxis = new CategoryAxis();

    NumberAxis xAxis = new NumberAxis();
    @FXML
    private StackedBarChart<Number, String> barChartSchedule ;

    ScheduleThread scheduleThread;

    public void passInput(ScheduleThread scheduleThread,String inputGraphName, String taskNum, int procNum){
        this.scheduleThread = scheduleThread;
        graphName.setText(inputGraphName);
        totalTask.setText(taskNum);
        numProcess.setText(String.valueOf(procNum));
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

    }

    public void start(){
        toggleBtn(control);
    }

    public void toggleBtn(StatusRefresh control){
        if(init){
            init = false;
            isRunning = true;
            scheduleThread.start();
            control.start();
            control.setStartTime();
        }else{
            isRunning = !isRunning;
        }
        if(isRunning){
            startBtn.setText("STOP");
            startBtn.setStyle("-fx-background-color: #A30000");
            statusText.setText("SCHEDULING");
            scheduleThread.start();
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

            formatStatesExamined(scheduleThread.getStates());
            bestTime.setText(scheduleThread.getBestTime() + "");
            updateBarChart();

            /*if (scheduleThread.getBestChanged()) {
                bestTime.setText(scheduleThread.getBestTime() + "");
                updateBarChart();
            }*/

            System.out.println(scheduleThread.isDone());
            if(scheduleThread.isDone()){
                this.stop();
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

    public void updateBarChart() {

        List<List<Task>> barList = getBestSchedule();
        if(barList == null){
            return;
        }
        barChartSchedule.getData().clear();

        XYChart.Series<Number, String> dataSeries1 = new XYChart.Series<Number, String>();
        int procNum = 1;

        for (List<Task> eachBar : barList ){
            Collections.sort(eachBar, (c1, c2) -> {
                if (c1.getStartingTime() > c2.getStartingTime()) return 1;
                if (c1.getStartingTime() < c2.getStartingTime()) return -1;
                return 0;
            });

//            dataSeries1.setName("P" + procNum);
            for(Task eachPart : eachBar){
                int length = eachPart.getDurationTime();
                dataSeries1.getData().add(new XYChart.Data<Number, String>(length, "P" + procNum));
            }
            procNum++;
        }
        barChartSchedule.getData().addAll(dataSeries1);




//        dataSeries1.setName("P1");
//        dataSeries1.getData().add(new XYChart.Data<Number, String>(100, "P1"));
//        final XYChart.Data<Number, String> bar = new XYChart.Data<Number,String>(50, "P1");
//        bar.nodeProperty().addListener((ov, oldNode, node) -> {
//            if (node != null) {
//
//                // Idle tasks should be transparent
//                node.setStyle("-fx-bar-fill: transparent");
//
//            }
//        });
//
//        dataSeries1.getData().add(bar);
//        dataSeries1.getData().add(new XYChart.Data<Number, String>(120, "P1"));
//
//
//        XYChart.Series<Number, String> dataSeries2 = new XYChart.Series<Number, String>();
//        dataSeries2.setName("P2");
//        dataSeries2.getData().add(new XYChart.Data<Number, String>(120, "P2"));
//        barChartSchedule.getData().addAll(dataSeries1,dataSeries2);

    }

    public List<List<Task>> getBestSchedule(){
        BestSchedule b = scheduleThread.getBestSchedule();
        List<List<Task>> barList = new ArrayList<>();

        if (b.getProcessors()==null){
            return null;
        }
        Processor[] processors = b.getProcessors();
        HashSet<Task> tasks;
        List<Task> eachBar = new ArrayList<>();


        for(int i=0 ; i<processors.length;i++){
            tasks=processors[i].getTasks();
            Iterator value = tasks.iterator();
            while (value.hasNext()){
                eachBar.add((Task) value.next());
            }
            barList.add(eachBar);
        }
        return barList;
    }
}