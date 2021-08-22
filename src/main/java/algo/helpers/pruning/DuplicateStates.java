package algo.helpers.pruning;

import algo.helpers.comparators.EdgesComparator;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * When two nodes have identical information, they are likely can be treated the same
 *
 * This class indicates helper functions to retrieve the duplicate nodes
 *
 * @author Wayne Yao
 */
public class DuplicateStates {

    private static LinkedList<Integer>[] duplicateList;

    /**
     * Private constructor to prevent instantiation of an object of this class.
     */
    private DuplicateStates() {
    }

    public static void initDuplicateStates(int numTasks, LinkedList<int[]>[] inEdges,LinkedList<int[]>[] outEdges,
                                            int[] weights) {
        duplicateList = DuplicateNodes(numTasks, inEdges,outEdges,weights);
    }
    /**
     * Get duplicate nodes list for all nodes
     * @return an array of integer list, each array index represent a node,
     * the corresponding list contains all the duplicate nodes for that node
     */
    private static LinkedList<Integer>[] DuplicateNodes(int numTasks, LinkedList<int[]>[] inEdges,
                                                            LinkedList<int[]>[] outEdges,
                                                            int[] weights){
        HashSet<Integer> memo = new HashSet<>();
        // since we already know the number of tasks, we can use array
        LinkedList<Integer>[] duplicateNodeList = new LinkedList[numTasks];

        for(int i = 0; i < numTasks; i++){
            if(!memo.contains(i)){
                // if we haven't seen this node before, we search for its duplicated nodes
                LinkedList<Integer> duplicateNodes = new LinkedList<>();
                duplicateNodes.add(i);
                for(int j = 0; j < numTasks; j++){
                    if(!memo.contains(j) && i != j){
                        if(duplicateCheck(i, j,inEdges,outEdges,weights)){
                            duplicateNodes.add(j);
                        }
                    }
                }

                // add the nodeList into the duplicated nodes list
                for(int node : duplicateNodes){
                    duplicateNodeList[node] = duplicateNodes;
                    memo.add(node); // if a node is duplicated to each other, they will have the same list
                }
            }
        }
        return duplicateNodeList;
    }

    /**
     * A helper method to help check if two tasks are equivalent
     * @param taskA
     * @param taskB
     * @return true indicating that taskA == taskB, otherwise taskA != taskB
     */
    private static boolean duplicateCheck(int taskA, int taskB,LinkedList<int[]>[] inEdges,LinkedList<int[]>[] outEdges,
                                           int[] weights){
        List<int[]> aParents = inEdges[taskA];
        List<int[]> bParents = inEdges[taskB];
        List<int[]> aChildren = outEdges[taskA];
        List<int[]> bChildren = outEdges[taskB];
        int aWeight = weights[taskA];
        int bWeight = weights[taskB];

        if(aWeight != bWeight){
            return false;
        }

        if((aParents.size() != bParents.size()) ||
                (aChildren.size() != bChildren.size())){
            return false;
        }

        // sort the list
        aParents.sort(new EdgesComparator());
        bParents.sort(new EdgesComparator());
        aChildren.sort(new EdgesComparator());
        bChildren.sort(new EdgesComparator());

        return compareEdgeRelations(aParents, bParents)
                && compareEdgeRelations(aChildren, bChildren);
    }

    /**
     * Compare if two task has the same parent/child and the same communication cost for each edges
     * @param listA
     * @param listB
     * @return true if they are equivalent, false if they are not.
     */
    private static boolean compareEdgeRelations(List<int[]> listA, List<int[]> listB){
        for(int i = 0; i < listA.size(); i++){
            int taskA = listA.get(i)[0];
            int taskACost = listA.get(i)[1];
            int taskB = listB.get(i)[0];
            int taskBCost = listB.get(i)[1];
            if(taskA != taskB || taskACost != taskBCost){
                return false;
            }
        }
        return true;
    }

    /**
     * Getter for duplicate nodes.
     * @param task task index
     * @returna list of duplicate tasks for a task.
     */
    public static LinkedList<Integer> getDuplicateNodes(int task) {
        return duplicateList[task];
    }

}
