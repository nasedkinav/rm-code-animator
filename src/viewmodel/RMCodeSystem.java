package viewmodel;

import model.BitMatrix;
import model.RMCode;
import util.Util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class RMCodeSystem {

    /**
     * RMCode instance
     */
    private RMCode code;

    /**
     * Constructs RMCode boundary
     *
     * @param code RMCode object
     */
    public RMCodeSystem(RMCode code) {
        this.code = code;
    }

    /**
     * Returns RMCode instance
     *
     * @return
     */
    public RMCode getCode() {
        return code;
    }


    public BitMatrix decode(BitMatrix data) {
        return code.decode(data);
    }

    public BitMatrix encode(BitMatrix prepared) {
        return code.encode(prepared);
    }

    /**
     * Returns model.BitMatrix of message blocks with specified beginning
     * of the message (in order to fit size of input message to code block length
     * by adding zeros in the beginning and one 1 bit)
     *
     * @param message message to encode
     * @param charset encoding
     * @return list of message blocks starting with exceed zeros and one 1
     */
    public BitMatrix prepareMessageToEncodeProcess(String message, String charset) {
        BitMatrix data = new BitMatrix();

        // get bit sequence from input text
        boolean[] bitSequence;
        try {
            bitSequence = Util.byteArrayToBitArray(message.getBytes(charset));
        } catch (UnsupportedEncodingException uee) {
            bitSequence = Util.byteArrayToBitArray(message.getBytes());
        }

        int messageLength = bitSequence.length; // plus one 1 bit meaning beginning of the message value
        int messageWordLength = code.getGeneratorMatrix().getRowNumber();
        // number of zero bit to be added in the beginning of message in order to match word size
        int exceedBit = (messageWordLength - (messageLength + 1) % messageWordLength) % messageWordLength;
        // message length + 1 because of exceed 1 at the beginning

        // transfer bit stream into sparse matrix of fixed code word size
        List<Boolean> messageWord = new ArrayList<>(messageWordLength);
        // -exceedBit - 1 because of exceed 1 at the beginning
        for (int i = -exceedBit - 1; i < messageLength; i++) {
            // check for starting one 1
            messageWord.add(i == -1 ? Boolean.TRUE : (i < 0 ? Boolean.FALSE : bitSequence[i]));
            if (messageWord.size() == messageWordLength) {
                data.addRow(messageWord);
                messageWord.clear();
            }
        }

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
    public String getValidMessageAfterDecoding(BitMatrix data, String charset) {
        boolean[] bits = new boolean[0];
        int bitIndex = 0;
        int redundantBit = 0;
        int decodedLength = data.getRowNumber() * data.getRow(0).size();
        boolean redundant = true;
        for (int i = 0; i < data.getRowNumber(); i++) {
            for (int j = 0; j < data.getRow(i).size(); j++) {
                if (redundant) {
                    if (!data.getRow(i).get(j)) {
                        redundantBit++;
                        continue;
                    }
                    redundant = false;
                    bits = new boolean[decodedLength - redundantBit - 1];
                    continue;
                }
                bits[bitIndex++] = data.getRow(i).get(j);
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

    /**
     * Returns valid message formed by adding exceed zero bits
     * in the beginning of first message in order to fit byte amount
     *
     * @param data matrix of bit vectors
     * @param charset message encoding
     * @return converted message
     */
    public String getValidMessageFromBitMatrix(BitMatrix data, String charset) {
        if (data == null || data.getRowNumber() == 0) {
            throw new IllegalArgumentException();
        }

        int messageSize = data.getRowNumber() * data.getRow(0).size();
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
        for (int i = 0; i < data.getRowNumber(); i++) {
            for (int j = 0; j < data.getRow(i).size(); j++) {
                bits[bitIndex++] = data.getRow(i).get(j);
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
