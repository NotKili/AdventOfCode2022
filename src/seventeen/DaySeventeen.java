package seventeen;

import util.DailyTask;

public class DaySeventeen {
    public static void main(String[] args) {
        DailyTask<?, ?> task = new TaskSeventeen();

        System.out.println("Result of the first task: " + task.runTaskOne());
        System.out.println("Result of the second task: " + task.runTaskTwo());
    }
}
