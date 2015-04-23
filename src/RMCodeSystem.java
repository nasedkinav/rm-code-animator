import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class RMCodeSystem {

    private RMCode code;

    public RMCodeSystem(RMCode code) {
        this.code = code;
    }

    public static void main(String[] args) {
        int m = 6;
        int r = 2;

        RMCode code = new RMCode(r, m);
        RMCodeSystem shell = new RMCodeSystem(code);
        System.out.println("RM(" + r + ", " + m + ")\nMessage length: " + code.getGeneratorMatrix().getMessageLength() +
                "\nBlock length: " + code.getGeneratorMatrix().getRow(0).size() + "\nRate: " + code.getRate() +
                "\nDistance: " + code.getDistance() + "\nMax correcting errors: " + code.getMaxErrorCorrection());
        System.out.println();
        byte[] bytes = "A".getBytes();
        String text = "ABCDEFGHIJKLMNOPQRSTUVWXYZАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ1234567890";
        System.out.println("Text:" + text);
        SparseMatrix encoded = shell.encodeMessage(text, "utf-8");
        System.out.println("Encoded:" + shell.getValidMessageFromSparseMatrix(encoded, "utf-8"));
        System.out.println(encoded);
        SparseMatrix decoded = shell.decodeMessage(encoded);
        System.out.println(decoded);
        System.out.println("Decoded:" + shell.getValidMessageAfterDecoding(decoded, "utf-8"));
    }

    public SparseMatrix encodeMessage(String message, String charset) {
        return code.encode(prepareMessageToEncodeProcess(message, charset));
    }

    public SparseMatrix decodeMessage(SparseMatrix data) {
        return code.decode(data);
    }

    /**
     * Returns SparseMatrix of message blocks with specified beginning
     * of the message (in order to fit size of input message to code block length
     * by adding zeros in the beginning and one 1 bit)
     *
     * @param message message to encode
     * @param charset encoding
     * @return list of message blocks starting with exceed zeros and one 1
     */
    public SparseMatrix prepareMessageToEncodeProcess(String message, String charset) {
        SparseMatrix data = new SparseMatrix();

        // get bit sequence from input text
        boolean[] bitSequence;
        try {
            bitSequence = Util.byteArrayToBitArray(message.getBytes(charset));
        } catch (UnsupportedEncodingException uee) {
            // TODO: find smth out with encodings
            bitSequence = Util.byteArrayToBitArray(message.getBytes());
        }

        int messageLength = bitSequence.length; // plus one 1 bit meaning beginning of the message value
        int messageWordLength = code.getGeneratorMatrix().getMessageLength();
        // number of zero bit to be added in the beginning of message in order to match word size
        int exceedBit = (messageWordLength - (messageLength + 1) % messageWordLength) % messageWordLength;
        // message length + 1 because of exceed 1 at the beginning

        // transfer bit stream into sparse matrix of fixed code word size
        List<Integer> messageWord = new ArrayList<Integer>(messageWordLength);
        // -exceedBit - 1 because of exceed 1 at the beginning
        for (int i = -exceedBit - 1; i < messageLength; i++) {
            // check for starting one 1
            messageWord.add(i == -1 ? 1 : (i < 0 ? 0 : (bitSequence[i] ? 1 : 0)));
            if (messageWord.size() == messageWordLength) {
                data.addRow(messageWord);
                messageWord.clear();
            }
        }
        System.out.println("Prepared:" + data);
        return data;
    }

    /**
     * Returns valid decoded message formed by removing exceed zero bits
     * in the beginning with one 1 (as beginning of the message)
     *
     * @param data    decoded message
     * @param charset encoding
     * @return String decoded text
     */
    public String getValidMessageAfterDecoding(SparseMatrix data, String charset) {
        SparseMatrix message = new SparseMatrix();

        boolean[] bits = new boolean[0];
        int bitIndex = 0;
        int redundantBit = 0;
        int decodedLength = data.getMessageLength() * data.getRow(0).size();
        boolean redundant = true;
        for (int i = 0; i < data.getMessageLength(); i++) {
            for (int j = 0; j < data.getRow(i).size(); j++) {
                if (redundant) {
                    if (data.getRow(i).get(j) == 0) {
                        redundantBit++;
                        continue;
                    }
                    redundant = false;
                    bits = new boolean[decodedLength - redundantBit - 1];
                    continue;
                }
                bits[bitIndex++] = data.getRow(i).get(j) != 0;
            }
        }

        String result;
        try {
            result = new String(Util.bitArrayToByteArray(bits), charset);
        } catch (UnsupportedEncodingException uee) {
            result = new String(Util.bitArrayToByteArray(bits));
        }

        return result;
    }

    public String getValidMessageFromSparseMatrix(SparseMatrix data, String charset) {
        if (data == null || data.getMessageLength() == 0) {
            throw new IllegalArgumentException();
        }

        int messageSize = data.getMessageLength() * data.getRow(0).size();
        // number of zero bits to be added at the beginning of the message
        // in order to match the message length of the code
        int exceedBit = (8 - messageSize % 8) % 8;
        int bitIndex = 0;

        boolean[] bits = new boolean[messageSize + exceedBit];
        // pre fill the message with exceeding zero bits
        for (; bitIndex < exceedBit; bitIndex++) {
            bits[bitIndex] = false;
        }
        // fill the bit sequence with data matrix values
        for (int i = 0; i < data.getMessageLength(); i++) {
            for (int j = 0; j < data.getRow(i).size(); j++) {
                bits[bitIndex++] = data.getRow(i).get(j) != 0;
            }
        }

        String result;
        try {
            result = new String(Util.bitArrayToByteArray(bits), charset);
        } catch (UnsupportedEncodingException uee) {
            result = new String(Util.bitArrayToByteArray(bits));
        }

        return result;
    }

}
