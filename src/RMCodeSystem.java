import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RMCodeSystem {

    private RMCode code;

    public RMCodeSystem(RMCode code) {
        this.code = code;
    }

    public SparseMatrix encode(String data) {
        SparseMatrix wordList = new SparseMatrix();
        // get bit sequence string "1..." from input text with first bit '1'
        String bitSequence = getBitSequenceFromString(data);

        int messageLength = bitSequence.length();
        int messageWordLength = code.getGeneratorMatrix().getMessageLength();
        int exceedBit = messageWordLength - messageLength % messageWordLength;

        // for each char in message bit sequence transfer it to 1 or 0
        // and match the length of the message word length
        List<Integer> messageWord = new ArrayList<Integer>(messageWordLength);
        for (int i = - exceedBit; i < messageLength; i ++) {
            messageWord.add(i < 0 ? 0 : (bitSequence.charAt(i) == '0' ? 0 : 1));
            if (messageWord.size() == messageWordLength) {
                wordList.addRow(messageWord);
                messageWord.clear();
            }
        }

        return code.encode(wordList);
    }

    public String decode(SparseMatrix data) {
        return new String(new BigInteger(getString(code.decode(data)), 2).toByteArray());
    }

    public String getStringFromBitMatrix(SparseMatrix data) {
        return new String(new BigInteger(getString(data), 2).toByteArray());
    }

    /**
     * Returns bit sequence string without redundant zero bits in the beginning
     * formed from list of code words (represented as binary sequences)
     *
     * @param data matrix of code words
     * @return bit sequence formed from bit matrix represented as String object
     */
    private String getString(SparseMatrix data) {
        String result = "";
        for (int i = 0; i < data.getMessageLength(); i ++) {
            for (int j = 0; j < data.getRow(i).size(); j ++) {
                int bit = data.getRow(i).get(j);
                if (result.length() == 0 && bit == 0) {
                    continue;
                }
                result += bit == 0 ? "0" : "1";
            }
        }

        return result;

    }
    private String getBitSequenceFromString(String data) {
        return new String(new BigInteger(data.getBytes()).toString(2).toCharArray());
    }

    public static void main(String[] args) {
        int m = 3;
        int r = 1;

        RMCode code = new RMCode(r, m);
        System.out.println(code.getRate() + "; " + code.getDistance() + "; " + code.getMaxErrorCorrection());

        String toEncode = "Katerina is the best";
        System.out.println("Text: " + toEncode);

        RMCodeSystem shell = new RMCodeSystem(code);
        SparseMatrix encoded = shell.encode(toEncode);

        System.out.println("Encoded: " + shell.getStringFromBitMatrix(encoded));
        System.out.println("Decoded: " + shell.decode(encoded));
    }

}
