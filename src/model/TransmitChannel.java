package model;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TransmitChannel {

    public static void transmitMessage(BitMatrix data, int error) {
        Random generator = new Random();

        for (int i = 0; i < data.getRowNumber(); i++) {
            List<Boolean> row = data.getRow(i);
            int currentError = generator.nextInt(error + 1);
            Set<Integer> errorIndex = new HashSet<Integer>();
            while (errorIndex.size() < currentError) {
                int currentIndex = generator.nextInt(row.size());
                if (!errorIndex.contains(currentIndex)) {
                    errorIndex.add(currentIndex);
                }
            }
            for (Integer index : errorIndex) {
                row.set(index, BinaryFiniteField.invert(row.get(index)));
            }
        }
    }
}
