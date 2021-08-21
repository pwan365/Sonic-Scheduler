package helpers;

import java.util.Comparator;
import java.util.LinkedList;

public class EdgesComparator implements Comparator<int[]> {
    @Override
    public int compare(int[] o1, int[] o2) {
        return o1[0] - o2[0];
    }
}
