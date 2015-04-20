import java.util.ArrayList;
import java.util.Arrays;
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

    public static void main(String[] args) {
        int m = 2;
        int r = 2;

        RMCode code = new RMCode(r, m);
        code.getGeneratorMatrix().print();
        System.out.println();
        code.getGeneratorMatrix().getCombination().print();
        System.out.println();

        List<List<Integer>> data = new ArrayList<List<Integer>>();
        data.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{1, 0, 1, 0, 1, 1, 0})));
//        code.encode(data).print();

//        List<List<Integer>> text = new ArrayList<List<Integer>>();
//        text.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{1,0,1,0,1,1,0})));
//        System.out.println();
//        print(code.encode(text));

    }
}
