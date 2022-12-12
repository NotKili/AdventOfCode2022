package twelve;

import util.DailyTask;

public class DayTwelve {

    public static void main(String[] args) {
        DailyTask<?, ?> task = new TaskTwelve();

        System.out.println("Result of the first task: " + task.runTaskOne());
        System.out.println("Result of the second task: " + task.runTaskTwo());
    }
}