package fourteen;

import fifteen.TaskFifteen;
import util.DailyTask;

public class DayFourteen {
    public static void main(String[] args) {
        // Instantiate Task object here
        DailyTask<?, ?> task = new TaskFourteen();

        System.out.println("Result of the first task: " + task.runTaskOne());
        System.out.println("Result of the second task: " + task.runTaskTwo());
    }
}
