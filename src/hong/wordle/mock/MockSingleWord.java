package hong.wordle.mock;

import hong.wordle.Const;
import hong.wordle.solver.Solver;
import hong.wordle.util.IOUtils;

import java.util.Collection;
import static hong.wordle.util.IOUtils.*;
public class MockSingleWord {

    private static final String HIDDEN_WORD = "fetid";
    public static void main(String[] args) {
        Collection<String> all = IOUtils.readAllWords(Const.WORD_LIST);
        long start = System.currentTimeMillis();
        MockWordle mock = new MockWordle(HIDDEN_WORD);
        Solver solver = new Solver(all);
        String response = null;
        while (true) {
            String guess = solver.next(response);
            print(solver.getConfirmedCount() + " - " + solver.getRemain() + "" + solver.getPossibility());
            response = mock.guess(guess);
            print(String.format("Tries %d: %s", mock.getRound(), guess));
            print(response);
            print("");
            if (response.equals(Const.CORRECT)) break;
        }
        double seconds = (System.currentTimeMillis() - start) / 1000.0;
        System.out.printf("Time spend: %.2fs%nTries: %d", seconds, mock.getRound());

    }
}
