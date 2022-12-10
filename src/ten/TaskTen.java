package ten;

import util.DailyTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class TaskTen extends DailyTask<List<String>, List<String>> {
    public TaskTen() {
        super(InputTen.TASK_ONE, InputTen.TASK_TWO);
    }

    @Override
    public String solveTaskOne(List<String> parsed) {
        CPU cpu = new CPU();

        parsed.forEach(cmd -> {
            cpu.addTask(cmd);
            cpu.cycle();
        });

        return cpu.result + "";
    }

    @Override
    public List<String> parseInputOne(List<String> input) {
        return input;
    }

    @Override
    public String solveTaskTwo(List<String> parsed) {
        CPU cpu = new CPU();

        parsed.forEach(cmd -> {
            cpu.addTask(cmd);
            cpu.cycle();
        });

        return printCRT(cpu.crt);
    }

    @Override
    public List<String> parseInputTwo(List<String> input) {
        return input;
    }

    static class CPU {
        // Default
        int cycleCounter;
        int register;
        List<List<Consumer<CPU>>> tasks;

        // Task 1
        int result;

        // Task 2
        int xDrawAt;
        int yDrawAt;
        int[][] crt;

        public CPU() {
            this.cycleCounter = 0;
            this.register = 1;
            tasks = new ArrayList<>();
            tasks.add(new ArrayList<>());
            tasks.add(new ArrayList<>());

            // Task 1
            this.result = 0;

            // Task 2
            this.xDrawAt = 0;
            this.yDrawAt = 0;
            this.crt = new int[6][40];
        }


        public void addTask(String task) {
            if (task.startsWith("noop")) {
                addNOOP();
            } else if (task.startsWith("addx")) {
                addX(Integer.parseInt(task.substring(5)));
            }
        }
        private void addNOOP() {
            tasks.get(0).add(a -> {});
        }
        private void addX(int num) {
            tasks.get(1).add(a -> a.register += num);
        }


        public void cycle() {
            // Increment cycle
            cycleCounter++;

            // Calculate result
            calculateSignalStrength();

            // Draw on CRT
            drawOnCRT();

            // Run tasks
            runTasks();
        }
        private void calculateSignalStrength() {
            if ((cycleCounter - 20) % 40 == 0) {
                result += cycleCounter * register;
            }
        }
        private void drawOnCRT() {
            if (xDrawAt == register || xDrawAt - 1 == register || xDrawAt + 1 == register) {
                crt[yDrawAt][xDrawAt] = 1;
            }

            xDrawAt++;

            if (xDrawAt > 39) {
                xDrawAt = 0;
                yDrawAt++;
            }
        }
        private void runTasks() {
            tasks.get(0).forEach(t -> {
                t.accept(this);
            });

            // Clear
            tasks.get(0).clear();

            // Shift
            tasks.get(0).addAll(tasks.get(1));

            // Clear
            tasks.get(1).clear();

            if (!tasks.get(0).isEmpty()) {
                cycle();
            }
        }
    }

    private static String printCRT(int[][] crt) {
        StringBuilder sb = new StringBuilder();
        for (int[] ints : crt) {
            sb.append("\n");
            for (int i : ints) {
                sb.append(i == 1 ? "#" : ".");
            }
        }
        sb.append("\n");

        return sb.toString();
    }
}
