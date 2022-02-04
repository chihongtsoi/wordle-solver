package hong.wordle.mock;

import hong.wordle.Const;
import hong.wordle.util.Util;

public class MockWordle {


    private final String hiddenWord;
    private final byte[] count;
    private int round;

    public MockWordle(String hiddenWord) {
        this.hiddenWord = hiddenWord;
        count = Util.toCharCount(hiddenWord);
        this.round = 0;
    }

    public String guess(String guess) {
        if (++round > Const.MOCK_WORDLE_LIMIT) throw new ExceedLimitException();
        return Util.wordleSimulation(hiddenWord, guess, count);
    }

    public int getRound() {
        return round;
    }

    public String getHiddenWord() {
        return hiddenWord;
    }
}
