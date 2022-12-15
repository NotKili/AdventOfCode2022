package fourteen;

import util.DailyTask;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TaskFourteen extends DailyTask<List<TaskFourteen.Formation>, List<TaskFourteen.Formation>> {

    public TaskFourteen() {
        super(InputFourteen.TASK_ONE, InputFourteen.TASK_TWO);
    }

    @Override
    public String solveTaskOne(List<Formation> formations) {
        int startX = 500;
        int startY = 0;
        int minX;int maxX;int minY;int maxY;

        formations.sort(Comparator.comparingInt(a -> a.minX));
        minX = Math.min(formations.get(0).minX, startX);
        formations.sort(Comparator.comparingInt(a -> a.maxX));
        maxX = Math.max(formations.get(formations.size() - 1).maxX, startX);
        formations.sort(Comparator.comparingInt(a -> a.minY));
        minY = Math.min(formations.get(0).minY, startY);
        formations.sort(Comparator.comparingInt(a -> a.maxY));
        maxY = Math.max(formations.get(formations.size() - 1).maxY, startY);

        int[][] cave = new int[maxY - minY + 1][maxX - minY + 1];

        formations.forEach(formation -> {
            formation.allCoordinates.forEach(c -> {
                cave[c.y - minY][c.x - minX] = 1;
            });
        });

        int count = 0;

        while (addSandCorn(cave, 500 - minX, 0)) {
            count++;
        }

        cave[0][500 - minX] = 2;

        return count + "";
    }

    public static boolean addSandCorn(int[][] cave, int x, int y) {
        if (x - 1 < 0 || x + 1 >= cave[0].length) {
            return false;
        }

        if (y < 0 || y + 1 >= cave.length) {
            return false;
        }

        int i = cave[y + 1][x];

        if (cave[y][x] == 0 && (i == 1 || i == 3)) {
            if (cave[y + 1][x - 1] == 1 || cave[y + 1][x - 1] == 3) {
                if (x + 1 < cave[y + 1].length && (cave[y + 1][x + 1] == 1 || cave[y + 1][x + 1] == 3)) {
                    cave[y][x] = 3;
                    return true;
                } else {
                    if (x + 1 < cave[y + 1].length) {
                        return addSandCorn(cave, x + 1, y + 1);
                    } else {
                        return false;
                    }
                }
            } else {
                return addSandCorn(cave, x - 1, y + 1);
            }
        } else if (cave[y][x] == 0) {
            return addSandCorn(cave, x, y + 1);
        } else {
            return false;
        }
    }

    @Override
    public List<Formation> parseInputOne(List<String> input) {
        return input.stream().map(s -> {
            String[] coords = s.split(" -> ");

            Formation f = new Formation();

            for (String c : coords) {
                String[] xy = c.split(",");
                f.traceLine(new Coordinate(Integer.parseInt(xy[0]), Integer.parseInt(xy[1])));
            }

            f.calcMinMax();
            return f;
        }).collect(Collectors.toList());
    }

    @Override
    public String solveTaskTwo(List<Formation> formations) {
        int startX = 500;
        int startY = 0;
        int minX;int maxX;int minY;int maxY;

        formations.sort(Comparator.comparingInt(a -> a.minX));
        minX = Math.min(formations.get(0).minX, startX);
        formations.sort(Comparator.comparingInt(a -> a.maxX));
        maxX = Math.max(formations.get(formations.size() - 1).maxX, startX);
        formations.sort(Comparator.comparingInt(a -> a.minY));
        minY = Math.min(formations.get(0).minY, startY);
        formations.sort(Comparator.comparingInt(a -> a.maxY));
        maxY = Math.max(formations.get(formations.size() - 1).maxY, startY);

        maxY += 2;
        int xModifier = (maxY - minY + 2);
        maxX += 2 * xModifier;

        int[][] cave = new int[maxY - minY + 1][maxX - minY + 1];

        formations.forEach(formation -> {
            formation.allCoordinates.forEach(c -> {
                cave[c.y - minY][(c.x - minX) + xModifier] = 1;
            });
        });

        for (int i = 0; i < (maxX - minX + 1); i++) {
            cave[maxY - minY][i] = 1;
        }

        int count = 0;

        while (addSandCorn(cave, 500 - minX + xModifier, 0)) {
            count++;
        }

        cave[0][500 - minX] = 2;

        return count + "";
    }

    @Override
    public List<Formation> parseInputTwo(List<String> input) {
        return parseInputOne(input);
    }

    static class Formation {
        Coordinate lastCoordinate;
        ArrayList<Coordinate> allCoordinates;
        int minX; int maxX; int minY; int maxY;

        public Formation() {
            this.allCoordinates = new ArrayList<>();
        }

        public void traceLine(Coordinate next) {
            if (lastCoordinate == null) {
                lastCoordinate = next;
                allCoordinates.add(next);
            } else {
                allCoordinates.addAll(lastCoordinate.traceTo(next));
                lastCoordinate = next;
            }
        }

        public void calcMinMax() {
            allCoordinates.sort(Comparator.comparingInt(a -> a.x));

            minX = allCoordinates.get(0).x;
            maxX = allCoordinates.get(allCoordinates.size() - 1).x;

            allCoordinates.sort(Comparator.comparingInt(a -> a.y));
            minY = allCoordinates.get(0).y;
            maxY = allCoordinates.get(allCoordinates.size() - 1).y;
        }
    }

    static class Coordinate {
        private final int x;
        private final int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public List<Coordinate> traceTo(Coordinate to) {
            List<Coordinate> toReturn = new ArrayList<>();

            if (this.x > to.x) {
                for (int i = 0; this.x >= to.x + i; i++) {
                    toReturn.add(new Coordinate(this.x - i, this.y));
                }
            } else if (this.x < to.x) {
                for (int i = 0; this.x + i <= to.x; i++) {
                    toReturn.add(new Coordinate(this.x + i, this.y));
                }
            } else if (this.y > to.y) {
                for (int i = 0; this.y >= to.y + i; i++) {
                    toReturn.add(new Coordinate(this.x, this.y - i));
                }
            } else if (this.y < to.y) {
                for (int i = 0; this.y + i <= to.y; i++) {
                    toReturn.add(new Coordinate(this.x, this.y + i));
                }
            }

            return toReturn;
        }
    }
}
