package algo.Solution;


import java.util.*;
import helpers.EdgesComparator;

public class BranchAndBound {
    //Graph
    IntGraph intGraph;
    int numProcessors;
    int  numTasks;
    int scheduled = 0;
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

    public LinkedList<Integer>[] equivalentList;

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
        equivalentList = getEquivalentNodes();

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
        scheduled += 1;


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
            return Integer.compare(task2OutEdgeCost, task1OutEdgeCost);
        });
    }

    public LinkedList<Integer>[] getEquivalentNodes(){
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


    /**
     * DeepCopy a current BranchAndBound object.
     * @return a deep copied current BranchAndBound object.
     */
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

        Iterator<Integer> it = scheduledTasks.iterator();
        while (it.hasNext()){
            int task = (Integer)it.next();
            c_branchandbound.scheduledTasks.add(task);
        }

        Iterator<Integer> unit = unscheduledTasks.iterator();
        while (unit.hasNext()){
            int task = (Integer)unit.next();
            c_branchandbound.unscheduledTasks.add(task);
        }


        for (int i = 0; i < numProcessors; i++){
            c_branchandbound.processorTimes[i] = processorTimes[i];
        }
        c_branchandbound.scheduled = scheduled;

        return c_branchandbound;

    }

}
