/*
 * VMStartEvent.java
 *
 * Author: Pieter Deelen
 * Created: September 12, 2005, 2:52 PM
 *
 */

package tracevis.model.types;

/**
 * An event which indicates that the JVM has started.
 * @author Pieter Deelen
 */
public class VMStartEvent extends AbstractEvent {
	/**
	 * Creates a new instance of VMStartEvent.
	 * @param timeStamp the time this event occurred.
	 */
	public VMStartEvent(long timeStamp) {
		super(timeStamp);
	}

	@Override
	public String toString() {
		return "VS:" + getTime();
	}
}