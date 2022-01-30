package hong.wordle.util;

import java.util.Comparator;

public class DoubleArrayComparator implements Comparator<double[]> {

    public static final DoubleArrayComparator comparator = new DoubleArrayComparator();

    @Override
    public int compare(double[] o1, double[] o2) {
        if (o1.length != o2.length) throw new IllegalArgumentException();
        for (int i = 0; i < o1.length; i++) if (o1[i] != o2[i]) return (int) (o1[i] * 10000) - (int) (o2[i] * 10000);
        return 0;
    }
}
