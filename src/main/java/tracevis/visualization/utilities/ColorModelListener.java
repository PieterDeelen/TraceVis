/*
 * ColorModelListener.java
 *
 * Author: Pieter Deelen
 * Created: March 29, 2006, 10:14 AM
 *
 */

package tracevis.visualization.utilities;

/**
 * A color model listener listens to changes in the color model.
 * @author Pieter Deelen
 */
public interface ColorModelListener {
	/**
	 * Indicates that one or more colors have changed.
	 */
	public void colorsChanged();
}
