/*
 * ObjectFreeEvent.java
 *
 * Author: Pieter Deelen
 * Created: September 7, 2005, 3:28 PM
 *
 */

package tracevis.model.types;


/**
 * An event which indicates that an object has been garbage collected.
 * @author Pieter Deelen
 */
public class ObjectFreeEvent extends AbstractEvent {
	private final String className;
	private final long objectID;

	/**
	 * Creates a new instance of ObjectFreeEvent.
	 * @param timeStamp the time this event occurred.
	 * @param className the name of the class this object is an instance of.
	 * @param objectID the identifier of the garbage collected object.
	 */
	public ObjectFreeEvent(long timeStamp, String className, long objectID) {
		super(timeStamp);
		this.className = className;
		this.objectID = objectID;
	}

	@Override
	public String toString() {
		return "OF:" + getTime() + ":" + getClassName();
	}

	public String getClassName() {
		return className;
	}

	public long getObjectID() {
		return objectID;
	}

}
