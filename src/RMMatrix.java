import java.util.*;

public class RMMatrix extends SparseMatrix {

    SparseMatrix combination = new SparseMatrix();

    private int width;

    public RMMatrix() { }

    public int getWidth() {
        return width;
    }

    public SparseMatrix getCombination() {
        return combination;
    }

    public RMMatrix(int r, int m) {
        if (m < 2 || r > m || r < 0) {
            throw new IllegalArgumentException();
        }
        width = (int)Math.pow(RMCode.q, m);

        // first row of n ones
        addRow(Collections.nCopies(width, 1));
        combination.addRow(Collections.nCopies(1, 0));

        // m rows which columns represent value sequences
        if (r == 0) {
            return;
        }
        for (int i = width / RMCode.q; i >= 1; i /= RMCode.q) {
            // (i = n / q; i >= 1; i /= q) or (i = 1; i < n; i *= q)
            List<Integer> row = new ArrayList<Integer>(width);
            for (int j = 0; j < width; j ++) {
                row.add((j / i + 1) % RMCode.q);
                // (j / i + 1) % q or (j / i) % q
            }
            addRow(row);
            combination.addRow(Collections.nCopies(1, combination.getHeight()));
        }

        // add multiplication matrix based on combination of rows (1, m)
        for (int i = 2; i <= r; i ++) {
            List<List<Integer>> combinationMI  = Util.combination(m, i);
            for (int j = 0; j < combinationMI.size(); j ++) {
                List<Integer> multiplication = new ArrayList<Integer>(getRow(combinationMI.get(j).get(0)));
                for (int k = 1; k < i; k ++) {
                    multiplication = LinearSpace.multiply(multiplication, getRow(combinationMI.get(j).get(k)));
                }
                addRow(multiplication);
            }
            combination.append(combinationMI);
        }
    }

    public SparseMatrix getCharacteristicVectors(int index) {
        SparseMatrix result = new SparseMatrix();


        return null;
    }
}
