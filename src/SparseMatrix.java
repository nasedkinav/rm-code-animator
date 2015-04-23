import java.util.ArrayList;
import java.util.List;

public class SparseMatrix {

    protected List<List<Integer>> collection;

    public SparseMatrix() {
        collection = new ArrayList<List<Integer>>();
    }

    public int getMessageLength() {
        return collection.size();
    }

    public List<Integer> getRow(int index) {
        return collection.get(index);
    }

    public List<Integer> getCol(int index) {
        List<Integer> column = new ArrayList<Integer>();

        for (List<Integer> row : collection) {
            column.add(row.get(index));
        }

        return column;
    }

    public void addRow(List<Integer> row) {
        collection.add(new ArrayList<Integer>(row));
    }

    public void addCol(List<Integer> col) {
        List<Integer> column = new ArrayList<Integer>(col);

        if (column.size() != collection.size()) {
            throw new IllegalArgumentException("Column height does not match matrix height");
        }

        for (int i = 0; i < collection.size(); i ++) {
            collection.get(i).add((column.get(i)));
        }
    }

    public void append(SparseMatrix matrix) {
        for (int i = 0; i < matrix.getMessageLength(); i ++) {
            addRow(matrix.getRow(i));
        }
    }

    public boolean isSquare() {
        if (collection.size() == 0) return true;

        int length = collection.get(0).size();
        for (int i = 1; i < collection.size(); i ++) {
            if (collection.get(i).size() != length)
                return false;
        }

        return true;
    }

    /**
     * Returns digit sequence string without redundant zero digits in the beginning
     *
     * @return digit sequence
     */
    public String toString() {
        String result = "";
        for (int i = 0; i < getMessageLength(); i ++) {
            for (int j = 0; j < getRow(i).size(); j ++) {
                int digit = getRow(i).get(j);
                if (result.length() == 0 && digit == 0) {
                    continue;
                }
                result += digit;
            }
        }

        return result;
    }

    public void print() {
        for (List<Integer> row : collection) {
            for (Integer bit : row) {
                System.out.print(bit + " ");
            }
            System.out.println();
        }
    }
}
