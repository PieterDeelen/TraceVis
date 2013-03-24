/*
 * StringUtils.java
 *
 * Author: Pieter Deelen
 * Created: July 4, 2005, 10:05 AM
 *
 */

package tracevis.utilities;

import java.util.Iterator;

/**
 * A String utility method class.
 * @author Pieter Deelen
 */
public final class StringUtils {

	/**
	 * Private constructor.
	 */
	private StringUtils() {}

	/**
	 * Returns a String composed of the Strings in the list referenced to by
	 * strings, with intervening occurrences of separator.
	 * @param strings an iterator pointing to a list of Strings.
	 * @param separator a separating String.
	 */
	public static String joinStrings(Iterator<String> strings, String separator) {
		if (!strings.hasNext()) {
			return "";
		} else {
			StringBuffer result = new StringBuffer();
			result.append(strings.next());

			while (strings.hasNext()) {
				result.append(separator + strings.next());
			}

			return result.toString();
		}
	}
}
