package five;

import util.DailyTask;

import java.util.*;

public class TaskFive extends DailyTask<List<String>, List<String>> {
    private ArrayList<Character>[] crates;

    public TaskFive(String TASK_ONE, String TASK_TWO) {
        super(TASK_ONE, TASK_TWO);
    }

    @Override
    public String solveTaskOne(List<String> parsed) {
        parsed.forEach(p -> {
            String[] split = p.split(" ");

            int amount = Integer.parseInt(split[0]);
            int start = Integer.parseInt(split[2]) - 1;
            int end = Integer.parseInt(split[4]) - 1;

            for (int i = 0; i < amount; i++) {
                if (crates[start].size() > 0) {
                    Character c = crates[start].remove(crates[start].size() - 1);
                    crates[end].add(c);
                }
            }
        });

        return printTopCrates();
    }

    @Override
    public List<String> parseInputOne(List<String> input) {
        List<String> instructions = new ArrayList<>();

        int len = input.get(0).length();
        int size = (int) Math.ceil(len / 4.0);

        this.crates = new ArrayList[size];

        input.forEach(i -> {
            if (i.isBlank()) {
                return;
            }

            if (i.startsWith("move")) {
                instructions.add(i.substring(5));

            } else if (!i.startsWith(" 1   ")) {
                for (int x = 0; x < 9; x++) {
                    char parsedChar = i.charAt(1 + x * 4);

                    if (parsedChar != ' ') {
                        if (crates[x] == null) {
                            crates[x] = new ArrayList<>();
                        }

                        crates[x].add(parsedChar);
                    }
                }
            }
        });

        for (ArrayList<Character> crate : crates) {
            Collections.reverse(crate);
        }

        return instructions;
    }

    @Override
    public String solveTaskTwo(List<String> parsed) {
        parsed.forEach(p -> {
            String[] split = p.split(" ");

            int amount = Integer.parseInt(split[0]);
            int start = Integer.parseInt(split[2]) - 1;
            int end = Integer.parseInt(split[4]) - 1;

            StringBuilder toAdd = new StringBuilder();
            for (int i = 0; i < amount; i++) {
                if (crates[start].size() > 0) {
                    toAdd.append(crates[start].remove(crates[start].size() - 1));
                }
            }
            toAdd.reverse();
            addAllChars(toAdd.toString(), crates[end]);
        });

        return printTopCrates();
    }

    @Override
    public List<String> parseInputTwo(List<String> input) {
        return parseInputOne(input);
    }

    private String printTopCrates() {
        StringBuilder sb = new StringBuilder();

        for (ArrayList<Character> crate : crates) {
            sb.append(crate.size() > 0 ? crate.get(crate.size() - 1) : "|");
        }

        return sb.toString();
    }

    private void addAllChars(String set, ArrayList<Character> list) {
        for (char c : set.toCharArray()) {
            list.add(c);
        }
    }
}
