/*
 * VertexSizeFunction.java
 *
 * Author: Pieter Deelen
 * Created: September 27, 2005, 11:20 AM
 *
 */

package tracevis.visualization.functions;

import edu.uci.ics.jung.graph.Vertex;

/**
 * A VertexSizeFunction is used to compute the size of a vertex.
 * @author Pieter Deelen
 */
public interface VertexSizeFunction {
	/**
	 * Returns the current size for the specified vertex.
	 * @param v the specified vertex.
	 */
	public int getSize(Vertex v);

	/**
	 * Returns the maximal size for the specified vertex.
	 * @param v the specified vertex.
	 */
	public int getMaxSize(Vertex v);
}
