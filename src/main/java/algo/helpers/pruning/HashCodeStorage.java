package algo.helpers.pruning;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * HashCodeStorage is created as a helper class to store the states that has been seen
 * before during the search and check whether the current state of searching has been seen.
 *
 * @author Wayne Yao
 */
public class HashCodeStorage {

    private static HashSet<Integer> seenStates;

    public static void initHashCodeStorage() {
        seenStates = new HashSet<>();
    }

    /**
     * Check if the current search state has been seen.
     *
     * @param taskInformation Task information
     * @param taskProcessors Processors that tasks has been allocated to.
     * @param numProcessors Number of processors
     * @param numTasks Number of tasks
     * @return A boolean value represents whether the current state has been seen.
     */
    public static boolean checkIfSeen(int [][] taskInformation,int [] taskProcessors,int numProcessors,int numTasks) {
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

            // Synchronized for multithreading.
            synchronized (HashCodeStorage.class){
                if (seenStates.contains(id)) {
                    return true;
                }
                else {
                    seenStates.add(id);
                }
            }
            return false;
    }
}
