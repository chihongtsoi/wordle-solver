package hong.wordle;

import java.util.Map;

public class Const {
    public static final String WORD_LIST = "words";
    public static final String FIRST_TRIES = "aeons";
    public static final String CORRECT = "CCCCC";
    public static final int MOCK_WORDLE_LIMIT = 10;

    public static final Map<Character, Integer> charRank = Map.ofEntries(
            Map.entry('s', 1),
            Map.entry('e', 2),
            Map.entry('a', 3),
            Map.entry('r', 4),
            Map.entry('o', 5),
            Map.entry('i', 6),
            Map.entry('l', 7),
            Map.entry('t', 8),
            Map.entry('n', 9),
            Map.entry('u', 10),
            Map.entry('d', 11),
            Map.entry('c', 12),
            Map.entry('p', 13),
            Map.entry('y', 14),
            Map.entry('m', 15),
            Map.entry('h', 16),
            Map.entry('g', 17),
            Map.entry('b', 18),
            Map.entry('k', 19),
            Map.entry('f', 20),
            Map.entry('w', 21),
            Map.entry('v', 22),
            Map.entry('z', 23),
            Map.entry('x', 24),
            Map.entry('j', 25),
            Map.entry('q', 26)
    );


}
