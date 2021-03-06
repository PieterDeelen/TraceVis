/*
 * ColorCustomizeDialog.java
 *
 * Created on March 15, 2006, 2:58 PM
 */

package tracevis.visualization.detail;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import tracevis.model.types.ClassData;
import tracevis.utilities.StringUtils;
import tracevis.visualization.utilities.ColorModel;
import edu.uci.ics.jung.graph.Vertex;


/**
 * A dialog which allows the user to specify the hue for one or more classes.
 * @author  Pieter Deelen
 */
public class ColorCustomizeDialog extends javax.swing.JDialog {
	private int row;
	private final Vertex vertex;
	private final ColorModel colorModel;

	private class HueGradient extends JLabel {
		public HueGradient() {}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D)g;

			Dimension size = getSize();
			float hue = 0.0f;
			float hueSlope = 1.0f / size.width;
			for (int x = 0; x < size.width; x++) {
				g2d.setColor(Color.getHSBColor(hue, 1.0f, 1.0f));
				Line2D line = new Line2D.Float(x, 0, x, size.height);
				g2d.draw(line);
				hue += hueSlope;
			}
		}

		@Override
		public Dimension getMinimumSize() {
			return new Dimension(200, 50);
		}
	}

	/**
	 * Creates new form ColorCustomizeDialog
	 * @param colorModel the color model.
	 * @param vertex the vertex to color.
	 * */
	public ColorCustomizeDialog(ColorModel colorModel, Vertex vertex) {
		super((Frame)null, true);
		initComponents();

		this.colorModel = colorModel;

		this.vertex = vertex;
		ClassData classData = (ClassData)vertex.getUserDatum("tracevis.model.Program");

		row = 0;
		addOption(classData.getName(), true);

		List<String> packageIdentifiers = new ArrayList(classData.getPackageIdentifiers());
		while (packageIdentifiers.size() > 0) {
			String packageName = StringUtils.joinStrings(packageIdentifiers.iterator(), ".") + ".*";
			addOption(packageName, false);
			packageIdentifiers.remove(packageIdentifiers.size() - 1);
		}
		addOption("all classes", false);

		pack();
	}

	private void addOption(String name, boolean selected) {
		JRadioButton radioButton = new JRadioButton(name, selected);
		classButtonGroup.add(radioButton);

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = row;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		classSelector.add(radioButton, gridBagConstraints);

		row++;
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        classButtonGroup = new javax.swing.ButtonGroup();
        colorButtonGroup = new javax.swing.ButtonGroup();
        colorPanel = new javax.swing.JPanel();
        classSelector = new javax.swing.JPanel();
        colorLabel = new javax.swing.JLabel();
        usingLabel = new javax.swing.JLabel();
        selectedColorButton = new javax.swing.JRadioButton();
        defaultColorButton = new javax.swing.JRadioButton();
        hueChooser = new javax.swing.JPanel();
        hueSlider = new javax.swing.JSlider();
        hueLabel = hueLabel = new HueGradient();
        hueTextLabel = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select Color");
        colorPanel.setLayout(new java.awt.GridBagLayout());

        classSelector.setLayout(new java.awt.GridBagLayout());

        colorLabel.setText("Color");
        classSelector.add(colorLabel, new java.awt.GridBagConstraints());

        usingLabel.setText("using");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        classSelector.add(usingLabel, gridBagConstraints);

        colorButtonGroup.add(selectedColorButton);
        selectedColorButton.setSelected(true);
        selectedColorButton.setText("selected color");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        classSelector.add(selectedColorButton, gridBagConstraints);

        colorButtonGroup.add(defaultColorButton);
        defaultColorButton.setText("default color");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        classSelector.add(defaultColorButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        colorPanel.add(classSelector, gridBagConstraints);

        hueChooser.setLayout(new java.awt.GridBagLayout());

        hueSlider.setMaximum(360);
        hueSlider.setPaintTrack(false);
        hueSlider.setValue(240);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        hueChooser.add(hueSlider, gridBagConstraints);

        hueLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hueLabel.setText("Hue Palette");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        hueChooser.add(hueLabel, gridBagConstraints);

        hueTextLabel.setText("Hue");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        hueChooser.add(hueTextLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        colorPanel.add(hueChooser, gridBagConstraints);

        getContentPane().add(colorPanel, java.awt.BorderLayout.CENTER);

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
    }
    // </editor-fold>//GEN-END:initComponents

	private void colorClasses() {
		vertex.getGraph();

		String classes = null;
		Enumeration<AbstractButton> buttons = classButtonGroup.getElements();
		while(buttons.hasMoreElements()) {
			AbstractButton button = buttons.nextElement();
			if (button.isSelected()) {
				classes = button.getText();
				break;
			}
		}

		String pattern = (classes.equals("all classes")) ? "*" : classes;

		if (selectedColorButton.isSelected()) {
			float selectedHue = hueSlider.getValue() / 360.0f;
			colorModel.setHue(pattern, selectedHue);
		} else /*if (defaultColorButton.isSelected())*/ {
			colorModel.resetHue(pattern);
		}
	}

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
		dispose();
	}//GEN-LAST:event_cancelButtonActionPerformed

	private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
		colorClasses();
	}//GEN-LAST:event_applyButtonActionPerformed

	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
		colorClasses();
		dispose();
	}//GEN-LAST:event_okButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.ButtonGroup classButtonGroup;
    private javax.swing.JPanel classSelector;
    private javax.swing.ButtonGroup colorButtonGroup;
    private javax.swing.JLabel colorLabel;
    private javax.swing.JPanel colorPanel;
    private javax.swing.JRadioButton defaultColorButton;
    private javax.swing.JPanel hueChooser;
    private javax.swing.JLabel hueLabel;
    private javax.swing.JSlider hueSlider;
    private javax.swing.JLabel hueTextLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JRadioButton selectedColorButton;
    private javax.swing.JLabel usingLabel;
    // End of variables declaration//GEN-END:variables

}
