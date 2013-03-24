/*
 * ThreadStartEvent.java
 *
 * Author: Pieter Deelen
 * Created: September 7, 2005, 3:21 PM
 *
 */

package tracevis.model.types;

/**
 * An event which indicates that a thread has been started.
 * @author Pieter Deelen
 */
public class ThreadStartEvent extends AbstractEvent {
	private final long threadID;

	/**
	 * Creates a new instance of ThreadStartEvent.
	 * @param timeStamp the time this event occurred.
	 * @param threadID the identifier of the started thread.
	 */
	public ThreadStartEvent(long timeStamp, long threadID) {
		super(timeStamp);
		this.threadID = threadID;
	}

	@Override
	public String toString() {
		return "TB:" + getTime() + ":" + threadID;
	}

	public long getThreadID() {
		return threadID;
	}
}
