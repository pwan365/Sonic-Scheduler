package algo.helpers.comparators;

import java.util.Comparator;

/**
 * A helper class implementing Comparator interface so that it can be used to sort the edges list
 *
 * @author Wayne Yao
 */
public class EdgesComparator implements Comparator<int[]> {

    /**
     * A helper method for sorting intGraph inEdge and outEdge list according to the node index.
     * Being used with comparator.
     * @param o1
     * @param o2
     * @return if result is not negative o1 will be put before o2, otherwise o1 will be put after o2.
     */
    @Override
    public int compare(int[] o1, int[] o2) {
        return o1[0] - o2[0];
    }
}
