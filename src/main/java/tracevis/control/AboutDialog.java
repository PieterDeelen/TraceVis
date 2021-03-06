/*
 * AboutDialog.java
 *
 * Created on January 2, 2006, 12:42 PM
 */

package tracevis.control;

/**
 * The about dialog.
 * @author  Pieter Deelen
 */
public class AboutDialog extends javax.swing.JDialog {

	/**
	 * Creates new form AboutDialog.
	 */
	public AboutDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        aboutPanel = new javax.swing.JPanel();
        programNameLabel = new javax.swing.JLabel();
        descriptionLabel = new javax.swing.JLabel();
        copyrightLabel = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About");
        setResizable(false);
        aboutPanel.setLayout(new java.awt.GridBagLayout());

        aboutPanel.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("controlShadow")));
        programNameLabel.setFont(new java.awt.Font("Dialog", 1, 24));
        programNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        programNameLabel.setText("TraceVis");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 5, 15);
        aboutPanel.add(programNameLabel, gridBagConstraints);

        descriptionLabel.setFont(new java.awt.Font("Dialog", 1, 14));
        descriptionLabel.setText("A Software Visualization Tool");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 10, 15);
        aboutPanel.add(descriptionLabel, gridBagConstraints);

        copyrightLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        copyrightLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        copyrightLabel.setText("(C) 2005-2006 Pieter Deelen and Huub van de Wetering");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 5, 15);
        aboutPanel.add(copyrightLabel, gridBagConstraints);

        getContentPane().add(aboutPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(closeButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
		setVisible(false);
	}//GEN-LAST:event_closeButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel aboutPanel;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel copyrightLabel;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JLabel programNameLabel;
    // End of variables declaration//GEN-END:variables

}
