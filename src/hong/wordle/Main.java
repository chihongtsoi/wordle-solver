package hong.wordle;

import hong.wordle.solver.Solver;
import hong.wordle.util.IOUtils;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import static hong.wordle.util.IOUtils.*;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Set<String> allWords = new HashSet<>(IOUtils.allWords());
        Solver solver = new Solver(allWords);
        String response = null;

        while (true) {
            System.out.println(String.format("%d Tries: %s", solver.getRound()+1, solver.next(response)));
            print(String.format("Remain %d possibilities: %s", solver.getRemain(), solver.getPossibility().toString()));
            response = scanner.nextLine();
            System.out.println();
            if (response.trim().equals("CCCCC")) break;
        }
        System.out.println("Answer: " + solver.getAnswer());
    }

}


