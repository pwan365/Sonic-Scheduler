package algo.helpers.costFunctions;


import java.util.LinkedList;

public class BottomLevel {
    static int [] bottomLevel;

    public static void initBottomLevel(int numTasks, int[] weights, LinkedList<int[]>[] outEdges) {
        bottomLevel = new int[numTasks];
        for (int i = 0; i < numTasks; i++) {
            bottomLevel[i] = -1;
        }
        for (int i = 0; i < numTasks; i++) {
            int weight = weights[i];
            calculateBottomLevel(i, weight,outEdges,weights);
        }
    }

    private static int calculateBottomLevel(int task, int total,LinkedList<int[]>[] outEdge,int [] weights) {
        if (bottomLevel[task] != -1) {
            return bottomLevel[task];
        }
        LinkedList<int[]> outEdges = outEdge[task];
        if (outEdges.size() == 0) {
            bottomLevel[task] = total;
            return total;
        }

        int max = 0;
        // Traverse through edges and pick the subtree with the maximum weight to form the critical path for this node.
        for (int i = 0; i < outEdges.size(); i++) {
            int child = outEdges.get(i)[0];
            int weight = weights[child];
            int temp = total + calculateBottomLevel(child, weight,outEdge,weights);

            max = Math.max(max, temp);
        }

        bottomLevel[task] = max;
        return max;
    }

    public static int pruneBLevel(int task, int cost, int processorStart) {
        return (bottomLevel[task] + cost + processorStart);
    }

    public static int returnBLevel(int task) {
        return bottomLevel[task];
    }
}
