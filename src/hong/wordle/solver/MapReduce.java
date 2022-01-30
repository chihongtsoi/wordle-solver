package hong.wordle.solver;

import hong.wordle.util.DoubleArrayComparator;
import hong.wordle.util.EntryComparator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static hong.wordle.util.Util.*;

public class MapReduce {

    public static final int POSITION = 1;
    public static final int CONTAIN = 2;
    public static final int CONFIRMED_COUNT = 3;
    public static final int AT_LEAST_COUNT = 4;
    private Map<Character, Set<String>>[] positionMaps;
    private Map<Character, Set<String>> containMap;
    private Map<Character, Set<String>[]> countMap;
    private final Collection<String> words;
    private final Collection<String> possibilities;

    public MapReduce(Collection<String> words, Collection<String> possibilities) {
        this.words = words;
        this.possibilities = possibilities;
        map();
    }

    private void init() {
        positionMaps = new Map[5];
        containMap = new ConcurrentHashMap<>();
        countMap = new ConcurrentHashMap<>();
        for (int i = 0; i < positionMaps.length; i++) {
            positionMaps[i] = new ConcurrentHashMap<>();
            for (char j = 'a'; j <= 'z'; j++) {
                positionMaps[i].put(j, newSet());
            }
        }
        for (char i = 'a'; i <= 'z'; i++) {
            containMap.put(i, newSet());
            Set<String>[] arr = new Set[3];
            countMap.put(i, arr);
            for (int j = 0; j < 3; j++) {
                arr[j] = newSet();
            }
        }

    }

    public void map() {
        init();
        possibilities.parallelStream().forEach(this::map);
    }

    private void map(String word) {
        boolean[] add = new boolean[26];
        int[] count = toCharCount(word);
        for (int i = 0; i < 5; i++) {
            char c = word.charAt(i);
            positionMaps[i].get(c).add(word);
            if (!add[c - 'a']) {
                containMap.get(c).add(word);
                add[c - 'a'] = true;
            }
        }
        for (int i = 0; i < 26; i++) {
            if (count[i] > 0) {
                char c = toChar(i);
                countMap.get(c)[count[i] - 1].add(word);
            }
        }
    }


    public String reduce(Map<Character, Integer> confirmed) {
        return words.parallelStream().map(p -> includeCount(p, confirmed))
                .max(new EntryComparator<>(Comparator.comparingInt(i -> i)))
                .orElseThrow().getKey();
    }


    private Map.Entry<String, Integer> includeCount(String s, Map<Character, Integer> confirmed) {
        Set<String> set = new HashSet<>();
        for (char c : s.toCharArray()) if (!confirmed.containsKey(c)) set.addAll(containMap.get(c));
        return Map.entry(s, set.size());
    }

    public String findLeftChar(Map<Character, Integer> confirmed) {
        Map<Character, Integer> map = getPossibleChar();
        List<Map.Entry<String, Integer>> l = words.parallelStream().map(p -> leftChar(p, map, confirmed))
                .sorted(new EntryComparator<>(Comparator.comparingInt(i -> i)))
                .collect(Collectors.toList());
        Map.Entry<String, Integer> e = l.stream().max(new EntryComparator<>(Comparator.comparingInt(i -> i)))
                .orElseThrow();
        return e.getKey();
    }


    private Map.Entry<String, Integer> leftChar(String s, Map<Character, Integer> map, Map<Character, Integer> confirmed) {
        int score = 0;
        int[] count = toCharCount(s);
        for (int i = 0; i < 26; i++) {
            if (count[i] <= 0) continue;
            char c = toChar(i);
            Integer m = map.get(c);
            if (m == null) continue;
            if (m > 1 && count[i] >= m) score += count[i] * 10;
            else score += 1;
        }
        return Map.entry(s, score);
    }

    public Map<Character, Integer> getPossibleChar() {
        Map<Character, Integer> map = new ConcurrentHashMap<>();
        possibilities.parallelStream().forEach(w -> {
            int[] count = toCharCount(w);
            for (int i = 0; i < 26; i++) {
                if (count[i] > 0) {
                    char c = toChar(i);
                    final int index = i;
                    map.compute(c, (k, v) -> v == null ? count[index] : Math.max(v, count[index]));
                }
            }
        });
        return map;
    }

    public String confirmedConstruction() {
        return possibilities.parallelStream().map(this::confirmedConstruction)
                .max(Map.Entry.comparingByValue(DoubleArrayComparator.comparator))
                .orElseThrow().getKey();
    }

    private Map.Entry<String, double[]> confirmedConstruction(String guess) {
        List<Integer> list = possibilities.parallelStream().map(s -> filterCount(s, guess)).collect(Collectors.toList());
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        double sum = 0;
        for (int i : list) {
            sum += i;
            min = Math.min(min, i);
            max = Math.max(max, i);
        }
        return Map.entry(guess, new double[]{sum / list.size(), -(max - min)});
    }

    private Integer filterCount(String hidden, String guess) {
        Solver solver = new Solver(possibilities, guess);
        solver.next(wordleSimulation(hidden, guess));
        return possibilities.size() - solver.getRemain();
    }

    public Collection<String> get(int type, int... args) {
        switch (type) {
            case POSITION:
                return positionMaps[args[0]].get((char) args[1]);
            case CONTAIN:
                return containMap.get((char) args[0]);
            case CONFIRMED_COUNT:
                return countMap.get((char) args[0])[args[1] - 1];
            case AT_LEAST_COUNT:
                Set<String> set = new HashSet<>();
                for (int i = 0; i < args[1] - 1; i++) {
                    set.addAll(countMap.get((char) args[0])[i]);
                }
                return set;
        }
        return null;
    }

    private Set<String> newSet() {
        return Collections.synchronizedSet(new HashSet<>());
    }

    public Map<Character, Set<String>>[] getPositionMaps() {
        return positionMaps;
    }

    public Map<Character, Set<String>> getContainMap() {
        return containMap;
    }

    public Map<Character, Set<String>[]> getCountMap() {
        return countMap;
    }
}

