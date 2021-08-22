package algo.helpers.pruning;

import algo.helpers.comparators.EdgesComparator;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class EquivalentStates {

    private static LinkedList<Integer>[] equivalentList;

    /**
     * Private constructor to prevent instantiation of an object of this class.
     */
    private EquivalentStates() {
    }

    public static void initEquivalentStates(int numTasks, LinkedList<int[]>[] inEdges,LinkedList<int[]>[] outEdges,
                                            int[] weights) {
        equivalentList = EquivalentNodes(numTasks, inEdges,outEdges,weights);
    }
    /**
     * Get equivalent nodes list for all nodes
     * @return an array of integer list, each array index represent a node,
     * the corresponding list contains all the equivalent nodes for that node
     */
    private static LinkedList<Integer>[] EquivalentNodes(int numTasks, LinkedList<int[]>[] inEdges,
                                                            LinkedList<int[]>[] outEdges,
                                                            int[] weights){
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
                        if(equivalentCheck(i, j,inEdges,outEdges,weights)){
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
    private static boolean equivalentCheck(int taskA, int taskB,LinkedList<int[]>[] inEdges,LinkedList<int[]>[] outEdges,
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

    private static boolean compareEdgeRelations(List<int[]> listA, List<int[]> listB){
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

    public static LinkedList<Integer> getEquivalentNodes(int task) {
        return equivalentList[task];
    }

}
