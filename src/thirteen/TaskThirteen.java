package thirteen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import util.DailyTask;

import java.util.List;
import java.util.stream.Collectors;

// Requires Google GSON version 2.10 as dependency
public class TaskThirteen extends DailyTask<List<String>, List<String>> {
    private final Gson gson;

    public TaskThirteen() {
        super(InputThirteen.TASK_ONE, InputThirteen.TASK_TWO);
        gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    }

    @Override
    public String solveTaskOne(List<String> packets) {
        int totalCount = 0;
        for (int i = 0; i < packets.size(); i += 3) {
            JsonArray left = gson.fromJson(packets.get(i), JsonArray.class);
            JsonArray right = gson.fromJson(packets.get(i + 1), JsonArray.class);

            if (compare(left, right) == -1) {
                totalCount += ((i / 3) + 1);
            }
        }

        return totalCount + "";
    }

    @Override
    public List<String> parseInputOne(List<String> input) {
        return input;
    }

    @Override
    public String solveTaskTwo(List<String> packets) {
        String splitterA = "[[2]]";
        String splitterB = "[[6]]";

        packets.add(splitterA);
        packets.add(splitterB);

        boolean hasChanged = true;

        while (hasChanged) {
            hasChanged = false;
            for (int i = 0; i < packets.size() - 1; i++) {
                JsonArray left = gson.fromJson(packets.get(i), JsonArray.class);
                JsonArray right = gson.fromJson(packets.get(i + 1), JsonArray.class);

                if (compare(left, right) == 1) {
                    String tmp = packets.get(i);
                    packets.set(i, packets.get(i + 1));
                    packets.set(i + 1, tmp);
                    hasChanged = true;
                }
            }
        }

        int first = packets.indexOf(splitterA) + 1;
        int second = packets.indexOf(splitterB) + 1;

        return first * second + "";
    }

    @Override
    public List<String> parseInputTwo(List<String> input) {
        // Strip blank lines
        return input.stream().filter(s -> !s.isBlank()).collect(Collectors.toList());
    }

    private static int compare(JsonElement left, JsonElement right) {
        // 2 Integers, normal compare
        if (left.isJsonPrimitive() && right.isJsonPrimitive()) {
            return Integer.compare(left.getAsInt(), right.getAsInt());

        // One Integer, One Array, make array of size 1 for integer and compare again
        } else if (left.isJsonPrimitive()) {
            JsonArray leftArray = new JsonArray();
            leftArray.add(left.getAsInt());
            JsonArray rightArray = right.getAsJsonArray();
            return compare(leftArray, rightArray);

        // One Integer, One Array, make array of size 1 for integer and compare again
        } else if (right.isJsonPrimitive()) {
            JsonArray leftArray = left.getAsJsonArray();
            JsonArray rightArray = new JsonArray();
            rightArray.add(right.getAsInt());
            return compare(leftArray, rightArray);

        // Two lists, iterate over elements and compare recursive until elements are not equal
        } else {
            JsonArray leftArray = left.getAsJsonArray();
            JsonArray rightArray = right.getAsJsonArray();

            int maxSize = Math.min(leftArray.size(), rightArray.size());

            for (int i = 0; i < maxSize; i++) {
                int compare = compare(leftArray.get(i), rightArray.get(i));

                if (compare != 0) {
                    return compare;
                }
            }

            return Integer.compare(leftArray.size(), rightArray.size());
        }
    }

}
