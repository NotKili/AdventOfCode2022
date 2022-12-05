package five;

import util.DailyTask;

public class DayFive {
    public static void main(String[] args) {
        // Instantiate Task object here
        DailyTask<?, ?> task = new TaskFive(InputFive.TASK_ONE, InputFive.TASK_TWO);

        System.out.println("Result of the first task: " + task.runTaskOne());
        System.out.println("Result of the second task: " + task.runTaskTwo());
    }
}
