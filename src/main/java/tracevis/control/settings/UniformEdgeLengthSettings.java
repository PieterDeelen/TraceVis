/*
 * UniformEdgeLengthSettings.java
 *
 * Author: Pieter Deelen
 * Created: August 29, 2005, 10:40 AM
 *
 */

package tracevis.control.settings;

import javax.swing.SpinnerNumberModel;

import tracevis.visualization.functions.Function;
import tracevis.visualization.functions.UniformEdgeLengthFunction;

/**
 * A configuration panel which allows the user the configure the uniform edge
 * length function.
 * @author  Pieter Deelen
 */
public class UniformEdgeLengthSettings extends FunctionSettings {
	private final UniformEdgeLengthFunction uniformEdgeLengthFunction;

	/**
	 * Creates new form UniformEdgeLengthSettings.
	 */
	public UniformEdgeLengthSettings(UniformEdgeLengthFunction uniformEdgeLengthFunction) {
		this.uniformEdgeLengthFunction = uniformEdgeLengthFunction;
		initComponents();
	}

	@Override
	public Function getFunction() {
		return uniformEdgeLengthFunction;
	}

	@Override
	public String getFunctionName() {
		return "Uniform";
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        edgeLengthLabel = new javax.swing.JLabel();
        edgeLengthSpinner = new javax.swing.JSpinner();
        fillerPanel = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        edgeLengthLabel.setText("Edge Length");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(edgeLengthLabel, gridBagConstraints);

        edgeLengthSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 10));
        edgeLengthSpinner.setValue(uniformEdgeLengthFunction.getLength());
        edgeLengthSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                edgeLengthSpinnerStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(edgeLengthSpinner, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(fillerPanel, gridBagConstraints);

    }
    // </editor-fold>//GEN-END:initComponents

	private void edgeLengthSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_edgeLengthSpinnerStateChanged
		Integer edgeLength = (Integer)edgeLengthSpinner.getValue();
		uniformEdgeLengthFunction.setLength(edgeLength);
	}//GEN-LAST:event_edgeLengthSpinnerStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel edgeLengthLabel;
    private javax.swing.JSpinner edgeLengthSpinner;
    private javax.swing.JPanel fillerPanel;
    // End of variables declaration//GEN-END:variables

}
