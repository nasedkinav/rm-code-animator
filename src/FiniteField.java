import java.util.*;

public class FiniteField {

    public static int q = 2;

    /**
     * Returns sum of two values in finite field F_q
     *
     * @param a first value
     * @param b second value
     * @return sum of two values in finite field F_q
     */
    public static int add(Integer a, Integer b) {
        return (a + b) % q;
    }

    /**
     * Returns sum of two vectors in finite field F_q
     *
     * @param a first vector
     * @param b second vector
     * @return sum of two vectors in finite field F_q
     */
    public static List<Integer> add(List<Integer> a, List<Integer> b) {
        if (a.size() != b.size()) {
            throw new IllegalArgumentException("Both vectors must be same size");
        }
        List<Integer> result = new ArrayList<Integer>(a.size());
        for (int i = 0; i < a.size(); i ++) {
            result.add(add(a.get(i), b.get(i)));
        }

        return result;
    }

    /**
     * Returns multiplication of two values in finite field F_q
     *
     * @param a first value
     * @param b second value
     * @return multiplication of two values in finite field F_q
     */
    public static int multiply(Integer a, Integer b) {
        return (a * b) % q;
    }

    /**
     * Returns multiplication of two vectors in finite field F_q
     *
     * @param a first vector
     * @param b second vector
     * @return multiplication of two vectors in finite field F_q
     */
    public static List<Integer> multiply(List<Integer> a, List<Integer> b) {
        if (a.size() != b.size()) {
            throw new IllegalArgumentException("Both vectors must be same size");
        }
        List<Integer> result = new ArrayList<Integer>(a.size());
        for (int i = 0; i < a.size(); i ++) {
            result.add(multiply(a.get(i), b.get(i)));
        }

        return result;
    }

    /**
     * Returns multiplication of vector and scalar in finite field F_q
     *
     * @param a vector
     * @param b scalar
     * @return multiplication of vector and scalar in finite field F_q
     */
    public static List<Integer> multiply(List<Integer> a, Integer b) {
        List<Integer> result = new ArrayList<Integer>(a.size());
        for (Integer bit : a) {
            result.add(multiply(bit, b));
        }

        return result;
    }

    /**
     * Returns scalar product of two vectors in finite field F_q
     *
     * @param a first vector
     * @param b second vector
     * @return scalar product of two vectors in finite field F_q
     */
    public static int dotProduct(List<Integer> a, List<Integer> b) {
        if (a.size() != b.size()) {
            throw new IllegalArgumentException("Both vectors must be same size");
        }

        int result = 0;
        for (int i = 0; i < a.size(); i ++) {
            result = add(result, multiply(a.get(i), b.get(i)));
        }

        return result;
    }

    /**
     * Returns multiplicative inverse of binary vector in finite field F_2
     *
     * @param a binary vector
     * @return multiplicative inverse of binary vector in finite field F_2
     */
    public static List<Integer> binaryInvert(List<Integer> a) {
        List<Integer> result = new ArrayList<Integer>();
        for (Integer bit : a) {
            if (bit != 0 && bit != 1) {
                throw new IllegalArgumentException("Vector must consist of bits only");
            }
            result.add((bit + 1) % 2);
        }

        return result;
    }
}
