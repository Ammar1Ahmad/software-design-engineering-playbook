import java.util.Map;
import java.util.TreeMap;

class SummaryRanges {

    private final TreeMap<Integer, Integer> map;

    public SummaryRanges() {
        map = new TreeMap<>();
    }

    public void addNum(int value) {

        if (map.containsKey(value)) {
            return;
        }

        Integer left = map.floorKey(value);
        Integer right = map.ceilingKey(value);

        int start = value;
        int end = value;

        if (left != null && map.get(left) + 1 >= value) {
            start = left;
            end = Math.max(end, map.get(left));
            map.remove(left);
        }

        if (right != null && right - 1 <= value) {
            end = Math.max(end, map.get(right));
            map.remove(right);
        }

        map.put(start, end);
    }

    public int[][] getIntervals() {

        int[][] result = new int[map.size()][2];

        int index = 0;

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            result[index][0] = entry.getKey();
            result[index][1] = entry.getValue();
            index++;
        }

        return result;
    }
}
