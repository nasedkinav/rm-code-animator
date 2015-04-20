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

        for (int i = 0; i < collection.size(); i ++) {
            try {
                column.add(collection.get(i).get(index));
            } catch (IndexOutOfBoundsException iobe) {
                column.add(null);
            } catch (Exception e) {
                // stub
            }
        }

        return column;
    }

    public void addRow(List<Integer> row) {
        collection.add(new ArrayList<Integer>(row));
    }

    public void addCol(List<Integer> col) {
        if (col.size() != collection.size()) {
            throw new IllegalArgumentException("Column height does not match matrix height");
        }

        for (int i = 0; i < collection.size(); i ++) {
            collection.get(i).add((col.get(i)));
        }
    }

    public void append(List<List<Integer>> matrix) {
        for (int i = 0; i < matrix.size(); i ++) {
            addRow(matrix.get(i));
        }
    }

    public void print() {
        for (int i = 0; i < collection.size(); i ++) {
            for (int j = 0; j < collection.get(i).size(); j ++) {
                System.out.print(collection.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
}
