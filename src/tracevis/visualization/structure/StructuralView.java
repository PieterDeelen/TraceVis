/*
 * StructuralView.java
 *
 * Author: Pieter Deelen
 * Created: March 28, 2006, 10:50 AM
 *
 */

package tracevis.visualization.structure;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;

import javax.swing.JPanel;
import javax.swing.UIManager;

import tracevis.model.Program;
import tracevis.model.types.ClassData;
import tracevis.model.types.ProgramListener;
import tracevis.visualization.functions.CallsReceivedVertexSizeFunction;
import tracevis.visualization.functions.CallsSentVertexSizeFunction;
import tracevis.visualization.functions.InstanceVertexSizeFunction;
import tracevis.visualization.functions.UniformEdgeLengthFunction;
import tracevis.visualization.functions.VertexSizeFunction;
import tracevis.visualization.utilities.BrushEventListener;
import tracevis.visualization.utilities.BrushMultiPickedState;
import edu.uci.ics.jung.graph.ArchetypeEdge;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.visualization.PickEventListener;
import edu.uci.ics.jung.visualization.PickSupport;
import edu.uci.ics.jung.visualization.ShapePickSupport;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.VisualizationViewer.ToolTipListener;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

/**
 * The structural view.
 * @author Pieter Deelen
 */
public class StructuralView extends JPanel {

	private final VisualizationViewer viewer;
	private final CustomRenderer renderer;
	private final CustomSpringLayout layout;

	private double wx, wy, ww, wh;

	private Rectangle selection;

	/**
	 * Determines which vertex size function is selected.
	 */
	public static enum SelectedVertexSizeFunction {
		CALLS_SENT("Calls Sent"),
		CALLS_RECEIVED("Calls Received"),
		INSTANCE("Instances");

		private final String friendlyName;

		SelectedVertexSizeFunction(String friendlyName) {
			this.friendlyName = friendlyName;
		}

		@Override
		public String toString() {
			return friendlyName;
		}
	}

	/**
	 * The settings for the structural view.
	 */
	public static class Settings {
		private final int transitionFrames;

		/**
		 * Creates a new Settings instance.)
		 * @param trail
		 * @param transitionFrames the number of frames to use for transitions.
		 */
		public Settings(int transitionFrames) {
			this.transitionFrames = transitionFrames;
		}

		/**
		 * Return the number of transition frames.
		 */
		public int getTransitionFrames() {
			return transitionFrames;
		}
	}

	private Settings settings;

	private SelectedVertexSizeFunction selectedVertexSizeFunction;

	private final CallsSentVertexSizeFunction callsSentVertexSizeFunction;
	private final CallsReceivedVertexSizeFunction callsReceivedVertexSizeFunction;
	private final InstanceVertexSizeFunction instanceVertexSizeFunction;

	/**
	 * Creates a new instance of StructuralView.
	 * @param program the program to visualize.
	 * @param pickedState the selection model.
	 */
	public StructuralView(Program program, BrushMultiPickedState pickedState) {
		program.addListener(new ProgramListenerImpl());

		settings = new Settings(10);

		PickBrushListener pickBrushListener = new PickBrushListener();
		pickedState.addListener(pickBrushListener);
		pickedState.addBrushEventListener(pickBrushListener);

		// Initialize vertex size functions.
		selectedVertexSizeFunction = SelectedVertexSizeFunction.CALLS_RECEIVED;
		callsSentVertexSizeFunction = new CallsSentVertexSizeFunction();
		callsReceivedVertexSizeFunction = new CallsReceivedVertexSizeFunction();
		instanceVertexSizeFunction = new InstanceVertexSizeFunction();

		// Initialize layout.
		layout = new CustomSpringLayout(program.getCallGraph());
		layout.setLengthFunction(new UniformEdgeLengthFunction(10));
		layout.setVertexSizeFunction(callsReceivedVertexSizeFunction);

		// Initialize renderer.
		renderer = new CustomRenderer(pickedState, program);
		renderer.setVertexSizeFunction(callsReceivedVertexSizeFunction);

		setLayout(new BorderLayout());
		// Create graph visualization component.
		viewer = new VisualizationViewer(layout, renderer);
		viewer.setBackground(Color.WHITE);
		viewer.setPickedState(pickedState);
		add(viewer, BorderLayout.CENTER);

		// Add a tool tip generator.
		ToolTipListener toolTipListener = new ToolTipGenerator(viewer);
		viewer.setToolTipListener(toolTipListener);

		// Use the actual vertex and edge shapes for selection.
		// TODO: Make a better PickSupport.
		PickSupport pickSupport = new ShapePickSupport();
		viewer.setPickSupport(pickSupport);

		SelectionPainter selectionPainter = new SelectionPainter();
		viewer.addPostRenderPaintable(selectionPainter);

		viewer.addComponentListener(new ViewResizer());
	}


	/**
	 * Recomputes the layout.
	 */
	public void recomputeLayout() {
		layout.recompute();

		Dimension bounds = layout.getBounds();
		setWindow(0.0, 0.0, bounds.getWidth(), bounds.getHeight());
	}

	/**
	 * Sets the view window. The window must be specified in layout-space.
	 * Note that the actual window may differ from the specified window,
	 * because this method tries to maintain the aspect ratio.
	 * @param x the x-coordinate of the upper left corner of the window.
	 * @param y the y-coordinate of the upper left corner of the window.
	 * @param w the width of the window.
	 * @param h the height of the window.
	 */
	public void setWindow(double x, double y, double w, double h) {
		wx = x;
		wy = y;
		ww = w;
		wh = h;

		double vw = viewer.getWidth();
		double vh = viewer.getHeight();

		double va = vw / vh;
		double wa = w / h;

		double nx, ny, nw, nh;
		if (va < wa) {
			nw = w;
			nh = w / va;
			nx = x;
			ny = y - (nh - h) / 2.0;
		} else {
			nw = h * va;
			nh = h;
			nx = x - (nw - w) / 2.0;
			ny = y;
		}

		MutableTransformer transformer = viewer.getLayoutTransformer();
		transformer.setToIdentity();
		transformer.translate(-nx, -ny);
		transformer.scale(vw / nw, vh / nh, new Point2D.Double(0.0, 0.0));

		viewer.repaint();
	}

	/**
	 * Pan the view window.
	 * @param px the x-coordinate of the pan vector.
	 * @param py the y-coordinate of the pan vector.
	 */
	public void pan(double px, double py) {
		setWindow(wx - px, wy - py, ww, wh);
	}

	/**
	 * Zoom the view window in or out.
	 * @param factor the zooming factor. Positive values below 1 zoom out,
	 *               values above 1 zoom in.
	 * @param focus the focus point.
	 */
	public void zoom(double factor, Point2D focus) {
		MutableTransformer transformer = viewer.getLayoutTransformer();
		transformer.scale(factor, factor, focus);

		Point2D ulp = new Point2D.Double(0.0, 0.0);
		Point2D lrp = new Point2D.Double(viewer.getWidth(), viewer.getHeight());
		Point2D tulp = transformer.inverseTransform(ulp);
		Point2D tlrp = transformer.inverseTransform(lrp);

		setWindow(tulp.getX(), tulp.getY(), tlrp.getX() - tulp.getX(),
		          tlrp.getY() - tulp.getY());
	}

	/**
	 * Returns the structural view settings.
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * Sets the structural view settings.
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/**
	 * Get the vertex size settings.
	 */
	public VertexSizeSettings getVertexSizeSettings() {
		VertexSizeSettings vertexSizeSettings = new VertexSizeSettings(
			callsReceivedVertexSizeFunction.getSettings(),
			callsSentVertexSizeFunction.getSettings(),
			instanceVertexSizeFunction.getSettings(),
			selectedVertexSizeFunction
		);

		return vertexSizeSettings;
	}

	/**
	 * Set the vertex size settings.
	 */
	public void setVertexSizeSettings(VertexSizeSettings vertexSizeSettings) {
		callsReceivedVertexSizeFunction.setSettings(vertexSizeSettings.getCallsReceivedSettings());
		callsSentVertexSizeFunction.setSettings(vertexSizeSettings.getCallsSentSettings());
		instanceVertexSizeFunction.setSettings(vertexSizeSettings.getInstanceSettings());

		selectedVertexSizeFunction = vertexSizeSettings.getSelectedFunction();
		VertexSizeFunction vertexSizeFunction;
		switch (selectedVertexSizeFunction) {
			case CALLS_RECEIVED:
				vertexSizeFunction = callsReceivedVertexSizeFunction;
				break;
			case CALLS_SENT:
				vertexSizeFunction = callsSentVertexSizeFunction;
				break;
			case INSTANCE:
				vertexSizeFunction = instanceVertexSizeFunction;
				break;
			default:
				throw new RuntimeException("Illegal case.");
		}

		layout.setVertexSizeFunction(vertexSizeFunction);
		renderer.setVertexSizeFunction(vertexSizeFunction);
	}

	/**
	 * Get the renderer settings.  */
	public CustomRenderer.Settings getRendererSettings() {
		return renderer.getSettings();
	}

	/**
	 * Set the renderer settings.
	 */
	public void setRendererSettings(CustomRenderer.Settings settings) {
		renderer.setSettings(settings);
	}

	/**
	 * Get the layout settings.
	 */
	public CustomSpringLayout.Settings getLayoutSettings() {
		return layout.getSettings();
	}

	/**
	 * Set the layout settings.
	 */
	public void setLayoutSettings(CustomSpringLayout.Settings settings) {
		layout.setSettings(settings);
	}

	/**
	 * Returns the viewer.
	 */
	VisualizationViewer getViewer() {
		return viewer;
	}

	/**
	 * Returns the selection.
	 */
	public Rectangle getSelection() {
		return selection;
	}

	/**
	 * Sets the selection.
	 */
	public void setSelection(Rectangle selection) {
		this.selection = selection;
		repaint();
	}

	/*
	 * Inner classes.
	 */

	private class ViewResizer extends ComponentAdapter {
        @Override
		public void componentResized(ComponentEvent e) {
			setWindow(wx, wy, ww, wh);
        }
    }

	private class PickBrushListener implements PickEventListener, BrushEventListener {
		@Override
		public void edgePicked(ArchetypeEdge e) {
			repaint();
		}

		@Override
		public void edgeUnpicked(ArchetypeEdge e) {
			repaint();
		}

		@Override
		public void vertexPicked(ArchetypeVertex v) {
			repaint();
		}

		@Override
		public void vertexUnpicked(ArchetypeVertex v) {
			repaint();
		}

		@Override
		public void edgeBrushed(ArchetypeEdge e) {
			repaint();
		}

		@Override
		public void edgeUnbrushed(ArchetypeEdge e) {
			repaint();
		}

		@Override
		public void vertexBrushed(ArchetypeVertex v) {
			repaint();
		}

		@Override
		public void vertexUnbrushed(ArchetypeVertex v) {
			repaint();
		}
	}

	private class ProgramListenerImpl implements ProgramListener {
		@Override
		public void traceLoaded() {
			viewer.suspend();

			layout.initialize(new Dimension(1000, 1000));
			// Update layout.
			layout.recompute();

			// Restore view point.
			Dimension bounds = layout.getBounds();
			setWindow(0.0, 0.0, bounds.getWidth(), bounds.getHeight());

			viewer.unsuspend();

			// Add a mouse listener.
			CustomGraphMouse graphMouse = new CustomGraphMouse(StructuralView.this);
			viewer.setGraphMouse(graphMouse);

			repaint();
		}

		@Override
		public void traceFiltered() {
			repaint();
		}

		@Override
		public void timeChanged() {
			repaint();
		}

		@Override
		public void methodEntered(ClassData callerData, ClassData calleeData) {
			if (callerData != null && calleeData != null) {
				Vertex callerVertex = callerData.getVertex();
				Vertex calleeVertex = calleeData.getVertex();
				Edge edge = callerVertex.findEdge(calleeVertex);
				if (edge != null) {
					Dimension size = viewer.getSize();

					EdgeData edgeData = (EdgeData)edge.getUserDatum("tracevis.visualization.ProgramView");
					edgeData.setInTransition(true);

					for (int i = 1; i <= settings.transitionFrames; i++) {
						float t = i / (settings.transitionFrames + 1.0f);
						edgeData.setTransition(t);
						viewer.paintImmediately(0, 0, size.width, size.height);
					}

					edgeData.setInTransition(false);
				}
			}
		}

		@Override
		public void methodExited(ClassData callerData, ClassData calleeData) {
			if (callerData != null && calleeData != null) {
				Vertex callerVertex = callerData.getVertex();
				Vertex calleeVertex = calleeData.getVertex();
				Edge edge = callerVertex.findEdge(calleeVertex);
				if (edge != null) {
					Dimension size = viewer.getSize();

					EdgeData edgeData = (EdgeData)edge.getUserDatum("tracevis.visualization.ProgramView");
					edgeData.setInTransition(true);

					for (int i = settings.transitionFrames; 1 <= i; i--) {
						float t = i / (settings.transitionFrames + 1.0f);
						edgeData.setTransition(t);
						viewer.paintImmediately(0, 0, size.width, size.height);
					}

					edgeData.setInTransition(false);
				}
			}
		}
	}

	class SelectionPainter implements VisualizationViewer.Paintable {
		private final Color selectionBorderColor;
		private final Color selectionColor;

		public SelectionPainter() {
			selectionBorderColor = UIManager.getDefaults().getColor("textHighlight");
			selectionColor = new Color(selectionBorderColor.getRed(),
									   selectionBorderColor.getGreen(),
									   selectionBorderColor.getBlue(), 16);
		}

		@Override
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D)g;

			if (selection != null) {
				g2d.setStroke(new BasicStroke(1.0f));
				g2d.setPaint(selectionBorderColor);
				g2d.draw(selection);
				g2d.setPaint(selectionColor);
				g2d.fill(selection);
			}
		}

		@Override
		public boolean useTransform() {
			return false;
		}
	}
}
