package gui;

import algo.Schedule.BestSchedule;
import algo.Schedule.Processor;
import algo.Schedule.Task;
import algo.Solution.ScheduleThread;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
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
    private StatusRefresh control = new StatusRefresh();
    private ScheduleThread scheduleThread;
    private int procNum;

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
    @FXML
    private Text statesExamined;
    @FXML
    private Text statesMagnitude;
    @FXML
    private Text bestTime;

    @FXML
    private StackedBarChart<Number, String> barChartSchedule ;
    CategoryAxis yAxis = new CategoryAxis();
    NumberAxis xAxis = new NumberAxis();

    /**
     * Method sets the global variables and GUI text to those passed into the controller as inputs.
     * @param scheduleThread Thread that runs the schedule algorithm
     * @param inputGraphName Name of the input graph file
     * @param taskNum Number of nodes that represent tasks in the input file
     * @param procNum Number of processors the schedule is required to run on based on user's input
     */
    public void passInput(ScheduleThread scheduleThread,String inputGraphName, String taskNum, int procNum){
        this.scheduleThread = scheduleThread;
        graphName.setText(inputGraphName);
        totalTask.setText(taskNum);
        this.procNum=procNum;
        numProcess.setText(String.valueOf(procNum));
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

    }

    /**
     * Method is called when the START button is pressed to begin the scheduling algorithm on the thread.
     */
    public void start(){
        // Start button is disabled and status of the GUI is changed
        startBtn.setDisable(true);
        statusText.setText("SCHEDULING");
        // Starts running the scheduling algorithm
        scheduleThread.start();
        control.start();
        control.setStartTime();

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

            if (now - lastUpdate < 50_000_0000) {
                return;
            } else {
                lastUpdate = now;
            }

            formatStatesExamined(scheduleThread.getStates());
            if (scheduleThread.getStates() > 0) {
                bestTime.setText(scheduleThread.getBestTime() + "");
            }
            updateBarChart();

            System.out.println(scheduleThread.isDone());
            if(scheduleThread.isDone()){
                statusText.setText("FINISHED");
                this.stop();
            }

            /*if (scheduleThread.getBestChanged()) {
                bestTime.setText(scheduleThread.getBestTime() + "");
                updateBarChart();
            }*/

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

            if(eachBar.size() != 0 && eachBar.get(0).getStartingTime() != 0){
                Task idlePart = new Task(0,eachBar.get(0).getStartingTime(),true);
                eachBar.add(idlePart);
            }

            int i = 1;
            while(eachBar.size() > i){
                Task currPart = eachBar.get(i);
                Task prevPart = eachBar.get(i-1);
                if(currPart.getStartingTime() != prevPart.getStartingTime() + prevPart.getDurationTime()){
                    int idleStartTime = prevPart.getStartingTime() + prevPart.getDurationTime();
                    int idleDuration = currPart.getStartingTime() -idleStartTime;
                    Task idlePart = new Task(idleStartTime,idleDuration,true);
                    eachBar.add(i,idlePart);
                    i++;
                }
                i++;
            }

//            dataSeries1.setName("P"+procNum);
            for(Task eachPart : eachBar){
                int length = eachPart.getDurationTime();
                final XYChart.Data<Number, String> bar = new XYChart.Data<Number,String>(length, procNum+"P");
                bar.nodeProperty().addListener((ov, oldNode, node) -> {
                    if (node != null) {
                        if(eachPart.isIdle()){
                            node.setStyle("-fx-bar-fill: transparent");
                        }else{
                            node.setStyle("-fx-bar-fill: #79b4de;-fx-border-color: #336699;");
                        }


                    }
                });
                dataSeries1.getData().add(bar);

            }
            procNum++;
        }
        barChartSchedule.getData().addAll(dataSeries1);
        barChartSchedule.setLegendVisible(false);

    }

    public List<List<Task>> getBestSchedule(){
        BestSchedule b = scheduleThread.getBestSchedule();
        List<List<Task>> barList = new ArrayList<>();

        if (b.getProcessors()==null){
            return null;
        }
        Processor[] processors = b.getProcessors();
        HashSet<Task> tasks;
        for(int i=0 ; i<processors.length;i++){
            List<Task> eachBar = new ArrayList<>();
            int j=0;
            tasks=processors[i].getTasks();
            for(Task task:tasks){
                eachBar.add(task);
                j++;
            }
            barList.add(eachBar);
        }

        return barList;
    }
}