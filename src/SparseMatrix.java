import java.util.ArrayList;
import java.util.List;

public class SparseMatrix {

    protected List<List<Boolean>> collection;

    public SparseMatrix() {
        collection = new ArrayList<List<Boolean>>();
    }

    public int getMessageLength() {
        return collection.size();
    }

    public List<Boolean> getRow(int index) {
        return collection.get(index);
    }

    public List<Boolean> getColumn(int index) {
        List<Boolean> column = new ArrayList<Boolean>();

        for (List<Boolean> row : collection) {
            column.add(row.get(index));
        }

        return column;
    }

    public void addRow(List<Boolean> row) {
        collection.add(new ArrayList<Boolean>(row));
    }

    public void addColumn(List<Boolean> column) {
        List<Boolean> result = new ArrayList<Boolean>(column);

        if (result.size() != collection.size()) {
            throw new IllegalArgumentException("Column height does not match matrix height");
        }

        for (int i = 0; i < collection.size(); i ++) {
            collection.get(i).add((result.get(i)));
        }
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
                result += getRow(i).get(j) ? "1" : "0";
            }
            result += "\n";
        }

        return result;
    }

}
