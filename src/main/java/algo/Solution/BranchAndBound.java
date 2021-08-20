package algo.Solution;


import java.util.*;

public class BranchAndBound {
    //Graph
    IntGraph intGraph;
    int numProcessors;
    int  numTasks;

    //Schedule information
    public Stack<Integer> time;
    public int idle;
    public HashSet<Integer> scheduledTasks;
    public HashSet<Integer> unscheduledTasks;

    //Processor information
    public int [] processorTimes;

    //Task information
    public int [] taskProcessors;
    //Index 0 is StartTime, Index 1 is Weight, Index 2 is Finish Time, Index 3 is communication cost.
    public int [][] taskInformation;

    //Cost Functions
    //Bottom Level
    public int [] bottomLevel;
    //Load Balancer
    public int graphWeight =0;

    //Set of seen schedules.
    public HashSet<Integer> seenStates = new HashSet<>();

    public BranchAndBound(IntGraph graph,int numberOfProcessors, boolean init) {
            intGraph = graph;
            numProcessors = numberOfProcessors;
            numTasks = graph.tasks.length;

            time = new Stack<>();

            scheduledTasks = new HashSet<>();
            unscheduledTasks = new HashSet<>();


            processorTimes = new int[numberOfProcessors];
            taskProcessors = new int[numTasks];

            taskInformation = new int[numTasks][4];
        bottomLevel = new int[numTasks];

        if (init){
            time.push(0);
            idle = 0;
            addUnscheduledTasks();
            setDefaultTaskInfo();
            setDefaultTaskProcessor();

            initB();
            initLB();
        }




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
        LinkedList<int[]> parents = intGraph.inEdges[task];
        int calcCost = 0;
        int processorTime = processorTimes[processor];
        for(int i=0; i<parents.size(); i ++) {
            //Index 0 is parent task, 1 is edge Weight.
            int[] parentInfo = parents.get(i);
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

    public boolean [] getOrder() {
        boolean [] validTasks = new boolean[numTasks];

        for(int i = 0; i < numTasks; i++){
            int nodeIndex = i;

            // if input is 0 size, find all root tasks
            if(intGraph.inEdges[i].size() == 0 && !scheduledTasks.contains(nodeIndex)){
                validTasks[nodeIndex] = true;
                continue;
            }

            boolean flag = true;
            for(int j = 0; j <intGraph.inEdges[i].size(); j++){
                int parent = intGraph.inEdges[i].get(j)[0];
                if(!scheduledTasks.contains(parent)){
                    flag = false;
                    validTasks[nodeIndex] = false;
                    break;
                }
            }
            if(flag && !scheduledTasks.contains(nodeIndex)){
                validTasks[nodeIndex] = true;
            }
        }
        return validTasks;
    }

    private void initB() {
        for(int i=0; i < numTasks;i++) {
            bottomLevel[i] = -1;
        }
        for (int i=0; i<numTasks;i++) {
            int weight = intGraph.weights[i];
            bottomLevel(i,weight);
        }
    }

    private int bottomLevel(int task, int total) {
        if (bottomLevel[task]!= -1) {
            return bottomLevel[task];
        }
        LinkedList<int[]> outEdges = intGraph.outEdges[task];
        if (outEdges.size() == 0) {
            bottomLevel[task] = total;
            return total;
        }

        int max = 0;
        // Traverse through edges and pick the subtree with the maximum weight to form the critical path for this node.
        for (int i =0; i < outEdges.size(); i ++) {
            int child = outEdges.get(i)[0];
            int weight = intGraph.weights[child];
            int temp = total+ bottomLevel(child,weight);

            max = Math.max(max,temp);
        }

        bottomLevel[task] = max;
        return max;
    }

    private void initLB() {
        int [] weights = intGraph.weights;
        for (int i = 0; i< weights.length; i++) {
            graphWeight += weights[i];
        }
    }

    protected int loadBalance(int currentCost) {
        return (int) Math.ceil((graphWeight + idle + currentCost) / numProcessors);
    }

    protected boolean [] normalise() {
        boolean zeroFlag = false;
        boolean [] processorStarted = new boolean[numProcessors];
        for (int i = 0; i < numProcessors; i ++) {
            if (processorTimes[i] == 0) {
                if (!zeroFlag) {
                    zeroFlag = true;
                    processorStarted[i] = true;
                }
                else {
                    processorStarted[i] = false;
                }
            }
            else {
                processorStarted[i] = true;
            }
        }
        return processorStarted;
    }

    public boolean checkSeen(int candTask, int candProcessor, int cost) {
        Set<Stack<Integer>> scheduleSet = new HashSet<>();
        Stack<Integer>[] stacks = new Stack[numProcessors];

        for (int i = 0; i < stacks.length; i++){
            stacks[i] = new Stack<>();
        }

        for(int scheduledTask: scheduledTasks){
            int startTime = taskInformation[scheduledTask][0];
            int allocatedProcessor = taskProcessors[scheduledTask];
            stacks[allocatedProcessor].add(scheduledTask);
            stacks[allocatedProcessor].add(startTime);
        }
        stacks[candProcessor].add(candTask);
        int taskStart = processorTimes[candProcessor] + cost;
        stacks[candProcessor].add(taskStart);

        for(Stack<Integer> stack : stacks){
            scheduleSet.add(stack);
        }

        int id = scheduleSet.hashCode();
        if (seenStates.contains(id)) {
            return true;
        }
        else {
            seenStates.add(id);
        }
        return false;
    }

    public BranchAndBound deepCopy(){
        BranchAndBound c_branchandbound = new BranchAndBound(intGraph, numProcessors, false);

        int numTasks = intGraph.tasks.length;
        for (int i = 0; i < numTasks; i++){
            c_branchandbound.taskProcessors[i] = taskProcessors[i];
            c_branchandbound.bottomLevel[i] = bottomLevel[i];
            for (int j = 0; j < 4; j ++){
                c_branchandbound.taskInformation[i][j] = taskInformation[i][j];
            }
        }
        for (int i = 0; i < time.size(); i++){
            c_branchandbound.time.add(i, time.get(i));
        }
        c_branchandbound.idle = idle;

        Iterator it = scheduledTasks.iterator();
        while (it.hasNext()){
            int task = (Integer)it.next();
            c_branchandbound.scheduledTasks.add(task);
        }

        Iterator unit = unscheduledTasks.iterator();
        while (unit.hasNext()){
            int task = (Integer)unit.next();
            c_branchandbound.unscheduledTasks.add(task);
        }

        for (int i = 0; i < numProcessors; i++){
            c_branchandbound.processorTimes[i] = processorTimes[i];
        }

        c_branchandbound.graphWeight = graphWeight;

        return c_branchandbound;

    }

}
