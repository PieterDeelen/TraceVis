package tracevis.model.types;

/**
 * The call assignment determines how calls are assigned to classes.
 */
public enum CallAssignment {
	/** Calls are assigned to the defining class. */
	DEFINING_CLASS,
	/** Calls are assigned to the object class. */
	OBJECT_CLASS
}