package algo.helpers.hashCode;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class HashCodeStorage {

    public static HashSet<Integer> seenStates;

    public static void initHashCodeStorage() {
        seenStates = new HashSet<>();
    }

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

            if (seenStates.contains(id)) {
                return true;
            }
            else {
                seenStates.add(id);
            }
            return false;
    }
}
