/*
 * LayoutSettingsDialog.java
 *
 * Author: Pieter Deelen
 * Created: October 19, 2005, 2:40 PM
 *
 */

package tracevis.control.settings;

import java.awt.Frame;
import java.util.LinkedList;
import java.util.List;

import tracevis.visualization.structure.CustomSpringLayout;
import tracevis.visualization.structure.VertexSizeSettings;

/**
 * A dialog which allows the user to configure the graph layout.
 * @author  Pieter Deelen
 */
public class LayoutSettingsDialog extends javax.swing.JDialog {
	private final List<SettingsListener> listeners;

	private final CustomSpringLayoutSettings layoutSettingsPanel;
	private final VertexSizeFunctionSelector vertexSizeSettingsPanel;

	/**
	 * Creates new form LayoutSettingsDialog.
	 */
	public LayoutSettingsDialog(CustomSpringLayout.Settings layoutSettings,
	                            VertexSizeSettings vertexSizeSettings) {
		super((Frame)null, false);
		listeners = new LinkedList<SettingsListener>();

		initComponents();

		layoutSettingsPanel = new CustomSpringLayoutSettings(layoutSettings);
		settingsPanel.add(layoutSettingsPanel);

		vertexSizeSettingsPanel = new VertexSizeFunctionSelector(vertexSizeSettings);
		settingsPanel.add(vertexSizeSettingsPanel);

		pack();
	}

	public CustomSpringLayout.Settings getLayoutSettings() {
		return layoutSettingsPanel.getSettings();
	}

	public VertexSizeSettings getVertexSizeSettings() {
		return vertexSizeSettingsPanel.getSettings();
	}

	public void addListener(SettingsListener listener) {
		listeners.add(listener);
	}

	public void removeListener(SettingsListener listener) {
		listeners.remove(listener);
	}

	private void fireSettingsChanged() {
		for (SettingsListener listener : listeners) {
			listener.settingsChanged(this);
		}
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        settingsPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Layout Settings");
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(okButton);

        applyButton.setText("Apply");
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(applyButton);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(cancelButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

        settingsPanel.setLayout(new javax.swing.BoxLayout(settingsPanel, javax.swing.BoxLayout.Y_AXIS));

        getContentPane().add(settingsPanel, java.awt.BorderLayout.CENTER);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
		dispose();
	}//GEN-LAST:event_cancelButtonActionPerformed

	private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
		fireSettingsChanged();
	}//GEN-LAST:event_applyButtonActionPerformed

	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
		fireSettingsChanged();
		dispose();
	}//GEN-LAST:event_okButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton okButton;
    private javax.swing.JPanel settingsPanel;
    // End of variables declaration//GEN-END:variables

}