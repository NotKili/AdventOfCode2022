package nineteen;

import util.DailyTask;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class TaskNineteen extends DailyTask<List<String>, List<String>> {
    public TaskNineteen() {
        super(InputNineteen.INPUT_ONE, InputNineteen.INPUT_TWO);
    }

    @Override
    public String solveTaskOne(List<String> parsed) {
        AtomicInteger result = new AtomicInteger(0);

        parsed.stream().parallel().forEach(input -> {
            Integer[] nums = parseSingleNumbers(input);

            int partial = getMaximumGeodeAmountWithParameters(nums[1], nums[2], nums[3], nums[4], nums[5], nums[6], 24);
            result.set(result.get() + (partial * nums[0]));
        });

        return result.get() + "";
    }

    @Override
    public List<String> parseInputOne(List<String> input) {
        return input.stream().map(line -> {
            line = line.replace("Blueprint ", "").replace(": ", ".");
            return line.substring(0, line.length() - 1);
        }).toList();
    }

    @Override
    public String solveTaskTwo(List<String> parsed) {
        AtomicInteger result = new AtomicInteger(1);

        parsed.stream().parallel().forEach(input -> {
            Integer[] nums = parseSingleNumbers(input);
            int partial = getMaximumGeodeAmountWithParameters(nums[1], nums[2], nums[3], nums[4], nums[5], nums[6], 32);
            result.set(result.get() * partial);
        });

        return result.get() + "";
    }

    @Override
    public List<String> parseInputTwo(List<String> input) {
        return parseInputOne(input);
    }

    static int max(int... nums) {
        return Arrays.stream(nums).max().orElse(-1);
    }
    static int ORE = 0;
    static int CLAY = 1;
    static int OBSIDIAN = 2;
    static int GEODE = 3;
    static int ORE_ROBOT = 4;
    static int CLAY_ROBOT = 5;
    static int OBSIDIAN_ROBOT = 6;
    static int GEODE_ROBOT = 7;
    static int TIME = 8;

    private static int getMaximumGeodeAmountWithParameters(int oreRobotCostsOre, int clayRobotCostsOre, int obsidianRobotCostsOre, int obsidianRobotCostsClay, int geodeRobotCostsOre, int geodeRobotCostsObsidian, int totalTime) {
        // Max value of geodes
        int maxGeodeAmount = 0;

        // Queue for all states to check
        Queue<State<Integer>> queue = new PriorityQueue<>((o1, o2) -> Integer.compare(o2.get(GEODE), o1.get(GEODE)) + Integer.compare(o2.get(GEODE_ROBOT), o1.get(GEODE_ROBOT)));

        // Set that contains all states that have been computed already
        HashSet<State<Integer>> alreadyUsedStates = new HashSet<>();

        State<Integer> startingState = new State<>(0, 0, 0, 0, 1, 0, 0, 0, totalTime);
        queue.add(startingState);

        while (!queue.isEmpty()) {
            State<Integer> currentState = queue.poll();

            // Optimization 1:
            // I decided to only prune branches on the last 2 levels because it seemed the optimization did not actually make the algorithm go faster for earlier levels
            // Last branch, eliminate branches that can't make it anymore
            if (currentState.get(TIME) == 1) {
                if (currentState.get(GEODE) + currentState.get(GEODE_ROBOT) <= maxGeodeAmount) {
                    continue;
                }
            // Second Last branch, eliminate branches that can't make it anymore
            } else if (currentState.get(TIME) == 2) {
                if (currentState.get(GEODE) + currentState.get(GEODE_ROBOT) * 2 + 1 <= maxGeodeAmount) {
                    continue;
                }
            }

            // Optimization 2:
            // You always only need as many robots per Material as you can use up each round.
            // This can compact multiple branches into a single state and will therefore speed up calculation by a lot
            // (i.e. s1 produces 4 ore per min, s2 produces 6 ore per min, but only 4 are needed, therefore s2 will always behave the same way as s1 & never create a different result)
            int maxOreRobotsNeeded = max(oreRobotCostsOre, clayRobotCostsOre, obsidianRobotCostsOre, geodeRobotCostsOre);
            currentState.modify(state -> {
                state.set(ORE_ROBOT, Math.min(maxOreRobotsNeeded, state.get(ORE_ROBOT)));
            });
            currentState.modify(state -> {
                state.set(CLAY_ROBOT, Math.min(obsidianRobotCostsClay, state.get(CLAY_ROBOT)));
            });
            currentState.modify(state -> {
                state.set(OBSIDIAN_ROBOT, Math.min(geodeRobotCostsObsidian, state.get(OBSIDIAN_ROBOT)));
            });

            // If our "compacted" state has been used once already, skip it, else add it to the calculated states & produce offsprings
            if (alreadyUsedStates.contains(currentState)) {
                continue;
            } else {
                alreadyUsedStates.add(currentState);
            }

            // Optimization 3:
            // On the last layer t = 1, you do not need to build a robot anymore because the resulting state will be pruned anyways with the robot having no effect on the production rate of any material
            // You essentially do not even need to create a new state, because the only paramter that matters is the geode amount you will have at t = 0
            // Therefore you skip the creation of 4 possible offsprings & instead just max-update the maxGeodeAmount value with the pre-calculated geode amount at t = 0 for this state
            if (currentState.get(TIME) == 1) {
                maxGeodeAmount = Math.max(maxGeodeAmount, currentState.get(GEODE) + currentState.get(GEODE_ROBOT));
            } else {
                // Optimization 4 - Idea: (This only works due to t being small in comparison to the materials needed to build certain robots. Much bigger t or very different input sets with very cheap build paths could possibly not return valid answers)
                // The idea is to maximize geode robot creation. If you can build a geode - robot, considering the input parameters, it does not make sense to build any other robot or even skip this step, due to t being too small for other changes to make a notable difference later on
                // than another geode robot now
                // If this optimization returns a wrong value for your input, simply add all other possible states (NO-ACTION, BUY-ORE, BUY-CLAY, BUY-OBSIDIAN) to the queue together with the BUY-GEODE action,
                // although this could lead to a significant increase in time needed for the algorithm to operate, depending on the input

                // Buy a Geode Robot
                if (geodeRobotCostsOre <= currentState.get(ORE) && geodeRobotCostsObsidian <= currentState.get(OBSIDIAN)) {
                    queue.add(currentState.copyAndModify(state -> {
                        // Mine
                        state.set(ORE, state.get(ORE) + state.get(ORE_ROBOT));
                        state.set(CLAY, state.get(CLAY) + state.get(CLAY_ROBOT));
                        state.set(OBSIDIAN, state.get(OBSIDIAN) + state.get(OBSIDIAN_ROBOT));
                        state.set(GEODE, state.get(GEODE) + state.get(GEODE_ROBOT));

                        // Buy Robot
                        state.set(ORE, state.get(ORE) - geodeRobotCostsOre);
                        state.set(OBSIDIAN, state.get(OBSIDIAN) - geodeRobotCostsObsidian);
                        state.set(GEODE_ROBOT, state.get(GEODE_ROBOT) + 1);

                        // Decrement Time
                        state.set(TIME, state.get(TIME) - 1);
                    }));

                // Buy an Ore Robot, or buy a Clay Robot or buy an Obsidian Robot or buy nothing
                } else {
                    // Buy nothing
                    queue.add(currentState.copyAndModify(state -> {
                        // Mine
                        state.set(ORE, state.get(ORE) + state.get(ORE_ROBOT));
                        state.set(CLAY, state.get(CLAY) + state.get(CLAY_ROBOT));
                        state.set(OBSIDIAN, state.get(OBSIDIAN) + state.get(OBSIDIAN_ROBOT));
                        state.set(GEODE, state.get(GEODE) + state.get(GEODE_ROBOT));

                        // Decrement Time
                        state.set(TIME, state.get(TIME) - 1);
                    }));

                    // Try to buy Ore Robot
                    if (oreRobotCostsOre <= currentState.get(ORE)) {
                        queue.add(currentState.copyAndModify(state -> {
                            // Mine
                            state.set(ORE, state.get(ORE) + state.get(ORE_ROBOT));
                            state.set(CLAY, state.get(CLAY) + state.get(CLAY_ROBOT));
                            state.set(OBSIDIAN, state.get(OBSIDIAN) + state.get(OBSIDIAN_ROBOT));
                            state.set(GEODE, state.get(GEODE) + state.get(GEODE_ROBOT));

                            // Buy Robot
                            state.set(ORE, state.get(ORE) - oreRobotCostsOre);
                            state.set(ORE_ROBOT, state.get(ORE_ROBOT) + 1);

                            // Decrement Time
                            state.set(TIME, state.get(TIME) - 1);
                        }));
                    }

                    // Try to buy Clay Robot
                    if (clayRobotCostsOre <= currentState.get(ORE)) {
                        queue.add(currentState.copyAndModify(state -> {
                            // Mine
                            state.set(ORE, state.get(ORE) + state.get(ORE_ROBOT));
                            state.set(CLAY, state.get(CLAY) + state.get(CLAY_ROBOT));
                            state.set(OBSIDIAN, state.get(OBSIDIAN) + state.get(OBSIDIAN_ROBOT));
                            state.set(GEODE, state.get(GEODE) + state.get(GEODE_ROBOT));

                            // Buy Robot
                            state.set(ORE, state.get(ORE) - clayRobotCostsOre);
                            state.set(CLAY_ROBOT, state.get(CLAY_ROBOT) + 1);

                            // Decrement Time
                            state.set(TIME, state.get(TIME) - 1);
                        }));
                    }

                    // Try to buy Obsidian Robot
                    if (obsidianRobotCostsOre <= currentState.get(ORE) && obsidianRobotCostsClay <= currentState.get(CLAY)) {
                        queue.add(currentState.copyAndModify(state -> {
                            // Mine
                            state.set(ORE, state.get(ORE) + state.get(ORE_ROBOT));
                            state.set(CLAY, state.get(CLAY) + state.get(CLAY_ROBOT));
                            state.set(OBSIDIAN, state.get(OBSIDIAN) + state.get(OBSIDIAN_ROBOT));
                            state.set(GEODE, state.get(GEODE) + state.get(GEODE_ROBOT));

                            // Buy Robot
                            state.set(ORE, state.get(ORE) - obsidianRobotCostsOre);
                            state.set(CLAY, state.get(CLAY) - obsidianRobotCostsClay);
                            state.set(OBSIDIAN_ROBOT, state.get(OBSIDIAN_ROBOT) + 1);

                            // Decrement Time
                            state.set(TIME, state.get(TIME) - 1);
                        }));
                    }
                }
            }

        }

        return maxGeodeAmount;
    }

    // Wrapper Class for T[] to allow the usage of it as a HashMap Key
    static class State<T> {
        private final T[] states;

        @SafeVarargs
        public State(T... states) {
            this.states = states;
        }

        public T get(int i) {
            return states[i];
        }

        public void set(int i, T element) {
            states[i] = element;
        }

        public T[] getStates() {
            return states;
        }

        public void modify(Consumer<State<T>> modifier) {
            modifier.accept(this);
        }

        public State<T> copyAndModify(Consumer<State<T>> modifier) {
            State<T> toModify = new State<>(states.clone());
            modifier.accept(toModify);
            return toModify;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State<?> state = (State<?>) o;
            return Arrays.equals(states, state.states);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(states);
        }

        @Override
        public String toString() {
            return "State{" +
                    "states=" + Arrays.toString(states) +
                    '}';
        }
    }

    static Integer[] parseSingleNumbers(String input) {
        Stack<Character> stack = new Stack<>();
        ArrayList<Integer> numbers = new ArrayList<>();

        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                stack.push(c);
            } else {
                if (!stack.isEmpty()) {
                    StringBuilder collector = new StringBuilder("");

                    while (!stack.isEmpty()) {
                        collector.append(stack.pop());
                    }

                    numbers.add(Integer.parseInt(collector.reverse().toString()));
                }
            }
        }

        return numbers.toArray(new Integer[] {});
    }
}
