package thirteen;

import ten.TaskTen;
import util.DailyTask;

public class DayThirteen {
    public static void main(String[] args) {
        DailyTask<?, ?> task = new TaskThirteen();

        System.out.println("Result of the first task: " + task.runTaskOne());
        System.out.println("Result of the second task: " + task.runTaskTwo());
    }
}
