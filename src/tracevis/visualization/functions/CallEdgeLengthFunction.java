/*
 * CallEdgeLengthFunction.java
 *
 * Author: Pieter Deelen
 * Created: July 13, 2005, 3:24 PM
 *
 */

package tracevis.visualization.functions;

import tracevis.model.types.CallData;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.visualization.SpringLayout;

/**
 * A LengthFunction that computes an edge's length from the number of calls on
 * this edge.
 * @author Pieter Deelen
 */
public class CallEdgeLengthFunction implements SpringLayout.LengthFunction, Function {
	private double basicLength;
	private boolean logarithmic;

	/**
	 * Creates a new instance of CallEdgeLengthFunction.
	 * @param basicLength the length of an edge with a zero call count.
	 */
	public CallEdgeLengthFunction(double basicLength, boolean logarithmic) {
		this.basicLength = basicLength;
		this.logarithmic = logarithmic;
	}

	@Override
	public double getLength(Edge edge) {
		int callCount = ((CallData)edge.getUserDatum("tracevis.model.Program")).getCallCount();
		double denominator;
		if (logarithmic) {
			denominator = Math.log(callCount) + 1;
		} else {
			denominator = callCount;
		}
		return basicLength / denominator;
	}

	public void setBasicLength(double length) {
		this.basicLength = length;
	}

	public double getBasicLength() {
		return basicLength;
	}

	public boolean isLogarithmic() {
		return logarithmic;
	}

	public void setLogarithmic(boolean logarithmic) {
		this.logarithmic = logarithmic;
	}
}
