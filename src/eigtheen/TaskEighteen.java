package eigtheen;

import util.DailyTask;

import java.util.*;
import java.util.stream.Collectors;

public class TaskEighteen extends DailyTask<Set<TaskEighteen.Coordinate3D>, Set<TaskEighteen.Coordinate3D>> {
    public TaskEighteen() {
        super(InputEighteen.INPUT_ONE, InputEighteen.INPUT_ONE);
    }

    @Override
    public String solveTaskOne(Set<Coordinate3D> coordinates) {
        int totalExposed = 0;

        for (Coordinate3D coordinate : coordinates) {
            int exposedSides = 6;

            for (Coordinate3D neighborCoordinate : coordinate.getAdjacent()) {
                exposedSides -= coordinates.contains(neighborCoordinate) ? 1 : 0;
            }

            totalExposed += exposedSides;
        }

        return totalExposed + "";
    }

    @Override
    public Set<Coordinate3D> parseInputOne(List<String> input) {
        return input.stream().map(s -> new Coordinate3D(s.split(","))).collect(Collectors.toSet());
    }

    @Override
    public String solveTaskTwo(Set<Coordinate3D> coordinates) {
        HashSet<Coordinate3D> trappedAir = Coordinate3D.findTrappedAir(new ArrayList<>(coordinates));

        // Create union of all non - air coordinates & the trapped air, allows direct comparison of neighbor status
        HashSet<Coordinate3D> unionOf = new HashSet<>(coordinates);
        unionOf.addAll(trappedAir);

        int totalExposed = 0;

        for (Coordinate3D coordinate : coordinates) {
            int exposedSides = 6;

            for (Coordinate3D neighborCoordinates : coordinate.getAdjacent()) {
                exposedSides -= unionOf.contains(neighborCoordinates) ? 1 : 0;
            }

            totalExposed += exposedSides;
        }

        return totalExposed + "";
    }

    @Override
    public Set<Coordinate3D> parseInputTwo(List<String> input) {
        return parseInputOne(input);
    }

    static class Coordinate3D {
        private final int x;
        private final int y;
        private final int z;

        public Coordinate3D(String[] s) {
            x = Integer.parseInt(s[0]);
            y = Integer.parseInt(s[1]);
            z = Integer.parseInt(s[2]);
        }

        public Coordinate3D(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Coordinate3D add(int[] toAdd) {
            return new Coordinate3D(x + toAdd[0], y + toAdd[1], z + toAdd[2]);
        }

        public static boolean isOutOfBound(Coordinate3D c, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
            if (c.x < minX || c.x > maxX) {
                return true;
            }

            if (c.y < minY || c.y > maxY) {
                return true;
            }

            return c.z < minZ || c.z > maxZ;
        }

        public static HashSet<Coordinate3D> findTrappedAir(List<Coordinate3D> coords) {
            HashSet<Coordinate3D> trappedAir = new HashSet<>();
            HashSet<Coordinate3D> airCoords = new HashSet<>();
            HashSet<Coordinate3D> nonAirCoords = new HashSet<>(coords);

            // Calculate min & max of x,y,z to know the boundaries of our region
            coords.sort(Comparator.comparing(Coordinate3D::getX));
            int minX = coords.get(0).x;
            int maxX = coords.get(coords.size() - 1).x;

            coords.sort(Comparator.comparing(Coordinate3D::getY));
            int minY = coords.get(0).y;
            int maxY = coords.get(coords.size() - 1).y;

            coords.sort(Comparator.comparing(Coordinate3D::getZ));
            int minZ = coords.get(0).z;
            int maxZ = coords.get(coords.size() - 1).z;

            // Compute & add all air spots within our region to the air coordinate set (Used for iteration later)
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        Coordinate3D c = new Coordinate3D(x, y, z);
                        if (!nonAirCoords.contains(c)) {
                            airCoords.add(c);
                        }
                    }
                }
            }

            HashSet<Coordinate3D> allCheckedCoords = new HashSet<>();

            // Each loop calculates all adjacent air coordinates for the given air coordinate & determines if they are fully enclosed by non-air coordinates
            for (Coordinate3D c : airCoords) {
                // Air - Coordinate has already been checked for being part of a pocket, it can be discarded (optimization)
                if (allCheckedCoords.contains(c)) {
                    continue;
                }

                // Set of all the coordinates checked within this loop, used to append the coordinates to the result set after
                HashSet<Coordinate3D> checkedCoords = new HashSet<>();

                // Queue that contains all air coordinates that need to have their neighbours checked
                Queue<Coordinate3D> toCheck = new LinkedList<>();

                // Add starting element
                toCheck.add(c);
                checkedCoords.add(c);

                boolean isPocket = true;
                outer:
                while (!toCheck.isEmpty()) {
                    Coordinate3D tc = toCheck.poll();

                    for (Coordinate3D adj : tc.getAdjacent()) {
                        // Neighbor Coordinate is out of boundary ( === no pocket
                        if (isOutOfBound(adj, minX, maxX, minY, maxY, minZ, maxZ)) {
                            isPocket = false;
                            break outer;
                        }

                        // Neighbor Coordinate has already been checked
                        if (checkedCoords.contains(adj)) {
                            continue;
                        }

                        // Neighbor Coordinate is not an air coordinate
                        if (nonAirCoords.contains(adj)) {
                            continue;
                        }

                        // Neighbor Coordinate is air coordinate
                        // Enqueue it to have its neighbors checked and add it to the pocket set
                        toCheck.add(adj);
                        checkedCoords.add(adj);
                    }
                }


                // Coordinate Space is valid pocket, add all coordinates from the space to the output set
                if (isPocket) {
                    trappedAir.addAll(checkedCoords);
                }

                // Mark all coordinates from the space as checked to prevent useless recalculations
                allCheckedCoords.addAll(checkedCoords);
            }

            return trappedAir;
        }

        public List<Coordinate3D> getAdjacent() {
            List<Coordinate3D> adj = new ArrayList<>(6);

            for (int[] is : new int[][] {new int[] {1, 0, 0}, new int[] {-1, 0, 0}, new int[] {0, 1, 0}, new int[] {0, -1, 0}, new int[] {0, 0, 1}, new int[] {0, 0, -1}}) {
                adj.add(this.add(is));
            }

            return adj;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate3D that = (Coordinate3D) o;
            return x == that.x && y == that.y && z == that.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + "," + z + ")";
        }
    }
}
