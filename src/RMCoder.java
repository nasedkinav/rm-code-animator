import com.sun.org.apache.xpath.internal.SourceTree;

import java.math.BigInteger;
import java.nio.charset.Charset;
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

    public SparseMatrix encode(SparseMatrix data) {
        if (generatorMatrix == null) {
            throw new IllegalStateException();
        }
        if (data == null) {
            throw new IllegalArgumentException();
        }

        SparseMatrix result = new SparseMatrix();
        for (int i = 0; i < data.getHeight(); i ++) {
            if (data.getRow(i).size() != generatorMatrix.getHeight()) {
                throw new IllegalArgumentException(i + " word length does not match generator matrix height");
            }
            List<Integer> codeWord = new ArrayList<Integer>(LinearSpace.multiply(generatorMatrix.getRow(0), data.getRow(i).get(0)));
            for (int j = 1; j < generatorMatrix.getHeight(); j ++) {
                codeWord = LinearSpace.add(codeWord, LinearSpace.multiply(generatorMatrix.getRow(j), data.getRow(i).get(j)));
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
            List<Integer> yR = new ArrayList<Integer>(data.getRow(i));
            List<Integer> coefficient = new ArrayList<Integer>();
            List<Integer> My = new ArrayList<Integer>(Collections.nCopies(generatorMatrix.getWidth(), 0));
            for (int j = generatorMatrix.getHeight() - 1; j > 0; j --) {
                // for each non-first row of generator matrix
                SparseMatrix characteristic = generatorMatrix.getCharacteristicVectors(j);
                List<Integer> dotProductValues = new ArrayList<Integer>();
                for (int k = 0; k < characteristic.getHeight(); k ++) {
                    dotProductValues.add(LinearSpace.dotProduct(characteristic.getRow(k), yR));
                }
                coefficient.add(Util.getMajorBit(dotProductValues));
                // multiply each coefficient by its corresponding row and add the resulting vectors
                My = LinearSpace.add(My, LinearSpace.multiply(generatorMatrix.getRow(j), coefficient.get(coefficient.size() - 1)));
                // reduce inner degree
                if (generatorMatrix.getCombination().getRow(j).size() != generatorMatrix.getCombination().getRow(j - 1).size()) {
                    yR = LinearSpace.add(yR, My);
                    My = new ArrayList<Integer>(Collections.nCopies(generatorMatrix.getWidth(), 0));
                }
            }
            coefficient.add(Util.getMajorBit(LinearSpace.add(My, yR)));
            Collections.reverse(coefficient);
            decoded.addRow(coefficient);
        }

        return decoded;
    }

    public SparseMatrix encode(String text) {
        SparseMatrix data = new SparseMatrix();
        char[] binaryChars = new BigInteger(text.getBytes()).toString(2).toCharArray();
        int codeWordLength = generatorMatrix.getHeight();
        int exceedBit = codeWordLength - binaryChars.length % codeWordLength;

        List<Integer> codeWord = new ArrayList<Integer>(codeWordLength);
        for (int i = - exceedBit; i < binaryChars.length; i ++) {
            codeWord.add(i < 0 ? 0 : (binaryChars[i] == '0' ? 0 : 1));
            if (codeWord.size() == codeWordLength) {
                data.addRow(codeWord);
                codeWord.clear();
            }
        }

        return encode(data);
    }

    public String decodeText(SparseMatrix data) {
        SparseMatrix decoded = decode(data);

        int codeWordLength = generatorMatrix.getHeight();
        int exceedBit = 0;
        int i = 0;
        while (decoded.getRow(i / codeWordLength).get(i % codeWordLength) == 0) {
            exceedBit ++;
            i ++;
        }
        int decodedMessageLength = decoded.getHeight() * codeWordLength;
        char[] binary = new char[decodedMessageLength - exceedBit];
        for (i = exceedBit; i < decodedMessageLength; i ++) {
            binary[i - exceedBit] = decoded.getRow(i / codeWordLength).get(i % codeWordLength) == 0 ? '0' : '1';
        }

        return new String(new BigInteger(new String(binary), 2).toByteArray());
    }

    public static void main(String[] args) {
        int m = 5;
        int r = 4;

        RMCoder code = new RMCoder(r, m);
//        code.getGeneratorMatrix().print();
//        System.out.println();
//        code.getGeneratorMatrix().getCombination().print();
//        System.out.println();
//        code.getGeneratorMatrix().getCharacteristicVectors(3).print();
//        System.out.println();

//        System.out.println("Max error count: " + ((int) Math.pow(2, m - r - 1) - 1));
//        SparseMatrix toencode = new SparseMatrix();
//        toencode.addRow(new ArrayList<Integer>(Collections.nCopies(code.getGeneratorMatrix().getHeight(), 0)));
//        toencode.print();
        String toEncode = "I finally did IT! Mother fucker";
        System.out.println("Text: " + toEncode);
        SparseMatrix encoded = code.encode(toEncode);
//        encoded.print();
        System.out.println("Decoded: " + code.decodeText(encoded));
//        List<List<Integer>> text = new ArrayList<List<Integer>>();
//        text.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{1,0,1,0,1,1,0})));
//        System.out.println();
//        print(code.encode(text));
    }
}
