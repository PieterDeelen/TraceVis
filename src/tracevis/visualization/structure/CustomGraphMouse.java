/*
 * CustomGraphMouse.java
 *
 * Author: Pieter Deelen
 * Created: July 12, 2005, 10:15 AM
 *
 */

package tracevis.visualization.structure;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

import tracevis.model.Program;
import tracevis.model.types.ClassData;
import tracevis.visualization.utilities.BrushState;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.PickSupport;
import edu.uci.ics.jung.visualization.PickedState;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * The mouse handler for the structural view.
 * @author Pieter Deelen
 */
class CustomGraphMouse implements VisualizationViewer.GraphMouse {
	private final StructuralView structuralView;
	private final VisualizationViewer viewer;

	private Vertex pickedVertex;
	private Point2D down;

	private final float in;
	private final float out;

	/**
	 * Creates a new instance of CustomGraphMouse.
	 */
	public CustomGraphMouse(StructuralView structuralView) {
		this.structuralView = structuralView;
		this.viewer = structuralView.getViewer();
		this.pickedVertex = null;
		this.down = null;
		this.in = 1.1f;
		this.out = 0.9f;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			// Try to pick a vertex or edge first.
			PickSupport pickSupport = viewer.getPickSupport();
			PickedState pickedState = viewer.getPickedState();
			Layout layout = viewer.getGraphLayout();

			if (pickSupport != null && pickedState != null && layout != null) {
				boolean pickedEdge = false;
				Point2D p = e.getPoint();
				Point2D ip = viewer.inverseViewTransform(p);

				// Try to pick a vertex first.
				Vertex v = pickSupport.getVertex(ip.getX(), ip.getY());
				if (v != null) {
					ClassData classData = (ClassData)v.getUserDatum("tracevis.model.Program");

					// The Control key triggers multiple selection.
					if (e.isControlDown()) {
						// Toggle selection of vertex.
						boolean isPicked = pickedState.isPicked(v);
						pickedState.pick(v, !isPicked);
					} else {
						// Deselect all vertices and edges.
						pickedState.clearPickedVertices();
						pickedState.clearPickedEdges();

						// Select the vertex.
						pickedState.pick(v, true);
					}

					// Save vertex for dragging.
					if (pickedState.isPicked(v)) {
						pickedVertex = v;
					}
				} else {
					// If picking a vertex fails, try to select an edge.
					Edge edge = pickSupport.getEdge(ip.getX(), ip.getY());
					if (edge != null) {
						// The Control key triggers multiple selection.
						if (e.isControlDown()) {
							// Toggle selection of edge.
							boolean isPicked = pickedState.isPicked(edge);
							pickedState.pick(edge, !isPicked);
						} else {
							// Deselect all vertices and edges.
							pickedState.clearPickedVertices();
							pickedState.clearPickedEdges();

							// Select the edge.
							pickedState.pick(edge, true);
						}

						pickedEdge = true;
					} else {
						// If no vertex or edge was picked, start to drag.
						structuralView.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					}
				}

				// Save current point for dragging.
				down = e.getPoint();
			}
		} else if (SwingUtilities.isRightMouseButton(e)) {
			Point2D p = e.getPoint();

			// Set new selection.
			Rectangle newSelection = new Rectangle((int)p.getX(), (int)p.getY(), 0, 0);
			structuralView.setSelection(newSelection);

			// Save current position for dragging.
			down = e.getPoint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			Layout layout = viewer.getGraphLayout();

			// Release the picked vertex.
			layout.unlockVertex(pickedVertex);
			pickedVertex = null;

			down = null;
			structuralView.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		} else if (SwingUtilities.isRightMouseButton(e)) {
			Rectangle s = structuralView.getSelection();

			if (s.width > 0 && s.height > 0) {
				Point2D ulp = new Point2D.Double(s.x, s.y);
				Point2D lrp = new Point2D.Double(s.x + s.width, s.y + s.height);
				Point2D tulp = viewer.inverseTransform(ulp);
				Point2D tlrp = viewer.inverseTransform(lrp);

				structuralView.setWindow(tulp.getX(), tulp.getY(),
				                      tlrp.getX() - tulp.getX(),
				                      tlrp.getY() - tulp.getY());
			}

			structuralView.setSelection(null);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (pickedVertex != null) {
				// If a vertex has been picked, move the picked vertex.
				Point2D p = e.getPoint();
				Point2D gp = viewer.inverseTransform(p);
				Point2D gd = viewer.inverseTransform(down);
				double dx = gp.getX() - gd.getX();
				double dy = gp.getY() - gd.getY();

				Layout layout = viewer.getGraphLayout();
				double vx = layout.getX(pickedVertex) + dx;
				double vy = layout.getY(pickedVertex) + dy;
				layout.forceMove(pickedVertex, vx, vy);
			} else {
				// If no vertex has been picked, move the view.
				Point2D q = viewer.inverseTransform(down);
				Point2D p = viewer.inverseTransform(e.getPoint());
				float dx = (float) (p.getX()-q.getX());
				float dy = (float) (p.getY()-q.getY());

				structuralView.pan(dx, dy);
			}
			down = e.getPoint();
		} else if (SwingUtilities.isRightMouseButton(e)) {
			Point2D p = e.getPoint();

			// Adapt selection to current mouse position.
			Rectangle s = new Rectangle();
			s.setFrameFromDiagonal(p, down);
			structuralView.setSelection(s);
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int amount = e.getWheelRotation();

		// Determine direction of rotation.
		if (amount > 0) {
			// Zoom out if wheel is turned towards the user.
			structuralView.zoom(out, e.getPoint());
		} else if (amount < 0) {
			// Zoom in if wheel is turned away from the user.
			structuralView.zoom(in, e.getPoint());
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point2D p = e.getPoint();
		Point2D ip = viewer.inverseViewTransform(p);
		BrushState brushState = (BrushState)viewer.getPickedState();
		PickSupport pickSupport = viewer.getPickSupport();

		// Try to pick a vertex first.
		Vertex v = pickSupport.getVertex(ip.getX(), ip.getY());
		if (v != null) {
			brushState.setBrushed(v);
		} else {
			// If picking a vertex fails, try to pick an edge.
			Edge edge = pickSupport.getEdge(ip.getX(), ip.getY());
			if (edge != null) {
				brushState.setBrushed(edge);
			} else {
				brushState.setBrushed((Edge)null);
				brushState.setBrushed((Vertex)null);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			if (e.getClickCount() == 2) {
				// Restore view to default view.
				CustomSpringLayout layout = (CustomSpringLayout)viewer.getGraphLayout();
				Dimension bounds = layout.getBounds();
				structuralView.setWindow(0, 0, bounds.getWidth(), bounds.getHeight());
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}
