/*
 * ToolTipGenerator.java
 *
 * Author: Pieter Deelen
 * Created: August 16, 2005, 3:12 PM
 *
 */

package tracevis.visualization.structure;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import tracevis.model.Program;
import tracevis.model.types.CallData;
import tracevis.model.types.ClassData;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.visualization.PickSupport;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * A ToolTipListener which returns:
 *  - if the cursor hovers above a vertex: the name of the class represented
 *    by the vertex.
 *  - if the cursor hovers above an edge: the number of calls the class
 *    represented by the source has made to the class represented by the
 *    destination.
 */
class ToolTipGenerator implements VisualizationViewer.ToolTipListener {
	private final VisualizationViewer viewer;

	/**
	 * Creates a new instance of ToolTipGenerator.
	 * @param viewer the visualization viewer to generate tool tips for.
	 */
	public ToolTipGenerator(VisualizationViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public String getToolTipText(MouseEvent event) {
		PickSupport pickSupport = viewer.getPickSupport();
		Point2D point = viewer.inverseViewTransform(event.getPoint());

		// Try to pick a vertex first.
		Vertex vertex = pickSupport.getVertex(point.getX(), point.getY());
		if (vertex != null) {
			ClassData classData = (ClassData)vertex.getUserDatum("tracevis.model.Program");
			return classData.getName();
		} else {
			// Otherwise, try to pick an edge.
			Edge edge = pickSupport.getEdge(point.getX(), point.getY());
			if (edge != null) {
				CallData callData = (CallData)edge.getUserDatum("tracevis.model.Program");
				return "Calls: " + Integer.toString(callData.getCallCount());
			} else {
				return "";
			}
		}
	}
}
