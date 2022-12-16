package sixteen;

import util.DailyTask;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class TaskSixteen extends DailyTask<HashMap<String, TaskSixteen.Valve>, HashMap<String, TaskSixteen.Valve>> {
    public TaskSixteen() {
        super(InputSixteen.TASK_ONE, InputSixteen.TASK_TWO);
    }

    static int optimumPartOne = 0;

    @Override
    public String solveTaskOne(HashMap<String, Valve> parsed) {
        calculateOptimalPath(parsed, new HashSet<>(List.of("AA")), 30, "AA", 0);
        return optimumPartOne + "";
    }

    private void calculateOptimalPath(HashMap<String, Valve> parsedValves, Set<String> openedValves, int minutesLeft, String currentValve, int totalCount) {
        // Update optimum
        optimumPartOne = Math.max(optimumPartOne, totalCount);

        // Leave because no more valves opened now will produce any outflow due to the outflow only starting next turn (which does not exist)
        if (minutesLeft == 1) {
            return;
        }

        // Valve is already opened, attempt to move to each next unopened valves if possible
        if (openedValves.contains(currentValve)) {
            // Iterate over all existing valves that have a value > 0 AND are unopened, then move there after subtracting the time to travel from the time left & validating that the travel is valid
            for (String nextUnopenedValve : parsedValves.values().stream().filter(valve -> !openedValves.contains(valve.name) && valve.value != 0).map(valve -> valve.name).toList()) {
                int minutesLeftAfterTravel = minutesLeft - (parsedValves.get(currentValve).minPathLengthsFromValveToOther.get(nextUnopenedValve));

                // Only move if enough time left for that move
                if (minutesLeftAfterTravel >= 1) {
                    calculateOptimalPath(parsedValves, openedValves, minutesLeftAfterTravel, nextUnopenedValve, totalCount);
                }
            }
        // Valve is unopened, open valve (consumes 1 minute) and search for next unopened Valves
        } else {
            int minutesAfterOpened = minutesLeft - 1;
            calculateOptimalPath(parsedValves, zip.apply(openedValves, currentValve), minutesAfterOpened, currentValve, totalCount + (parsedValves.get(currentValve).value * minutesAfterOpened));
        }
    }

    static BiFunction<Set<String>, String, Set<String>> zip = (set, element) -> {
        Set<String> newSet = new HashSet<>(set);
        newSet.add(element);
        return newSet;
    };

    @Override
    public HashMap<String, Valve> parseInputOne(List<String> input) {
        HashMap<String, Valve> valves = new HashMap<>();

        input.forEach(line -> {
            line = line
                    .replace("Valve ", "")
                    .replace(" has flow rate=", "-")
                    .replace("; tunnels lead to valves ", "-")
                    .replace("; tunnel leads to valve ", "-");

            String[] inputSplit = line.split("-");
            valves.put(inputSplit[0], new Valve(
                    inputSplit[0],
                    Integer.parseInt(inputSplit[1]),
                    new HashSet<>(Arrays.stream(inputSplit[2].split(", ")).collect(Collectors.toList()))
            ));
        });

        valves.forEach((key, valve) -> valve.calculateMinPathLength(valves));

        return valves;
    }

    @Override
    public String solveTaskTwo(HashMap<String, Valve> parsed) {
        calculateOptimalPathWithElephant(parsed, new HashSet<>(List.of("AA")), 26, "AA", 0, true);
        return optimumPartTwo + "";
    }

    @Override
    public HashMap<String, Valve> parseInputTwo(List<String> input) {
        return parseInputOne(input);
    }

    static int optimumPartTwo = 0;

    private void calculateOptimalPathWithElephant(HashMap<String, Valve> parsedValves, Set<String> openedValves, int minutesLeft, String currentValve, int totalCount, boolean isPlayerTurn) {
        // Update optimum
        optimumPartTwo = Math.max(optimumPartTwo, totalCount);

        // Leave because no more valves opened now will produce any outflow due to the outflow only starting next turn (which does not exist)
        if (minutesLeft == 1) {
            return;
        }

        // Valve is already opened, attempt to move to each next unopened valves if possible (Same for player & elephant)
        if (openedValves.contains(currentValve)) {
            // Iterate over all existing valves that have a value > 0 AND are unopened, then move there after subtracting the time to travel from the time left & validating that the travel is valid
            for (String nextUnopenedValve : parsedValves.values().stream().filter(valve -> !openedValves.contains(valve.name) && valve.value != 0).map(valve -> valve.name).toList()) {
                int minutesLeftAfterTravel = minutesLeft - (parsedValves.get(currentValve).minPathLengthsFromValveToOther.get(nextUnopenedValve));

                // Only move if enough time left for that move
                if (minutesLeftAfterTravel >= 1) {
                    calculateOptimalPathWithElephant(parsedValves, openedValves, minutesLeftAfterTravel, nextUnopenedValve, totalCount, isPlayerTurn);
                }
            }
        // Valve is unopened, open valve (consumes 1 minute) and then move to next unopened valves again
        } else {
            int minutesAfterOpened = minutesLeft - 1;
            int newCount = totalCount + (parsedValves.get(currentValve).value * minutesAfterOpened);

            calculateOptimalPathWithElephant(parsedValves, zip.apply(openedValves, currentValve), minutesAfterOpened, currentValve, newCount, isPlayerTurn);

            // Run search again for elephant from start knowing which nodes have been opened already (by the player) & with count produced by all opened valves (probably inefficient af)
            if (isPlayerTurn) {
                calculateOptimalPathWithElephant(parsedValves, zip.apply(openedValves, currentValve), 26, "AA", newCount, false);
            }
        }
    }

    static class Valve {
        private final String name;
        private final int value;
        private final HashSet<String> adjacentNodes;
        private final HashMap<String, Integer> minPathLengthsFromValveToOther;

        public Valve(String name, int value, HashSet<String> adjacentNodes) {
            this.name = name;
            this.value = value;
            this.adjacentNodes = adjacentNodes;
            this.minPathLengthsFromValveToOther = new HashMap<>();
        }

        public void calculateMinPathLength(HashMap<String, Valve> valves) {
            valves.values().forEach(valve -> {
                if (valve != this) {
                    int depth = 1;
                    Set<String> nodes = adjacentNodes;

                    while (true) {
                        Set<String> nextDepthNodes = new HashSet<>();
                        for (String n : nodes) {
                            if (n.equals(valve.name)) {
                                minPathLengthsFromValveToOther.put(valve.name, depth);
                                return;
                            }
                            nextDepthNodes.addAll(valves.get(n).adjacentNodes);
                        }
                        depth++;
                        nodes = nextDepthNodes;
                    }
                }
            });
        }
    }
}
