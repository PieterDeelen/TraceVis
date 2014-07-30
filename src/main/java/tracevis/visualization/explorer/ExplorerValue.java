/*
 * NodeValue.java
 *
 * Author: Huub van de Wetering
 * Created: April 24, 2006, 9:26 AM
 *
 */

package tracevis.visualization.explorer;

import java.awt.Color;

import tracevis.utilities.treenode.TreeNodeValue;
import edu.uci.ics.jung.graph.Vertex;

/**
 *
 * @author Huub van de Wetering
 */
public class ExplorerValue implements TreeNodeValue<ExplorerValue> {
    private Color color;
    private String name;
    private boolean selected;
    private final Vertex v;

    public ExplorerValue(Vertex v, String text, Color color, boolean isSelected) {
        this.color = color;
        this.name = text;
        this.selected = isSelected;
        this.v = v;
    }

    String getText() {
        return name;
    }

    String getShortText() {
        return name.substring(name.lastIndexOf('.')+1);
    }

    public void setColor(Color c) {
        color = c;
    }

    public Color getColor() {
        return color;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
	public void setValue(ExplorerValue v) {
        name = v.getText();
        color = v.getColor();
        selected = v.isSelected();
    }

    @Override
	public String toString() {
        return "[" + name + "," + color + "," + selected + "]";
    }

    public Vertex getVertex() {
        return v;
    }
}
