/*
 * MethodExitEvent.java
 *
 * Author: Pieter Deelen
 * Created: September 7, 2005, 2:01 PM
 *
 */

package tracevis.model.types;



/**
 * An event which indicates that a thread has left a method.
 * @author Pieter Deelen
 */
public class MethodExitEvent extends AbstractEvent {
	private final long threadID;
	private final String className;
	private final String methodName;

	private Frame frame;

	/**
	 * Creates a new instance of MethodExitEvent.
	 * @param timeStamp the time this event occurred.
	 * @param threadID the thread leaving this method.
	 * @param className the name of the class defining the left method.
	 * @param methodName the name of the left method.
	 */
	public MethodExitEvent(long timeStamp, long threadID,
	                       String className, String methodName)
	{
		super(timeStamp);
		this.threadID = threadID;
		this.className = className;
		this.methodName = methodName;

		this.frame = null;
	}

	@Override
	public String toString() {
		return "MX:" + getTime() + ":" + threadID + ":" + className + ":" + methodName;
	}

	public Frame getFrame() {
		return frame;
	}

	public void setFrame(Frame frame) {
		this.frame = frame;
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
}
