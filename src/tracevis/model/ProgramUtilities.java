package tracevis.model;

import java.util.ArrayList;
import java.util.List;

public class ProgramUtilities {

	/**
	 * Returns whether the specified class name belongs to an inner class.
	 * @param className the specified class.
	 */
	public static boolean isInnerClass(String className) {
		return className.indexOf("$") != -1;
	}

	/**
	 * Returns the name of the enclosing class of the specified class.
	 * @param className the specified class.
	 */
	public static String getEnclosingClassName(String className) {
		int index = className.indexOf("$");
		if (index == -1) {
			return className;
		} else {
			return className.substring(0, index);
		}
	}

	/**
	 * Returns the inner class name of the specified class.
	 * @param className the specified class.
	 */
	public static String getInnerClassName(String className) {
		int index = className.indexOf("$");
		if (index == -1) {
			return "";
		} else {
			return className.substring(index + 1);
		}
	}

	/** Returns a list containing the identifiers the name of the enclosing
	 * package is composed of. For instance, for the class "java.util.Map" this
	 * method would return the list ["java", "util"].
	 */
	public static List<String> getPackageIdentifiers(String className) {
		String[] splitIdentifiers = className.split("\\.");
		List<String> packageIdentifiers = new ArrayList<String>();
		for (int i = 0; i < splitIdentifiers.length - 1; i++) {
			packageIdentifiers.add(splitIdentifiers[i]);
		}

		return packageIdentifiers;
	}

	/**
	 * Returns the short (or simple) name of the specified class.
	 * @param className the specified class.
	 */
	public static String getShortName(String className) {
		int index = className.lastIndexOf(".");
		String shortName = className.substring(index+1);
		return shortName;
	}

}
