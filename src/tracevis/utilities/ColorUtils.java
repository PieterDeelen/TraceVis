/*
 * ColorUtils.java
 *
 * Author: Pieter Deelen
 * Created: October 13, 2005, 10:07 AM
 *
 */

package tracevis.utilities;

import java.awt.Color;

/**
 * A color utility method class.
 * @author Pieter Deelen
 */
public class ColorUtils {
	/**
	 * Returns the Color specified by h (hue), s (saturation), b (brightness)
	 * and a (alpha).
	 */
	public static Color getHSBAColor(float h, float s, float b, float a) {
		Color hsb = Color.getHSBColor(h, s, b);
		Color hsba = new Color(hsb.getRed(), hsb.getGreen(), hsb.getBlue(), (int)(a * 255));
		return hsba;
	}
}
