package gui;

import algo.BestSchedule;
import algo.ScheduleThread;
import io.InputReader;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import org.graphstream.graph.Graph;

import java.util.*;

/**
 * Class for controlling the GUI visualiser - alters the information/design displayed.
 * Linked to a FXML file that creates the user interface.
 *
 * @author Samuel Chen, Kayla Kautai
 */
public class Controller {

    private StatusRefresh control = new StatusRefresh(); // Controls this thread to update the GUI periodically
    private ScheduleThread scheduleThread; // Thread that runs the scheduling algorithm
    private int procNum; // Number of processors
    private int[][] taskInfo;
    private int[] taskProc;

    // FXML annotations to tag the nonpublic controller member fields of the FXML markup
    @FXML
    private Text graphName; // Displays the file name of the input graph
    @FXML
    private Text timeElapsed; // Displays the actual time taken to run the schedule thread
    @FXML
    private Text totalTask; // Displays the number of total tasks, nodes in the input graph
    @FXML
    private Text numProcess; // Displays the number of processors requested to solve the schedule
    @FXML
    private Text statusText; // Displays the status of the visualiser GUI - STANDBY, SCHEDULING, FINISHED
    @FXML
    private Text statesExamined; // Displays the number of states currently searched through
    @FXML
    private Text statesMagnitude; // Displays the abbreviation of the states - THOUSAND, MILLION, BILLION
    @FXML
    private Text bestTime; // Displays the current best (shortest) scheduled finish time
    @FXML
    private Button startBtn; // Button that starts the scheduling thread

    // Displays a bar chart for the best partial schedule found through the scheduler.
    // A bar represents a task. Each bar is assigned to a processor on the y-axis and has a length in time on the x-axis.
    @FXML
    private StackedBarChart<Number, String> barChartSchedule ;
    @FXML
    private Text chartTitle;

    /**
     * Method sets the global variables and GUI text to those passed into the controller as inputs.
     * @param scheduleThread Thread that runs the schedule algorithm
     * @param inputGraphName Name of the input graph file
     * @param procNum Number of processors the schedule is required to run on based on user's input
     */
    public void passInput(ScheduleThread scheduleThread,String inputGraphName, int procNum,int threadCount){
        // Initialise the text on the GUI to display the input graph information
        graphName.setText(inputGraphName);
        totalTask.setText(getTaskNum(inputGraphName)+"");
        this.procNum=procNum;
        numProcess.setText(String.valueOf(threadCount));
        // Set up the thread for scheduling algorithm
        this.scheduleThread = scheduleThread;
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        // Set up GUI to show it is waiting for user to start the scheduling
        setGUIInitial();
    }

    /**
     * Method is called when the START button is pressed to begin the scheduling algorithm on the thread.
     */
    public void start(){
        // Modifies GUI to show scheduling has begun
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

            // Updates the number of states inspected already
            if(scheduleThread.getStates() > 0){
                formatStatesExamined(scheduleThread.getStates());
            }
            // Updates the best time found so far if it has inspected more than one state
            if (scheduleThread.getBestTime() != Integer.MAX_VALUE) {
                bestTime.setText(scheduleThread.getBestTime() + "");
            }
            // Updates the bar chart with the best schedule found so far
            updateBarChart();

            // Checks if the scheduling is finished to stop the thread and update the GUI to show it is finished
            if(scheduleThread.isDone()){
                setGUICompleted();
                this.stop();
            }

            // Formats the animated timer to be in minutes:seconds.milliseconds for display in the GUI
            long elapsedMillis = System.currentTimeMillis() - startTime;
            int milliseconds = (int) ( elapsedMillis % 1000);
            int seconds = (int) ((elapsedMillis / 1000) % 60);
            int minutes = (int) ((elapsedMillis / (1000 * 60)) % 60);
            timeElapsed.setText(String.format("%02d:%02d.%02d",minutes,seconds,milliseconds/10));
        }

        /**
         * call to set the start time of the animation class
         */
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

        List<List<int[]>> barList = getBestSchedule();
        if (barList == null) {
            return;
        }

        // Clear the current bar chart data
        barChartSchedule.getData().clear();

        XYChart.Series<Number, String> dataSeries1 = new XYChart.Series<>();
        int procNum = 1;

        // Sort the tasks in each processor's by starting time
        for (List<int[]> eachBar : barList ){
            Collections.sort(eachBar, (c1, c2) -> {
                if (c1[0] > c2[0]) return 1;
                if (c1[0] < c2[0]) return -1;
                return 0;
            });

            // Add idle task as the beginning of each processor that doesn't have starting time of 0
            if(eachBar.size() != 0 && eachBar.get(0)[0] != 0){
                int[] idlePart = new int[]{0,eachBar.get(0)[0],1,eachBar.get(0)[3]};
                eachBar.add(0,idlePart);
            }

            // Add idle task between tasks that has gap between them
            int i = 1;
            while(eachBar.size() > i){
                int[] currPart = eachBar.get(i);
                int[] prevPart = eachBar.get(i-1);
                if(currPart[0] != prevPart[0] + prevPart[1]){
                    int idleStartTime = prevPart[0] + prevPart[2];
                    int idleDuration = currPart[0] -idleStartTime - prevPart[1];
                    int[] idlePart = new int[]{idleStartTime,idleDuration,1,eachBar.get(i)[2]};
                    eachBar.add(i,idlePart);
                    i++;
                }
                i++;
            }

            // Add bar to the series
            for(int[] eachPart : eachBar){
                int length = eachPart[1];
                final XYChart.Data<Number, String> bar = new XYChart.Data<>(length, "P" + procNum);
                bar.nodeProperty().addListener((ov, oldNode, node) -> {
                    //set color of each type of bar, transparent for idle section, blue for task bar section
                    if (node != null) {
                        if(eachPart[2] == 1){
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
     * @return a list of tasks
     */
    public List<List<int []>> getBestSchedule(){
        BestSchedule b = scheduleThread.getBestSchedule();

        //If best schedule has not been found return null as we have nothing to show.
        if (b.getTime() == Integer.MAX_VALUE) {
            return null;
        }
        List<List<int[]>> barList = new ArrayList<>();

        taskInfo = b.getTaskInformation();
        taskProc = b.getTaskProcessors();

        //store all node information in a list of int array
        List<int[]> nodeList = new ArrayList<>();
        for(int i=0;i<taskProc.length;i++){
            int[] eachTask = new int[]{taskInfo[i][0],taskInfo[i][1],0,taskProc[i]};
            nodeList.add(eachTask);
        }


        for(int i=0;i<procNum;i++){
            List<int[]> eachBar = new ArrayList<>();
            for(int[] node : nodeList){
                if(node[3] == i){
                    eachBar.add(node);
                }
            }
            barList.add(eachBar);
        }

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