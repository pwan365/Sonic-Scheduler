package gui;

import algo.Schedule.Processor;
import algo.Schedule.Task;
import algo.Solution.BestSchedule;
import algo.Solution.ScheduleThread;
import io.InputReader;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import org.graphstream.graph.Graph;

import java.util.*;

public class Controller {

    private StatusRefresh control = new StatusRefresh();
    private ScheduleThread scheduleThread;
    private int procNum;
    private int[][] taskInfo;
    private int[] taskProc;

    @FXML
    private Text graphName;
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
    private Text chartTitle;

    @FXML
    private StackedBarChart<Number, String> barChartSchedule ;
    CategoryAxis yAxis = new CategoryAxis();
    NumberAxis xAxis = new NumberAxis();

    /**
     * Method sets the global variables and GUI text to those passed into the controller as inputs.
     * @param scheduleThread Thread that runs the schedule algorithm
     * @param inputGraphName Name of the input graph file
     * @param procNum Number of processors the schedule is required to run on based on user's input
     */
    public void passInput(ScheduleThread scheduleThread,String inputGraphName, int procNum){
        this.scheduleThread = scheduleThread;
        graphName.setText(inputGraphName);
        totalTask.setText(getTaskNum(inputGraphName)+"");
        this.procNum=procNum;
        numProcess.setText(String.valueOf(procNum));
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        setGUIInitial();
    }

    /**
     * Method is called when the START button is pressed to begin the scheduling algorithm on the thread.
     */
    public void start(){
        setGUIRunning();
        // Starts running the scheduling algorithm
        scheduleThread.start();
        control.start();
        control.setStartTime();

    }


    /**
     * Polls periodically for new information to update the GUI
     * Starts a timer whilst running the scheduler to display the time elapsed.
     */
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

            if(scheduleThread.getStates() != Integer.MAX_VALUE){
                formatStatesExamined(scheduleThread.getStates());
            }
            if (scheduleThread.getStates() > 0) {
                bestTime.setText(scheduleThread.getBestTime() + "");
            }

            updateBarChart();

            System.out.println(scheduleThread.isDone());
            if(scheduleThread.isDone()){
                setGUICompleted();
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
            //int hours = (int) (elapsedMillis / (1000 * 60 * 60));
            timeElapsed.setText(String.format("%02d:%02d.%02d",/*hours,*/minutes,seconds,milliseconds/10));
        }

        public void setStartTime(){
            startTime = System.currentTimeMillis();
        }
    }

    /**
     * Updates the states examined on the GUI and formats large numbers into abbreviations (i.e. thousand, million)
     * @param currentStates The number of states the scheduler has searched through so far
     */
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

    /**
     * Alters the bar chart to display tasks as bars aligned with the specific, dedicated
     * processor on the y-axis.
     */
    public void updateBarChart() {


        List<List<Task>> barList = getBestSchedule();
        if (barList == null) {
            return;
        }


        barChartSchedule.getData().clear();

        XYChart.Series<Number, String> dataSeries1 = new XYChart.Series<Number, String>();
        int procNum = 1;

        //sort the task in each processors by starting time
        for (List<Task> eachBar : barList ){
            Collections.sort(eachBar, (c1, c2) -> {
                if (c1.getStartingTime() > c2.getStartingTime()) return 1;
                if (c1.getStartingTime() < c2.getStartingTime()) return -1;
                return 0;
            });
            System.out.println(procNum+"P--------");

            for(Task task : eachBar){
                System.out.println("StartTime:"+task.getStartingTime()+" DurationTime:"+task.getDurationTime()+" Total:"+(task.getStartingTime()+task.getDurationTime()));
            }

            //add idle task as the beginning of each processor that doesn't have starting time of 0
            if(eachBar.size() != 0 && eachBar.get(0).getStartingTime() != 0){
                Task idlePart = new Task(0,eachBar.get(0).getStartingTime(),true,eachBar.get(0).getProcNum());
                eachBar.add(0,idlePart);
            }

            //add idle task between tasks that has gap between them
            int i = 1;
            while(eachBar.size() > i){
                Task currPart = eachBar.get(i);
                Task prevPart = eachBar.get(i-1);
                if(currPart.getStartingTime() != prevPart.getStartingTime() + prevPart.getDurationTime()){
                    int idleStartTime = prevPart.getStartingTime() + prevPart.getDurationTime();
                    int idleDuration = currPart.getStartingTime() -idleStartTime;
                    Task idlePart = new Task(idleStartTime,idleDuration,true,eachBar.get(i).getProcNum());
                    eachBar.add(i,idlePart);
                    i++;
                }
                i++;
            }

            //add bar to the series
            for(Task eachPart : eachBar){
                int length = eachPart.getDurationTime();
                final XYChart.Data<Number, String> bar = new XYChart.Data<Number,String>(length, "P" + procNum);
                bar.nodeProperty().addListener((ov, oldNode, node) -> {
                    //set color of each type of bar, transparent for idle section, blue for task bar section
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

    /**
     * Method used to extract a list of a list of tasks from a bestSchedule object
     * stored as global variable.
     * @return a list of tasks
     */
    public List<List<Task>> getBestSchedule(){
        BestSchedule b = scheduleThread.getBestSchedule();
        if (b.getTime() == Integer.MAX_VALUE) {
            return null;
        }
        List<List<Task>> barList = new ArrayList<>();

        if (b.getTaskInformation()==null || b.getTaskProcessors()==null){
            return null;
        }

        taskInfo = b.getTaskInformation();
        taskProc = b.getTaskProcessors();


        //int taskInfo
        //int[][0]:startTime
        //int[][1]:Weight/Duration
        //int[][2]:EndTime
        //int[0][3]:Cost
//        int[0] taskproc
        List<Task> nodeList = new ArrayList<>();
        for(int i=0;i<taskProc.length;i++){
            Task eachTask = new Task(taskInfo[i][0],taskInfo[i][1],false,taskProc[i]);
            nodeList.add(eachTask);
        }

        for(int i=0;i<procNum;i++){
            List<Task> eachBar = new ArrayList<>();
            for(Task node : nodeList){
                if(node.getProcNum() == i){
                    eachBar.add(node);
                }
            }
            barList.add(eachBar);
        }






//        HashSet<Task> tasks;
//        System.out.println("-------------");
//        for(int i=0 ; i<processors.length;i++){
//            System.out.println("P"+i);
//
//            List<Task> eachBar = new ArrayList<>();
////            int j=0;
//
//                tasks=processors[i].getTasks();
//                for(Task task:tasks){
////                    System.out.println("StartTime:"+task.getStartingTime()+" DurationTime:"+task.getDurationTime()+" Total:"+(task.getStartingTime()+task.getDurationTime()));
//                    eachBar.add(task);
////                    j++;
//                }
//                barList.add(eachBar);
//
//        }
        return barList;
    }

    /**
     * change the property of the GUI when task are being scheduled
     */
    public void setGUIRunning(){
        // Start button is disabled and status of the GUI is changed
        startBtn.setDisable(true);
        chartTitle.setText("PARTIAL OPTIMAL SCHEDULE");
        statusText.setFill(Color.GREEN);
        statusText.setText("SCHEDULING");
    }

    /**
     * change the property of the GUI when task scheduling is completed
     */
    public void setGUICompleted(){
        statusText.setText("FINISHED");
        chartTitle.setText("OPTIMAL SCHEDULE");
    }

    /**
     * change the property of the GUI when GUI is first loaded
     */
    public void setGUIInitial(){
        chartTitle.setText("FIND THE OPTIMAL SCHEDULE");
        chartTitle.setFill(Paint.valueOf("#336699"));
        chartTitle.setStyle("-fx-font-weight: bold");
    }

    /**
     * A helper method that retrieve the number of nodes in the input graph
     * @param inputFileName name of the input graph
     * @return integer that indicates the number of nodes in the graph
     */
    public int getTaskNum(String inputFileName){
        InputReader reader = new InputReader(inputFileName);
        Graph inputGraph = reader.read();
        return inputGraph.getNodeCount();
    }
}