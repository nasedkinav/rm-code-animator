import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RMCode {

    /**
     * Generator matrix of Reed-Muller code RM(r, m)
     */
    private RMMatrix generatorMatrix;

    /**
     * Code rate (message length / block length)
     */
    private double rate;

    /**
     * The minimal Hamming distance between code words
     */
    private int distance;

    /**
     * Maximum number of errors that code can correct while decoding
     */
    private int error;

    public RMCode(int r, int m) {
        generatorMatrix = new RMMatrix(r, m);
        rate = generatorMatrix.getMessageLength() / Math.pow(2, m);
        distance = (int)Math.pow(2, m - r);
        error = (int)Math.pow(2, m - r - 1) - 1;
        if (error < 0) error = 0;
    }

    public RMMatrix getGeneratorMatrix() {
        return generatorMatrix;
    }

    public double getRate() {
        return rate;
    }

    public int getDistance() {
        return distance;
    }

    public int getMaxErrorCorrection() {
        return error;
    }

    public SparseMatrix encode(SparseMatrix data) {
        if (generatorMatrix == null) {
            throw new IllegalStateException("Generator matrix has not been initialized");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data to encodeText is empty");
        }

        SparseMatrix result = new SparseMatrix();
        for (int i = 0; i < data.getMessageLength(); i ++) {
            if (data.getRow(i).size() != generatorMatrix.getMessageLength()) {
                throw new IllegalArgumentException("Word length does not match generator matrix height");
            }
            List<Integer> codeWord = new ArrayList<Integer>(FiniteField.multiply(generatorMatrix.getRow(0), data.getRow(i).get(0)));
            for (int j = 1; j < generatorMatrix.getMessageLength(); j ++) {
                codeWord = FiniteField.add(codeWord, FiniteField.multiply(generatorMatrix.getRow(j), data.getRow(i).get(j)));
            }
            result.addRow(codeWord);
        }

        return result;
    }

    public SparseMatrix decode(SparseMatrix data) {
        if (generatorMatrix == null) {
            throw new IllegalStateException("Generator matrix has not been initialized");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data to decode is empty");
        }

        SparseMatrix decoded = new SparseMatrix();
        for (int i = 0; i < data.getMessageLength(); i ++) {
            // for each received encoded word
            List<Integer> yR = new ArrayList<Integer>(data.getRow(i));
            List<Integer> coefficient = new ArrayList<Integer>();
            List<Integer> My = new ArrayList<Integer>(Collections.nCopies(generatorMatrix.getWidth(), 0));
            for (int j = generatorMatrix.getMessageLength() - 1; j > 0; j --) {
                // for each non-first row of generator matrix
                SparseMatrix characteristic = generatorMatrix.getCharacteristicVectors(j);
                List<Integer> dotProductValues = new ArrayList<Integer>();
                for (int k = 0; k < characteristic.getMessageLength(); k ++) {
                    dotProductValues.add(FiniteField.dotProduct(characteristic.getRow(k), yR));
                }
                coefficient.add(Util.getMajorBit(dotProductValues));
                // multiply each coefficient by its corresponding row and add the resulting vectors
                My = FiniteField.add(My, FiniteField.multiply(generatorMatrix.getRow(j), coefficient.get(coefficient.size() - 1)));
                // reduce inner degree
                if (generatorMatrix.getCombination().getRow(j).size() != generatorMatrix.getCombination().getRow(j - 1).size()) {
                    yR = FiniteField.add(yR, My);
                    My = new ArrayList<Integer>(Collections.nCopies(generatorMatrix.getWidth(), 0));
                }
            }
            coefficient.add(Util.getMajorBit(FiniteField.add(My, yR)));
            Collections.reverse(coefficient);
            decoded.addRow(coefficient);
        }

        return decoded;
    }

}
