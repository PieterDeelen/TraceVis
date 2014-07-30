/*
 * TimeLineViewListener.java
 *
 * Author: Pieter Deelen
 * Created: October 5, 2005, 12:57 PM
 *
 */

package tracevis.visualization.timeline;

import java.util.EventListener;

/**
 * A time line view listener listens for changes in the time line view.
 * @author Pieter Deelen
 */
public interface TimeLineViewListener extends EventListener {
	/**
	 * Indicates that the view range has changed.
	 */
	public void viewRangeChanged();
}
