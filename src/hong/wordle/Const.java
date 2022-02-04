package hong.wordle;

import java.util.Map;

public class Const {
    public static final String WORD_LIST = "wordle-word-list.txt";
    public static final String FIRST_TRIES = "toeas";
    public static final String CORRECT = "CCCCC";
    public static final int MOCK_WORDLE_LIMIT = 10;

    public static final Map<Character, Integer> charRank = Map.ofEntries(
            Map.entry('s', 0),
            Map.entry('e', 1),
            Map.entry('a', 2),
            Map.entry('o', 3),
            Map.entry('r', 4),
            Map.entry('i', 5),
            Map.entry('l', 6),
            Map.entry('t', 7),
            Map.entry('n', 8),
            Map.entry('u', 9),
            Map.entry('d', 10),
            Map.entry('y', 11),
            Map.entry('c', 12),
            Map.entry('p', 13),
            Map.entry('m', 14),
            Map.entry('h', 15),
            Map.entry('g', 16),
            Map.entry('b', 17),
            Map.entry('k', 18),
            Map.entry('w', 19),
            Map.entry('f', 20),
            Map.entry('v', 21),
            Map.entry('z', 22),
            Map.entry('j', 23),
            Map.entry('x', 24),
            Map.entry('q', 25)
    );


}
