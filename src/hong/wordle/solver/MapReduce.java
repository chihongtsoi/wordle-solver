package hong.wordle.solver;

import hong.wordle.util.DoubleArrayComparator;
import hong.wordle.util.WordEntryComparator;
import hong.wordle.util.IntArrayComparator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static hong.wordle.util.Util.*;

public class MapReduce {

    public static final int POSITION = 1;
    public static final int CONTAIN = 2;
    public static final int CONFIRMED_COUNT = 3;
    public static final int AT_LEAST_COUNT = 4;
    private List<Map<Character, Set<String>>> positionMaps;
    private Map<Character, Set<String>> containMap;
    private Map<Character, List<Set<String>>> countMap;
    private final Collection<String> words;
    private final Collection<String> possibilities;

    public MapReduce(Collection<String> words, Collection<String> possibilities) {
        this.words = words;
        this.possibilities = possibilities;
        map();
    }

    private void init() {
        positionMaps = new ArrayList<>(5);
        containMap = new ConcurrentHashMap<>();
        countMap = new ConcurrentHashMap<>();
        for (int i = 0; i < 5; i++) {
            positionMaps.add(new ConcurrentHashMap<>()) ;
            for (char j = 'a'; j <= 'z'; j++) {
                positionMaps.get(i).put(j, newSet());
            }
        }
        for (char i = 'a'; i <= 'z'; i++) {
            containMap.put(i, newSet());
            List<Set<String>> arr = new ArrayList<>(3);
            for (int j = 0; j < 3; j++) {
                arr.add(newSet());
            }
            countMap.put(i, arr);
        }

    }

    public void map() {
        init();
        possibilities.parallelStream().forEach(this::map);
    }

    private void map(String word) {
        boolean[] add = new boolean[26];
        byte[] count = toCharCount(word);
        for (int i = 0; i < 5; i++) {
            char c = word.charAt(i);
            positionMaps.get(i).get(c).add(word);
            if (!add[c - 'a']) {
                containMap.get(c).add(word);
                add[c - 'a'] = true;
            }
        }
        for (int i = 0; i < 26; i++) {
            if (count[i] > 0) {
                char c = toChar(i);
                countMap.get(c).get(count[i] - 1).add(word);
            }
        }
    }


    public String reduce(Map<Character, Integer> confirmed) {
        Map<Character, Integer> charCount = possibilityCharCount();
        return words.parallelStream().map(p -> includeCount(p, confirmed,charCount))
                .max(new WordEntryComparator<>(IntArrayComparator.comparator))
                .orElseThrow().getKey();
    }


    private Map.Entry<String, int[]> includeCount(String s, Map<Character, Integer> confirmed,Map<Character, Integer> charCount) {
        Set<String> include = new HashSet<>();
        Set<String> position = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            char c = s.charAt(i);
            if (!confirmed.containsKey(c) ){
                include.addAll(containMap.get(c));
                position.addAll(positionMaps.get(i).get(c));
            }
        }
        Set<String> count = new HashSet<>();
        byte[] counting = toCharCount(s);
        for (int i = 0; i < 26; i++) {
            char c = toChar(i);
            if(counting[i]>1 && counting[i]>=charCount.getOrDefault(c, Integer.MAX_VALUE)){
                count.addAll(countMap.get(c).get(counting[i]-1));
            }
        }
        return Map.entry(s, new int[]{include.size(), position.size(), count.size()});
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
                return positionMaps.get(args[0]).get((char) args[1]);
            case CONTAIN:
                return containMap.get((char) args[0]);
            case CONFIRMED_COUNT:
                return countMap.get((char) args[0]).get(args[1] - 1);
            case AT_LEAST_COUNT:
                Set<String> set = new HashSet<>();
                for (int i = 0; i < args[1] - 1; i++) {
                    set.addAll(countMap.get((char) args[0]).get(i));
                }
                return set;
        }
        return null;
    }

    private Map<Character, Integer> possibilityCharCount(){
        Map<Character, Integer> count = new ConcurrentHashMap<>();
        possibilities.parallelStream().forEach(s->{
            byte[] charCount = toCharCount(s);
            for (int i = 0; i < 26; i++) {
                char c = toChar(i);
                final int index = i;
                count.compute(c,(k,v)->v==null? charCount[index]:Math.max(charCount[index],v));
            }
        });
        return count;

    }
    private static Set<String> newSet() {
        return Collections.synchronizedSet(new HashSet<>());
    }

    public Map<Character, Set<String>> getContainMap() {
        return containMap;
    }

}

