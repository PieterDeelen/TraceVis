/*
 * ColorModel.java
 *
 * Author: Pieter Deelen
 * Created: March 28, 2006, 3:43 PM
 *
 */

package tracevis.visualization.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Assigns hues to classes.
 * @author Pieter Deelen
 */
public class ColorModel {
	private ColorNode root;
	private final float defaultHue;
	private final List<ColorModelListener> listeners;

	/**
	 * Creates a new instance of ColorModel.
	 * @param defaultHue the default hue.
	 */
	public ColorModel(float defaultHue) {
		root = new ColorNode();
		this.defaultHue = defaultHue;
		listeners = new ArrayList<ColorModelListener>();
	}

	/**
	 * Assigns the specified hue to all classes whose names fit the specified
	 * pattern. This pattern can have one of two shapes:
	 * <ul>
	 *   <li>
	 *     &lt;package&gt;.&lt;class&gt;: Assigns the specified hue to
	 *     &lt;package&gt;.&lt;class&gt;.
	 *   </li>
	 *   <li>
	 *     &lt;package&gt;.*: Assigns the specified hue to all subpackages and
	 *     subclasses of &lt;package&gt;.
	 *   </li>
	 * </ul>
	 * @param pattern the specified pattern.
	 * @param hue the specified hue.
	 */
	public void setHue(String pattern, float hue) {
		addPattern(pattern, hue, true);

		fireColorsChanged();
	}

	/**
	 * Assigns the default hue to all classes whose name fit the specified
	 * pattern.
	 * @param pattern the specified pattern.
	 * @see #setHue
	 */
	public void resetHue(String pattern) {
		addPattern(pattern, null, true);

		fireColorsChanged();
	}

	/**
	 * Returns the hue for the specified class.
	 * @param className the name of the specified class.
	 */
	public float getHue(String className) {
		List<String> patternParts = Arrays.asList(className.split("\\."));
		Float hue = root.retrieve(patternParts);
		return (hue != null) ? hue : defaultHue;
	}

	/**
	 * Loads a color model file.
	 * @param colorModelFile the color model file.
	 * @throws IOException if the file could not be read.
	 */
	public void load(File colorModelFile) throws IOException {
		root = new ColorNode();

		Properties properties = new Properties();
		properties.load(new FileInputStream(colorModelFile));
		Enumeration<?> names = properties.propertyNames();
		while (names.hasMoreElements()) {
			String name = (String)names.nextElement();
			String value = properties.getProperty(name);
			Float hue = Float.parseFloat(value);
			addPattern(name, hue, false);
		}

		fireColorsChanged();
	}

	/**
	 * Saves a color model file.
	 * @param colorModelFile the color model file.
	 * @throws IOException if the file could not be saved.
	 */
	public void save(File colorModelFile) throws IOException {
		Properties properties = new Properties();

		List<String> patterns = root.getPatterns("");

		for (String pattern : patterns) {
			List<String> patternParts = Arrays.asList(pattern.split("\\."));
			Float hue = root.retrieve(patternParts);
			properties.setProperty(pattern, Float.toString(hue));
		}

		properties.store(new FileOutputStream(colorModelFile), "");
	}

	/**
	 * Adds a color model listener.
	 *@param listener the listener to add.
	 */
	public void addListener(ColorModelListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a color model listener.
	 * @param listener the listener to remove.
	 */
	public void removeListener(ColorModelListener listener) {
		listeners.remove(listener);
	}

	private void fireColorsChanged() {
		for (ColorModelListener listener : listeners) {
			listener.colorsChanged();
		}
	}

	private void addPattern(String pattern, Float hue, boolean overWrite) {
		List<String> patternParts = Arrays.asList(pattern.split("\\."));
		root.insert(patternParts, hue, overWrite);
	}

	private static class ColorNode {
		private final Map<String, ColorNode> children;
		private Float hue;

		ColorNode() {
			children = new HashMap<String, ColorNode>();
			hue = null;
		}

		void insert(List<String> patternParts, Float hue, boolean overWrite) {
			if (patternParts.size() > 0) {
				String head = patternParts.get(0);
				if (overWrite && head.equals("*")) {
					children.clear();
				}

				ColorNode child = children.get(head);
				if (child == null) {
					child = new ColorNode();
					children.put(head, child);
				}

				List<String> tail = patternParts.subList(1, patternParts.size());
				child.insert(tail, hue, overWrite);
			} else {
				this.hue = hue;
			}
		}

		Float retrieve(List<String> classNameParts) {
			if (classNameParts.size() > 0) {
				String head = classNameParts.get(0);
				ColorNode child = children.get(head);
				if (child == null) {
					child = children.get("*");
				}
				if (child == null) {
					return hue;
				} else {
					List<String> tail = classNameParts.subList(1, classNameParts.size());
					Float childHue = child.retrieve(tail);
					return (childHue == null) ? hue : childHue;
				}
			}  else {
				return hue;
			}
		}

		List<String> getPatterns(String prefix) {
			List<String> patterns = new LinkedList<String>();

			if (hue != null) {
				patterns.add(prefix);
			}

			for (String name : children.keySet()) {
				ColorNode node = children.get(name);
				String newPrefix = prefix.equals("") ? name : prefix + "." + name;
				patterns.addAll(node.getPatterns(newPrefix));
			}

			return patterns;
		}
	}
}
