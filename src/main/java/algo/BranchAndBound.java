package algo;


import java.util.*;

import algo.helpers.pruning.BottomLevel;
import algo.helpers.pruning.DuplicateStates;
import algo.helpers.pruning.LoadBalancer;
import algo.helpers.pruning.HashCodeStorage;

/**
 * BranchAndBound class contains all the task information and methods to help the algorithm
 * in both SequentialSearch and ParallelSearch to run. It records the information during
 * the search and call helper methods for pruning or checking. It will also deep copy itself
 * in multithreading.
 *
 * @author Luxman Jeyarajah, Wayne Yao
 */
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

//    public LinkedList<Integer>[] equivalentList;

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

        if (init) {
            time.push(0);
            idle = 0;
            addUnscheduledTasks();
            setDefaultTaskInfo();
            setDefaultTaskProcessor();
            BottomLevel.initBottomLevel(numTasks, intGraph.weights, intGraph.outEdges);
            LoadBalancer.initLoadBalancer(intGraph.weights);
            HashCodeStorage.initHashCodeStorage();
            DuplicateStates.initDuplicateStates(numTasks,intGraph.inEdges,intGraph.outEdges, intGraph.weights);
        }
    }

    /**
     * An Initialization method.
     * Add unscheduled tasks
     */
    private void addUnscheduledTasks() {
        for (int i = 0; i < numTasks; i++) {
            unscheduledTasks[i] = true;
        }
    }

    /**
     * An Initialization method.
     * Set default task information
     */
    private void setDefaultTaskInfo() {
        for (int i = 0; i < numTasks; i++) {
            taskInformation[i] = new int[]{-1, -1, -1, -1};
        }
    }

    /**
     * An Initialization method.
     * Set default processor for a task to be allocated to.
     */
    private void setDefaultTaskProcessor() {
        for (int i = 0; i < numTasks; i++) {
            taskProcessors[i] = -1;
        }
    }

    /**
     * Add a task to a processor and update related info
     *
     * @param task Task to be added
     * @param processor Processor to be added on
     * @param cost Communication Cost
     */
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

    /**
     * Remove a task from a processor and update related info
     *
     * @param task Task to be removed
     * @param processor Processor for a task to be removed from
     * @param cost Communication Cost
     */
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

    /**
     * Calculate the communication cost to schedule a task on a processor.
     *
     * @param task Task to be scheduled.
     * @param processor Processor to be scheduled on
     * @return Communication Cost
     */
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

    /**
     * Get a boolean array which represents which task is ready to be scheduled.
     * @return A Boolean array. If array[i] = true, then tasks[i] is ready to be scheduled.
     */
    public boolean[] getOrder() {
        boolean[] validTasks = new boolean[numTasks];

        for (int i = 0; i < numTasks; i++) {

            // if input is 0 size, find all root tasks
            if (intGraph.inEdges[i].size() == 0 && !scheduledTasks[i]) {
                validTasks[i] = true;
                continue;
            }

            boolean flag = true;
            for (int j = 0; j < intGraph.inEdges[i].size(); j++) {
                int parent = intGraph.inEdges[i].get(j)[0];
                if (!scheduledTasks[parent]) {
                    flag = false;
                    validTasks[i] = false;
                    break;
                }
            }
            if (flag && !scheduledTasks[i]) {
                validTasks[i] = true;
            }
        }
        return validTasks;
    }



//    /**
//     * Get equivalent nodes list for all nodes
//     * @return an array of integer list, each array index represent a node,
//     * the corresponding list contains all the equivalent nodes for that node
//     */
//    protected LinkedList<Integer>[] getEquivalentNodes(){
//        HashSet<Integer> memo = new HashSet<>();
//        // since we already know the number of tasks, we can use array
//        LinkedList<Integer>[] equivalentNodesList = new LinkedList[numTasks];
//
//        for(int i = 0; i < numTasks; i++){
//            if(!memo.contains(i)){
//                // if we haven't seen this node before, we search for its equivalent nodes
//                LinkedList<Integer> equivalentNodes = new LinkedList<>();
//                equivalentNodes.add(i);
//                for(int j = 0; j < numTasks; j++){
//                    if(!memo.contains(j) && i != j){
//                        if(equivalentCheck(i, j)){
//                            equivalentNodes.add(j);
//                        }
//                    }
//                }
//
//                // add the nodeList into the equivalent nodes list
//                for(int node : equivalentNodes){
//                    equivalentNodesList[node] = equivalentNodes;
//                    memo.add(node); // if a node is equivalent to each other, they will have the same list
//                }
//            }
//        }
//        return equivalentNodesList;
//    }
//
//    /**
//     * A helper method to help check if two tasks are equivalent
//     * @param taskA
//     * @param taskB
//     * @return true indicating that taskA == taskB, otherwise taskA != taskB
//     */
//    private boolean equivalentCheck(int taskA, int taskB){
//        List<int[]> aParents = intGraph.inEdges[taskA];
//        List<int[]> bParents = intGraph.inEdges[taskB];
//        List<int[]> aChildren = intGraph.outEdges[taskA];
//        List<int[]> bChildren = intGraph.outEdges[taskB];
//        int aWeight = intGraph.weights[taskA];
//        int bWeight = intGraph.weights[taskB];
//
//        if(aWeight != bWeight){
//            return false;
//        }
//
//        if((aParents.size() != bParents.size()) ||
//                (aChildren.size() != bChildren.size())){
//            return false;
//        }
//
//        // sort the list
//        aParents.sort(new EdgesComparator());
//        bParents.sort(new EdgesComparator());
//        aChildren.sort(new EdgesComparator());
//        bChildren.sort(new EdgesComparator());
//
//        return compareEdgeRelations(aParents, bParents)
//                && compareEdgeRelations(aChildren, bChildren);
//    }
//
//    private boolean compareEdgeRelations(List<int[]> listA, List<int[]> listB){
//        for(int i = 0; i < listA.size(); i++){
//            int childA = listA.get(i)[0];
//            int childACost = listA.get(i)[1];
//            int childB = listB.get(i)[0];
//            int childBCost = listB.get(i)[1];
//            if(childA != childB || childACost != childBCost){
//                return false;
//            }
//        }
//        return true;
//    }

    /**
     * DeepCopy a current BranchAndBound object.
     * @return a deep copied current BranchAndBound object.
     *
     */
    public BranchAndBound deepCopy() {
        BranchAndBound c_branchandbound = new BranchAndBound(intGraph, numProcessors, false);
        int numTasks = this.numTasks;
        for (int i = 0; i < numTasks; i++) {
            c_branchandbound.taskProcessors[i] = taskProcessors[i];
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
