/*
 * TimeLineSettingsDialog.java
 *
 * Created on October 28, 2005, 3:32 PM
 */

package tracevis.control.settings;

import java.awt.Frame;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SpinnerNumberModel;

import tracevis.visualization.functions.Scale;
import tracevis.visualization.timeline.TimeLineSettings;

/**
 * A dialog which allows the user to configure the time line view.
 * @author  Pieter Deelen
 */
public class TimeLineSettingsDialog extends javax.swing.JDialog {
	private final List<SettingsListener> listeners;

	/** Creates new form TimeLineSettingsDialog */
	public TimeLineSettingsDialog(TimeLineSettings timeLineSettings) {
		super((Frame)null, false);
		initComponents();

		switch (timeLineSettings.getMode()) {
			case SHOW_ACTIVITY:
				showActivityButton.setSelected(true);
				break;
			case SHOW_INSTANCES:
				showInstancesButton.setSelected(true);
				break;
		}

		switch (timeLineSettings.getScale()) {
			case LINEAR:
				scaleChooser.setSelectedItem("Linear");
				break;
			case SQUARE_ROOT:
				scaleChooser.setSelectedItem("Square Root");
				break;
			case LOGARITHMIC:
				scaleChooser.setSelectedItem("Logarithmic");
		}

		activityExponentSpinner.setValue(timeLineSettings.getActivityExponent());

		listeners = new LinkedList<SettingsListener>();
	}

	public TimeLineSettings getTimeLineSettings() {
		TimeLineSettings.Mode mode;
		if (showActivityButton.isSelected()) {
			mode = TimeLineSettings.Mode.SHOW_ACTIVITY;
		} else if (showInstancesButton.isSelected()) {
			mode = TimeLineSettings.Mode.SHOW_INSTANCES;
		} else {
			throw new RuntimeException();
		}

		Scale scale;
		String selectedScale = (String)scaleChooser.getSelectedItem();
		if (selectedScale.equals("Linear")) {
			scale = Scale.LINEAR;
		} else if (selectedScale.equals("Square Root")) {
			scale = Scale.SQUARE_ROOT;
		} else if (selectedScale.equals("Logarithmic")) {
			scale = Scale.LOGARITHMIC;
		} else {
			throw new RuntimeException();
		}

		double activityExponent = (Double)activityExponentSpinner.getValue();

		return new TimeLineSettings(mode, scale, activityExponent);
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
        java.awt.GridBagConstraints gridBagConstraints;

        showButtonGroup = new javax.swing.ButtonGroup();
        settingsPanel = new javax.swing.JPanel();
        showActivityButton = new javax.swing.JRadioButton();
        showInstancesButton = new javax.swing.JRadioButton();
        dummyPanel = new javax.swing.JPanel();
        activityExponentLabel = new javax.swing.JLabel();
        activityExponentSpinner = new javax.swing.JSpinner();
        scaleLabel = new javax.swing.JLabel();
        scaleChooser = new javax.swing.JComboBox();
        buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Time Line Settings");
        settingsPanel.setLayout(new java.awt.GridBagLayout());

        settingsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("controlShadow")));
        showButtonGroup.add(showActivityButton);
        showActivityButton.setText("Show Activity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        settingsPanel.add(showActivityButton, gridBagConstraints);

        showButtonGroup.add(showInstancesButton);
        showInstancesButton.setText("Show Instances");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        settingsPanel.add(showInstancesButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        settingsPanel.add(dummyPanel, gridBagConstraints);

        activityExponentLabel.setText("Activity Exponent");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 5);
        settingsPanel.add(activityExponentLabel, gridBagConstraints);

        activityExponentSpinner.setModel(new SpinnerNumberModel(0.0, 0.0, 1.0, 0.1));
        activityExponentSpinner.setValue(0.00);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        settingsPanel.add(activityExponentSpinner, gridBagConstraints);

        scaleLabel.setText("Scale");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 30, 5, 5);
        settingsPanel.add(scaleLabel, gridBagConstraints);

        scaleChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Linear", "Square Root", "Logarithmic" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        settingsPanel.add(scaleChooser, gridBagConstraints);

        getContentPane().add(settingsPanel, java.awt.BorderLayout.CENTER);

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

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
    private javax.swing.JLabel activityExponentLabel;
    private javax.swing.JSpinner activityExponentSpinner;
    private javax.swing.JButton applyButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel dummyPanel;
    private javax.swing.JButton okButton;
    private javax.swing.JComboBox scaleChooser;
    private javax.swing.JLabel scaleLabel;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.JRadioButton showActivityButton;
    private javax.swing.ButtonGroup showButtonGroup;
    private javax.swing.JRadioButton showInstancesButton;
    // End of variables declaration//GEN-END:variables

}
