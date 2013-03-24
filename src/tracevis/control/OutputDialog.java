/*
 * OutputDialog.java
 *
 * Created on September 16, 2005, 12:05 PM
 */

package tracevis.control;

import java.io.OutputStream;

import javax.swing.JDialog;
import javax.swing.text.DefaultCaret;

/**
 * A dialog which shows the output of a running program.
 * @author  Pieter Deelen
 */
class OutputDialog extends javax.swing.JDialog {
	private final OutputStream outputStream;
	private final OutputStream errorStream;

	/** Creates new form OutputDialog */
	public OutputDialog(JDialog parent, boolean modal) {
		super(parent, modal);
		initComponents();

		outputStream = new OutputStream() {
			@Override
			public void write(int b) {
				outputArea.append(Character.toString((char)b));
			}

			@Override
			public void write(byte[] b) {
				outputArea.append(new String(b));
			}

			@Override
			public void write(byte[] b, int off, int len) {
				outputArea.append(new String(b, off, len));
			}
		};

		errorStream = new OutputStream() {
			@Override
			public void write(int b) {
				outputArea.append(Character.toString((char)b));
			}

			@Override
			public void write(byte[] b) {
				outputArea.append(new String(b));
			}

			@Override
			public void write(byte[] b, int off, int len) {
				outputArea.append(new String(b, off, len));
			}
		};
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        closeButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();
        outputScroller = new javax.swing.JScrollPane();
        outputArea = new javax.swing.JTextArea();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        closeButton.setText("Close");
        closeButton.setEnabled(false);
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(closeButton, gridBagConstraints);

        statusLabel.setText("Program Running");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(statusLabel, gridBagConstraints);

        outputArea.setColumns(80);
        outputArea.setFont(new java.awt.Font("Monospaced", 0, 10));
        outputArea.setRows(25);
        outputArea.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        DefaultCaret caret = (DefaultCaret)outputArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        outputScroller.setViewportView(outputArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(outputScroller, gridBagConstraints);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents

	private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
		dispose();
	}//GEN-LAST:event_closeButtonActionPerformed

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public OutputStream getErrorStream() {
		return errorStream;
	}

	public void programFinished() {
		closeButton.setEnabled(true);
		statusLabel.setText("Program Stopped");
	}

	public void showError(String message) {
		closeButton.setEnabled(true);
		statusLabel.setText("Failed to load program: " + message);
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JTextArea outputArea;
    private javax.swing.JScrollPane outputScroller;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables

}
