package six;

import util.DailyTask;

public class DaySix {
    public static void main(String[] args) {
        // Instantiate Task object here
        DailyTask<?, ?> task = new TaskSix(InputSix.TASK_ONE, InputSix.TASK_TWO);

        System.out.println("Result of the first task: " + task.runTaskOne());
        System.out.println("Result of the second task: " + task.runTaskTwo());
    }
}
