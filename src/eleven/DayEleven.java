package eleven;

import util.DailyTask;

public class DayEleven {

    public static void main(String[] args) {
        DailyTask<?, ?> task = new TaskEleven();

        System.out.println("Result of the first task: " + task.runTaskOne());
        System.out.println("Result of the second task: " + task.runTaskTwo());
    }
}
