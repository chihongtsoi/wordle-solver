package hong.wordle.util;

import hong.wordle.Const;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class IOUtils {
    public static List<String> allWords() {
        return readAllWords(Const.WORD_LIST);
    }

    public static List<String> readAllWords(String path) {
        List<String> words = Collections.synchronizedList(new ArrayList<>());
        File file = new File(path);
        File[] files;
        if (file.isFile()) files = new File[]{file};
        else files = file.listFiles();
        Arrays.stream(files).parallel().forEach(f -> {
            try (Scanner scanner = new Scanner(f)) {
                while (scanner.hasNext()) {
                    String word = scanner.nextLine().trim();
                    if (!word.isBlank()) {
                        words.add(word);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        return words;
    }

}
