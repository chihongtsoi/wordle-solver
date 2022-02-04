package hong.wordle.util;

import java.util.Comparator;
import java.util.Map;

public class WordEntryComparator<T> implements Comparator<Map.Entry<String, T>> {

    private final Comparator<T> valueComparator;

    public WordEntryComparator(Comparator<T> valueComparator) {
        this.valueComparator = valueComparator;
    }

    @Override
    public int compare(Map.Entry<String, T> o1, Map.Entry<String, T> o2) {
        int c = valueComparator.compare(o1.getValue(), o2.getValue());
        return c == 0 ? WordComparator.comparator.compare(o1.getKey(), o2.getKey()) : c;
    }

}
