package algo.Solution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

public abstract class BranchAndBound {
    //Graph
    IntGraph intGraph;
    int numProcessors;
    int  numTasks;

    //Schedule information
    protected Stack<Integer> time;
    private Integer idle;
    private HashSet<Integer> scheduledTasks;
    private HashSet<Integer> unscheduledTasks;

    //Processor information
    private int [] processorTimes;

    //Task information
    protected int [] taskProcessors;
    //Index 0 is StartTime, Index 1 is Weight, Index 2 is Finish Time, Index 3 is communication cost.
    protected int [][] taskInformation;

    public BranchAndBound(IntGraph graph,int numberOfProcessors) {
        intGraph = graph;
        numProcessors = numberOfProcessors;
        numTasks = graph.tasks.length;

        time = new Stack<>();
        time.push(0);
        idle = 0;
        scheduledTasks = new HashSet<>();
        unscheduledTasks = new HashSet<>();

        addUnscheduledTasks();

        processorTimes = new int[numberOfProcessors];
        taskProcessors = new int[numTasks];

        taskInformation = new int[numTasks][3];

        setDefaultTaskInfo();
        setDefaultTaskProcessor();
    }

    private void addUnscheduledTasks() {
        for (int i = 0; i < numTasks; i++) {
            unscheduledTasks.add(i);
        }
    }

    private void setDefaultTaskInfo() {
        for (int i = 0; i < numTasks; i++) {
            taskInformation[i] = new int[]{-1,-1,-1,-1};
        }
    }

    private void setDefaultTaskProcessor() {
        for (int i = 0; i < numTasks; i++) {
            taskProcessors[i] = -1;
        }
    }

    protected void addTask(int task, int processor, int cost) {

        //Set Task information
        //Set Task StartTime
        int startTime = processorTimes[processor] + cost;
        taskInformation[task][0] = startTime;
        //Set Task WeightTime
        int weight = intGraph.weights[task];
        taskInformation[task][1] = weight;
        //Set Task EndTime
        int endTime = startTime + weight;
        taskInformation[task][2] = endTime;
        //Set Task Communication cost.
        taskInformation[task][3] = cost;
        //Set processor task is scheduled on
        taskProcessors[task] = processor;

        //Set Processor information
        //Set Processor latest time.
        processorTimes[processor] = endTime;

        //Set Schedule information
        idle += cost;
        scheduledTasks.add(task);
        unscheduledTasks.remove(task);

        //Set new schedule end time.
        int currentTime = time.peek();
        time.push(Math.max(endTime,currentTime));
    }

    protected void removeTask(int task, int processor, int cost) {
        //Reset Schedule info
        time.pop();
        idle -= cost;
        scheduledTasks.remove(task);
        unscheduledTasks.add(task);

        //Reset Processor Information
        int taskWeight = taskInformation[task][1];
        int oldTime = processorTimes[processor];
        int newTime = oldTime - taskWeight - cost;
        processorTimes[processor] = newTime;

        //Reset Task Info
        //Set Task StartTime
        taskInformation[task][0] = -1;
        //Set Task WeightTime
        taskInformation[task][1] = -1;
        //Set Task EndTime
        taskInformation[task][2] = -1;
        //Set Task Communication cost.
        taskInformation[task][3] = -1;
        //Set processor task is scheduled on
        taskProcessors[task] = -1;
    }

    protected int commCost(int task,int processor) {
        LinkedList<Integer[]> parents = intGraph.inEdges[task];
        int calcCost = 0;
        int processorTime = processorTimes[processor];
        for(int i=0; i<parents.size(); i ++) {
            //Index 0 is parent task, 1 is edge Weight.
            Integer[] parentInfo = parents.get(i);
            int parent = parentInfo[0];
            int edgeWeight = parentInfo[1];
            int parentProcessor = taskProcessors[parent];

            if (parentProcessor != processor) {
                int parentEndTime = taskInformation[parentInfo[0]][2];
                int comm = parentEndTime + edgeWeight - processorTime;
                calcCost = Math.max(calcCost,comm);
            }
        }
        return calcCost;
    }

    public ArrayList<Integer> getOrder() {
        ArrayList<Integer> result = new ArrayList<>();

        for(int i = 0; i < numTasks; i++){
            int nodeIndex = i;

            // if input is 0 size, find all root tasks
            if(intGraph.inEdges[i].size() == 0 && !scheduledTasks.contains(nodeIndex)){
                result.add(nodeIndex);
                continue;
            }

            boolean flag = true;
            for(int j = 0; j <intGraph.inEdges[i].size(); j++){
                int parent = intGraph.inEdges[i].get(j)[0];
                if(!scheduledTasks.contains(parent)){
                    flag = false;
                    break;
                }
            }
            if(flag && !scheduledTasks.contains(nodeIndex)){
                result.add(nodeIndex);
            }
        }
        return result;
    }
}
