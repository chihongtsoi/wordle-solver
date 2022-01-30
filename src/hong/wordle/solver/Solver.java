package hong.wordle.solver;


import hong.wordle.Const;
import hong.wordle.util.Tiles;
import hong.wordle.util.Util;
import hong.wordle.util.WordComparator;

import java.util.*;

public class Solver {
    private final Set<String> possibility;
    private final Map<Character, Integer> confirmed;
    private final MapReduce mapReduce;
    private String current_try;
    private int round;
    private int confirmedCount;
    private boolean confirmedDuplicate;

    public Solver(Collection<String> allWords) {
        this(allWords, Const.FIRST_TRIES);
    }

    public Solver(Collection<String> allWords, String current_try) {
        this.possibility = new HashSet<>(allWords);
        this.mapReduce = new MapReduce(allWords, possibility);
        this.current_try = current_try;
        this.confirmed = new HashMap<>();
        this.round = 0;
    }

    public String next(String s) {
        if (++round == 1) return current_try;
        Tiles[] feedbacks = validateResponse(s.trim());
        processPossibility(feedbacks);
        mapReduce.map();
        switch (possibility.size()) {
            case 0:
                throw new RuntimeException("No word match");
            case 1:
                current_try = possibility.toArray(String[]::new)[0];
                break;
            case 2:
                String[] tmp = possibility.toArray(String[]::new);
                current_try = WordComparator.comparator.compare(tmp[0], tmp[1]) > 0 ? tmp[0] : tmp[1];
                break;
            default:
                if (confirmedCount >= 5) {
                    current_try = mapReduce.confirmedConstruction();
                } else if (!confirmedDuplicate && confirmedCount >= 4 && hasDuplicateChar()) {
                    current_try = mapReduce.findLeftChar(confirmed);
                } else {
                    current_try = mapReduce.reduce(confirmed);
                }
        }
        possibility.remove(current_try);
        return current_try;
    }

    private Tiles[] validateResponse(String s) {
        if (s.length() != 5) return null;
        Tiles[] feedbacks = new Tiles[5];
        for (int i = 0; i < 5; i++) feedbacks[i] = Tiles.valueOf(String.valueOf(s.charAt(i)));
        return feedbacks;
    }

    private void processPossibility(Tiles[] feedbacks) {
        int[] correct_count = new int[26];
        int[] guess_count = new int[26];
        for (int i = 0; i < 5; i++) {
            if (feedbacks[i] == Tiles.C || feedbacks[i] == Tiles.P) correct_count[current_try.charAt(i) - 'a']++;
            guess_count[current_try.charAt(i) - 'a']++;
        }
        for (int i = 0; i < 5; i++) {
            switch (feedbacks[i]) {
                case C:
                    possibility.retainAll(mapReduce.get(MapReduce.POSITION, i, current_try.charAt(i)));

                    break;
                case P:
                    possibility.retainAll(mapReduce.get(MapReduce.CONTAIN, current_try.charAt(i)));
                    possibility.removeAll(mapReduce.get(MapReduce.POSITION, i, current_try.charAt(i)));
                    break;
                case W:
                    if (correct_count[current_try.charAt(i) - 'a'] <= 0)
                        possibility.removeAll(mapReduce.get(MapReduce.CONTAIN, current_try.charAt(i)));
                    break;
            }
            if (!confirmed.containsKey(current_try.charAt(i))) {
                if (feedbacks[i] != Tiles.W) confirmedCount++;
                confirmed.put(current_try.charAt(i), 1);
            }
        }
        for (int i = 0; i < 26; i++) {
            if (guess_count[i] <= 0) continue;
            char c = Util.toChar(i);
            if (correct_count[i] > 1) {
                possibility.removeAll(mapReduce.get(MapReduce.AT_LEAST_COUNT, (char) ('a' + i), correct_count[i]));
                confirmedDuplicate = true;
            }
            if (guess_count[i] > correct_count[i] && correct_count[i] > 0) {
                Collection<String> set = mapReduce.get(MapReduce.CONFIRMED_COUNT, c, correct_count[i]);
                possibility.retainAll(set);
                int pcount = confirmed.get(c);
                if (correct_count[i] > pcount) confirmedCount += correct_count[i] - pcount;
                confirmed.put(c, correct_count[i]);
            }
        }
    }

    private boolean hasDuplicateChar() {
        return possibility.parallelStream().anyMatch(Util::hasDuplicateChar);
    }

    public String getAnswer() {
        return possibility.size() == 1 ? possibility.toArray(String[]::new)[0] : current_try;
    }

    public Set<String> getPossibility() {
        return possibility;
    }

    public int getRemain() {
        return possibility.size();
    }

    public int getRound() {
        return round;
    }

    public int getConfirmedCount() {
        return confirmedCount;
    }

}
