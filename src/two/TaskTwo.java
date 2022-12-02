package two;

import util.DailyTask;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskTwo extends DailyTask<List<String>, List<String>> {

    public TaskTwo() {
        super(InputTwo.TASK_ONE, InputTwo.TASK_ONE);
    }

    @Override
    public String solveTaskOne(List<String> parsed) {
        AtomicInteger res = new AtomicInteger(0);

        parsed.forEach(r -> {
            String[] spl = r.split(" ");
            res.addAndGet(calcOutcomeTaskOne(spl[0], spl[1]));
        });

        return res.get() + "";
    }

    @Override
    public List<String> parseInputOne(List<String> input) {
        return input;
    }

    @Override
    public String solveTaskTwo(List<String> parsed) {
        AtomicInteger res = new AtomicInteger(0);

        parsed.forEach(r -> {
            String[] spl = r.split(" ");
            res.addAndGet(calcOutcomeTaskTwo(spl[0], spl[1]));
        });

        return res.get() + "";
    }

    @Override
    public List<String> parseInputTwo(List<String> input) {
        return input;
    }

    private static int calcOutcomeTaskTwo(String a, String b) {
        switch (a) {
            // Rock
            case "A":
                switch (b) {
                    // Loss (Sc)
                    case "X":
                        return 3;
                        // Draw (Rock)
                    case "Y":
                        return 1 + 3;
                        // Win (Paper)
                    case "Z":
                        return 2 + 6;
                }
                // paper
            case "B":
                switch (b) {
                    // Loss (Rock)
                    case "X":
                        return 1;
                    // Draw (Paper)
                    case "Y":
                        return 2 + 3;
                    // Win (Sci)
                    case "Z":
                        return 3 + 6;
                }
                // Scissor
            case "C":
                switch (b) {
                    // Loss (Paper)
                    case "X":
                        return 2;
                    // Draw (Sci)
                    case "Y":
                        return 3 + 3;
                    // Win (Ro)
                    case "Z":
                        return 1 + 6;
                }
        }
        return 0;
    }

    private static int calcOutcomeTaskOne(String a, String b) {
        switch (a) {
            case "A":
                switch (b) {
                    case "X":
                        return 1 + 3;
                    case "Y":
                        return 2 + 6;
                    case "Z":
                        return 3;
                }
            case "B":
                switch (b) {
                    case "X":
                        return 1;
                    case "Y":
                        return 2 + 3;
                    case "Z":
                        return 3 + 6;
                }
            case "C":
                switch (b) {
                    case "X":
                        return 1 + 6;
                    case "Y":
                        return 2;
                    case "Z":
                        return 3 + 3;
                }
        }
        return 0;
    }
}
