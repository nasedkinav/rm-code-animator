package model;

import util.Util;

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

    /**
     * Array of decoding matrix order rows (yR)
     */
    private String[][] yByOrderR;

    /**
     * Constructs an object of RM code
     *
     * @param r order parameter
     * @param m block length parameter
     */
    public RMCode(int r, int m) {
        generatorMatrix = new RMMatrix(r, m);
        rate = generatorMatrix.getRowNumber() / Math.pow(2, m);
        distance = (int)Math.pow(2, m - r);
        error = (int)Math.pow(2, m - r - 1) - 1;
        if (error < 0) error = 0;
    }

    /**
     * Returns generator matrix
     *
     * @return generator matrix
     */
    public RMMatrix getGeneratorMatrix() {
        return generatorMatrix;
    }

    /**
     * Returns code rate
     * @return code rate
     */
    public double getRate() {
        return rate;
    }

    /**
     * Returns minimal Hamming distance of code
     * @return code distance
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Returns maximum error number that code can correct
     * @return maximum error correction value
     */
    public int getMaxErrorCorrection() {
        return error;
    }

    /**
     * Returns yR according to message and order indexes
     *
     * @param message message index
     * @param order   order index
     * @return yR
     */
    public String getOrderY(int message, int order) {
        return yByOrderR[message][order];
    }

    /**
     * Encodes given message
     * @param data message
     * @return encoded matrix of messages
     */
    public BitMatrix encode(BitMatrix data) {
        if (generatorMatrix == null) {
            throw new IllegalStateException("Generator matrix has not been initialized");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data to encode is empty");
        }

        BitMatrix result = new BitMatrix();
        for (int i = 0; i < data.getRowNumber(); i++) {
            if (data.getRow(i).size() != generatorMatrix.getRowNumber()) {
                throw new IllegalArgumentException("Word length does not match generator matrix height");
            }
            List<Boolean> codeWord = new ArrayList<>(BinaryFiniteField.multiply(generatorMatrix.getRow(0), data.getRow(i).get(0)));
            for (int j = 1; j < generatorMatrix.getRowNumber(); j++) {
                codeWord = BinaryFiniteField.add(codeWord, BinaryFiniteField.multiply(generatorMatrix.getRow(j), data.getRow(i).get(j)));
            }
            result.addRow(codeWord);
        }

        return result;
    }

    /**
     * Decodes encoded message
     * @param data encoded message
     * @return matrix of decoded messages
     */
    public BitMatrix decode(BitMatrix data) {
        if (generatorMatrix == null) {
            throw new IllegalStateException("Generator matrix has not been initialized");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data to decode is empty");
        }

        BitMatrix decoded = new BitMatrix();
        yByOrderR = new String[data.getRowNumber()][generatorMatrix.getRowNumber()];
        for (int i = 0; i < data.getRowNumber(); i++) {
            // for each received encoded word
            List<Boolean> yR = new ArrayList<>(data.getRow(i));
            List<Boolean> coefficient = new ArrayList<>();
            List<Boolean> My = new ArrayList<>(Collections.nCopies(generatorMatrix.getWidth(), Boolean.FALSE));
            for (int j = generatorMatrix.getRowNumber() - 1; j > 0; j--) {
                // add yR to decoding parameter
                yByOrderR[i][j] = "";
                for (int k = 0; k < generatorMatrix.getWidth(); k++) {
                    yByOrderR[i][j] += yR.get(k) ? '1' : '0';
                }
                // for each non-first row of generator matrix
                BitMatrix characteristic = generatorMatrix.getCharacteristicVectors(j);
                List<Boolean> dotProductValues = new ArrayList<>();
                for (int k = 0; k < characteristic.getRowNumber(); k++) {
                    dotProductValues.add(BinaryFiniteField.scalarProduct(characteristic.getRow(k), yR));
                }
                coefficient.add(Util.getMajorBit(dotProductValues));
                // multiply each coefficient by its corresponding row and add the resulting vectors
                My = BinaryFiniteField.add(My, BinaryFiniteField.multiply(generatorMatrix.getRow(j), coefficient.get(coefficient.size() - 1)));
                // reduce inner degree
                if (generatorMatrix.getCombination().get(j).size() != generatorMatrix.getCombination().get(j - 1).size()) {
                    yR = BinaryFiniteField.add(yR, My);
                    My = new ArrayList<>(Collections.nCopies(generatorMatrix.getWidth(), Boolean.FALSE));
                }
            }
            yR = BinaryFiniteField.add(yR, My);
            // add yR to decoding parameter
            yByOrderR[i][0] = "";
            for (int k = 0; k < generatorMatrix.getWidth(); k++) {
                yByOrderR[i][0] += yR.get(k) ? '1' : '0';
            }
            coefficient.add(Util.getMajorBit(yR));
            Collections.reverse(coefficient);
            decoded.addRow(coefficient);
        }

        return decoded;
    }

}
