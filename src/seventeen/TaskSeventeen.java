package seventeen;

import util.DailyTask;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaskSeventeen extends DailyTask<String, String> {
    public TaskSeventeen() {
        super(InputSeventeen.TASK_ONE, InputSeventeen.TASK_ONE);
    }

    @Override
    public String solveTaskOne(String parsed) {
        Cave cave = new Cave(parsed, 2022);
        cave.simulate();
        return cave.getHeight() + "";
    }

    @Override
    public String parseInputOne(List<String> input) {
        return input.get(0);
    }

    @Override
    public String solveTaskTwo(String parsed) {
        Cave cave = new Cave(parsed, 6000);
        long iterationsCount = 1000000000000L;

        // Size of test-input needs to be changed depending on input, it could happen that 6000 is not big enough
        String sequence = cave.simulateAndReturnDiffs();
        String periodicSequence = findPeriodicSequence(sequence, 100);

        if (periodicSequence == null) {
            return "ERROR - Sequence NULL";
        }

        int startIndex = sequence.indexOf(periodicSequence);
        int sumStart;

        if (startIndex != -1) {
            String startSequence = sequence.substring(0, sequence.indexOf(periodicSequence));
            sumStart = Arrays.stream(startSequence.split("")).map(Integer::parseInt).reduce(Integer::sum).orElse(0);
            iterationsCount -= startSequence.length();
        } else {
            sumStart = 0;
        }

        int periodicSequenceSum = Arrays.stream(periodicSequence.split("")).map(Integer::parseInt).reduce(Integer::sum).orElse(0);
        long periodicOccurrences = iterationsCount / periodicSequence.length();

        long restPeriodicLength = iterationsCount % periodicSequence.length();
        long restSum = 0;

        if (restPeriodicLength > 0) {
            restSum = Arrays.stream(periodicSequence.substring(0, (int) restPeriodicLength).split("")).map(Integer::parseInt).reduce(Integer::sum).orElse(0);
        }

        return (sumStart + (periodicSequenceSum * periodicOccurrences) + restSum) + "";
    }

    @Override
    public String parseInputTwo(List<String> input) {
        return parseInputOne(input);
    }

    static String findPeriodicSequence(String input, int minSize) {
        int length = input.length();

        // start index
        for (int i = 1; i < input.length(); i++) {
            // periodic string length
            for (int j = minSize; j < (input.length() - i); j++) {
                String toTest = input.substring(i, i + j);
                boolean hasFound = true;

                int numPermutations = (length - i - toTest.length()) / toTest.length();

                if (numPermutations <= 1) {
                    break;
                }

                // offset
                for (int k = 1; k < numPermutations; k++) {
                    int start = i + j + (k * toTest.length());
                    int end = start + toTest.length();
                    if (!toTest.equals(input.substring(start, end))) {
                        hasFound = false;
                        break;
                    }
                }

                if (hasFound) {
                    return toTest;
                }
            }
        }

        return null;
    }

    static class Cave {
        int caveHeight;
        int runSize;
        int[][] cave;
        String movement;
        int currentMovementIndex;
        Circle<Function<Coordinate, Rock>> rocksToDrop;
        int currentMaxHeight;

        public Cave(String movement, int runSize) {
            this.runSize = runSize;
            this.caveHeight = runSize * 3;
            cave = new int[caveHeight][9];
            this.movement = movement;
            this.currentMovementIndex = 0;
            this.currentMaxHeight = 0;

            for (int y = 0; y < caveHeight; y++) {
                cave[y][0] = -1;
                cave[y][8] = -1;
            }

            for (int x = 0; x < 9; x++) {
                cave[0][x] = -2;
            }

            // Rock - Formations
            this.rocksToDrop = new Circle<>();
            rocksToDrop.add((c) -> new Rock(
                    List.of(c.addX(2), c.addX(3), c.addX(4), c.addX(5))
            ));

            rocksToDrop.add((c) -> new Rock(
                    List.of(
                            c.addXY(3, 2),
                            c.addXY(2, 1), c.addXY(3, 1), c.addXY(4, 1),
                            c.addX(3)
                    )
            ));

            rocksToDrop.add((c) -> new Rock(
                    List.of(
                            c.addXY(4, 2),
                            c.addXY(4, 1),
                            c.addX(2), c.addX(3), c.addX(4)
                    )
            ));

            rocksToDrop.add((c) -> new Rock(
                    List.of(
                            c.addXY(2, 3),
                            c.addXY(2, 2),
                            c.addXY(2, 1),
                            c.addX(2)
                    )
            ));

            rocksToDrop.add((c) -> new Rock(
                    List.of(
                            c.addXY(2, 1), c.addXY(3, 1),
                            c.addX(2), c.addX(3)
                    )
            ));
        }

        public String simulateAndReturnDiffs() {
            int[] diffLinear = new int[runSize];
            int[] linearHeights = new int[runSize];
            for (int i = 0; i < runSize; i++) {
                dropRock(rocksToDrop.getCurrentAndMove().apply(new Coordinate(1, currentMaxHeight + 4)));
                linearHeights[i] = currentMaxHeight;

                if (i == 0) {
                    diffLinear[i] = currentMaxHeight;
                } else {
                    diffLinear[i] = currentMaxHeight - linearHeights[i - 1];
                }
            }

            return Arrays.toString(diffLinear).replace("[", "").replace("]", "").replace(", ", "");
        }

        public void simulate() {
            for (int i = 0; i < runSize; i++) {
                dropRock(rocksToDrop.getCurrentAndMove().apply(new Coordinate(1, currentMaxHeight + 4)));
            }
        }

        public void dropRock(Rock rock) {
            do {
                Rock tmp = rock;
                rock = pushAround(rock);

                if (rock.hasHitGround(cave)) {
                    rock = tmp;
                }
            } while (!moveOneDown(rock));

            drawRock(rock);
            updateHeight();
        }

        private Rock pushAround(Rock rock) {
            char movement = this.movement.charAt(currentMovementIndex++);

            if (currentMovementIndex == this.movement.length()) {
                currentMovementIndex = 0;
            }

            return switch (movement) {
                case '>' -> rock.moveInX(1);
                case '<' -> rock.moveInX(-1);
                default -> rock;
            };
        }

        private boolean moveOneDown(Rock rock) {
            Rock moved = rock.moveDown(1);

            if (moved.hasHitGround(cave)) {
                return true;
            } else {
                rock.setCoords(moved.coords);
                return false;
            }
        }

        private void updateHeight() {
            boolean hasFound;

            currentMaxHeight = 0;

            for (int y = 1; y < caveHeight; y++) {
                hasFound = false;
                for (int x = 1; x < 9; x++) {
                    if (cave[y][x] == 1) {
                        currentMaxHeight++;
                        hasFound = true;
                        break;
                    }
                }
                if (!hasFound) {
                    break;
                }
            }
        }

        private void drawRock(Rock rock) {
            for (Coordinate c : rock.coords) {
                cave[c.y][c.x] = 1;
            }
        }

        public int getHeight() {
            return currentMaxHeight;
        }
    }

    static class Rock {
        private List<Coordinate> coords;

        public Rock(List<Coordinate> coords) {
            this.coords = coords;
        }

        public Rock moveDown(int byY) {
            Rock copy = copy();

            for (Coordinate c : copy.coords) {
                c.y -= byY;
            }

            return copy;
        }

        public Rock moveInX(int byX) {
            Rock copy = copy();

            for (Coordinate c : copy.coords) {
                c.x += byX;

                if (c.x > 7 || c.x < 1) {
                    return this;
                }
            }

            return copy;
        }

        public boolean hasHitGround(int[][] cave) {
            for (Coordinate c : coords) {
                if (cave[c.y][c.x] != 0) {
                    return true;
                }
            }

            return false;
        }

        public void setCoords(List<Coordinate> coords) {
            this.coords = coords;
        }

        public Rock copy() {
            return new Rock(coords.stream().map(Coordinate::copy).collect(Collectors.toList()));
        }

        @Override
        public String toString() {
            return "Rock{" +
                    "coords=" + coords +
                    '}';
        }
    }

    static class Circle<T> {
        private Node<T> head;
        private Node<T> addAt;
        private Node<T> current;

        T getCurrentAndMove() {
            T value = current.value;
            current = current.next;
            return value;
        }

        void add(T element) {
            Node<T> toInsert = new Node<>(element);

            toInsert.next = head;

            if (addAt == null) {
                head = toInsert;
                addAt = toInsert;
                current = toInsert;
                toInsert.next = toInsert;
            } else {
                addAt.next = toInsert;
                addAt = toInsert;
            }
        }

        static class Node<T> {
            T value;
            Node<T> next;

            public Node(T value) {
                this.value = value;
            }
        }
    }

    static class Coordinate {
        int x;
        int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Coordinate addX(int x) {
            return new Coordinate(this.x + x, this.y);
        }

        public Coordinate addY(int y) {
            return new Coordinate(this.x, this.y + y);
        }

        public Coordinate addXY(int x, int y) {
            return addX(x).addY(y);
        }

        public Coordinate copy() {
            return new Coordinate(x, y);
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return x == that.x && y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
