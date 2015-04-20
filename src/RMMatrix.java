import javax.sound.sampled.Line;
import java.util.*;

public class RMMatrix extends SparseMatrix {

    SparseMatrix combination = new SparseMatrix();

    private int width;
    private int m;

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
        this.m = m;
        width = (int)Math.pow(RMCode.q, m);

        // first row of n ones
        addRow(Collections.nCopies(width, 1));
        combination.addRow(Collections.nCopies(1, 0));

        // m rows which columns represent value sequences
        if (r == 0) {
            return;
        }
        List<Integer> row = new ArrayList<Integer>(width);
        for (int i = width / RMCode.q; i >= 1; i /= RMCode.q) {
            // (i = n / q; i >= 1; i /= q) or (i = 1; i < n; i *= q)
            for (int j = 0; j < width; j ++) {
                row.add((j / i + 1) % RMCode.q);
                // (j / i + 1) % q or (j / i) % q
            }
            addRow(row);
            row.clear();
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
        if (index < 1) {
            throw new IllegalArgumentException();
        }
        SparseMatrix result = new SparseMatrix();

        Set<Integer> combinationSet = new HashSet<Integer>(combination.getRow(index));
        List<Integer> monomialIndexes = new ArrayList<Integer>();
        // get monomial indexes which are not involved in a row combination
        for (int i = 1; i <= m; i ++) {
            if (! combinationSet.contains(i)) {
                monomialIndexes.add(i);
            }
        }

        SparseMatrix monomialCombination = new SparseMatrix();
        List<Integer> row = new ArrayList<Integer>(width);
        // get combination of bool function inputs
        final int variety = (int)Math.pow(RMCode.q, monomialIndexes.size());
        for (int i = variety / RMCode.q; i >= 1; i /= RMCode.q) {
            for (int j = 0; j < variety; j ++) {
                row.add(j / i % RMCode.q);
            }
            monomialCombination.addRow(row);
            row.clear();
        }

        // produce combination of characteristic vectors
        for (int i = 0; i < variety; i ++) {
            List<Integer> inputs = monomialCombination.getCol(i);
            List<Integer> multiplication = new ArrayList<Integer>(
                    inputs.get(0) == 0 ? getRow(monomialIndexes.get(0)) : LinearSpace.invert(getRow(monomialIndexes.get(0))));
            for (int j = 1; j < inputs.size(); j ++) {
                multiplication = LinearSpace.multiply(multiplication,
                        inputs.get(j) == 0? getRow(monomialIndexes.get(j)) : LinearSpace.invert(getRow(monomialIndexes.get(j))));
            }
            result.addRow(multiplication);
        }

        return result;
    }
}
