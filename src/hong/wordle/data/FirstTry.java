package hong.wordle.data;

import hong.wordle.solver.MapReduce;
import hong.wordle.util.IOUtils;

import java.util.Collection;
import java.util.Collections;

public class FirstTry {
    public static void main(String[] args) {
        Collection<String> all = IOUtils.allWords();
        MapReduce mapReduce = new MapReduce(all, all);
        System.out.println(mapReduce.reduce(Collections.EMPTY_MAP));
    }
}
