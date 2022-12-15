package fifteen;

import util.DailyTask;

public class DayFifteen {
    public static void main(String[] args) {
        // Instantiate Task object here
        DailyTask<?, ?> task = new TaskFifteen();

        System.out.println("Result of the first task: " + task.runTaskOne());
        System.out.println("Result of the second task: " + task.runTaskTwo());
    }
}
