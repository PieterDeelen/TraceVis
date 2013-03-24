/*
 * ConstantPool.java
 *
 * Author: Pieter Deelen
 * Created: September 13, 2005, 9:44 AM
 *
 */

package tracevis.model;

import java.util.HashMap;
import java.util.Map;

/**
 * A pool for constants. A trace may contain many instances of a single class
 * or method name. If each instance of a name would be stored in its own
 * instance of String, memory usage would be unacceptably (and unnecessarily)
 * high. The constant pool prevents this by storing a single copy of each name.  * This single copy can be retrieved by using the method {@link #getConstant}.
 * @author Pieter Deelen
 */
class ConstantPool {
	private final Map<String, String> constants;

	/**
	 * Creates a new instance of ConstantPool.
	 */
	public ConstantPool() {
		constants = new HashMap<String, String>();
	}

	/**
	 * Returns the shared copy of the specified name.
	 * @param name the specified name.
	 */
	public String getConstant(String name) {
		String constant = constants.get(name);
		if (constant == null) {
			constant = name;
			constants.put(constant, constant);
		}

		return constant;
	}
}
