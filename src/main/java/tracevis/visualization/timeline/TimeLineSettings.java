/*
 * TimeLineSettings.java
 *
 * Author: Pieter Deelen
 * Created: October 31, 2005, 10:34 AM
 *
 */

package tracevis.visualization.timeline;

import tracevis.visualization.functions.Scale;

/**
 * Time line settings.
 * @author Pieter Deelen
 */
public class TimeLineSettings {
	/**
	 * The time line mode determines what the time line shows.
	 */
	public static enum Mode {
		/** Show class activity. */
		SHOW_ACTIVITY,
		/** Show instance chart. */
		SHOW_INSTANCES
	}

	private final Mode mode;
	private final Scale scale;
	private final double activityExponent;

	/**
	 * Create a new instance of TimeLineSettings.
	 * @param mode the time line mode.
	 * @param scale the scale to use for visualizing values.
	 * @param value determines how to show values.
	 * @param activityExponent determines the amount in which the classes are
	 *                         scaled according to their activity.
	 */
	public TimeLineSettings(Mode mode, Scale scale, double activityExponent) {
		this.mode = mode;
		this.scale = scale;
		this.activityExponent = activityExponent;
	}

	/**
	 * Returns the time line mode.
	 */
	public Mode getMode() {
		return mode;
	}

	/**
	 * Returns the scale.
	 */
	public Scale getScale() {
		return scale;
	}

	/**
	 * Returns the activity exponent.
	 */
	public double getActivityExponent() {
		return activityExponent;
	}
}
