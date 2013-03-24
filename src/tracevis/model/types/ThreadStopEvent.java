/*
 * ThreadStopEvent.java
 *
 * Author: Pieter Deelen
 * Created: September 7, 2005, 3:21 PM
 *
 */

package tracevis.model.types;

/**
 * An event which indicates that a thread has stopped.
 * @author Pieter Deelen
 */
public class ThreadStopEvent extends AbstractEvent {
	private final long threadID;

	/**
	 * Creates a new instance of ThreadStopEvent.
	 * @param timeStamp the time this event occurred.
	 * @param threadID the identifier of the stopped thread.
	 */
	public ThreadStopEvent(long timeStamp, long threadID) {
		super(timeStamp);
		this.threadID = threadID;
	}

	@Override
	public String toString() {
		return "TE:" + getTime() + ":" + threadID;
	}

	public long getThreadID() {
		return threadID;
	}
}
