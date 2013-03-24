package tracevis.model.types;


/**
 * A frame on the call stack.
 */
public class Frame {
	private final ClassData definingClass;
	private final ClassData actualClass;
	private final long object;
	private final String method;

	/**
	 * Creates a new frame.
	 * @param definingClass the defining class for the call.
	 * @param actualClass the actual (or object) class for the call.
	 * @param object a positive number identifying the called object, if the
	 *               called method is an instance method, 0 otherwise.
	 * @param method the name of the called method.
	 */
	public Frame(ClassData definingClass, ClassData actualClass, long object, String method) {
		this.definingClass = definingClass;
		this.actualClass = actualClass;
		this.object = object;
		this.method = method;
	}

	/**
	 * Returns the defining class for the call.
	 */
	public ClassData getDefiningClass() {
		return definingClass;
	}

	/**
	 * Returns the actual (or object) class for the call.
	 */
	public ClassData getActualClass() {
		return actualClass;
	}

	/**
	 * Returns a number identifying the called objext.
	 */
	public long getObject() {
		return object;
	}

	/**
	 * Returns the name of the called method.
	 */
	public String getMethod() {
		return method;
	}

	@Override
	public String toString() {
		return "[" + definingClass + ":" + method + "]";
	}
}