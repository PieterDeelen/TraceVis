/*
 * CallsSentVertexSizeFunction.java
 *
 * Author: Pieter Deelen
 * Created: July 14, 2005, 9:44 AM
 *
 */

package tracevis.visualization.functions;

import tracevis.model.types.ClassData;
import edu.uci.ics.jung.graph.Vertex;

/**
 * A VertexSizeFunction which computes a vertex's size from the number of calls
 * the class represented by this vertex has sent.
 * @author Pieter Deelen
 */
public class CallsSentVertexSizeFunction implements VertexSizeFunction {
	/**
	 * The settings (or parameters) for the vertex size function.
	 */
	public static class Settings {
		private final int scaleFactor;
		private final Scale scale;

		/**
		 * Creates a new instance of Settings.
		 * @param scaleFactor the scale factor.
		 * @param scale the scale to use.
		 */
		public Settings(int scaleFactor, Scale scale) {
			this.scaleFactor = scaleFactor;
			this.scale = scale;
		}

		/**
		 * Creates a new instance from another instance of Settings.
		 */
		public Settings(Settings settings) {
			this(settings.scaleFactor, settings.scale);
		}

		/**
		 * Returns the scale factor.
		 */
		public int getScaleFactor() {
			return scaleFactor;
		}

		/**
		 * Returns the scale.
		 */
		public Scale getScale() {
			return scale;
		}
	}

	private Settings settings;

	/**
	 * Creates a new instance of CallsSentVertexSizeFunction.
	 */
	public CallsSentVertexSizeFunction() {
		this(2, Scale.LOGARITHMIC);
	}

	/**
	 * Creates a new instance of CallsReceivedVertexSizeFunction.
	 * @param scaleFactor a factor by which to scale the vertex's size.
	 * @param scale the scale to use.
	 */
	public CallsSentVertexSizeFunction(int scaleFactor, Scale scale) {
		settings = new Settings(scaleFactor, scale);
	}

	/**
	 * Creates a new instance of CallsReceivedVertexSizeFunction using the
	 * specified settings.
	 * @param settings the specified settings.
	 */
	public CallsSentVertexSizeFunction(Settings settings) {
		this.settings = settings;
	}

	/**
	 * Returns the size for the specified vertex.
	 * @param v the specified vertex.
	 */
	@Override
	public int getSize(Vertex v) {
		ClassData classData = (ClassData)v.getUserDatum("tracevis.model.Program");
		int callCount = classData.getCallsSent();

		return computeSize(v, callCount);
	}

	/**
	 * Returns the size for the specified vertex.
	 * @param v the specified vertex.
	 */
	@Override
	public int getMaxSize(Vertex v) {
		ClassData classData = (ClassData)v.getUserDatum("tracevis.model.Program");
		int callCount = classData.getMaxCallsSent();

		return computeSize(v, callCount);
	}

	private int computeSize(Vertex v, int value) {
		switch (settings.scale) {
			case LINEAR:
				return settings.scaleFactor * value;
			case SQUARE_ROOT:
				return (int)(settings.scaleFactor * Math.sqrt(value + 1));
			case LOGARITHMIC:
				return (int)(settings.scaleFactor * Math.log(value + 1));
			default:
				// Shouldn't happen.
				return 0;
		}
	}

	/**
	 * Returns the settings for this vertex size function.
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * Sets the settings for this vertex size function.
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
}
