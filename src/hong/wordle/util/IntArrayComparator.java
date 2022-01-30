package hong.wordle.util;

import java.util.Comparator;

public class IntArrayComparator implements Comparator<int[]> {

    public static final IntArrayComparator comparator = new IntArrayComparator();

    @Override
    public int compare(int[] o1, int[] o2) {
        if (o1.length != o2.length) throw new IllegalArgumentException();
        for (int i = 0; i < o1.length; i++) if (o1[i] != o2[i]) return o1[i] - o2[i];
        return 0;
    }
}
