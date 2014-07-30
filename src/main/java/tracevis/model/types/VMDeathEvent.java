/*
 * VMDeathEvent.java
 *
 * Author: Pieter Deelen
 * Created: September 12, 2005, 2:52 PM
 *
 */

package tracevis.model.types;

/**
 * An event which indicates that the JVM has terminated.
 * @author Pieter Deelen
 */
public class VMDeathEvent extends AbstractEvent {
	/**
	 * Creates a new instance of VMDeathEvent.
	 * @param timeStamp the time this event occurred.
	 */
	public VMDeathEvent(long timeStamp) {
		super(timeStamp);
	}

	@Override
	public String toString() {
		return "VD:" + getTime();
	}
}