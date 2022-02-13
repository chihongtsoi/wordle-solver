package hong.wordle.util;

import java.util.Comparator;

public class WordComparator implements Comparator<String> {

    public static final WordComparator comparator = new WordComparator();

    @Override
    public int compare(String s1, String s2) {
        int r1 = 0, r2 = 0;
        for (int i = 0; i < 5; i++) {
            r1 += Const.charRank.get(s1.charAt(i));
            r2 += Const.charRank.get(s2.charAt(i));
        }
        return r2 - r1;
    }
}
