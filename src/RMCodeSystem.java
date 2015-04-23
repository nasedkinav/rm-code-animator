import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RMCodeSystem {

    private RMCode code;

    public RMCodeSystem(RMCode code) {
        this.code = code;
    }

    public SparseMatrix encodeText(String message) {
        return code.encode(convertMessageToSparseMatrix(message));
    }

    public SparseMatrix decode(SparseMatrix data) {
        return code.decode(data);
    }

    public SparseMatrix convertMessageToSparseMatrix(String message) {
        SparseMatrix data = new SparseMatrix();

        // get bit sequence from input text
        boolean[] bitSequence = byteArrayToBitArray(message.getBytes());

        int messageLength = bitSequence.length;
        int messageWordLength = code.getGeneratorMatrix().getMessageLength();
        // number of zero bit to be added in the beginning of message in order to match word size
        int exceedBit = (messageWordLength - messageLength % messageWordLength) % messageWordLength;

        // transfer bit stream into sparse matrix of fixed code word size
        List<Integer> messageWord = new ArrayList<Integer>(messageWordLength);
        for (int i = - exceedBit; i < messageLength; i ++) {
            messageWord.add(i < 0 ? 0 : (bitSequence[i] ? 1 : 0));
            if (messageWord.size() == messageWordLength) {
                data.addRow(messageWord);
                messageWord.clear();
            }
        }

        return data;
    }

    public byte[] convertSparseMatrixToByteArray(SparseMatrix data, boolean toExpand) {
        if (data == null || data.getMessageLength() == 0) {
            throw new IllegalArgumentException();
        }

        boolean[] bits;
        int messageSize = data.getMessageLength() * data.getRow(0).size();
        int exceedBit;

        if (! toExpand) {
            // remove first exceeded bits
            exceedBit = messageSize % 8;
            bits = new boolean[messageSize - exceedBit];
            int bitIndex = 0;
            for (int i = 0; i < data.getMessageLength(); i++) {
                for (int j = 0; j < data.getRow(i).size(); j++) {
                    if (exceedBit-- > 0) {
                        continue;
                    }
                    bits[bitIndex++] = data.getRow(i).get(j) != 0;
                }
            }
        } else {
            exceedBit = (8 - messageSize % 8) % 8;
            bits = new boolean[messageSize + exceedBit];
            int bitIndex = 0;
            for (; bitIndex<exceedBit; bitIndex++){
                bits[bitIndex] = false;
            }
            for (int i = 0; i < data.getMessageLength(); i++) {
                for (int j = 0; j < data.getRow(i).size(); j++) {
                    bits[bitIndex++] = data.getRow(i).get(j) != 0;
                }
            }
        }

        return bitArrayToByteArray(bits);
    }

    public boolean[] byteArrayToBitArray(byte[] bytes) {
        boolean[] bits = new boolean[bytes.length * 8];
        for (int i = 0; i < bytes.length * 8; i++) {
            if ((bytes[i / 8] & (1 << (7 - (i % 8)))) > 0)
                bits[i] = true;
        }
        return bits;
    }

    public byte[] bitArrayToByteArray(boolean[] bits) {
        if (bits.length % 8 != 0) {
            throw new IllegalArgumentException();
        }

        byte[] bytes = new byte[bits.length / 8];
        String singleByte = "";
        for (int i = 0; i < bits.length; i ++) {
            singleByte += bits[i] ? "1" : "0";
            if (i % 8 == 7) {
                bytes[i / 8] = (byte) Integer.parseInt(singleByte, 2);
                singleByte = "";
            }
        }

        return bytes;
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

//        String text = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//        System.out.println("Text:" + text);
//        SparseMatrix encoded = shell.encodeText(text);
//        System.out.println("Encoded:" + new String(shell.convertSparseMatrixToByteArray(encoded, true)));
//        SparseMatrix decoded = shell.decode(encoded);
//        System.out.println("Decoded:" + new String(shell.convertSparseMatrixToByteArray(decoded, false)));
        byte[] first = new byte[1];
        byte[] second = new byte[1];
        try {
            first = "╫".getBytes("utf-8");
            second = "╫".getBytes();
            String firstS = new String(first, "utf-8");
            String secondS = new String(second);
            System.out.println(firstS);
            System.out.println(secondS);
            System.out.println(new String(new byte[]{0}));
        } catch (Exception e) {
        }
        System.out.println();
    }

}
