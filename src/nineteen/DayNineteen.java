package nineteen;

import util.DailyTask;

public class DayNineteen {
    public static void main(String[] args) {
        // Instantiate Task object here
        DailyTask<?, ?> task = new TaskNineteen();

        System.out.println("Result of the first task: " + task.runTaskOne());
        System.out.println("Result of the second task: " + task.runTaskTwo());
    }
}
