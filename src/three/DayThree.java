package three;

import one.InputOne;
import one.TaskOne;
import util.DailyTask;

public class DayThree {
    public static void main(String[] args) {
        // Instantiate Task object here
        DailyTask<?, ?> task = new TaskThree();

        System.out.println("Result of the first task: " + task.runTaskOne());
        System.out.println("Result of the second task: " + task.runTaskTwo());
    }
}
