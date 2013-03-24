/*
 * BrushState.java
 *
 * Author: Pieter Deelen
 * Created: September 28, 2005, 4:20 PM
 *
 */

package tracevis.visualization.utilities;

import edu.uci.ics.jung.graph.ArchetypeEdge;
import edu.uci.ics.jung.graph.ArchetypeVertex;

/**
 * A BrushState maintains the state of what has been brushed in the graph.
 * @author Pieter Deelen
 */
public interface BrushState {
	/**
	 * Sets the state of the specified vertex to brushed and the state of all
	 * other vertices to unbrushed.
	 * @param v the specified vertex. Pass null instead of an actual vertex to
	 *        set all vertices to unbrushed.
	 */
	public void setBrushed(ArchetypeVertex v);

	/**
	 * Sets the state of the specified edge to brushed and the state of all
	 * other edges to unbrushed.
	 * @param e the specified edge. Pass null instead of an actual edge to
	 *        set all edges to unbrushed.
	 */
	public void setBrushed(ArchetypeEdge e);

	/**
	 * Returns whether the specified vertex is brushed.
	 * @param v the specified vertex.
	 */
	public boolean isBrushed(ArchetypeVertex v);

	/**
	 * Returns whether the specified edge is brushed.
	 * @param e the specified edge.
	 */
	public boolean isBrushed(ArchetypeEdge e);
}
