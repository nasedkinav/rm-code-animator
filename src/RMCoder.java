import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RMCoder {

    public static final int q = 2;

    private RMMatrix generatorMatrix;

    public RMCoder(int r, int m) {
        generatorMatrix = new RMMatrix(r, m);
    }

    public RMMatrix getGeneratorMatrix() {
        return generatorMatrix;
    }

    public SparseMatrix encode(SparseMatrix toEncode) {
        if (generatorMatrix == null) {
            throw new IllegalStateException();
        }
        if (toEncode == null) {
            throw new IllegalArgumentException();
        }

        SparseMatrix result = new SparseMatrix();
        for (int i = 0; i < toEncode.getHeight(); i ++) {
            if (toEncode.getRow(i).size() != generatorMatrix.getHeight()) {
                throw new IllegalArgumentException(i + " word length does not match generator matrix height");
            }
            List<Integer> codeWord = new ArrayList<Integer>(LinearSpace.multiply(generatorMatrix.getRow(0), toEncode.getRow(i).get(0)));
            for (int j = 1; j < generatorMatrix.getHeight(); j ++) {
                codeWord = LinearSpace.add(codeWord, LinearSpace.multiply(generatorMatrix.getRow(j), toEncode.getRow(i).get(j)));
            }
            result.addRow(codeWord);
        }

        return result;
    }

    public SparseMatrix decode(SparseMatrix toDecode) {
        if (generatorMatrix == null) {
            throw new IllegalStateException();
        }
        if (toDecode == null) {
            throw new IllegalArgumentException();
        }

        SparseMatrix decoded = new SparseMatrix();
        for (int i = 0; i < toDecode.getHeight(); i ++) {
            // for each received encoded word
            List<Integer> coefficient = new ArrayList<Integer>();
            List<Integer> My = new ArrayList<Integer>(Collections.nCopies(generatorMatrix.getWidth(), 0));
            for (int j = generatorMatrix.getHeight() - 1; j > 0; j --) {
                // for each non-first row of generator matrix
                SparseMatrix characteristic = generatorMatrix.getCharacteristicVectors(j);
                List<Integer> dotProductValues = new ArrayList<Integer>();
                for (int k = 0; k < characteristic.getHeight(); k ++) {
                    dotProductValues.add(LinearSpace.dotProduct(characteristic.getRow(k), toDecode.getRow(i)));
                }
                coefficient.add(Util.getMajorBit(dotProductValues));
                // multiply each coefficient by its corresponding row and add the resulting vectors
                My = LinearSpace.add(My, LinearSpace.multiply(generatorMatrix.getRow(j), coefficient.get(coefficient.size() - 1)));
            }
            // TODO: reformat step 3
            coefficient.add(Util.getMajorBit(LinearSpace.add(My, toDecode.getRow(i))));
            Collections.reverse(coefficient);
            decoded.addRow(coefficient);
        }

        return decoded;
    }

    public static void main(String[] args) {
        int m = 3;
        int r = 1;

        RMCoder code = new RMCoder(r, m);
        code.getGeneratorMatrix().print();
        System.out.println();
        code.getGeneratorMatrix().getCombination().print();
        System.out.println();
//        code.getGeneratorMatrix().getCharacteristicVectors(3).print();
//        System.out.println();

        System.out.println("Max error count: " + ((int) Math.pow(2, m - r - 1) - 1));
        SparseMatrix toencode = new SparseMatrix();
        toencode.addRow(new ArrayList<Integer>(Arrays.asList(new Integer[]{0, 1, 1, 0, 1, 0, 1})));
        toencode.print();
        code.encode(toencode).print();
        SparseMatrix encoded = new SparseMatrix();
        encoded.addRow(new ArrayList<Integer>(Arrays.asList(new Integer[]{0, 1, 1, 1, 0, 1, 0, 0})));
        encoded.print();
        code.decode(encoded).print();

//        List<List<Integer>> text = new ArrayList<List<Integer>>();
//        text.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{1,0,1,0,1,1,0})));
//        System.out.println();
//        print(code.encode(text));
        List<Integer> a = new ArrayList<Integer>();
        Integer d = 5;
        a.add(d);
        List<Integer> b = new ArrayList<Integer>(a);
        a.set(0, 1);
        System.out.println();
    }
}
