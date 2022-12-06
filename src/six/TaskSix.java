package six;

import util.DailyTask;

import java.util.HashSet;
import java.util.List;

public class TaskSix extends DailyTask<String, String> {

    public TaskSix(String TASK_ONE, String TASK_TWO) {
        super(TASK_ONE, TASK_TWO);
    }

    @Override
    public String solveTaskOne(String parsed) {
        return searchForFirstDistinct(4, parsed.toCharArray());
    }

    @Override
    public String parseInputOne(List<String> input) {
        return input.get(0);
    }

    @Override
    public String solveTaskTwo(String parsed) {
        return searchForFirstDistinct(14, parsed.toCharArray());
    }

    @Override
    public String parseInputTwo(List<String> input) {
        return input.get(0);
    }

    private String searchForFirstDistinct(int distinctAmount, char[] chars) {
        HashSet<Character> controll = new HashSet<>();
        int count = 0;
        int lastCheckIndex = 0;

        while (true) {
            for (int i = lastCheckIndex; i < chars.length; i++) {
                if (controll.contains(chars[i])) {
                    controll.clear();
                    count = 0;
                    lastCheckIndex++;
                    break;
                } else {
                    controll.add(chars[i]);
                    count++;

                    if (count == distinctAmount) {
                        return (i + 1) + "";
                    }
                }
            }

            if (lastCheckIndex == chars.length) {
                return "ERROR303";
            }
        }
    }
}
