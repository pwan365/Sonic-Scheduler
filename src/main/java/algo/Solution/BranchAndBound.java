package algo.Solution;


import java.util.*;

public abstract class BranchAndBound {
    //Graph
    IntGraph intGraph;
    int numProcessors;
    int  numTasks;
    int scheduled = 0;

    //Schedule information
    protected Stack<Integer> time;
    private int idle;
    private boolean [] scheduledTasks;
    protected boolean[] unscheduledTasks;

    //Processor information
    protected int [] processorTimes;

    //Task information
    protected int [] taskProcessors;
    //Index 0 is StartTime, Index 1 is Weight, Index 2 is Finish Time, Index 3 is communication cost.
    protected int [][] taskInformation;

    //Cost Functions
    //Bottom Level
    protected int [] bottomLevel;
    //Load Balancer
    protected int graphWeight =0;

    //Set of seen schedules.
    protected HashSet<Integer> seenStates = new HashSet<>();

    public BranchAndBound(IntGraph graph,int numberOfProcessors) {
        intGraph = graph;
        numProcessors = numberOfProcessors;
        numTasks = graph.tasks.length;

        time = new Stack<>();
        time.push(0);
        idle = 0;
        scheduledTasks = new boolean[numTasks];
        unscheduledTasks = new boolean[numTasks];

        addUnscheduledTasks();

        processorTimes = new int[numberOfProcessors];
        taskProcessors = new int[numTasks];

        taskInformation = new int[numTasks][3];

        setDefaultTaskInfo();
        setDefaultTaskProcessor();

        //Initialize bottom levels
        bottomLevel = new int[numTasks];
        initB();

//        for (int i =0; i < bottomLevel.length; i ++) {
//            System.out.println("Task");
//            System.out.println(i);
//            System.out.println("B Level");
//            System.out.println(bottomLevel[i]);
//        }

        //Initialize Load Balancer
        initLB();
    }

    private void addUnscheduledTasks() {
        for (int i = 0; i < numTasks; i++) {
            unscheduledTasks[i] = true;
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
        scheduledTasks[task] = true;
        unscheduledTasks[task] = false;
        scheduled += 1;

        //Set new schedule end time.
        int currentTime = time.peek();
        time.push(Math.max(endTime,currentTime));
    }

    protected void removeTask(int task, int processor, int cost) {
        //Reset Schedule info
        time.pop();
        idle -= cost;
        scheduledTasks[task] = false;
        unscheduledTasks[task] = true;
        scheduled -= 1;

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
            if(intGraph.inEdges[i].size() == 0 && !scheduledTasks[nodeIndex]){
                validTasks[nodeIndex] = true;
                continue;
            }

            boolean flag = true;
            for(int j = 0; j <intGraph.inEdges[i].size(); j++){
                int parent = intGraph.inEdges[i].get(j)[0];
                if(!scheduledTasks[parent]){
                    flag = false;
                    validTasks[nodeIndex] = false;
                    break;
                }
            }
            if(flag && !scheduledTasks[nodeIndex]){
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
//        Set<Stack<Integer>> scheduleSet = new HashSet<>();
//        Stack<Integer>[] stacks = new Stack[numProcessors];
//
//        for (int i = 0; i < stacks.length; i++){
//            stacks[i] = new Stack<>();
//        }
//
//
//
//        for(int i = 0; i <numTasks; i ++){
//            if (scheduledTasks[i]) {
//                int startTime = taskInformation[i][0];
//                int allocatedProcessor = taskProcessors[i];
//                stacks[allocatedProcessor].add(i);
//                stacks[allocatedProcessor].add(startTime);
//            }
//        }
//
//        stacks[candProcessor].add(candTask);
//        int taskStart = processorTimes[candProcessor] + cost;
//        stacks[candProcessor].add(taskStart);
//
//        for(Stack<Integer> stack : stacks){
//            scheduleSet.add(stack);
//        }
        int id2 = Arrays.hashCode(taskProcessors);
        int id1 = Arrays.deepHashCode(taskInformation);
        Stack<Integer> stacks = new Stack<>();
        stacks.add(id1);
        stacks.add(id2);
        int id = stacks.hashCode();
        if (seenStates.contains(id)) {
            return true;
        }
        else {
            seenStates.add(id);
        }
        return false;
    }
}
