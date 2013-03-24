/*
 * NodeWidget.java
 *
 * Author: Huub van de Wetering
 * Created: April 28, 2006, 4:54 PM
 *
 */

package tracevis.utilities.treenode;

import java.awt.Component;

import javax.swing.JTree;

/**
 *h
 * @author Huub van de Wetering
 */
public interface TreeNodeWidget<Value> {
    public void setValue(Value v);
    public Value getValue();
    Component getTreeCellEditorComponent(JTree tree, Object value,
					 boolean isSelected, boolean expanded,
					 boolean leaf, int row);
    Component getTreeCellRenderComponent(JTree tree, Object value,
					 boolean isSelected, boolean expanded,
					 boolean leaf, int row, boolean hasFocus);
}
