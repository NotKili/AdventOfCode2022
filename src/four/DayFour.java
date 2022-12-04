package four;

import util.DailyTask;

public class DayFour {
        public static void main(String[] args) {
        // Instantiate Task object here
        DailyTask<?, ?> task = new TaskFour(InputFour.TASK_ONE, InputFour.TASK_TWO);

        System.out.println("Result of the first task: " + task.runTaskOne());
        System.out.println("Result of the second task: " + task.runTaskTwo());
    }
}
