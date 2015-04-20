import java.util.ArrayList;
import java.util.List;

public class SparseMatrix {

    protected List<List<Integer>> collection;

    public SparseMatrix() {
        collection = new ArrayList<List<Integer>>();
    }

    public int getHeight() {
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

    public void append(List<List<Integer>> matrix) {
        for (List<Integer> row : matrix) {
            addRow(row);
        }
    }

    public void append(SparseMatrix matrix) {
        for (int i = 0; i < matrix.getHeight(); i ++) {
            addRow(matrix.getRow(i));
        }
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
