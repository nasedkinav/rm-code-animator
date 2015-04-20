import java.util.*;

public class LinearSpace {

    private static final int q = 2;

    public static Integer add(Integer a, Integer b) {
        return (a + b) % q;
    }

    public static List<Integer> add(List<Integer> a, List<Integer> b) {
        if (a.size() != b.size()) {
            throw new IllegalArgumentException();
        }
        List<Integer> result = new ArrayList<Integer>(a.size());
        for (int i = 0; i < a.size(); i ++) {
            result.add(add(a.get(i), b.get(i)));
        }

        return result;
    }

    public static Integer multiply(Integer a, Integer b) {
        return (a * b) % q;
    }

    public static List<Integer> multiply(List<Integer> a, List<Integer> b) {
        if (a.size() != b.size()) {
            throw new IllegalArgumentException();
        }
        List<Integer> result = new ArrayList<Integer>(a.size());
        for (int i = 0; i < a.size(); i ++) {
            result.add(multiply(a.get(i), b.get(i)));
        }

        return result;
    }

    public static List<Integer> multiply(List<Integer> a, Integer b) {
        List<Integer> result = new ArrayList<Integer>(a.size());
        for (int i = 0; i < a.size(); i ++) {
            result.add(multiply(a.get(i), b));
        }

        return result;
    }

    public static Integer dotProduct(List<Integer> a, List<Integer> b) {
        if (a.size() != b.size()) {
            throw new IllegalArgumentException();
        }

        Integer result = 0;
        for (int i = 0; i < a.size(); i ++) {
            result = add(result, multiply(a.get(i), b.get(i)));
        }

        return result;
    }

    public static List<Integer> invert(List<Integer> a) {
        List<Integer> result = new ArrayList<Integer>(a);
        for (int i = 0; i < result.size(); i ++) {
            result.set(i, (result.get(i) + 1) % q);
        }

        return result;
    }
}
