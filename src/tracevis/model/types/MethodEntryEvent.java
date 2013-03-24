/*
 * MethodEntryEvent.java
 *
 * Author: Pieter Deelen
 * Created: September 7, 2005, 1:22 PM
 *
 */

package tracevis.model.types;


/**
 * An event which indicates that a thread has entered a method.
 * @author Pieter Deelen
 */
public class MethodEntryEvent extends AbstractEvent {
	private final long threadID;
	private final String className;
	private final String methodName;
	private final long objectID;

	private Frame frame;

	/**
	 * Creates a new instance of MethodEntryEvent.
	 * @param timeStamp the time this event occurred.
	 * @param threadID the thread entering this method.
	 * @param className the name of the class defining the entered method.
	 * @param methodName the name of the entered method.
	 * @param objectID the current object.
	 */
	public MethodEntryEvent(long timeStamp, long threadID,
	                        String className, String methodName, long objectID) {
		super(timeStamp);
		this.threadID = threadID;
		this.className = className;
		this.methodName = methodName;
		this.objectID = objectID;
	}

	@Override
	public String toString() {
		return "MN:" + getTime() + ":" + getThreadID() + ":" + getClassName() + ":" + getMethodName();
	}

	public long getThreadID() {
		return threadID;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	public long getObjectID() {
		return objectID;
	}

	public Frame getFrame() {
		return frame;
	}

	public void setFrame(Frame frame) {
		this.frame = frame;
	}
}
