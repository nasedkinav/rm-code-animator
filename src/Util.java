import java.util.ArrayList;
import java.util.List;

public class Util {

    public static SparseMatrix combination(int n, int k) {
        if (k > n) {
            throw new IllegalArgumentException();
        }
        if (k < 1 || n < 1) {
            throw new IllegalArgumentException();
        }

        SparseMatrix result = new SparseMatrix();
        List<Integer> combination = new ArrayList<Integer>(k);
        for (int i = 1; i <= k; i ++) {
            combination.add(i);
        }
        if (k == n) {
            result.addRow(new ArrayList<Integer>(combination));
            return result;
        }

        int p = k;
        while (p >= 1) {
            result.addRow(new ArrayList<Integer>(combination));
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

    public static int getMajorBit(List<Integer> a) {
        int zeroGreater = 0;

        for (Integer bit : a) {
            if (bit == 0) {
                zeroGreater++;
                continue;
            }
            zeroGreater--;
        }

        if (zeroGreater == 0) {
            throw new RuntimeException("Number of ones equals to number of zeros in dot product values");
        }
        return zeroGreater > 0 ? 0 : 1;
    }
}
