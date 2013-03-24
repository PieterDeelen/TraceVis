/*
 * AbstractEvent.java
 *
 * Author: Pieter Deelen
 * Created: April 11, 2006, 4:23 PM
 *
 */

package tracevis.model.types;

/**
 *
 * @author Pieter Deelen
 */
public abstract class AbstractEvent implements Event {
	private final long timeStamp;
	private boolean filtered;

	/**
	 * Creates a new instance of AbstractEvent.
	 */
	public AbstractEvent(long timeStamp) {
		this.timeStamp = timeStamp;
		this.filtered = false;
	}

	@Override
	public long getTime() {
		return timeStamp;
	}

	@Override
	public void setFiltered(boolean filtered) {
		this.filtered = filtered;
	}

	@Override
	public boolean isFiltered() {
		return filtered;
	}
}
