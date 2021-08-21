package algo.Solution;



import helpers.EdgesComparator;

import java.util.*;

public abstract class BranchAndBound {
    //Graph
    protected IntGraph intGraph;
    protected int numProcessors;
    protected int  numTasks;
    protected int scheduled = 0;

    //Schedule information
    protected Stack<Integer> time;
    protected int idle;
    protected boolean [] scheduledTasks;
    protected boolean[] unscheduledTasks;

    //Processor information
    protected int [] processorTimes;

    //Task information
    protected int [] taskProcessors;
    //Index 0 is StartTime, Index 1 is Weight, Index 2 is Finish Time, Index 3 is communication cost.
    protected int [][] taskInformation;

//    protected int [][] test;

    //Cost Functions
    //Bottom Level
    protected int [] bottomLevel;
    //Load Balancer
    protected int graphWeight =0;

    //Set of seen schedules.
    protected HashSet<Integer> seenStates = new HashSet<>();

    //List of equivalent nodes
    protected LinkedList<Integer>[] equivalentNodesList;

//    Stack<Integer>[] stacks;

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

        taskInformation = new int[numTasks][];
//        test = new int[numTasks][];

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

//        stacks = new Stack[numProcessors];
//
//        for (int i = 0; i < stacks.length; i++){
//            stacks[i] = new Stack<>();
//        }

        equivalentNodesList = this.getEquivalentNodes();

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

//        stacks[processor].add(task);
//        stacks[processor].add(task);
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

//        stacks[processor].pop();
//        stacks[processor].pop();
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

    public boolean checkSeen() {
        Set<Stack<Integer>> scheduleSet = new HashSet<>();
        Stack<Integer>[] stacks = new Stack[numProcessors];

        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = new Stack<>();
        }

        //Add tasks ids and start times to the stack which represents the processor
        for(int i = 0; i < numTasks; i++){
            if(taskInformation[i][0] != -1){
                stacks[taskProcessors[i]].add(i);
                stacks[taskProcessors[i]].add(taskInformation[i][0]);
            }
        }

        // Add the stacks to a set.
        for(Stack<Integer> stack : stacks) {
            scheduleSet.add(stack);
        }

//        Set<Stack<Integer>> scheduleSet = new HashSet<>();
//
//        for(Stack<Integer> stack : stacks){
//            scheduleSet.add(stack);
//        }
//        int id = Arrays.deepHashCode(taskInformation);
        int id = scheduleSet.hashCode();
        if (seenStates.contains(id)) {
            return true;
        }
        else {
            seenStates.add(id);
        }
        return false;
    }



    protected LinkedList<Integer> toFTOList(boolean[] candidateTasks) {
        int child = -1;
        int parentProcessor = -1;
        LinkedList<Integer> result = new LinkedList<>();

        for(int i = 0; i < candidateTasks.length; i++){
            if(candidateTasks[i]){
                result.add(i);
            }
        }

        if (result.size() == 0) {
            return null;
        }


        for (int i = 0; i < candidateTasks.length; i++) {
            // To be an FTO, every node must have at most one parent and at most one child
            if(candidateTasks[i]){
                int childrenSize = intGraph.outEdges[i].size();

                if (intGraph.inEdges[i].size() > 1 || childrenSize > 1) {
                    return null;
                }

                // Every node must have the same child IF they have a child
                if (childrenSize > 0) {
                    int taskChild = intGraph.outEdges[i].get(0)[0];
                    if (child == -1) {
                        child = taskChild;
                    } else if (child != taskChild) {
                        return null;
                    }
                }

                // every node must have their parents on the same processor IF they have a parent.
                if (intGraph.inEdges[i].size() > 0) {
                    int taskParent = intGraph.inEdges[i].get(0)[0];
                    int taskParentProcessor = taskProcessors[taskParent];
                    if (parentProcessor == -1) {
                        parentProcessor = taskParentProcessor;
                    } else if (parentProcessor != taskParentProcessor) {
                        return null;
                    }
                }
            }

            // sort by non-decreasing data ready time, i.e. finish time of parent + weight of edge
            sortByDataReadyTime(result);
//            System.out.println(result);
            // verify if the candidate tasks are ordered by out edge cost in non-increasing order,
            // if not we do not have a FTO.
            int prevOutEdgeCost = Integer.MAX_VALUE;
            for (int j = 0; j < candidateTasks.length; j++) {
                if(candidateTasks[j]){
                    int edgeCost;
                    if (intGraph.outEdges[j].size() == 0) {
                        // there is no out edge, cost is 0
                        edgeCost = 0;
                    }
                    else {
                        int taskChild = intGraph.outEdges[j].get(0)[0];
                        edgeCost = intGraph.outEdges[j].get(0)[1];
                    }

                    // if our current edge is larger than the previous edge, we don't have a FTO.
                    if (edgeCost > prevOutEdgeCost) {
                        return null;
                    } else {
                        prevOutEdgeCost = edgeCost;
                    }
                }

            }
        }

        // we have a FTO!
        return result;
    }

    private void sortByDataReadyTime(LinkedList<Integer> candidateTasks) {
//        System.out.println("START");
        candidateTasks.sort((task1, task2) -> {
            int task1DataReadyTime = 0;
            int task2DataReadyTime = 0;

            if (intGraph.inEdges[task1].size() != 0) {
                int parent = intGraph.inEdges[task1].get(0)[0];
                int commCost = intGraph.inEdges[task1].get(0)[1];
                task1DataReadyTime = taskInformation[parent][2] + commCost;
            }

            if (intGraph.inEdges[task2].size() != 0) {
                int parent = intGraph.inEdges[task2].get(0)[0];
                int commCost = intGraph.inEdges[task2].get(0)[1];
                task2DataReadyTime = taskInformation[parent][2] + commCost;
            }

//            System.out.println("Task 1");
//            System.out.println(task1);
//            System.out.println(task1DataReadyTime);
//            System.out.println("Task 2");
//            System.out.println(task2);
//            System.out.println(task2DataReadyTime);

            if (task1DataReadyTime < task2DataReadyTime) {
                return -1;
            }
            if (task1DataReadyTime > task2DataReadyTime) {
                return 1;
            }

            // Data ready times are equal, break the tie using the out-edge cost
            int task1OutEdgeCost = 0;
            int task2OutEdgeCost = 0;
            if (intGraph.outEdges[task1].size() != 0) {
                task1OutEdgeCost = intGraph.outEdges[task1].get(0)[1];
            }
            if (intGraph.outEdges[task2].size() != 0) {
                task2OutEdgeCost = intGraph.outEdges[task2].get(0)[1];
            }
//            System.out.println("Task 1 OUT");
//            System.out.println(task1);
//            System.out.println(task1OutEdgeCost);
//            System.out.println("Task 2 OUT");
//            System.out.println(task2);
//            System.out.println(task2OutEdgeCost);

            return Integer.compare(task2OutEdgeCost, task1OutEdgeCost);
        });
    }

    private LinkedList<Integer>[] getEquivalentNodes(){
        HashSet<Integer> memo = new HashSet<>();
        LinkedList<Integer>[] equivalentNodesList = new LinkedList[numTasks];

        for(int i = 0; i < numTasks; i++){
            if(!memo.contains(i)){
                LinkedList<Integer> equivalentNodes = new LinkedList<>();
                equivalentNodes.add(i);
                for(int j = 0; j < numTasks; j++){
                    if(i == j){
                        continue;
                    }
                    if(!memo.contains(j)){
                        if(this.equivalentCheck(i, j)){
                            equivalentNodes.add(j);
                        }
                    }
                }

                for(int j = 0; j < equivalentNodes.size(); j++){
                    equivalentNodesList[equivalentNodes.get(j)] = equivalentNodes;
                    memo.add(equivalentNodes.get(j));
                }
            }
        }
        return equivalentNodesList;
    }

    private boolean equivalentCheck(int taskA, int taskB){
        if(intGraph.weights[taskA] != intGraph.weights[taskB]){
            return false;
        }

        if((intGraph.inEdges[taskA].size() != intGraph.inEdges[taskB].size()) ||
                (intGraph.outEdges[taskA].size() != intGraph.outEdges[taskB].size())){
            return false;
        }

        List<int[]> AParents = intGraph.inEdges[taskA];
        List<int[]> BParents = intGraph.inEdges[taskB];
        List<int[]> AChildren = intGraph.outEdges[taskA];
        List<int[]> BChildren = intGraph.outEdges[taskB];

        Collections.sort(AParents, new EdgesComparator());
        Collections.sort(BParents, new EdgesComparator());
        Collections.sort(AChildren, new EdgesComparator());
        Collections.sort(BChildren, new EdgesComparator());

        for(int i = 0; i < AParents.size(); i++){
            int parentA = AParents.get(i)[0];
            int parentACost = AParents.get(i)[1];
            int parentB = BParents.get(i)[0];
            int parentBCost = BParents.get(i)[1];
            if(parentA != parentB || parentACost != parentBCost){
                return false;
            }
        }

        for(int i = 0; i < AChildren.size(); i++){
            int childA = AChildren.get(i)[0];
            int childACost = AChildren.get(i)[1];
            int childB = BChildren.get(i)[0];
            int childBCost = BChildren.get(i)[1];
            if(childA != childB || childACost != childBCost){
                return false;
            }
        }

        return true;
    }
}
