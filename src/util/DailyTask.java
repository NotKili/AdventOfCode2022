package util;

import java.util.Arrays;
import java.util.List;

public abstract class DailyTask<A, B> {
    private final String TASK_ONE;
    private final String TASK_TWO;

    public DailyTask(String TASK_ONE, String TASK_TWO) {
        this.TASK_ONE = TASK_ONE;
        this.TASK_TWO = TASK_TWO;
    }

    ///////////////////////
    ///    TASK ONE     ///
    ///////////////////////

    public String runTaskOne() {
        A parsed = parseInputOne(splitInput(TASK_ONE));

        // Work with input & return
        return solveTaskOne(parsed);
    }

    abstract String solveTaskOne(A parsed);

    abstract A parseInputOne(List<String> input);



    ///////////////////////
    ///    TASK TWO     ///
    ///////////////////////

    public String runTaskTwo() {
        B parsed = parseInputTwo(splitInput(TASK_TWO));

        // Work with input & return
        return solveTaskTwo(parsed);
    }

    abstract String solveTaskTwo(B parsed);

    abstract B parseInputTwo(List<String> input);

    private static List<String> splitInput(String input) {
        return Arrays.stream(input.split("\n")).toList();
    }
}
