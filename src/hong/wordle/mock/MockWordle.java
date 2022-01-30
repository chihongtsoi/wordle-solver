package hong.wordle.mock;

import hong.wordle.Const;
import hong.wordle.util.Util;

public class MockWordle {


    private final String hiddenWord;
    private final int[] count;
    private int round;

    public MockWordle(String hiddenWord) {
        this.hiddenWord = hiddenWord;
        count = new int[26];
        for (int i = 0; i < hiddenWord.length(); i++) {
            count[hiddenWord.charAt(i) - 'a']++;
        }
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
