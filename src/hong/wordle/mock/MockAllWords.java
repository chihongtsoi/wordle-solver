package hong.wordle.mock;

import hong.wordle.Const;
import hong.wordle.solver.Solver;
import hong.wordle.util.IOUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MockAllWords {
    public static void main(String[] args) {
        Collection<String> words = IOUtils.readAllWords(Const.WORD_LIST);
        long start = System.currentTimeMillis();
        List<Integer> rounds = words.parallelStream()
                .map(w -> {
                    MockWordle mock = new MockWordle(w);
                    Solver solver = new Solver(words);
                    String feedback = "";
                    try {
                        while (!Const.CORRECT.equals(feedback)) {
                            String guess = solver.next(feedback);
                            feedback = mock.guess(guess);
                        }
                    } catch (ExceedLimitException e) {
                        System.out.println(mock.getHiddenWord() + " Exceed");
                        return null;
                    } catch (Exception e) {
                        System.out.println(mock.getHiddenWord() + " Exception");
                        e.printStackTrace();
                        return null;
                    }
                    return mock.getRound();
                })
                .filter(Objects::nonNull).collect(Collectors.toList());
        double seconds = (System.currentTimeMillis() - start) / 1000.0;
        System.out.printf("Time spend: %.2fs%n", seconds);
        System.out.println("Exceed 6: " + rounds.stream().filter(r -> r > 6).count());
        System.out.println("Average tries: " + rounds.stream().mapToInt(Integer::intValue).average().orElseThrow());
    }
}
