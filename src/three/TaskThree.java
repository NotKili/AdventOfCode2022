package three;

import util.DailyTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class TaskThree extends DailyTask<List<String[]>, List<String[]>> {

    public TaskThree() {
        super(InputThree.TASK_ONE, InputThree.TASK_ONE);
    }

    @Override
    public String solveTaskOne(List<String[]> parsed) {
        return parsed.stream().mapToInt(part -> {
            int split = part.length / 2;
            String[] sub = new String[split];

            System.arraycopy(part, 0, sub, 0, split);

            HashSet<String> partOne = new HashSet<>(Arrays.stream(sub).toList());

            for (int i = split; i < part.length; i++) {
                if (partOne.contains(part[i])) {
                    return calcSum(part[i]);
                }
            }
            return 0;
        }).sum() + "";
    }

    @Override
    public List<String[]> parseInputOne(List<String> input) {
        return input.stream().map(s -> s.split("")).collect(Collectors.toList());
    }

    @Override
    public String solveTaskTwo(List<String[]> parsed) {
        return parsed.stream().mapToInt(part -> {
            HashSet<String> charsOne = new HashSet<>(Arrays.stream(part[0].split("")).toList());
            charsOne.retainAll(Arrays.stream(part[1].split("")).toList());
            charsOne.retainAll(Arrays.stream(part[2].split("")).toList());

            if (charsOne.size() > 0) {
                return calcSum(charsOne.stream().findAny().get());
            } else {
                return 0;
            }
        }).sum() + "";
    }

    @Override
    public List<String[]> parseInputTwo(List<String> input) {
        List<String[]> newList = new ArrayList<>(input.size() / 3);

        String[] tmp = new String[3];
        int c = 0;
        for (String s : input) {
            tmp[c++] = s;

            if (c == 3) {
                newList.add(tmp);
                tmp = new String[3];
                c = 0;
            }
        }
        return newList;
    }

    private static int calcSum(String s) {
        char t = s.charAt(0);
        if (Character.isLowerCase(t)) {
            return t - 'a' + 1;
        } else {
            return t - 'A' + 27;
        }
    }
}
