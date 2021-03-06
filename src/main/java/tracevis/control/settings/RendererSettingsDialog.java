/*
 * RendererSettingsDialog.java
 *
 * Author: Pieter Deelen
 * Created: October 19, 2005, 2:49 PM
 *
 */

package tracevis.control.settings;

import java.awt.Frame;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SpinnerNumberModel;

import tracevis.visualization.structure.CustomRenderer;
import tracevis.visualization.structure.StructuralView;

/**
 * A dialog which allows the user to configure the renderer of the structural
 * view.
 * @author  Pieter Deelen
 */
public class RendererSettingsDialog extends javax.swing.JDialog {
	private final List<SettingsListener> listeners;

	/**
	 * Creates new form RendererSettingsDialog.
	 */
	public RendererSettingsDialog(CustomRenderer.Settings rendererSettings,
	                              StructuralView.Settings viewSettings)
	{
		super((Frame)null, false);
		listeners = new LinkedList<SettingsListener>();

		initComponents();

		switch (rendererSettings.getColorMode()) {
			case STACK:
				colorModeStack.setSelected(true);
				break;
			case CUSTOM:
				colorModeCustom.setSelected(true);
			default:
				break;
		}

		switch (rendererSettings.getLabelSettings()) {
			case NONE:
				noLabelsButton.setSelected(true);
				break;
			case SHORT:
				shortLabelsButton.setSelected(true);
				break;
			case LONG:
				longLabelsButton.setSelected(true);
				break;
		}

		edgeCurvinessSpinner.setValue((double)rendererSettings.getEdgeCurviness());
		transitionFramesSpinner.setValue(viewSettings.getTransitionFrames());
		drawBackgroundGraphButton.setSelected(rendererSettings.drawBackgroundGraph());
	}

	public CustomRenderer.Settings getRendererSettings() {

		CustomRenderer.ColorMode colorMode = null;
		if (colorModeStack.isSelected()) {
			colorMode = CustomRenderer.ColorMode.STACK;
		} else if (colorModeCustom.isSelected()) {
			colorMode = CustomRenderer.ColorMode.CUSTOM;
		}

		CustomRenderer.LabelSettings labelSettings = null;
		if (noLabelsButton.isSelected()) {
			labelSettings = CustomRenderer.LabelSettings.NONE;
		} else if (shortLabelsButton.isSelected()) {
			labelSettings = CustomRenderer.LabelSettings.SHORT;
		} else if (longLabelsButton.isSelected()) {
			labelSettings = CustomRenderer.LabelSettings.LONG;
		}

		double edgeCurviness = (Double)edgeCurvinessSpinner.getValue();

		boolean drawBackgroundGraph = drawBackgroundGraphButton.isSelected();

		return new CustomRenderer.Settings(colorMode,
		                                   labelSettings, (float)edgeCurviness,
		                                   drawBackgroundGraph);
	}

	public StructuralView.Settings getViewSettings() {
		int transitionFrames = (Integer)transitionFramesSpinner.getValue();
		return new StructuralView.Settings(transitionFrames);
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

        colorModeButtonGroup = new javax.swing.ButtonGroup();
        labelSettingsGroup = new javax.swing.ButtonGroup();
        buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        settingsPanel = new javax.swing.JPanel();
        colorMode = new javax.swing.JLabel();
        colorModeStack = new javax.swing.JRadioButton();
        colorModeCustom = new javax.swing.JRadioButton();
        noLabelsButton = new javax.swing.JRadioButton();
        shortLabelsButton = new javax.swing.JRadioButton();
        longLabelsButton = new javax.swing.JRadioButton();
        vertexLabelsLabel = new javax.swing.JLabel();
        edgeCurvinessLabel = new javax.swing.JLabel();
        edgeCurvinessSpinner = new javax.swing.JSpinner();
        transitionFramesLabel = new javax.swing.JLabel();
        transitionFramesSpinner = new javax.swing.JSpinner();
        fillerPanel = new javax.swing.JPanel();
        drawBackgroundGraphButton = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Renderer Settings");
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

        settingsPanel.setLayout(new java.awt.GridBagLayout());

        settingsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("controlShadow")));
        colorMode.setText("Color Model");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        settingsPanel.add(colorMode, gridBagConstraints);

        colorModeButtonGroup.add(colorModeStack);
        colorModeStack.setText("Stack");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 3, 5);
        settingsPanel.add(colorModeStack, gridBagConstraints);

        colorModeButtonGroup.add(colorModeCustom);
        colorModeCustom.setText("Custom");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 5, 5);
        settingsPanel.add(colorModeCustom, gridBagConstraints);

        labelSettingsGroup.add(noLabelsButton);
        noLabelsButton.setText("No Labels");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 3, 5);
        settingsPanel.add(noLabelsButton, gridBagConstraints);

        labelSettingsGroup.add(shortLabelsButton);
        shortLabelsButton.setText("Short Labels");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 3, 5);
        settingsPanel.add(shortLabelsButton, gridBagConstraints);

        labelSettingsGroup.add(longLabelsButton);
        longLabelsButton.setText("Long Labels");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 5, 5);
        settingsPanel.add(longLabelsButton, gridBagConstraints);

        vertexLabelsLabel.setText("Vertex Labels");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        settingsPanel.add(vertexLabelsLabel, gridBagConstraints);

        edgeCurvinessLabel.setText("Edge Curviness");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        settingsPanel.add(edgeCurvinessLabel, gridBagConstraints);

        edgeCurvinessSpinner.setModel(new SpinnerNumberModel(0.0, 0.0, 50.0, 1.0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        settingsPanel.add(edgeCurvinessSpinner, gridBagConstraints);

        transitionFramesLabel.setText("Transition Frames");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        settingsPanel.add(transitionFramesLabel, gridBagConstraints);

        transitionFramesSpinner.setModel(new SpinnerNumberModel(0, 0, 50, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        settingsPanel.add(transitionFramesSpinner, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        settingsPanel.add(fillerPanel, gridBagConstraints);

        drawBackgroundGraphButton.setText("Draw Background Graph");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        settingsPanel.add(drawBackgroundGraphButton, gridBagConstraints);

        getContentPane().add(settingsPanel, java.awt.BorderLayout.NORTH);

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
    private javax.swing.JButton applyButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel colorMode;
    private javax.swing.ButtonGroup colorModeButtonGroup;
    private javax.swing.JRadioButton colorModeCustom;
    private javax.swing.JRadioButton colorModeStack;
    private javax.swing.JCheckBox drawBackgroundGraphButton;
    private javax.swing.JLabel edgeCurvinessLabel;
    private javax.swing.JSpinner edgeCurvinessSpinner;
    private javax.swing.JPanel fillerPanel;
    private javax.swing.ButtonGroup labelSettingsGroup;
    private javax.swing.JRadioButton longLabelsButton;
    private javax.swing.JRadioButton noLabelsButton;
    private javax.swing.JButton okButton;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.JRadioButton shortLabelsButton;
    private javax.swing.JLabel transitionFramesLabel;
    private javax.swing.JSpinner transitionFramesSpinner;
    private javax.swing.JLabel vertexLabelsLabel;
    // End of variables declaration//GEN-END:variables

}
