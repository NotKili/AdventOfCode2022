package eight;

import util.DailyTask;

import java.util.List;

public class TaskEight extends DailyTask<int[][], int[][]> {
    public TaskEight() {
        super(InputEight.TASK_ONE, InputEight.TASK_TWO);
    }

    @Override
    public String solveTaskOne(int[][] treeGrid) {
        int maxX = treeGrid[0].length - 1;
        int maxY = treeGrid.length - 1;

        int visibleTrees = 0;

        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                if (isVisible(x, y, treeGrid)) {
                    visibleTrees++;
                }
            }
        }

        return visibleTrees + "";
    }

    private static boolean isVisible(int treeX, int treeY, int[][] grid) {
        int xMax = grid[treeY].length - 1;
        int yMax = grid.length - 1;

        if (treeX == 0 || treeY == 0) {
            return true;
        }

        if (treeX == xMax || treeY == yMax) {
            return true;
        }

        int treeHight = grid[treeY][treeX];
        boolean isVisible = true;

        // 0 -> treeX
        for (int i = 0; i < treeX; i++) {
            if (grid[treeY][i] >= treeHight) {
                isVisible = false;
                break;
            }
        }

        if (isVisible) {
            return true;
        }

        isVisible = true;
        // xMax -> treeX
        for (int i = xMax; i > treeX; i--) {
            if (grid[treeY][i] >= treeHight) {
                isVisible = false;
                break;
            }
        }
        if (isVisible) {
            return true;
        }

        // 0 -> treeY
        isVisible = true;
        for (int i = 0; i < treeY; i++) {
            if (grid[i][treeX] >= treeHight) {
                isVisible = false;
                break;
            }
        }
        if (isVisible) {
            return true;
        }

        // yMax -> treeY
        isVisible = true;
        for (int i = yMax; i > treeY; i--) {
            if (grid[i][treeX] >= treeHight) {
                isVisible = false;
                break;
            }
        }

        return isVisible;
    }

    @Override
    public int[][] parseInputOne(List<String> input) {
        return input.stream().map(s -> s.split("")).map(array -> {
            int len = array.length;
            int[] arr = new int[len];

            for (int i = 0; i < len; i++) {
                arr[i] = Integer.parseInt(array[i]);
            }

            return arr;
        }).toList().toArray(new int[][] {});
    }

    @Override
    public String solveTaskTwo(int[][] treeGrid) {
        int xMax = treeGrid[0].length - 1;
        int yMax = treeGrid.length - 1;

        int maxDistance = 0;

        for (int y = 0; y < yMax; y++) {
            for (int x = 0; x < xMax; x++) {
                int dist = calcScenicDistance(x, y, treeGrid);

                if (dist > maxDistance) {
                    maxDistance = dist;
                }
            }
        }

        return maxDistance + "";
    }

    private static int calcScenicDistance(int treeX, int treeY, int[][] grid) {
        if (treeX == 0 || treeY == 0) {
            return 0;
        }

        int xMax = grid[treeY].length - 1;
        int yMax = grid.length - 1;

        if (treeX == xMax || treeY == yMax) {
            return 0;
        }

        int height = grid[treeY][treeX];
        int a = -1, b = -1, c = -1, d = -1;

        // o -> x
        for (int i = treeX - 1; i >= 0; i--) {
            if (grid[treeY][i] >= height) {
                a = treeX - i;
                break;
            }
        }
        if (a == -1) {
            a = treeX;
        }

        // xmax - x
        for (int i = treeX + 1; i <= xMax; i++) {
            if (grid[treeY][i] >= height) {
                b = i - treeX;
                break;
            }
        }
        if (b == -1) {
            b = xMax - treeX;
        }

        // 0 - y
        for (int i = treeY - 1; i >= 0; i--) {
            if (grid[i][treeX] >= height) {
                c = treeY - i;
                break;
            }
        }
        if (c == -1) {
            c = treeY;
        }

        // y - ymax
        for (int i = treeY + 1; i <= yMax; i++) {
            if (grid[i][treeX] >= height) {
                d = i - treeY;
                break;
            }
        }
        if (d == -1) {
            d = yMax - treeY;
        }

        return a * b * c * d;
    }

    @Override
    public int[][] parseInputTwo(List<String> input) {
        return parseInputOne(input);
    }
}
