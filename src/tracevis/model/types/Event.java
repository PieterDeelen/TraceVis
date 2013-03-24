/*
 * Event.java
 *
 * Author: Pieter Deelen
 * Created: September 7, 2005, 1:17 PM
 *
 */

package tracevis.model.types;

/**
 * Specifies the methods all event classes should have.
 * @author Pieter Deelen
 */
public interface Event {
	/**
	 * Returns the time the event occurred.
	 */
	public long getTime();

	public void setFiltered(boolean filtered);

	public boolean isFiltered();
}
