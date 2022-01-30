package hong.wordle.mock;

import hong.wordle.Const;
import hong.wordle.solver.Solver;
import hong.wordle.util.IOUtils;

import java.util.Collection;

public class MockSingleWord {

    private static final String HIDDEN_WORD = "light";
    public static void main(String[] args) {
        Collection<String> all = IOUtils.readAllWords("words");
        long start = System.currentTimeMillis();

        MockWordle mock = new MockWordle(HIDDEN_WORD);
        Solver solver = new Solver(all);
        String response = null;
        while (true) {
            String guess = solver.next(response);
            System.out.println(solver.getConfirmedCount() + " - " + solver.getRemain() + "" + solver.getPossibility());
            response = mock.guess(guess);
            System.out.printf("Tries %d: %s%n", mock.getRound(), guess);
            System.out.println(response);
            System.out.println();
            if (response.equals(Const.CORRECT)) break;
        }
        double seconds = (System.currentTimeMillis() - start) / 1000.0;
        System.out.printf("Time spend: %.2fs%nTries: %d", seconds, mock.getRound() - 1);

    }
}
