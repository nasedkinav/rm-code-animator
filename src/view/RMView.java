/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view;

import model.BitMatrix;
import model.RMCode;
import model.TransmitChannel;
import viewmodel.RMCodeSystem;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RMView extends javax.swing.JFrame {

    private final Object lock = new Object();
    private final double maxSleepTime = 10000;
    // TODO: smth with encodings
    private final String charset = "windows-1251";
    private RMCodeSystem codeSystem;
    private int m = 3;
    private int r = 1;
    private BitMatrix converted;
    private BitMatrix encoded;
    private BitMatrix transmitted;
    private BitMatrix decoded;
    private double sleepTime = maxSleepTime / 2;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel RMLabel;
    private javax.swing.JCheckBox animationCheckBox;
    private javax.swing.JLabel blockLabel;
    private javax.swing.JTextField blockText;
    private javax.swing.JButton convertButton;
    private javax.swing.JLabel convertedMessageLabel;
    private javax.swing.JScrollPane convertedMessageScroll;
    private javax.swing.JTextArea convertedMessageText;
    private javax.swing.JButton decodeButton;
    private javax.swing.JLabel decodedLabel;
    private javax.swing.JScrollPane decodedScroll;
    private javax.swing.JLabel decodedStringLabel;
    private javax.swing.JTextField decodedStringText;
    private javax.swing.JTextArea decodedText;
    private javax.swing.JLabel distanceLabel;
    private javax.swing.JTextField distanceText;
    private javax.swing.JButton encodeButton;
    private javax.swing.JLabel encodedLabel;
    private javax.swing.JScrollPane encodedScroll;
    private javax.swing.JTextArea encodedText;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JTextField errorText;
    private javax.swing.JLabel generatorLabel;
    private javax.swing.JScrollPane generatorScroll;
    private javax.swing.JTextArea generatorText;
    private javax.swing.JCheckBox highlightCheckBox;
    private javax.swing.JTextField inputMessage;
    private javax.swing.JLabel inputMessageLabel;
    private javax.swing.JLabel lengthLabel;
    private javax.swing.JTextField lengthText;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JTextField messageLengthText;
    private javax.swing.JPanel messagePanel;
    private javax.swing.JLabel orderLabel;
    private javax.swing.JTextField orderText;
    private javax.swing.JLabel parametersLabel;
    private javax.swing.JLabel rateLabel;
    private javax.swing.JTextField rateText;
    private javax.swing.JScrollPane rowScroll;
    private javax.swing.JTextArea rowText;
    private javax.swing.JLabel signLabel;
    private javax.swing.JSlider slider;
    private javax.swing.JLabel sliderLabel;
    private javax.swing.JButton transmitButton;
    private javax.swing.JLabel transmittedLabel;
    private javax.swing.JScrollPane transmittedScroll;
    private javax.swing.JLabel transmittedStringLabel;
    private javax.swing.JTextField transmittedStringText;
    private javax.swing.JTextArea transmittedText;

    /**
     * Creates new form RMView
     */
    public RMView() {
        initComponents();
        initHandlers();

        inputMessage.setText("Hello, world!");
        lengthText.setText("" + m);
        orderText.setText("" + r);

        initCode();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RMView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RMView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RMView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RMView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RMView().setVisible(true);
            }
        });
    }

    private void reset() {
        // set buttons enabling
        convertButton.setEnabled(true);
        encodeButton.setEnabled(false);
        transmitButton.setEnabled(false);
        decodeButton.setEnabled(false);

        // flush text
        convertedMessageText.setText("");
        encodedText.setText("");
        transmittedText.setText("");
        decodedText.setText("");
        rowText.setText("");
        transmittedStringText.setText("");
        decodedStringText.setText("");

        // flush matrices
        converted = encoded = transmitted = decoded = null;
    }

    private void disableControls() {
        lengthText.setEnabled(false);
        orderText.setEnabled(false);

        convertButton.setEnabled(false);
        encodeButton.setEnabled(false);
        transmitButton.setEnabled(false);
        decodeButton.setEnabled(false);
    }

    private void sleepThread() {
//        synchronized (lock) {
        try {
            Thread.currentThread().sleep((int) sleepTime);
        } catch (InterruptedException ie) {
            // do nothing
        }
//        }
    }

    private void addHighlight(JTextArea area, int p0, int p1, Color c) {
        try {
            area.getHighlighter().addHighlight(p0, p1, new DefaultHighlighter.DefaultHighlightPainter(c));
        } catch (BadLocationException ble) {
            // do nothing
        }
    }

    private void initHandlers() {
        lengthText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initCode();
            }
        });
        orderText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initCode();
            }
        });
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputMessage.getText().length() == 0) {
                    JOptionPane.showMessageDialog(messagePanel, "Please input message to proceed",
                            "Incorrect input message", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                reset();
                converted = codeSystem.prepareMessageToEncodeProcess(inputMessage.getText(), charset);
                convertedMessageText.setText(converted.toString());
                // highlight message start
                addHighlight(convertedMessageText, 0, convertedMessageText.getText().indexOf('1') + 1, Color.yellow);
                encodeButton.setEnabled(true);
            }
        });
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                synchronized (lock) {
                    // TODO: set min to 10k and max to 50ms
                    sleepTime = maxSleepTime * (1 - (double) slider.getValue() / (slider.getMaximum() - slider.getMinimum()));
                }
            }
        });
        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (converted == null) {
                    JOptionPane.showMessageDialog(messagePanel, "Input message should be converted into bit stream before encoding",
                            "Incorrect state", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                disableControls();
                encodedText.setText("");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        encoded = codeSystem.encode(converted);
                        String[] convertedRows = converted.toString().split("\n");
                        String[] encodedRows = encoded.toString().split("\n");

                        for (int i = 0; i < convertedRows.length; i++) {
                            convertedMessageText.getHighlighter().removeAllHighlights();
                            addHighlight(convertedMessageText, i * (convertedRows[i].length() + 1),
                                    i * (convertedRows[i].length() + 1) + convertedRows[i].length(), Color.yellow);
                            convertedMessageText.setCaretPosition(i * (convertedRows[i].length() + 1));
                            // set row
                            rowText.setText("");
                            rowText.getHighlighter().removeAllHighlights();
                            generatorText.getHighlighter().removeAllHighlights();
                            for (int j = 0; j < convertedRows[i].length(); j++) {
                                char bit = convertedRows[i].charAt(j);
                                rowText.append(bit + (j == convertedRows[i].length() - 1 ? "" : "\n"));
                                // highlight xor rows
                                if (bit == '1') {
                                    addHighlight(rowText, 2 * j, 2 * j + 1, Color.yellow);
                                    addHighlight(generatorText,
                                            j * (codeSystem.getCode().getGeneratorMatrix().getWidth() + 1),
                                            j * (codeSystem.getCode().getGeneratorMatrix().getWidth() + 1) + codeSystem.getCode().getGeneratorMatrix().getWidth(),
                                            Color.yellow);
                                }
                            }
                            // append encoded row
                            encodedText.append(encodedRows[i] + (i == convertedRows.length - 1 ? "" : "\n"));
                            encodedText.getHighlighter().removeAllHighlights();
                            addHighlight(encodedText, i * (encodedRows[i].length() + 1),
                                    i * (encodedRows[i].length() + 1) + encodedRows[i].length(), Color.green);
                            // set caret
                            encodedText.setCaretPosition(encodedText.getText().length() - 1);
                            sleepThread();
                        }
                        convertedMessageText.getHighlighter().removeAllHighlights();
                        convertedMessageText.setCaretPosition(0);
                        encodedText.getHighlighter().removeAllHighlights();
                        encodedText.setCaretPosition(0);
                        rowText.setText("");
                        rowText.getHighlighter().removeAllHighlights();
                        generatorText.getHighlighter().removeAllHighlights();
                        // enable controls
                        convertButton.setEnabled(true);
                        encodeButton.setEnabled(true);
                        transmitButton.setEnabled(true);
                        lengthText.setEnabled(true);
                        orderText.setEnabled(true);
                    }
                }).start();
            }
        });
        transmitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (encoded == null) {
                    JOptionPane.showMessageDialog(messagePanel, "Converted message should be encoded before transmitting",
                            "Incorrect state", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                disableControls();
                transmittedText.setText("");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        transmitted = new BitMatrix(encoded);
                        TransmitChannel.transmitMessage(transmitted, codeSystem.getCode().getMaxErrorCorrection());

                        for (int i = 0; i < transmitted.getRowNumber(); i++) {
                            encodedText.getHighlighter().removeAllHighlights();
                            addHighlight(encodedText, i * (codeSystem.getCode().getGeneratorMatrix().getWidth() + 1),
                                    i * (codeSystem.getCode().getGeneratorMatrix().getWidth() + 1) + codeSystem.getCode().getGeneratorMatrix().getWidth(),
                                    Color.yellow);
                            encodedText.setCaretPosition(i * (codeSystem.getCode().getGeneratorMatrix().getWidth() + 1));
                            // first append
                            for (int j = 0; j < transmitted.getRow(i).size(); j++) {
                                transmittedText.append(transmitted.getRow(i).get(j) ? "1" : "0");
                            }
                            if (i != transmitted.getRowNumber() - 1) transmittedText.append("\n");
                            // then highlight
                            for (int j = 0; j < transmitted.getRow(i).size(); j++) {
                                if (transmitted.getRow(i).get(j) ^ encoded.getRow(i).get(j)) {
                                    addHighlight(transmittedText, i * (codeSystem.getCode().getGeneratorMatrix().getWidth() + 1) + j,
                                            i * (codeSystem.getCode().getGeneratorMatrix().getWidth() + 1) + j + 1,
                                            Color.red);
                                }
                            }
                            transmittedText.setCaretPosition(transmittedText.getText().length() - 1);
                            sleepThread();
                        }
                        encodedText.getHighlighter().removeAllHighlights();
                        encodedText.setCaretPosition(0);
                        transmittedText.setCaretPosition(0);
                        transmittedStringText.setText(codeSystem.getValidMessageFromBitMatrix(transmitted, charset));
                        // enable controls
                        convertButton.setEnabled(true);
                        encodeButton.setEnabled(true);
                        transmitButton.setEnabled(true);
                        decodeButton.setEnabled(true);
                        lengthText.setEnabled(true);
                        orderText.setEnabled(true);
                    }
                }).start();
            }
        });
        decodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (transmitted == null) {
                    JOptionPane.showMessageDialog(messagePanel, "Encoded message should be transmitted thought channel before decoding",
                            "Incorrect state", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                disableControls();
                decodedText.setText("");


            }
        });
    }

    private void initCode() {
        try {
            if (lengthText.getText().length() == 0) lengthText.setText("0");
            if (orderText.getText().length() == 0) orderText.setText("0");
            m = Integer.parseInt(lengthText.getText());
            r = Integer.parseInt(orderText.getText());
            codeSystem = new RMCodeSystem(new RMCode(r, m));

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Please, input correct number", "Incorrect code parameters",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Incorrect code parameters",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        RMCode code = codeSystem.getCode();
        blockText.setText("" + code.getGeneratorMatrix().getWidth());
        messageLengthText.setText("" + code.getGeneratorMatrix().getRowNumber());
        distanceText.setText("" + code.getDistance());
        rateText.setText("" + String.format("%.2f", code.getRate()));
        errorText.setText("" + code.getMaxErrorCorrection());
        generatorText.setText(code.getGeneratorMatrix().toString());
        reset();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        messagePanel = new javax.swing.JPanel();
        inputMessageLabel = new javax.swing.JLabel();
        inputMessage = new javax.swing.JTextField();
        convertedMessageLabel = new javax.swing.JLabel();
        convertedMessageScroll = new javax.swing.JScrollPane();
        convertedMessageText = new javax.swing.JTextArea();
        encodedScroll = new javax.swing.JScrollPane();
        encodedText = new javax.swing.JTextArea();
        encodedLabel = new javax.swing.JLabel();
        transmittedScroll = new javax.swing.JScrollPane();
        transmittedText = new javax.swing.JTextArea();
        transmittedLabel = new javax.swing.JLabel();
        decodedLabel = new javax.swing.JLabel();
        decodedScroll = new javax.swing.JScrollPane();
        decodedText = new javax.swing.JTextArea();
        convertButton = new javax.swing.JButton();
        encodeButton = new javax.swing.JButton();
        transmitButton = new javax.swing.JButton();
        decodeButton = new javax.swing.JButton();
        transmittedStringLabel = new javax.swing.JLabel();
        transmittedStringText = new javax.swing.JTextField();
        decodedStringLabel = new javax.swing.JLabel();
        decodedStringText = new javax.swing.JTextField();
        RMLabel = new javax.swing.JLabel();
        lengthLabel = new javax.swing.JLabel();
        orderLabel = new javax.swing.JLabel();
        lengthText = new javax.swing.JTextField();
        orderText = new javax.swing.JTextField();
        blockLabel = new javax.swing.JLabel();
        messageLabel = new javax.swing.JLabel();
        blockText = new javax.swing.JTextField();
        messageLengthText = new javax.swing.JTextField();
        distanceLabel = new javax.swing.JLabel();
        distanceText = new javax.swing.JTextField();
        rateLabel = new javax.swing.JLabel();
        rateText = new javax.swing.JTextField();
        errorLabel = new javax.swing.JLabel();
        errorText = new javax.swing.JTextField();
        generatorLabel = new javax.swing.JLabel();
        rowScroll = new javax.swing.JScrollPane();
        rowText = new javax.swing.JTextArea();
        signLabel = new javax.swing.JLabel();
        generatorScroll = new javax.swing.JScrollPane();
        generatorText = new javax.swing.JTextArea();
        slider = new javax.swing.JSlider();
        sliderLabel = new javax.swing.JLabel();
        parametersLabel = new javax.swing.JLabel();
        highlightCheckBox = new javax.swing.JCheckBox();
        animationCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Reed-Muller code animator");
        setLocationByPlatform(true);
        setPreferredSize(new java.awt.Dimension(776, 790));
        setResizable(false);

        messagePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        inputMessageLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        inputMessageLabel.setText("Input message to encode:");

        inputMessage.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        inputMessage.setText("ABCDEFGHIJKLMNOPQRSTUVWXYZАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ0123456789");

        convertedMessageLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        convertedMessageLabel.setText("Converted message:");

        convertedMessageText.setEditable(false);
        convertedMessageText.setColumns(20);
        convertedMessageText.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        convertedMessageText.setRows(5);
        convertedMessageText.setPreferredSize(null);
        convertedMessageScroll.setViewportView(convertedMessageText);

        encodedText.setEditable(false);
        encodedText.setColumns(20);
        encodedText.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        encodedText.setRows(5);
        encodedText.setPreferredSize(null);
        encodedScroll.setViewportView(encodedText);

        encodedLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        encodedLabel.setText("Encoded message bit stream:");

        transmittedText.setEditable(false);
        transmittedText.setColumns(20);
        transmittedText.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        transmittedText.setRows(5);
        transmittedText.setPreferredSize(null);
        transmittedScroll.setViewportView(transmittedText);

        transmittedLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        transmittedLabel.setText("Transmitted bit stream:");

        decodedLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        decodedLabel.setText("Decoded bit stream:");

        decodedText.setEditable(false);
        decodedText.setColumns(20);
        decodedText.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        decodedText.setRows(5);
        decodedText.setPreferredSize(null);
        decodedScroll.setViewportView(decodedText);

        convertButton.setText("Convert message");
        convertButton.setPreferredSize(new java.awt.Dimension(146, 23));

        encodeButton.setText("Encode data");

        transmitButton.setText("Transmit encoded message");

        decodeButton.setText("Decode received data");

        transmittedStringLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        transmittedStringLabel.setText("Transmitted message through channel:");

        transmittedStringText.setEditable(false);
        transmittedStringText.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N

        decodedStringLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        decodedStringLabel.setText("Decoded message:");

        decodedStringText.setEditable(false);
        decodedStringText.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N

        javax.swing.GroupLayout messagePanelLayout = new javax.swing.GroupLayout(messagePanel);
        messagePanel.setLayout(messagePanelLayout);
        messagePanelLayout.setHorizontalGroup(
                messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(messagePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(transmittedStringText)
                                        .addComponent(inputMessage)
                                        .addComponent(decodedStringText)
                                        .addGroup(messagePanelLayout.createSequentialGroup()
                                                .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(inputMessageLabel)
                                                        .addGroup(messagePanelLayout.createSequentialGroup()
                                                                .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                        .addComponent(convertButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                                                                        .addComponent(convertedMessageScroll, javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(convertedMessageLabel, javax.swing.GroupLayout.Alignment.LEADING))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(encodedLabel)
                                                                        .addComponent(encodedScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                                                                        .addComponent(encodeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(transmittedScroll)
                                                                        .addComponent(transmittedLabel)
                                                                        .addComponent(transmitButton, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(decodedLabel)
                                                                        .addComponent(decodedScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                                                                        .addComponent(decodeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                        .addComponent(transmittedStringLabel)
                                                        .addComponent(decodedStringLabel))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        messagePanelLayout.setVerticalGroup(
                messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(messagePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(inputMessageLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(convertedMessageLabel)
                                        .addComponent(encodedLabel)
                                        .addComponent(transmittedLabel)
                                        .addComponent(decodedLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(decodedScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                        .addComponent(transmittedScroll)
                                        .addComponent(convertedMessageScroll)
                                        .addComponent(encodedScroll))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(convertButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(encodeButton)
                                        .addComponent(transmitButton)
                                        .addComponent(decodeButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(transmittedStringLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(transmittedStringText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(decodedStringLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(decodedStringText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        RMLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        RMLabel.setText("Reed Muller code parameters:");
        RMLabel.setToolTipText("");

        lengthLabel.setText("Length determinator m:");

        orderLabel.setText("Code order r:");

        blockLabel.setText("Block length:");

        messageLabel.setText("Message length:");

        blockText.setEditable(false);

        messageLengthText.setEditable(false);

        distanceLabel.setText("Minimum distance:");

        distanceText.setEditable(false);

        rateLabel.setText("Code rate:");

        rateText.setEditable(false);

        errorLabel.setText("Max error correction:");

        errorText.setEditable(false);

        generatorLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        generatorLabel.setText("Encoding block and code generator matrix:");

        rowScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        rowText.setEditable(false);
        rowText.setColumns(20);
        rowText.setRows(5);
        rowScroll.setViewportView(rowText);

        signLabel.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        signLabel.setText("⊙");

        generatorText.setEditable(false);
        generatorText.setColumns(20);
        generatorText.setRows(5);
        generatorScroll.setViewportView(generatorText);

        sliderLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        sliderLabel.setText("Animation velocity:");

        parametersLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        parametersLabel.setText("Parameters:");

        highlightCheckBox.setText("Highlight animation");

        animationCheckBox.setText("Perform animation");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(messagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(RMLabel)
                                                                .addGap(33, 33, 33)
                                                                .addComponent(generatorLabel))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(lengthLabel)
                                                                        .addComponent(orderLabel)
                                                                        .addComponent(blockLabel)
                                                                        .addComponent(messageLabel)
                                                                        .addComponent(distanceLabel)
                                                                        .addComponent(rateLabel)
                                                                        .addComponent(errorLabel))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(errorText, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                                .addComponent(distanceText)
                                                                                .addComponent(messageLengthText)
                                                                                .addComponent(blockText)
                                                                                .addComponent(lengthText, javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(orderText, javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(rateText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addGap(18, 18, 18)
                                                                .addComponent(rowScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(signLabel)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(generatorScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(slider, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(sliderLabel)
                                                                        .addComponent(parametersLabel)
                                                                        .addComponent(highlightCheckBox)
                                                                        .addComponent(animationCheckBox))))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(messagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(RMLabel)
                                        .addComponent(generatorLabel))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(89, 89, 89)
                                                .addComponent(signLabel))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(lengthLabel)
                                                                        .addComponent(lengthText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(orderLabel)
                                                                        .addComponent(orderText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(blockLabel)
                                                                        .addComponent(blockText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(messageLabel)
                                                                        .addComponent(messageLengthText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(distanceLabel)
                                                                        .addComponent(distanceText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(rateText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(rateLabel))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(errorLabel)
                                                                        .addComponent(errorText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addComponent(rowScroll)
                                                        .addComponent(generatorScroll)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(sliderLabel)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(slider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(parametersLabel)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(animationCheckBox)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(highlightCheckBox)))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // End of variables declaration//GEN-END:variables
}