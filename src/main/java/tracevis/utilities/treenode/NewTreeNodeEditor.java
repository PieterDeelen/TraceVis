/*
 * NewTreeNodeEditor.java
 *
 * Author: Huub van de Wetering
 * Created: May 4, 2006, 2:32 PM
 *
 */

package tracevis.utilities.treenode;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;

/**
 * 
 * @author Huub van de Wetering
 */
public class NewTreeNodeEditor<EditorWidget extends TreeNodeWidget<Value>, Value extends TreeNodeValue<Value>>
		extends AbstractCellEditor implements TreeCellEditor {

	private final EditorWidget widget;

	/** Creates a new instance of NewTreeNodeEditor */
	public NewTreeNodeEditor(EditorWidget widget) {
		this.widget = widget;
	}

	Value nodeValue; // nodeValue stored in node

	@Override
	public Value getCellEditorValue() {
		return nodeValue;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		nodeValue = (Value) node.getUserObject();
		return widget.getTreeCellEditorComponent(tree, value, isSelected,
				expanded, leaf, row);
	}

	@Override
	public boolean stopCellEditing() {
		// change the userObject beloging to the current tree node
		nodeValue.setValue(widget.getValue());
		fireEditingStopped();
		return false; // continue editing till cancelation
	}

	@Override
	public void cancelCellEditing() {
		stopCellEditing();
		super.cancelCellEditing();
	}

	/** editing starts after middle mouse button event. **/
	@Override
	public boolean isCellEditable(EventObject anEvent) {
		if (anEvent instanceof MouseEvent) {
			MouseEvent mouse = (MouseEvent) anEvent;
			return mouse.getButton() == MouseEvent.BUTTON2;
		}
		return true;
	}
}
