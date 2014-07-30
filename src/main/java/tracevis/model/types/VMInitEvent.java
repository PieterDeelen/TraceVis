/*
 * VMInitEvent.java
 *
 * Author: Pieter Deelen
 * Created: September 12, 2005, 2:52 PM
 *
 */

package tracevis.model.types;

/**
 * An event which indicates that the JVM is initialized.
 * @author Pieter Deelen
 */
public class VMInitEvent extends AbstractEvent {
	/**
	 * Creates a new instance of VMInitEvent.
	 * @param timeStamp the time this event occurred.
	 */
	public VMInitEvent(long timeStamp) {
		super(timeStamp);
	}

	@Override
	public String toString() {
		return "VI:" + getTime();
	}
}
