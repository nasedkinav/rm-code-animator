package model;

import java.util.ArrayList;
import java.util.List;

public class BinaryFiniteField {

    /**
     * Returns sum of two values in finite field F_2
     *
     * @param a first value
     * @param b second value
     * @return sum of two values in finite field F_2
     */
    public static boolean add(boolean a, boolean b) {
        return a ^ b;
    }

    /**
     * Returns sum of two vectors in finite field F_2
     *
     * @param a first vector
     * @param b second vector
     * @return sum of two vectors in finite field F_2
     */
    public static List<Boolean> add(List<Boolean> a, List<Boolean> b) {
        if (a.size() != b.size()) {
            throw new IllegalArgumentException("Both vectors must be same size");
        }

        List<Boolean> result = new ArrayList<Boolean>(a.size());
        for (int i = 0; i < a.size(); i ++) {
            result.add(add(a.get(i), b.get(i)));
        }

        return result;
    }

    /**
     * Returns multiplication of two values in finite field F_2
     *
     * @param a first value
     * @param b second value
     * @return multiplication of two values in finite field F_2
     */
    public static boolean multiply(boolean a, boolean b) {
        return a & b;
    }

    /**
     * Returns multiplication of two vectors in finite field F_2
     *
     * @param a first vector
     * @param b second vector
     * @return multiplication of two vectors in finite field F_2
     */
    public static List<Boolean> multiply(List<Boolean> a, List<Boolean> b) {
        if (a.size() != b.size()) {
            throw new IllegalArgumentException("Both vectors must be same size");
        }

        List<Boolean> result = new ArrayList<Boolean>(a.size());
        for (int i = 0; i < a.size(); i ++) {
            result.add(multiply(a.get(i), b.get(i)));
        }

        return result;
    }

    /**
     * Returns multiplication of vector and scalar in finite field F_2
     *
     * @param a vector
     * @param b scalar
     * @return multiplication of vector and scalar in finite field F_2
     */
    public static List<Boolean> multiply(List<Boolean> a, boolean b) {
        List<Boolean> result = new ArrayList<Boolean>(a.size());
        for (boolean bit : a) {
            result.add(multiply(bit, b));
        }

        return result;
    }

    /**
     * Returns scalar product of two vectors in finite field F_2
     *
     * @param a first vector
     * @param b second vector
     * @return scalar product of two vectors in finite field F_2
     */
    public static boolean scalarProduct(List<Boolean> a, List<Boolean> b) {
        if (a.size() != b.size()) {
            throw new IllegalArgumentException("Both vectors must be same size");
        }

        boolean result = false;
        for (int i = 0; i < a.size(); i ++) {
            result = add(result, multiply(a.get(i), b.get(i)));
        }

        return result;
    }

    /**
     * Returns inverse value of a single bit
     *
     * @param a bit to be inverted
     * @return inverse value of a bit
     */
    public static Boolean invert(Boolean a) {
        return a ^ Boolean.TRUE;
    }

    /**
     * Returns multiplicative inverse of binary vector in finite field F_2
     *
     * @param a binary vector
     * @return multiplicative inverse of binary vector in finite field F_2
     */
    public static List<Boolean> invert(List<Boolean> a) {
        List<Boolean> result = new ArrayList<Boolean>();
        for (boolean bit : a) {
            result.add(invert(bit));
        }

        return result;
    }
}
