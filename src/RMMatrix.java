import java.util.*;

public class RMMatrix extends SparseMatrix {

    List<List<Integer>> combination = new ArrayList<List<Integer>>();

    private int width;
    private int m;

    public RMMatrix(int r, int m) {
        if (m < 2 || r >= m || r < 0) {
            throw new IllegalArgumentException();
        }
        this.m = m;
        width = (int)Math.pow(2, m);

        // first row of n ones
        addRow(Collections.nCopies(width, true));
        combination.add(Collections.nCopies(1, 0));

        // m rows which columns represent value sequences
        if (r == 0) {
            return;
        }
        List<Boolean> row = new ArrayList<Boolean>(width);
        for (int i = width / 2; i >= 1; i /= 2) {
            // (i = n / q; i >= 1; i /= q) or (i = 1; i < n; i *= q)
            for (int j = 0; j < width; j ++) {
                row.add(((j / i) % 2) != 0);
                // (j / i + 1) % q or (j / i) % q
            }
            addRow(row);
            row.clear();
            combination.add(Collections.nCopies(1, combination.size()));
        }

        // add multiplication matrix based on combination of rows (1, m)
        for (int i = 2; i <= r; i ++) {
            List<List<Integer>> combinationMI = Util.combination(m, i);
            for (int j = 0; j < combinationMI.size(); j ++) {
                List<Boolean> multiplication = new ArrayList<Boolean>(getRow(combinationMI.get(j).get(0)));
                for (int k = 1; k < i; k ++) {
                    multiplication = BinaryFiniteField.multiply(multiplication, getRow(combinationMI.get(j).get(k)));
                }
                addRow(multiplication);
            }
            for (int j = 0; j < combinationMI.size(); j ++) {
                // append local combinationMI to combination
                combination.add(new ArrayList<Integer>(combinationMI.get(j)));
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public List<List<Integer>> getCombination() {
        return combination;
    }

    public SparseMatrix getCharacteristicVectors(int index) {
        if (index < 1) {
            throw new IllegalArgumentException();
        }
        SparseMatrix result = new SparseMatrix();

        // get monomial index which are not involved in a row combination
        Set<Integer> combinationSet = new HashSet<Integer>(combination.get(index));
        List<Integer> monomialIndex = new ArrayList<Integer>();
        for (int i = 1; i <= m; i ++) {
            if (! combinationSet.contains(i)) {
                monomialIndex.add(i);
            }
        }

        // get combination of bool function inputs
        SparseMatrix monomialCombination = new SparseMatrix();
        List<Boolean> row = new ArrayList<Boolean>(width);
        final int variety = (int)Math.pow(2, monomialIndex.size());
        for (int i = variety / 2; i >= 1; i /= 2) {
            for (int j = 0; j < variety; j ++) {
                row.add((j / i % 2) != 0);
            }
            monomialCombination.addRow(row);
            row.clear();
        }

        // produce combination of characteristic vectors
        for (int i = 0; i < variety; i ++) {
            List<Boolean> inputs = monomialCombination.getColumn(i);
            List<Boolean> multiplication = new ArrayList<Boolean>(
                    !inputs.get(0) ? getRow(monomialIndex.get(0)) : BinaryFiniteField.invert(getRow(monomialIndex.get(0))));
            for (int j = 1; j < inputs.size(); j ++) {
                multiplication = BinaryFiniteField.multiply(multiplication,
                        !inputs.get(j) ? getRow(monomialIndex.get(j)) : BinaryFiniteField.invert(getRow(monomialIndex.get(j))));
            }
            result.addRow(multiplication);
        }

        return result;
    }
}
