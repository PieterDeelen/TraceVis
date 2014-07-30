/*
 * BrushEventListener.java
 *
 * Author: Pieter Deelen
 * Created: November 8, 2005, 8:23 PM
 *
 */

package tracevis.visualization.utilities;

import java.util.EventListener;

import edu.uci.ics.jung.graph.ArchetypeEdge;
import edu.uci.ics.jung.graph.ArchetypeVertex;

/**
 * A brush event listener listens for brush events.
 * @author Pieter Deelen
 */
public interface BrushEventListener extends EventListener {
	/**
	 * Indicates that the specified edge was brushed.
	 * @param e the specified edge.
	 */
	void edgeBrushed(ArchetypeEdge e);

	/**
	 * Indicates that the specified edge was unbrushed.
	 * @param e the specified edge.
	 */
	void edgeUnbrushed(ArchetypeEdge e);

	/**
	 * Indicates that the specified vertex was brushed.
	 * @param v the specified vertex.
	 */
	void vertexBrushed(ArchetypeVertex v);

	/**
	 * Indicates that the specified vertex was unbrushed.
	 * @param v the specified vertex.
	 */
	void vertexUnbrushed(ArchetypeVertex v);
}
