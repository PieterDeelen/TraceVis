/*
 * MyTreeRenderer.java
 *
 * Author: Huub van de Wetering
 * Created: April 28, 2006, 1:26 PM
 *
 */

package tracevis.utilities.treenode;

/**
 *
 * @author Huub van de Wetering
 */

public class TreeNodeRenderer<RenderWidget extends TreeNodeWidget<Value>, Value extends TreeNodeValue> implements javax.swing.tree.TreeCellRenderer {
	private final RenderWidget widget;

    public TreeNodeRenderer(RenderWidget w) {
        widget =w;
    }

    @Override
	public java.awt.Component getTreeCellRendererComponent(
            javax.swing.JTree tree,
            Object value,
            boolean sel,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {
        return widget.getTreeCellRenderComponent(tree,value, sel,expanded,leaf,row,hasFocus);
    }
}
