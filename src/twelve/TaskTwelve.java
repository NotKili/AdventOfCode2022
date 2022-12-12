package twelve;

import util.DailyTask;

import java.util.*;

public class TaskTwelve extends DailyTask<char[][], char[][]> {
    public TaskTwelve() {
        super(InputTwelve.TASK_ONE, InputTwelve.TASK_TWO);
    }

    @Override
    public String solveTaskOne(char[][] parsed) {
        int[][] map = new int[parsed.length][parsed[0].length];

        Coordinate startCoordinate = null;
        Coordinate endCoordinate = null;

        for (int y = 0; y < parsed.length; y++) {
            for (int x = 0; x < parsed[y].length; x++) {
                char c = parsed[y][x];

                if (c == 'E') {
                    endCoordinate = new Coordinate(x, y, 0);
                    map[y][x] = 'z';
                } else if (c == 'S') {
                    startCoordinate = new Coordinate(x, y, 0);
                    map[y][x] = 'a';
                } else {
                    map[y][x] = c;
                }
            }
        }

        int[][] alreadyParsedWithCost = new int[map.length][map[0].length];
        for (int[] arr : alreadyParsedWithCost) {
            Arrays.fill(arr, -1);
        }

        PriorityQueue<Coordinate> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a.cost));

        alreadyParsedWithCost[startCoordinate.y][startCoordinate.x] = startCoordinate.cost;
        queue.add(startCoordinate);

        while (!queue.isEmpty()) {
            Coordinate currentCoordinate = queue.poll();

            if (currentCoordinate.equals(endCoordinate)) {
                return currentCoordinate.cost + "";
            }

            for (Coordinate neighbor : currentCoordinate.getNeighbors()) {
                if (Coordinate.isLegal(currentCoordinate, neighbor, map)) {
                    int nodeCost = neighbor.cost;
                    if (alreadyParsedWithCost[neighbor.y][neighbor.x] == -1 || nodeCost < alreadyParsedWithCost[neighbor.y][neighbor.x]) {
                        alreadyParsedWithCost[neighbor.y][neighbor.x] = nodeCost;
                        queue.add(neighbor);
                    }
                }
            }
        }

        return "ERROR";
    }

    @Override
    public char[][] parseInputOne(List<String> input) {
        char[][] tmp = new char[input.size()][input.get(0).length()];

        for (int i = 0; i < input.size(); i++) {
            tmp[i] = input.get(i).toCharArray();
        }

        return tmp;
    }

    @Override
    public String solveTaskTwo(char[][] parsed) {
        int[][] map = new int[parsed.length][parsed[0].length];

        Coordinate endCoordinate = null;

        for (int y = 0; y < parsed.length; y++) {
            for (int x = 0; x < parsed[y].length; x++) {
                char c = parsed[y][x];

                if (c == 'E') {
                    endCoordinate = new Coordinate(x, y, 0);
                    map[y][x] = 'z';
                } else if (c == 'S') {
                    map[y][x] = 'a';
                } else {
                    map[y][x] = c;
                }
            }
        }

        int shortestPath = Integer.MAX_VALUE;

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == 'a') {
                    int[][] alreadyParsedWithCost = new int[map.length][map[0].length];
                    for (int[] arr : alreadyParsedWithCost) {
                        Arrays.fill(arr, -1);
                    }

                    PriorityQueue<Coordinate> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a.cost));

                    Coordinate startCoordinate = new Coordinate(x, y, 0);
                    alreadyParsedWithCost[startCoordinate.y][startCoordinate.x] = startCoordinate.cost;
                    queue.add(startCoordinate);

                    while (!queue.isEmpty()) {
                        Coordinate currentCoordinate = queue.poll();

                        if (currentCoordinate.equals(endCoordinate)) {
                            shortestPath = Math.min(shortestPath, currentCoordinate.cost);
                            break;
                        }

                        for (Coordinate neighbor : currentCoordinate.getNeighbors()) {
                            if (Coordinate.isLegal(currentCoordinate, neighbor, map)) {
                                int nodeCost = neighbor.cost;
                                if (alreadyParsedWithCost[neighbor.y][neighbor.x] == -1 || nodeCost < alreadyParsedWithCost[neighbor.y][neighbor.x]) {
                                    alreadyParsedWithCost[neighbor.y][neighbor.x] = nodeCost;
                                    queue.add(neighbor);
                                }
                            }
                        }
                    }
                }
            }
        }

        return shortestPath + "";
    }

    @Override
    public char[][] parseInputTwo(List<String> input) {
        return parseInputOne(input);
    }

    static class Coordinate {
        int x;
        int y;
        int cost;

        public Coordinate(int x, int y, int cost) {
            this.x = x;
            this.y = y;
            this.cost = cost;
        }

        // Counts the length of the path already
        public Coordinate[] getNeighbors() {
            return new Coordinate[] {
                    new Coordinate(x - 1, y, cost + 1),
                    new Coordinate(x + 1, y, cost + 1),
                    new Coordinate(x, y - 1, cost + 1),
                    new Coordinate(x, y + 1, cost + 1)
            };
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate coordinate = (Coordinate) o;
            return x == coordinate.x && y == coordinate.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        public static boolean isLegal(Coordinate parent, Coordinate neighbor, int[][] map) {
            // Out of bound (1/2)
            if (neighbor.x < 0 || neighbor.y < 0) {
                return false;
            }

            // Out of bound (2/2)
            if (neighbor.y >= map.length || neighbor.x >= map[neighbor.y].length) {
                return false;
            }

            // Is lower, equal or +1
            return map[neighbor.y][neighbor.x] <= map[parent.y][parent.x] + 1;
        }
    }
}
