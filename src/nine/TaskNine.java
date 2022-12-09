package nine;

import util.DailyTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class TaskNine extends DailyTask<List<String>, List<String>> {
    public TaskNine() {
        super(InputNine.TASK_ONE, InputNine.TASK_ONE);
    }

    @Override
    public String solveTaskOne(List<String> parsed) {
        Rope rope = new Rope(0, 0, 1);

        for (String moveCmd : parsed) {
            rope.move(moveCmd);
        }

        return rope.getTailEndTracker().size() + "";
    }

    @Override
    public List<String> parseInputOne(List<String> input) {
        return input;
    }

    @Override
    public String solveTaskTwo(List<String> parsed) {
        Rope rope = new Rope(0, 0, 9);

        for (String moveCmd : parsed) {
            rope.move(moveCmd);
        }

        return rope.getTailEndTracker().size() + "";    }

    @Override
    public List<String> parseInputTwo(List<String> input) {
        return input;
    }

    static class Rope {
        private Tuple head;
        private final ArrayList<Tuple> tail;
        private final HashSet<Tuple> tailEndTracker;

        public Rope(int initX, int initY, int tailLen) {
            this.head = new Tuple(initX, initY);
            this.tail = new ArrayList<>(tailLen);
            this.tailEndTracker = new HashSet<>();

            for (int i = 0; i < tailLen; i++) {
                this.tail.add(new Tuple(initX, initY));
            }

            tailEndTracker.add(getLastTail());
        }

        public HashSet<Tuple> getTailEndTracker() {
            return tailEndTracker;
        }

        public void move(String moveCmd) {
            String[] spl = moveCmd.split(" ");

            int n = Integer.parseInt(spl[1]);

            switch (spl[0]) {
                case "R" -> moveRight(n);
                case "L" -> moveLeft(n);
                case "U" -> moveUp(n);
                case "D" -> moveDown(n);
            }
        }

        private void tailFollow() {
            for (int i = 0; i < tail.size(); i++) {
                Tuple becomeAdjacentTo;

                if (i == 0) {
                    becomeAdjacentTo = head;
                }  else {
                    becomeAdjacentTo = tail.get(i - 1);
                }

                tail.set(i, tail.get(i).becomeAdjacent(becomeAdjacentTo));
            }

            tailEndTracker.add(getLastTail());
        }

        private void moveRight(int steps) {
            for (int i = 0; i < steps; i++) {
                head = head.addX(1);
                tailFollow();
            }
        }

        private void moveLeft(int steps) {
            for (int i = 0; i < steps; i++) {
                head = head.addX(-1);
                tailFollow();
            }
        }

        private void moveUp(int steps) {
            for (int i = 0; i < steps; i++) {
                head = head.addY(-1);
                tailFollow();
            }
        }

        private void moveDown(int steps) {
            for (int i = 0; i < steps; i++) {
                head = head.addY(1);
                tailFollow();
            }
        }

        private Tuple getLastTail() {
            return this.tail.get(this.tail.size() - 1);
        }
    }

    static class Tuple {
        int x;
        int y;

        public Tuple(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Tuple becomeAdjacent(Tuple t) {
            // is already adjacent
            if (this.isAdjacent(t)) {
                return this;
            } else if (this.y == t.y) {
                return addX(this.x > t.x ? -1 : 1);
            } else if (this.x == t.x) {
                return addY(this.y > t.y ? -1 : 1);
            } else {
                if (this.x > t.x && this.y > t.y) {
                    return add(-1, -1);
                } else if (this.x < t.x && this.y < t.y) {
                    return add(1, 1);
                } else if (this.x > t.x) {
                    return add(-1, 1);
                } else {
                    return add(1, -1);
                }
            }
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        private boolean isAdjacent(Tuple t) {
            int x1 = this.x; int x2 = t.x;
            int y1 = this.y; int y2 = t.y;

            if (x1 == x2 && y1 == y2) {
                return true;
            } else if (x1 == (x2 - 1) && y1 == y2) {
                return true;
            } else if (x1 == (x2 + 1) && y1 == y2) {
                return true;
            } else if (x1 == x2 && y1 == (y2 - 1)) {
                return true;
            } else if (x1 == (x2 - 1) && y1 == (y2 - 1)) {
                return true;
            } else if (x1 == (x2 + 1) && y1 == (y2 - 1)) {
                return true;
            } else if (x1 == x2 && y1 == (y2 + 1)) {
                return true;
            } else if (x1 == (x2 - 1) && y1 == (y2 + 1)) {
                return true;
            } else return x1 == (x2 + 1) && y1 == (y2 + 1);
        }

        public Tuple add(int x, int y) {
            return new Tuple(this.x + x, this.y + y);
        }

        public Tuple addX(int x) {
            return new Tuple(this.x + x, this.y);
        }

        public Tuple addY(int y) {
            return new Tuple(this.x, this.y + y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple tuple = (Tuple) o;
            return x == tuple.x && y == tuple.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
