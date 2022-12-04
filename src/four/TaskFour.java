package four;

import util.DailyTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TaskFour extends DailyTask<List<String[]>, List<String[]>> {

    public TaskFour(String TASK_ONE, String TASK_TWO) {
        super(TASK_ONE, TASK_TWO);
    }

    @Override
    public String solveTaskOne(List<String[]> parsed) {
        return parsed.stream().mapToInt(input -> {
                String[] s1S = input[0].split("-");
                String[] s2S = input[1].split("-");

                if (isInRange(s1S, s2S)) {
                    return 1;
                }
                return 0;
            }
        ).sum() + "";
    }

    @Override
    public List<String[]> parseInputOne(List<String> input) {
        return input.stream().map(s -> s.split(",")).collect(Collectors.toList());
    }

    @Override
    public String solveTaskTwo(List<String[]> parsed) {
        return parsed.stream().mapToInt(input -> {
                    String[] s1S = input[0].split("-");
                    String[] s2S = input[1].split("-");

                    if (overlaps(s1S, s2S)) {
                        return 1;
                    }
                    return 0;
                }
        ).sum() + "";
    }

    @Override
    public List<String[]> parseInputTwo(List<String> input) {
        return parseInputOne(input);
    }

    private static boolean isInRange(String[] a, String[] b) {
        int i1 = Integer.parseInt(a[0]);
        int i2 = Integer.parseInt(a[1]);
        int e1 = Integer.parseInt(b[0]);
        int e2 = Integer.parseInt(b[1]);

        if (i1 > e1) {
            return i2 <= e2;
        } else if (i1 == e1) {
            return true;
        } else {
            return i2 >= e2;
        }
    }

    private static boolean overlaps(String[] a, String[] b) {
        int i1 = Integer.parseInt(a[0]);
        int i2 = Integer.parseInt(a[1]);
        int e1 = Integer.parseInt(b[0]);
        int e2 = Integer.parseInt(b[1]);

        if (i2 == e2 || i1 == e1 || i2 == e1 || i1 == e2) {
            return true;
        }

        if (i1 > e1) {
            return e2 > i1;
        } else {
            return i2 > e1;
        }
    }
}
