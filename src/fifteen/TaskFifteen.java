package fifteen;

import util.DailyTask;

import java.util.*;
import java.util.stream.Collectors;

public class TaskFifteen extends DailyTask<List<TaskFifteen.Tuple<TaskFifteen.Coordinate>>, List<TaskFifteen.Tuple<TaskFifteen.Coordinate>>> {

    public TaskFifteen() {
        super(InputFifteen.TASK_ONE, InputFifteen.TASK_TWO);
    }

    @Override
    public String solveTaskOne(List<Tuple<Coordinate>> parsed) {
        HashSet<Integer> beacons = new HashSet<>();
        HashSet<Integer> spotsInBetween = new HashSet<>();
        int yToCalc = 2000000;

        parsed.stream().filter(t -> isYBetween(t, yToCalc)).forEach(t -> {
            if (t.b.y == yToCalc) {
                beacons.add(t.b.x);
            }
            spotsInBetween.addAll(calcKnownPointsOnLine(t, yToCalc));
        });

        // Remove beacons
        spotsInBetween.removeIf(beacons::contains);

        return spotsInBetween.size() + "";
    }

    private static HashSet<Integer> calcKnownPointsOnLine(Tuple<Coordinate> tuple, int y) {
        HashSet<Integer> knownX = new HashSet<>();

        int dist = manhattan(tuple);
        int yDist = Math.abs(tuple.a.y - y);

        knownX.add(tuple.a.x);

        int i = 1;
        while (i <= (dist - yDist)) {
            knownX.add(tuple.a.x + i);
            knownX.add(tuple.a.x - i);
            i++;
        }

        return knownX;
    }

    private static int manhattan(Tuple<Coordinate> coordinateTuple) {
        return Math.abs(coordinateTuple.a.x - coordinateTuple.b.x) + Math.abs(coordinateTuple.a.y - coordinateTuple.b.y);
    }

    private static boolean isYBetween(Tuple<Coordinate> tuple, int yToTest) {
        int dist = manhattan(tuple);

        // Tuple.a contains the sensor, Tuple.b contains the beacon
        if (tuple.a.y <= yToTest && tuple.a.y + dist >= yToTest) {
            return true;
        } else return tuple.a.y > yToTest && tuple.a.y - dist <= yToTest;
    }

    @Override
    public List<Tuple<TaskFifteen.Coordinate>> parseInputOne(List<String> input) {
        return input.stream().map(s -> {
            s = s.replace("Sensor at ", "")
                    .replace(": closest beacon is at ", ";")
                    .replace(" ", "")
                    .replace("x=", "")
                    .replace("y=", "");

            String[] coords = s.split(";");

            return new Tuple<>(new Coordinate(coords[0]), new Coordinate(coords[1]));
        }).collect(Collectors.toList());
    }

    @Override
    public String solveTaskTwo(List<Tuple<TaskFifteen.Coordinate>> parsed) {
        // Tuple.a contains the sensor, Tuple.b contains the beacon
        for (Tuple<Coordinate> tuple : parsed) {
            int manhattanDistance = manhattan(tuple);

            // Iterate over edges of "diamond" structure & check for each coordinate if it lies within any other diamond structure using the manhattan distance
            for (int deltaY = 0; deltaY <= manhattanDistance + 1; deltaY++) {
                int deltaX = (manhattanDistance + 1) - deltaY;
                int xToCheck;
                int yToCheck;

                // + x. + y direction
                xToCheck = tuple.a.x + deltaX;
                yToCheck = tuple.a.y + deltaY;
                String result = isValidAndFree(parsed, xToCheck, yToCheck);
                if (result != null) {
                    return result;
                }

                // + x. - y direction
                xToCheck = tuple.a.x + deltaX;
                yToCheck = tuple.a.y - deltaY;
                result = isValidAndFree(parsed, xToCheck, yToCheck);
                if (result != null) {
                    return result;
                }

                // - x. + y direction
                xToCheck = tuple.a.x - deltaX;
                yToCheck = tuple.a.y + deltaY;
                result = isValidAndFree(parsed, xToCheck, yToCheck);
                if (result != null) {
                    return result;
                }

                // - x. - y direction
                xToCheck = tuple.a.x - deltaX;
                yToCheck = tuple.a.y - deltaY;
                result = isValidAndFree(parsed, xToCheck, yToCheck);
                if (result != null) {
                    return result;
                }
            }
        }

        return "null";
    }

    private static String isValidAndFree(List<Tuple<Coordinate>> parsedTuples, int xToCheck, int yToCheck) {
        if ((xToCheck >= 0 && xToCheck <= 4000000) && (yToCheck >= 0 && yToCheck <= 4000000)) {
            Coordinate c = new Coordinate(xToCheck, yToCheck);

            if (isFree(c, parsedTuples)) {
                return c.x * 4000000L + c.y + "";
            }
        }
        return null;
    }

    private static boolean isFree(Coordinate toCheck, List<Tuple<Coordinate>> other) {
        for (Tuple<Coordinate> tuple : other) {
            int distanceToPoint = manhattan(new Tuple<>(toCheck, tuple.a));
            int distanceToBeacon = manhattan(tuple);

            if (distanceToPoint <= distanceToBeacon) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Tuple<TaskFifteen.Coordinate>> parseInputTwo(List<String> input) {
        return parseInputOne(input);
    }

    static class Tuple<T> {
        T a;
        T b;

        public Tuple(T a, T b) {
            this.a = a;
            this.b = b;
        }

        public T getA() {
            return a;
        }

        public T getB() {
            return b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TaskFifteen.Tuple<?> tuple = (TaskFifteen.Tuple<?>) o;
            return Objects.equals(a, tuple.a) && Objects.equals(b, tuple.b);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }

    static class Coordinate {
        private final int x;
        private final int y;

        public Coordinate(String input) {
            String[] split = input.split(",");

            x = Integer.parseInt(split[0]);
            y = Integer.parseInt(split[1]);
        }

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public String toString() {
            return "Coordinate{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TaskFifteen.Coordinate that = (TaskFifteen.Coordinate) o;
            return x == that.x && y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
