package model;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TransmitChannel {

    /**
     * Transmits message through channel with errors
     *
     * @param data  message to be transmitted
     * @param error maximum number of errors to be placed in a single message block
     */
    public static void transmitMessage(BitMatrix data, int error) {
        Random generator = new Random();

        for (int i = 0; i < data.getRowNumber(); i++) {
            List<Boolean> row = data.getRow(i);
            int currentError = generator.nextInt(error + 1);
            Set<Integer> errorIndex = new HashSet<>();
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
