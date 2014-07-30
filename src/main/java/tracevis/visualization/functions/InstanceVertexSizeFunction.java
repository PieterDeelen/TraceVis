/*
 * InstanceVertexSizeFunction.java
 *
 * Author: Pieter Deelen
 * Created: September 1, 2005, 9:53 AM
 *
 */

package tracevis.visualization.functions;

import tracevis.model.types.ClassData;
import edu.uci.ics.jung.graph.Vertex;

/**
 * A VertexSizeFunction which computes a vertex's size from the number of
 * instances the class represented by this vertex currently has.
 * @author Pieter Deelen
 */
public class InstanceVertexSizeFunction implements VertexSizeFunction {
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
	 * Creates a new instance of InstanceVertexSizeFunction.
	 */
	public InstanceVertexSizeFunction() {
		this(2, Scale.LOGARITHMIC);
	}

	/**
	 * Creates a new instance of InstanceVertexSizeFunction.
	 * @param scaleFactor a factor by which to scale the vertex's
	 *                    size.
	 * @param scale the scale to use.
	 */
	public InstanceVertexSizeFunction(int scaleFactor, Scale scale) {
		this.settings = new Settings(scaleFactor, scale);
	}

	/**
	 * Creates a new instance of InstanceVertexSizeFunction
	 * with the specified settings.
	 * @param settings the specified settings.
	 */
	public InstanceVertexSizeFunction(Settings settings) {
		this.settings = settings;
	}

	/**
	 * Returns the size for the specified vertex.
	 * @param v the specified vertex.
	 */
	@Override
	public int getSize(Vertex v) {
		ClassData classData = (ClassData)v.getUserDatum("tracevis.model.Program");
		int instanceCount = classData.getInstanceCount();

		return computeSize(v, instanceCount);
	}

	/**
	 * Returns the maximal size for the specified vertex.
	 * @param v the specified vertex.
	 */
	@Override
	public int getMaxSize(Vertex v) {
		ClassData classData = (ClassData)v.getUserDatum("tracevis.model.Program");
		int instanceCount = classData.getMaxInstanceCount();

		return computeSize(v, instanceCount);
	}

	private int computeSize(Vertex v, int value) {
		switch (settings.scale) {
			case LINEAR:
				return settings.scaleFactor * (value + 1);
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
