import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RMCode {

    public static final int q = 2;

    private int m = 0;
    private int r = 0;

    private RMMatrix generatorMatrix;

    public RMCode(int r, int m) {
        this.m = m;
        this.r = r;
        generatorMatrix = new RMMatrix(r, m);
    }

    public RMMatrix getGeneratorMatrix() {
        return generatorMatrix;
    }

    public SparseMatrix encode(List<List<Integer>> data) {
        if (generatorMatrix == null) {
            throw new IllegalStateException();
        }
        if (data == null) {
            throw new IllegalArgumentException();
        }

        SparseMatrix result = new SparseMatrix();
        for (int i = 0; i < data.size(); i ++) {
            if (data.get(i).size() != generatorMatrix.getHeight()) {
                throw new IllegalArgumentException(i + " word length does not match generator matrix height");
            }
            List<Integer> codeWord = new ArrayList<Integer>(LinearSpace.multiply(generatorMatrix.getRow(0), data.get(i).get(0)));
            for (int j = 1; j < generatorMatrix.getHeight(); j ++) {
                codeWord = LinearSpace.add(codeWord, LinearSpace.multiply(generatorMatrix.getRow(j), data.get(i).get(j)));
            }
            result.addRow(codeWord);
        }

        return result;
    }

    public SparseMatrix decode(SparseMatrix data) {
        if (generatorMatrix == null) {
            throw new IllegalStateException();
        }
        if (data == null) {
            throw new IllegalArgumentException();
        }

        SparseMatrix decoded = new SparseMatrix();
        for (int i = 0; i < data.getHeight(); i ++) {
            // for each received encoded word
            List<Integer> coefficient = new ArrayList<Integer>();
            List<Integer> My = new ArrayList<Integer>(Collections.nCopies(generatorMatrix.getWidth(), 0));
            for (int j = generatorMatrix.getHeight() - 1; j > 0; j --) {
                // for each non-first row of generator matrix
                SparseMatrix characteristic = generatorMatrix.getCharacteristicVectors(j);
                List<Integer> dotProductValues = new ArrayList<Integer>();
                for (int k = 0; k < characteristic.getHeight(); k ++) {
                    dotProductValues.add(LinearSpace.dotProduct(characteristic.getRow(k), data.getRow(i)));
                }
                coefficient.add(Util.getMajorBit(dotProductValues));
                // multiply each coefficient by its corresponding row and add the resulting vectors
                My = LinearSpace.add(My, LinearSpace.multiply(generatorMatrix.getRow(j), coefficient.get(coefficient.size() - 1)));
            }
            coefficient.add(Util.getMajorBit(LinearSpace.add(My, data.getRow(i))));
            Collections.reverse(coefficient);
            decoded.addRow(coefficient);
        }

        return decoded;
    }

    public static void main(String[] args) {
        int m = 4;
        int r = 1;

        RMCode code = new RMCode(r, m);
        code.getGeneratorMatrix().print();
        System.out.println();
        code.getGeneratorMatrix().getCombination().print();
        System.out.println();
//        code.getGeneratorMatrix().getCharacteristicVectors(3).print();
//        System.out.println();

        List<List<Integer>> data = new ArrayList<List<Integer>>();
        data.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{0, 1, 1, 0, 1})));
        code.encode(data).print();
        SparseMatrix encoded = new SparseMatrix();
        encoded.addRow(new ArrayList<Integer>(Arrays.asList(new Integer[]{1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0})));
        encoded.print();
        code.decode(encoded).print();

//        List<List<Integer>> text = new ArrayList<List<Integer>>();
//        text.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{1,0,1,0,1,1,0})));
//        System.out.println();
//        print(code.encode(text));

    }
}
