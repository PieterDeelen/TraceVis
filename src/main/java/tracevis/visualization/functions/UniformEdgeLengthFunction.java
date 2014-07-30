/*
 * UniformEdgeLengthFunction.java
 *
 * Author: Pieter Deelen
 * Created: August 29, 2005, 10:33 AM
 *
 */

package tracevis.visualization.functions;

import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.visualization.SpringLayout;

/**
 * An edge length function which returns the same length for every edge.
 * @author Pieter Deelen
 */
public class UniformEdgeLengthFunction implements SpringLayout.LengthFunction, Function {
	private double length;

	/**
	 * Creates a new instance of UniformEdgeLengthFunction.
	 * @param length the uniform edge length.
	 */
	public UniformEdgeLengthFunction(double length) {
		this.length = length;
	}

	@Override
	public double getLength(Edge edge) {
		return length;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}
}
