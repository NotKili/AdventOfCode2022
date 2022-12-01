package one;

import util.DailyTask;

public class DayOne {
        public static void main(String[] args) {
        // Instantiate Task object here
        DailyTask<?, ?> task = new TaskOne(InputOne.TASK_ONE, InputOne.TASK_TWO);

        System.out.println("Result of the first task: " + task.runTaskOne());
        System.out.println("Result of the second task: " + task.runTaskTwo());
    }
}
