import java.util.ArrayList;
import java.util.List;

public class Util {

    public static List<List<Integer>> combination(int n, int k) {
        if (k > n) {
            throw new IllegalArgumentException();
        }
        if (k < 1 || n < 1) {
            throw new IllegalArgumentException();
        }

        List<List<Integer>> result = new ArrayList<List<Integer>>();
        List<Integer> combination = new ArrayList<Integer>(k);
        for (int i = 1; i <= k; i ++) {
            combination.add(i);
        }
        if (k == n) {
            result.add(new ArrayList<Integer>(combination));
            return result;
        }

        int p = k;
        while (p >= 1) {
            result.add(new ArrayList<Integer>(combination));
            if (combination.get(k - 1) == n) {
                p --;
            } else {
                p = k;
            }
            if (p >= 1) {
                for (int i = k; i >= p; i --) {
                    combination.set(i - 1, combination.get(p - 1) + i - p + 1);
                }
            }
        }

        return result;
    }

    public static Integer getMajorBit(List<Integer> a) {
        int zero = 0;
        int ones = 0;

        for (int i = 0; i < a.size(); i ++) {
            if (a.get(i) == 0) {
                zero ++;
            } else {
                ones ++;
            }
        }

        return zero > ones ? 0 : 1;
    }
}
