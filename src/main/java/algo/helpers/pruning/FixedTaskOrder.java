package algo.helpers.pruning;

import java.util.LinkedList;
import java.util.List;


/**
 * Idea reference: https://www.sciencedirect.com/science/article/abs/pii/S0305054813002542
 *
 * According to the report, to solve fork or join graphs the best way is to use a Fixed Task Order,
 * where candidate tasks satisfying the following requirement:
 *  1. all the tasks have either no child or has exactly one same child
 *  2. all the tasks have either no parent or the exactly one parent are scheduled on the same processor
 *  3. The tasks are sorted in non-decreasing data ready time (finish time + communication cost of parent)
 *  4. The tasks are also sorted in non-increasing child edge communication cost.
 *
 * This class contains helper functions to generate the FTO.
 *
 * @author Luxman Jeyarajah, Wayne Yao
 */
public class FixedTaskOrder {

    /**
     * Prevent instantiation of the class.
     */
    private FixedTaskOrder() {
    }

    public static boolean[] checkFTO(boolean[] candidateTasks,int[] taskProcessors,int [][] taskInformation,
                              LinkedList<int[]>[] outEdges, LinkedList<int[]>[] inEdges,int numTasks) {

        LinkedList<Integer> fto = FixedTaskOrder.getFTO(candidateTasks,taskProcessors,taskInformation,
                outEdges, inEdges);

        if (fto != null) {
            int first = fto.poll();
            for (int i =0;i < numTasks;i++) {
                if (i != first) {
                    candidateTasks[i] = false;
                }
            }
        }
        return candidateTasks;
    }

    /**
     * Generates a list of integers representing a Fixed Task Ordered list of tasks
     * @param candidateTasks a boolean array indicating which task is valid to be scheduled
     * @return a list of integers, which is the FTO list
     */
    private static LinkedList<Integer> getFTO(boolean[] candidateTasks,int[] taskProcessors,int [][] taskInformation,
                                            LinkedList<int[]>[] outEdges, LinkedList<int[]>[] inEdges) {
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
            LinkedList<int[]> childrenList = outEdges[i];
            LinkedList<int[]> parentList = inEdges[i];

            if (parentList.size() > 1 || childrenList.size() > 1) {
                return null;
            } else if (childrenList.size() > 0){
                // candidate tasks either have no child, or all tasks have the same child
                int taskChild = outEdges[i].get(0)[0];
                if (child == -1) {
                    child = taskChild;
                }
                if (child != taskChild){
                    return null;
                }
            } else if (parentList.size() > 0){
                // nodes either have the same parent or all parents are scheduled on the same processor
                int parent = inEdges[i].get(0)[0];
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
        result.sort((task1, task2) -> DRT(task1, task2,taskInformation,outEdges,inEdges));

        // if the out edge cost is not ordered in non-increasing order, it is not a fto list
        int prevOEC = Integer.MAX_VALUE;
        for (int j : result) {
            List<int[]> taskChildren = outEdges[j];
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
    private static int DRT(int task1, int task2,int [][] taskInformation,
                           LinkedList<int[]>[] outEdges, LinkedList<int[]>[] inEdges){
        int drt1 = 0; // data ready time for task1
        int drt2 = 0; // data ready time for task2

        List<int[]> task1Parents = inEdges[task1];
        List<int[]> task2Parents = inEdges[task2];

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
            List<int[]> task1Children = outEdges[task1];
            List<int[]> task2Children = outEdges[task2];

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


}
