package ten;

import util.DailyTask;

public class DayTen {

    public static void main(String[] args) {
        // Instantiate Task object here
        DailyTask<?, ?> task = new TaskTen();

        System.out.println("Result of the first task: " + task.runTaskOne());
        System.out.println("Result of the second task: " + task.runTaskTwo());
    }
}
