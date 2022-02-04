package hong.wordle.util;

public class Util {
    public static String wordleSimulation(String hidden, String guess, byte[] count) {
        byte[] tmp = count.clone();
        char[] result = new char[5];
        for (int i = 0; i < 5; i++) {
            char c = guess.charAt(i);
            if (c == hidden.charAt(i)) {
                result[i] = Tiles.CORRECT.toChar();
                tmp[c - 'a']--;
            }
        }
        for (int i = 0; i < 5; i++) {
            char c = guess.charAt(i);
            if (c != hidden.charAt(i)) {
                if (tmp[c - 'a'] > 0) {
                    result[i] = Tiles.PRESENT.toChar();
                    tmp[c - 'a']--;
                } else {
                    result[i] = Tiles.ABSENT.toChar();
                }
            }
        }
        return new String(result);
    }

    public static String wordleSimulation(String hidden, String guess) {
        return wordleSimulation(hidden, guess, toCharCount(hidden));
    }

    public static byte[] toCharCount(String s) {
        byte[] count = new byte[26];
        for (int i = 0; i < s.length(); i++) count[s.charAt(i) - 'a']++;
        return count;
    }

    public static boolean hasDuplicateChar(String s) {
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 5; j++) {
                if (s.charAt(i) == s.charAt(j)) return true;
            }
        }
        return false;
    }

    public static char toChar(int i) {
        return (char) (i + 'a');
    }
}
