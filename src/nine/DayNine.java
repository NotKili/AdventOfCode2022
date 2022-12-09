package nine;

import four.InputFour;
import four.TaskFour;
import util.DailyTask;

import java.util.*;

public class DayNine {

    public static void main(String[] args) {
        // Instantiate Task object here
        DailyTask<?, ?> task = new TaskNine();

        System.out.println("Result of the first task: " + task.runTaskOne());
        System.out.println("Result of the second task: " + task.runTaskTwo());
    }
}
