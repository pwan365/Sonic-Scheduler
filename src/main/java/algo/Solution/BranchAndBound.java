package algo.Solution;


import java.util.*;
import algo.Solution.helpers.EdgesComparator;
import algo.Solution.helpers.costFunctions.BottomLevel;

public class BranchAndBound {
    //Graph
    IntGraph intGraph;
    int numProcessors;
    int numTasks;
    int scheduled = 0;
    //Schedule information
    public Stack<Integer> time;
    public int idle;
    public boolean[] scheduledTasks;
    public boolean[] unscheduledTasks;


    //Processor information
    public int[] processorTimes;

    //Task information
    public int[] taskProcessors;
    //Index 0 is StartTime, Index 1 is Weight, Index 2 is Finish Time, Index 3 is communication cost.
    public int[][] taskInformation;

    //Cost Functions
    //Bottom Level
    public int[] bottomLevel;
    //Load Balancer
    public int graphWeight = 0;

    //Set of seen schedules.
    public HashSet<Integer> seenStates = new HashSet<>();

    public LinkedList<Integer>[] equivalentList;

    public BranchAndBound(IntGraph graph, int numberOfProcessors, boolean init) {
        intGraph = graph;
        numProcessors = numberOfProcessors;
        numTasks = graph.tasks.length;
        time = new Stack<>();
        scheduledTasks = new boolean[numTasks];
        unscheduledTasks = new boolean[numTasks];
        processorTimes = new int[numberOfProcessors];
        taskProcessors = new int[numTasks];
        taskInformation = new int[numTasks][4];
        bottomLevel = BottomLevel.initBottomLevel(numTasks,intGraph.weights, intGraph.outEdges);
        equivalentList = getEquivalentNodes();

        if (init) {
            time.push(0);
            idle = 0;
            addUnscheduledTasks();
            setDefaultTaskInfo();
            setDefaultTaskProcessor();
//            initB();
            initLB();
        }
    }

    private void addUnscheduledTasks() {
        for (int i = 0; i < numTasks; i++) {
            unscheduledTasks[i] = true;
        }
    }

    private void setDefaultTaskInfo() {
        for (int i = 0; i < numTasks; i++) {
            taskInformation[i] = new int[]{-1, -1, -1, -1};
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
        time.push(Math.max(endTime, currentTime));

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

    protected int commCost(int task, int processor) {
        LinkedList<int[]> parents = intGraph.inEdges[task];
        int calcCost = 0;
        int processorTime = processorTimes[processor];
        for (int i = 0; i < parents.size(); i++) {
            //Index 0 is parent task, 1 is edge Weight.
            int[] parentInfo = parents.get(i);
            int parent = parentInfo[0];
            int edgeWeight = parentInfo[1];
            int parentProcessor = taskProcessors[parent];

            if (parentProcessor != processor) {
                int parentEndTime = taskInformation[parentInfo[0]][2];
                int comm = parentEndTime + edgeWeight - processorTime;
                calcCost = Math.max(calcCost, comm);
            }
        }
        return calcCost;
    }

    public boolean[] getOrder() {
        boolean[] validTasks = new boolean[numTasks];

        for (int i = 0; i < numTasks; i++) {
            int nodeIndex = i;

            // if input is 0 size, find all root tasks
            if (intGraph.inEdges[i].size() == 0 && !scheduledTasks[nodeIndex]) {
                validTasks[nodeIndex] = true;
                continue;
            }

            boolean flag = true;
            for (int j = 0; j < intGraph.inEdges[i].size(); j++) {
                int parent = intGraph.inEdges[i].get(j)[0];
                if (!scheduledTasks[parent]) {
                    flag = false;
                    validTasks[nodeIndex] = false;
                    break;
                }
            }
            if (flag && !scheduledTasks[nodeIndex]) {
                validTasks[nodeIndex] = true;
            }
        }
        return validTasks;
    }

//    private void initB() {
//        for (int i = 0; i < numTasks; i++) {
//            bottomLevel[i] = -1;
//        }
//        for (int i = 0; i < numTasks; i++) {
//            int weight = intGraph.weights[i];
//            bottomLevel(i, weight);
//        }
//    }
//
//    private int bottomLevel(int task, int total) {
//        if (bottomLevel[task] != -1) {
//            return bottomLevel[task];
//        }
//        LinkedList<int[]> outEdges = intGraph.outEdges[task];
//        if (outEdges.size() == 0) {
//            bottomLevel[task] = total;
//            return total;
//        }
//
//        int max = 0;
//        // Traverse through edges and pick the subtree with the maximum weight to form the critical path for this node.
//        for (int i = 0; i < outEdges.size(); i++) {
//            int child = outEdges.get(i)[0];
//            int weight = intGraph.weights[child];
//            int temp = total + bottomLevel(child, weight);
//
//            max = Math.max(max, temp);
//        }
//
//        bottomLevel[task] = max;
//        return max;
//    }

    private void initLB() {
        int[] weights = intGraph.weights;
        for (int i = 0; i < weights.length; i++) {
            graphWeight += weights[i];
        }
    }

    protected int loadBalance(int currentCost) {
        return (int) Math.ceil((graphWeight + idle + currentCost) / numProcessors);
    }

    public boolean checkSeen() {
        Set<List<Integer>> scheduleSet = new HashSet<>(); // constant time operation so using hashSet
        List<Integer>[] lists = new LinkedList[numProcessors];

        for (int i = 0; i < lists.length; i++) {
            lists[i] = new LinkedList<>();
        }

        //Add tasks ids and start times to the list which represents the processor
        for(int i = 0; i < numTasks; i++){
            if(taskInformation[i][0] != -1){
                lists[taskProcessors[i]].add(i);
                lists[taskProcessors[i]].add(taskInformation[i][0]);
            }
        }

        // Add the lists to the set.
        for(List<Integer> stack : lists) {
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

    /**
     * Generates a list of integers representing a Fixed Task Ordered list of tasks
     * @param candidateTasks a boolean array indicating which task is valid to be scheduled
     * @return a list of integers, which is the FTO list
     */
    protected LinkedList<Integer> toFTOList(boolean[] candidateTasks) {
        LinkedList<Integer> result = new LinkedList<>();

        // construct a list of candidate tasks
        for(int i = 0; i < candidateTasks.length; i++){
            if(candidateTasks[i]){
                result.add(i);
            }
        }

        // if no candidate task return null
        if (result.size() == 0) {
            return null;
        }

        int child = -1;
        int parentProcessor = -1;

        for (int i: result) {
            // Check if the candidate task has maximum one child and one parent
            LinkedList<int[]> childrenList = intGraph.outEdges[i];
            LinkedList<int[]> parentList = intGraph.inEdges[i];

            if (parentList.size() > 1 || childrenList.size() > 1) {
                return null;
            } else if (childrenList.size() > 0){
                // candidate tasks either have no child, or all tasks have the same child
                int taskChild = intGraph.outEdges[i].get(0)[0];
                if (child == -1) {
                    child = taskChild;
                }
                if (child != taskChild){
                    return null;
                }
            } else if (parentList.size() > 0){
                // nodes either have the same parent or all parents are scheduled on the same processor
                int parent = intGraph.inEdges[i].get(0)[0];
                int parentTaskProcessor = taskProcessors[parent];
                if (parentProcessor == -1) {
                    parentProcessor = parentTaskProcessor;
                }
                if (parentProcessor != parentTaskProcessor) {
                    return null;
                }
            }
        }

        // sort by data ready time in a non-decreasing order
        result.sort((task1, task2) -> compareDRT(task1, task2));

        // if the out edge cost is not ordered in non-increasing order, it is not a fto list
        int prevOEC = Integer.MAX_VALUE;
        for (int j : result) {
            List<int[]> taskChildren = intGraph.outEdges[j];
            int commCost;
            if (taskChildren.size() == 0) {
                // there is no out edge, cost is 0
                commCost = 0;
            }
            else {
                commCost = taskChildren.get(0)[1];
            }

            // if our current edge is larger than the previous edge, we don't have a FTO.
            if (commCost > prevOEC) {
                return null;
            } else {
                prevOEC = commCost;
            }
        }

        return result;
    }

    /**
     * Compare data ready time for two tasks. If they are equal, we then compare out-communication-cost
     * @param task1
     * @param task2
     * @return if return value is negative, it puts task1 after task2. Otherwise it puts task2 after task1
     */
    private int compareDRT(int task1, int task2){
        int drt1 = 0; // data ready time for task1
        int drt2 = 0; // data ready time for task2

        List<int[]> task1Parents = intGraph.inEdges[task1];
        List<int[]> task2Parents = intGraph.inEdges[task2];

        if (task1Parents.size() > 0) {
            int parent = task1Parents.get(0)[0];
            int commCost = task1Parents.get(0)[1];
            drt1 = taskInformation[parent][2] + commCost;
        }

        if (task2Parents.size() > 0) {
            int parent = task2Parents.get(0)[0];
            int commCost = task2Parents.get(0)[1];
            drt2 = taskInformation[parent][2] + commCost;
        }

        // comparison for drt
        if (drt1 < drt2) {
            return -1;
        } else if (drt1 > drt2) {
            return 1;
        } else{
            List<int[]> task1Children = intGraph.outEdges[task1];
            List<int[]> task2Children = intGraph.outEdges[task2];

            // Data ready times are equal, now we compare communication cost
            int outCommCost1 = 0;
            int outCommCost2 = 0;
            if (task1Children.size() > 0) {
                outCommCost1 = task1Children.get(0)[1];
            }
            if (task2Children.size() > 0) {
                outCommCost2 = task2Children.get(0)[1];
            }
            return outCommCost2 - outCommCost1;
        }
    }

    /**
     * Get equivalent nodes list for all nodes
     * @return an array of integer list, each array index represent a node,
     * the corresponding list contains all the equivalent nodes for that node
     */
    protected LinkedList<Integer>[] getEquivalentNodes(){
        HashSet<Integer> memo = new HashSet<>();
        // since we already know the number of tasks, we can use array
        LinkedList<Integer>[] equivalentNodesList = new LinkedList[numTasks];

        for(int i = 0; i < numTasks; i++){
            if(!memo.contains(i)){
                // if we haven't seen this node before, we search for its equivalent nodes
                LinkedList<Integer> equivalentNodes = new LinkedList<>();
                equivalentNodes.add(i);
                for(int j = 0; j < numTasks; j++){
                    if(!memo.contains(j) && i != j){
                        if(equivalentCheck(i, j)){
                            equivalentNodes.add(j);
                        }
                    }
                }

                // add the nodeList into the equivalent nodes list
                for(int node : equivalentNodes){
                    equivalentNodesList[node] = equivalentNodes;
                    memo.add(node); // if a node is equivalent to each other, they will have the same list
                }
            }
        }
        return equivalentNodesList;
    }

    /**
     * A helper method to help check if two tasks are equivalent
     * @param taskA
     * @param taskB
     * @return true indicating that taskA == taskB, otherwise taskA != taskB
     */
    private boolean equivalentCheck(int taskA, int taskB){
        List<int[]> aParents = intGraph.inEdges[taskA];
        List<int[]> bParents = intGraph.inEdges[taskB];
        List<int[]> aChildren = intGraph.outEdges[taskA];
        List<int[]> bChildren = intGraph.outEdges[taskB];
        int aWeight = intGraph.weights[taskA];
        int bWeight = intGraph.weights[taskB];

        if(aWeight != bWeight){
            return false;
        }

        if((aParents.size() != bParents.size()) ||
                (aChildren.size() != bChildren.size())){
            return false;
        }

        // sort the list
        Collections.sort(aParents, new EdgesComparator());
        Collections.sort(bParents, new EdgesComparator());
        Collections.sort(aChildren, new EdgesComparator());
        Collections.sort(bChildren, new EdgesComparator());

        if(!compareEdgeRelations(aParents, bParents)
                || !compareEdgeRelations(aChildren, bChildren)){
            return false;
        }

        return true;
    }

    private boolean compareEdgeRelations(List<int[]> listA, List<int[]> listB){
        for(int i = 0; i < listA.size(); i++){
            int childA = listA.get(i)[0];
            int childACost = listA.get(i)[1];
            int childB = listB.get(i)[0];
            int childBCost = listB.get(i)[1];
            if(childA != childB || childACost != childBCost){
                return false;
            }
        }
        return true;
    }

    /**
     * DeepCopy a current BranchAndBound object.
     * @return a deep copied current BranchAndBound object.
     *
     * */
    public BranchAndBound deepCopy() {
        BranchAndBound c_branchandbound = new BranchAndBound(intGraph, numProcessors, false);
        int numTasks = intGraph.tasks.length;
        for (int i = 0; i < numTasks; i++) {
            c_branchandbound.taskProcessors[i] = taskProcessors[i];
            c_branchandbound.bottomLevel[i] = bottomLevel[i];
            for (int j = 0; j < 4; j++) {
                c_branchandbound.taskInformation[i][j] = taskInformation[i][j];
            }
        }
        for (int i = 0; i < time.size(); i++) {
            c_branchandbound.time.add(i, time.get(i));
        }
        c_branchandbound.idle = idle;

        for (int i = 0; i < scheduledTasks.length; i++){
            c_branchandbound.scheduledTasks[i] = scheduledTasks[i];
        }

        for (int i = 0; i < unscheduledTasks.length; i++){
            c_branchandbound.unscheduledTasks[i] = unscheduledTasks[i];
        }

        for (int i = 0; i < numProcessors; i++) {
            c_branchandbound.processorTimes[i] = processorTimes[i];
        }
        c_branchandbound.scheduled = scheduled;

        return c_branchandbound;

    }

}
