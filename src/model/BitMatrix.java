package model;

import java.util.ArrayList;
import java.util.List;

public class BitMatrix {

    /**
     * Collection of bits represented as bit matrix
     */
    protected List<List<Boolean>> collection;

    /**
     * Constructs an object of model.BitMatrix
     */
    public BitMatrix() {
        collection = new ArrayList<>();
    }

    /**
     * Constructs a deep copy of a BitMatrix object
     *
     * @param data matrix to be copied
     */
    public BitMatrix(BitMatrix data) {
        collection = new ArrayList<>();
        for (int i = 0; i < data.getRowNumber(); i++) {
            collection.add(new ArrayList<>(data.getRow(i)));
        }
    }

    /**
     * Returns number of matrix rows
     *
     * @return number of rows
     */
    public int getRowNumber() {
        return collection.size();
    }

    /**
     * Returns matrix row by index
     *
     * @param index index of the row
     * @return certain row of the matrix
     */
    public List<Boolean> getRow(int index) {
        return collection.get(index);
    }

    /**
     * Returns matrix column by index
     *
     * @param index index of the column
     * @return certain column of the matrix
     */
    public List<Boolean> getColumn(int index) {
        List<Boolean> column = new ArrayList<>();

        for (List<Boolean> row : collection) {
            column.add(row.get(index));
        }

        return column;
    }

    /**
     * Adds a copy of argument row in the end of the matrix
     *
     * @param row row to be added
     */
    public void addRow(List<Boolean> row) {
        collection.add(new ArrayList<>(row));
    }

    /**
     * Adds a copy of argument row as a last column of the matrix
     *
     * @param column row to be added as a column
     */
    public void addColumn(List<Boolean> column) {
        List<Boolean> result = new ArrayList<>(column);

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
        for (int i = 0; i < getRowNumber(); i++) {
            for (int j = 0; j < getRow(i).size(); j ++) {
                result += getRow(i).get(j) ? "1" : "0";
            }
            result += "\n";
        }

        return result;
    }

}
