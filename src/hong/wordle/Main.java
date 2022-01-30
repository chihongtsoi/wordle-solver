package hong.wordle;

import hong.wordle.solver.Solver;
import hong.wordle.util.IOUtils;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Set<String> allWords = new HashSet<>(IOUtils.readAllWords("words.txt"));
        Solver solver = new Solver(allWords);
        String response = null;

        while (true) {
            System.out.printf("%d Tries: %s%n", solver.getRound(), solver.next(response));
            System.out.printf("Remain %d possibilities: %s%n", solver.getRemain(), solver.getPossibility().toString());
            response = scanner.nextLine();
            System.out.println();
            if (response.trim().equals("CCCCC")) break;
        }
        System.out.println("Answer: " + solver.getAnswer());
    }

}

/*
WWCPW
WPWWW
WWWCC
CCCCC
 */
