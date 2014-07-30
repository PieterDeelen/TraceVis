/*
 * ClassLoadEvent.java
 *
 * Author: Pieter Deelen
 * Created: September 7, 2005, 2:11 PM
 *
 */

package tracevis.model.types;


/**
 * An event which indicates that a class has been loaded.
 * @author Pieter Deelen
 */
public class ClassLoadEvent extends AbstractEvent {
	private final String className;

	/**
	 * Creates a new instance of ClassLoadEvent.
	 * @param timeStamp the load time.
	 * @param className the name of the loaded class.
	 */
	public ClassLoadEvent(long timeStamp, String className) {
		super(timeStamp);
		this.className = className;
	}

	@Override
	public String toString() {
		return "CL:" + getTime() + ":" + className;
	}

	public String getClassName() {
		return className;
	}
}
