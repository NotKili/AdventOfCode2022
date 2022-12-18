package eigtheen;

import util.DailyTask;

public class DayEighteen {
    public static void main(String[] args) {
        DailyTask<?, ?> task = new TaskEighteen();

        System.out.println("Result of the first task: " + task.runTaskOne());
        System.out.println("Result of the second task: " + task.runTaskTwo());
    }
}
