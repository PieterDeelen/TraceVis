/*
 * MyTreeCell.java
 *
 * Author: Huub van de Wetering
 * Created: April 19, 2006, 1:31 PM
 */

package tracevis.visualization.explorer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;

import tracevis.utilities.treenode.NewTreeNodeEditor;
import tracevis.utilities.treenode.TreeNodeWidget;

/**
 *
 * @author Huub van de Wetering
 */
public class ExplorerWidget extends javax.swing.JPanel implements TreeNodeWidget<ExplorerValue> {

	private final NewTreeNodeEditor<ExplorerWidget, ExplorerValue> editor;
	private int count=0;
	private String fullQualifiedName;

	private static int total=0;

    /** Creates new form MyTreeCell */
    public ExplorerWidget() {
        this(false);
    }

    public JComponent getComponent() {
        return this;
    }

    @Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        ExplorerValue nodeValue = (ExplorerValue)node.getUserObject();
            this.setValue(nodeValue);
            this.setOpaque(false);
        return this;
    }

    @Override
	public Component getTreeCellRenderComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
            ExplorerValue nodeValue = (ExplorerValue)node.getUserObject();
            this.setValue(nodeValue);
            this.setOpaque(isSelected);
            if (isSelected) {
                this.setBackground(UIManager.getLookAndFeelDefaults().getColor("Button.select"));
            }
            return this;
    }

    public NewTreeNodeEditor<ExplorerWidget, ExplorerValue> getCellEditor() {
        return editor;
    }

    public ExplorerWidget(boolean editable) {
        editor = editable ? new NewTreeNodeEditor<ExplorerWidget, ExplorerValue>(this): null;
        initComponents();
        colorButton.setEnabled(true);
        checkBox.setEnabled(true);
        slider.setVisible(editable);
        checkBox.setVisible(false); // no need for it right now
        colorChooserFrame.pack();
        count=total; total++;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        colorChooserFrame = new javax.swing.JFrame();
        jColorChooser1 = new javax.swing.JColorChooser();
        colorButton = new javax.swing.JButton();
        checkBox = new javax.swing.JCheckBox();
        label = new javax.swing.JLabel();
        slider = new javax.swing.JSlider();

        colorChooserFrame.getContentPane().add(jColorChooser1, java.awt.BorderLayout.CENTER);

        setBackground(new java.awt.Color(255, 255, 204));
        setOpaque(false);
        colorButton.setBackground(new java.awt.Color(255, 51, 51));
        colorButton.setMaximumSize(new java.awt.Dimension(15, 15));
        colorButton.setMinimumSize(new java.awt.Dimension(15, 15));
        colorButton.setPreferredSize(new java.awt.Dimension(15, 15));
        add(colorButton);

        checkBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        checkBox.addItemListener(new java.awt.event.ItemListener() {
            @Override
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkBoxItemStateChanged(evt);
            }
        });

        add(checkBox);

        label.setText("jLabel1");
        add(label);

        slider.setMaximum(360);
        slider.setPreferredSize(new java.awt.Dimension(100, 16));
        slider.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderStateChanged(evt);
            }
        });

        add(slider);

    }// </editor-fold>//GEN-END:initComponents

    private void sliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderStateChanged
        float hue = (1.0f*slider.getValue())/slider.getMaximum();
        colorButton.setBackground(Color.getHSBColor(hue,1,1));
        //if (editor!=null) editor.stopCellEditing();
    }//GEN-LAST:event_sliderStateChanged

    private void checkBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkBoxItemStateChanged
        //if (editor!=null) editor.stopCellEditing();
    }//GEN-LAST:event_checkBoxItemStateChanged

    void setText(String s) {
        fullQualifiedName = s;
        int i = s.lastIndexOf('.');
        label.setText(s.substring(i+1));
    }

    String getText() {
        return fullQualifiedName;
    }

    boolean isSelected() {
        return checkBox.isSelected();
    }

    void setSelected(boolean on) {
        checkBox.setSelected(on);
    }

    void setColor(Color color) {
        colorButton.setBackground(color);
        int r =color.getRed(), g = color.getGreen(), b = color.getBlue();
        float hue = Color.RGBtoHSB(r,g,b,null)[0];

        slider.setValue((int)(hue*slider.getMaximum()));
    }

    java.awt.Color getColor() {
        return colorButton.getBackground();
    }

    @Override
	public ExplorerValue getValue() {
        return new ExplorerValue(null,fullQualifiedName, colorButton.getBackground(), checkBox.isSelected());
    }

    @Override
	public void setValue(ExplorerValue v) {
        setText(v.getText());
        setColor(v.getColor());
        setSelected(v.isSelected());
        this.setOpaque(true);
    }

    @Override
	public String toString() {
        return "ExplorerWidget("+count+")";
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkBox;
    private javax.swing.JButton colorButton;
    private javax.swing.JFrame colorChooserFrame;
    private javax.swing.JColorChooser jColorChooser1;
    private javax.swing.JLabel label;
    private javax.swing.JSlider slider;
    // End of variables declaration//GEN-END:variables
}