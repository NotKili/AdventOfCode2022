package eight;

import util.DailyTask;

public class DayEight {
    public static void main(String[] args) {
        DailyTask<?, ?> task = new TaskEight();

        System.out.println("Result of the first task: " + task.runTaskOne());
        System.out.println("Result of the second task: " + task.runTaskTwo());
    }
}
