package one;

import util.DailyTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskOne extends DailyTask<List<Integer>, List<Integer>> {

    public TaskOne(String TASK_ONE, String TASK_TWO) {
        super(TASK_ONE, TASK_TWO);
    }

    @Override
    public String solveTaskOne(List<Integer> parsed) {
        return Collections.max(parsed) + "";
    }

    @Override
    public List<Integer> parseInputOne(List<String> input) {
        List<Integer> toAdd = new ArrayList<>();

        AtomicInteger counter = new AtomicInteger(0);

        input.forEach(line -> {
            if (line.isEmpty()) {
                toAdd.add(counter.get());
                counter.set(0);
            } else {
                counter.addAndGet(Integer.parseInt(line));
            }
        });

        return toAdd;
    }

    @Override
    public List<Integer> parseInputTwo(List<String> input) {
        return parseInputOne(input);
    }

    @Override
    public String solveTaskTwo(List<Integer> parsed) {
        Collections.sort(parsed);
        int last = parsed.size() - 1;
        return (parsed.get(last) + parsed.get(last - 1) + parsed.get(last - 2)) + "";
    }
}
