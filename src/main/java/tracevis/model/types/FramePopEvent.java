/*
 * FramePopEvent.java
 *
 * Author: Pieter Deelen
 * Created: September 7, 2005, 3:10 PM
 *
 */

package tracevis.model.types;



/**
 * An event which indicates that a frame has been popped due to an exception.
 * @author Pieter Deelen
 */
public class FramePopEvent extends AbstractEvent {
	private final long threadID;
	private final String className;
	private final String methodName;

	private Frame frame;

	/**
	 * Creates a new instance of FramePopEvent.
	 * @param timeStamp the time this event occurred.
	 * @param threadID the thread in which the frame was popped.
	 * @param className the name of the class defining the popped method.
	 * @param methodName the name of the popped method.
	 */
	public FramePopEvent(long timeStamp, long threadID, String className, String methodName) {
		super(timeStamp);
		this.threadID = threadID;
		this.className = className;
		this.methodName = methodName;
	}

	@Override
	public String toString() {
		return "FP:" + getTime() + ":" + threadID + ":" + className + ":" + methodName;
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

	public Frame getFrame() {
		return frame;
	}

	public void setFrame(Frame frame) {
		this.frame = frame;
	}
}
