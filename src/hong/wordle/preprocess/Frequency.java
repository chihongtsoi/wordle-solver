package hong.wordle.preprocess;

import hong.wordle.solver.MapReduce;
import hong.wordle.util.IOUtils;

import java.util.Collection;
import java.util.Map;

public class Frequency {
    public static void main(String[] args) {
        Collection<String> all = IOUtils.allWords();
        MapReduce mapReduce = new MapReduce(all, all);
        mapReduce.getContainMap().entrySet().stream().map(e -> Map.entry(e.getKey(), e.getValue().size()))
                .sorted(Map.Entry.comparingByValue((v1, v2) -> v2 - v1)).forEach(System.out::println);
    }
}
