package util;

public abstract class Template {
    private static final String TASK_ONE = """
            """;

    private static final String TASK_TWO = """
            """;

    public static void main(String[] args) {
        // Instantiate Task object here
        DailyTask<?, ?> task = null;

        System.out.println("Result of the first task: " + task.runTaskOne());
        System.out.println("Result of the second task: " + task.runTaskTwo());
    }
}
