/*
 * ObjectAllocationEvent.java
 *
 * Author: Pieter Deelen
 * Created: September 7, 2005, 3:34 PM
 *
 */

package tracevis.model.types;


/**
 * An event which indicates that an object has been allocated.
 * @author Pieter Deelen
 */
public class ObjectAllocationEvent extends AbstractEvent {
	private final String className;
	private final long objectID;

	/**
	 * Creates a new instance of ObjectAllocationEvent.
	 * @param timeStamp the time this event occurred.
	 * @param className the name of the class this object is an instance of.
	 * @param objectID the identifier of the allocated object.
	 */
	public ObjectAllocationEvent(long timeStamp, String className, long objectID) {
		super(timeStamp);
		this.className = className;
		this.objectID = objectID;
	}

	@Override
	public String toString() {
		return "OA:" + getTime() + ":" + className;
	}

	public String getClassName() {
		return className;
	}

	public long getObjectID() {
		return objectID;
	}
}
