package sixteen;

import util.DailyTask;

public class DaySixteen {
    public static void main(String[] args) {
        DailyTask<?, ?> task = new TaskSixteen();

        System.out.println("Result of the first task: " + task.runTaskOne());
        System.out.println("Result of the second task: " + task.runTaskTwo());
    }
}
