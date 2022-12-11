package eleven;

import util.DailyTask;

import java.util.*;
import java.util.function.Function;

public class TaskEleven extends DailyTask<ArrayList<TaskEleven.Monkey>, ArrayList<TaskEleven.MonkeySmart>> {
    public TaskEleven() {
        super(InputEleven.TASK_ONE, InputEleven.TASK_TWO);
    }

    @Override
    public String solveTaskOne(ArrayList<Monkey> monkeys) {
        for (int r = 0; r < 20; r++) {
            for (Monkey monkey : monkeys) {
                monkey.inspectAll(monkeys);
            }
        }

        monkeys.sort((a, b) -> Long.compare(b.getInspected(), a.getInspected()));

        return ((long) monkeys.get(0).getInspected() * (long) monkeys.get(1).getInspected()) + "";
    }

    @Override
    public ArrayList<Monkey> parseInputOne(List<String> input) {
        ArrayList<Monkey> monkeys = new ArrayList<>();

        LinkedList<Integer> startingItems = new LinkedList<>();
        Function<Integer, Integer> modifier = null;
        Function<Integer, Integer> monkeyChooser = null;

        for (int i = 0; i < input.size(); i++) {
            String s = input.get(i);

            if (s.startsWith("Monkey ")) {
                if (modifier != null && monkeyChooser != null) {
                    monkeys.add(new Monkey(startingItems, modifier, monkeyChooser));
                    startingItems = new LinkedList<>();
                    modifier = null;
                    monkeyChooser = null;
                }
            } else if (s.startsWith("  Starting items: ")) {
                String[] items = s.substring(18).split(", ");

                for (String in : items) {
                    startingItems.add(Integer.parseInt(in));
                }
            } else if (s.startsWith("  Operation: new = ")) {
                modifier = parseFunc(s.substring(19));

            } else if (s.startsWith("  Test: divisible by ")) {
                String onTrue = input.get(i + 1);
                String onFalse = input.get(i + 2);

                // i = i + 2;

                long div = Long.parseLong(s.substring(21));
                int t = Integer.parseInt(onTrue.substring(29));
                int f = Integer.parseInt(onFalse.substring(30));

                monkeyChooser = (x) -> {
                    if (x % div == 0) {
                        return t;
                    } else {
                        return f;
                    }
                };
            }
        }

        if (modifier != null && monkeyChooser != null) {
            monkeys.add(new Monkey(startingItems, modifier, monkeyChooser));
        }

        return monkeys;
    }

    @Override
    public String solveTaskTwo(ArrayList<MonkeySmart> monkeys) {
        for (int r = 0; r < 10000; r++) {
            for (int i = 0; i < monkeys.size(); i++) {
                monkeys.get(i).inspectAll(monkeys);
            }
        }

        monkeys.sort((a, b) -> Long.compare(b.getInspected(), a.getInspected()));

        return ((long) monkeys.get(0).getInspected() * (long) monkeys.get(1).getInspected()) + "";
    }

    @Override
    public ArrayList<MonkeySmart> parseInputTwo(List<String> input) {
        ArrayList<MonkeySmart> monkeys = new ArrayList<>();

        int monkeyID = 0;
        int modToTest = 1;
        LinkedList<Integer> startingItems = new LinkedList<>();
        Function<Integer, Integer> function = null;
        Function<Integer, Integer> apeChooser = null;

        for (int i = 0; i < input.size(); i++) {
            String s = input.get(i);

            if (s.startsWith("Monkey ")) {
                if (function != null && apeChooser != null) {
                    monkeys.add(monkeyID, new MonkeySmart(monkeyID, modToTest, startingItems, function, apeChooser));
                    startingItems = new LinkedList<>();
                    function = null;
                    apeChooser = null;
                }

                monkeyID = Integer.parseInt(s.substring(7, 8));
            } else if (s.startsWith("  Starting items: ")) {
                String[] items = s.substring(18).split(", ");

                for (String in : items) {
                    startingItems.add(Integer.parseInt(in));
                }
            } else if (s.startsWith("  Operation: new = ")) {
                function = parseFunc(s.substring(19));

            } else if (s.startsWith("  Test: divisible by ")) {
                String onTrue = input.get(i + 1);
                String onFalse = input.get(i + 2);

                int div = Integer.parseInt(s.substring(21));
                int t = Integer.parseInt(onTrue.substring(29));
                int f = Integer.parseInt(onFalse.substring(30));

                modToTest = div;
                apeChooser = (x) -> {
                    if (x % div == 0) {
                        return t;
                    } else {
                        return f;
                    }
                };
            }
        }

        if (function != null && apeChooser != null) {
            monkeys.add(monkeyID, new MonkeySmart(monkeyID, modToTest, startingItems, function, apeChooser));
        }

        for (MonkeySmart ms : monkeys) {
            ms.updateItems(monkeys);
        }

        return monkeys;
    }

    static class Monkey {
        int inspected = 0;
        LinkedList<Integer> items;
        Function<Integer, Integer> action;
        Function<Integer, Integer> monkeyChooser;

        public Monkey(LinkedList<Integer> items, Function<Integer, Integer> action, Function<Integer, Integer> monkeyChooser) {
            this.items = items;
            this.action = action;
            this.monkeyChooser = monkeyChooser;
        }

        public void inspectAll(ArrayList<Monkey> monkeys) {
            inspected += items.size();

            while (items.size() > 0) {
                int res = action.apply(items.removeFirst());

                res = res / 3;

                int monkeyNum = monkeyChooser.apply(res);
                monkeys.get(monkeyNum).throwTo(res);
            }
        }

        public void throwTo(int worryLevel) {
            this.items.add(worryLevel);
        }

        public int getInspected() {
            return inspected;
        }
    }

    static class MonkeySmart {
        int id;
        int inspected = 0;
        int modulo;
        LinkedList<Integer> itemsTMP;
        LinkedList<Integer[]> items;
        Function<Integer, Integer> action;
        Function<Integer, Integer> monkeyChooser;

        public MonkeySmart(int id, int modulo, LinkedList<Integer> items, Function<Integer, Integer> action, Function<Integer, Integer> monkeyChooser) {
            this.id = id;
            this.modulo = modulo;
            this.itemsTMP = items;
            this.items = new LinkedList<>();
            this.action = action;
            this.monkeyChooser = monkeyChooser;
        }

        public void updateItems(ArrayList<MonkeySmart> monkeys) {
            for (Integer integer : itemsTMP) {
                Integer[] value = new Integer[monkeys.size()];
                for (int i = 0; i < monkeys.size(); i++) {
                    value[i] = integer % monkeys.get(i).getModulo();
                }
                items.add(value);
            }
        }

        public void inspectAll(ArrayList<MonkeySmart> monkeys) {
            inspected += items.size();

            while (items.size() > 0) {
                Integer[] newVals = Arrays.stream(items.removeFirst()).map(i -> action.apply(i)).toList().toArray(new Integer[] {});

                for (int i = 0; i < newVals.length; i++) {
                    newVals[i] = newVals[i] % monkeys.get(i).getModulo();
                }

                int monkeyNum = monkeyChooser.apply(newVals[id]);
                monkeys.get(monkeyNum).throwTo(newVals);
            }
        }

        public void throwTo(Integer[] itemWorryLevel) {
            this.items.add(itemWorryLevel);
        }

        public int getInspected() {
            return inspected;
        }

        public int getModulo() {
            return modulo;
        }
    }

    private static Function<Integer, Integer> parseFunc(String func) {
        String[] parts = func.split(" ");

        boolean isAOld = parts[0].equals("old");
        boolean isBOld = parts[2].equals("old");

        return switch (parts[1]) {
            case "*" -> (x) -> (isAOld ? x : Integer.parseInt(parts[0])) * (isBOld ? x : Integer.parseInt(parts[2]));
            case "+" -> (x) -> (isAOld ? x : Integer.parseInt(parts[0])) + (isBOld ? x : Integer.parseInt(parts[2]));
            default -> null;
        };
    }
}
