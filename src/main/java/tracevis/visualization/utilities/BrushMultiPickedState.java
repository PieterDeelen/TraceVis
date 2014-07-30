/*
 * BrushMultiPickedState.java
 *
 * Author: Pieter Deelen
 * Created: September 28, 2005, 4:08 PM
 *
 */

package tracevis.visualization.utilities;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.jung.graph.ArchetypeEdge;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.visualization.MultiPickedState;

/**
 * Maintains the state of what has been picked or brushed in the graph. Multiple
 * elements can be picked simultaneously, but only one element can be brushed at
 * one time.
 * @author Pieter Deelen
 */
public class BrushMultiPickedState extends MultiPickedState implements BrushState {
	private ArchetypeVertex brushedVertex;
	private ArchetypeEdge brushedEdge;

	private final List<BrushEventListener> listeners;

	/**
	 * Creates a new instance of BrushMultiPickedState.
	 */
	public BrushMultiPickedState() {
		this.brushedVertex = null;
		this.brushedEdge = null;

		this.listeners = new ArrayList<BrushEventListener>();
	}

	@Override
	public void setBrushed(ArchetypeVertex v) {
		if (brushedVertex != v) {
			ArchetypeEdge oldBrushedEdge = brushedEdge;
			ArchetypeVertex oldBrushedVertex = brushedVertex;
			brushedVertex = v;
			brushedEdge = null;

			if (oldBrushedEdge != null) {
				fireEdgeUnbrushed(oldBrushedEdge);
			} else if (oldBrushedVertex != null) {
				fireVertexUnbrushed(oldBrushedVertex);
			}

			if (brushedVertex != null) {
				fireVertexBrushed(brushedVertex);
			}
		}
	}

	@Override
	public void setBrushed(ArchetypeEdge e) {
		if (brushedEdge != e) {
			ArchetypeEdge oldBrushedEdge = brushedEdge;
			ArchetypeVertex oldBrushedVertex = brushedVertex;
			brushedVertex = null;
			brushedEdge = e;

			if (oldBrushedEdge != null) {
				fireEdgeUnbrushed(oldBrushedEdge);
			} else if (oldBrushedVertex != null) {
				fireVertexUnbrushed(oldBrushedVertex);
			}

			if (brushedEdge != null) {
				fireEdgeBrushed(brushedEdge);
			}
		}
	}

	/**
	 * Sets all vertices to unbrushed.
	 */
	public void clearBrushed() {
		brushedVertex = null;
		brushedEdge = null;
	}

	@Override
	public boolean isBrushed(ArchetypeVertex v) {
		return brushedVertex != null && brushedVertex.equals(v);
	}

	@Override
	public boolean isBrushed(ArchetypeEdge e) {
		return brushedEdge != null && brushedEdge.equals(e);
	}

	/**
	 * Adds a listener for brush events.
	 * @param listener the listener to add.
	 */
	public void addBrushEventListener(BrushEventListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a listener for brush events.
	 * @param listener the listener to remove.
	 */
	public void removeBrushEventListener(BrushEventListener listener) {
		listeners.remove(listener);
	}

	private void fireEdgeBrushed(ArchetypeEdge e) {
		for (BrushEventListener listener : listeners) {
			listener.edgeBrushed(e);
		}
	}

	private void fireEdgeUnbrushed(ArchetypeEdge e) {
		for (BrushEventListener listener : listeners) {
			listener.edgeUnbrushed(e);
		}
	}

	private void fireVertexBrushed(ArchetypeVertex v) {
		for (BrushEventListener listener : listeners) {
			listener.vertexBrushed(v);
		}
	}

	private void fireVertexUnbrushed(ArchetypeVertex v) {
		for (BrushEventListener listener : listeners) {
			listener.vertexUnbrushed(v);
		}
	}

	public ArchetypeVertex getBrushedVertex() {
		return brushedVertex;
	}

	public ArchetypeEdge getBrushedEdge() {
		return brushedEdge;
	}
}
