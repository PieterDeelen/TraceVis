/*
 * CustomRenderer.java
 *
 * Author: Pieter Deelen
 * Created: June 17, 2005, 10:45 AM
 *
 */

package tracevis.visualization.structure;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.JComponent;

import tracevis.model.ProgramInterface;
import tracevis.model.types.CallData;
import tracevis.model.types.ClassData;
import tracevis.utilities.ColorUtils;
import tracevis.utilities.MathUtils;
import tracevis.visualization.functions.VertexSizeFunction;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.DirectedEdge;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Element;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.utils.Pair;
import edu.uci.ics.jung.visualization.DefaultGraphLabelRenderer;
import edu.uci.ics.jung.visualization.PickedState;
import edu.uci.ics.jung.visualization.PluggableRenderer;

/**
 * A custom renderer.
 * @author Pieter Deelen
 */
public class CustomRenderer extends PluggableRenderer {
	private final PickedState pickedState;
	private final ProgramInterface program;

	/**
	 * The color mode determines the way vertices and edges are colored.
	 * */
	public static enum ColorMode {
		/** Vertices and edges which have been active recently are
		 * highlighted. */
		TIMESTAMP,
		/** Vertices and edges in the stack are colored. */
		STACK,
		/** Vertices are colored using user-supplied colors. */
		CUSTOM
	}

	/**
	 * The label settings determine which name is displayed along vertices.
	 */
	public static enum LabelSettings {
		/** No name is displayed. */
		NONE,
		/** The simple class name is displayed. */
		SHORT,
		/** The canonical class name is displayed. */
		LONG
	}

	/**
	 * The renderer settings.
	 */
	public static class Settings {
		private final ColorMode colorMode;
		private final LabelSettings labelSettings;
		private final float edgeCurviness;
		private final boolean drawBackgroundGraph;

		/**
		 * Creates a new instance of Settings.
		 * @param colorDecayInterval
		 * @param colorMode the color mode.
		 * @param labelSettings the label settings.
		 * @param edgeCurviness determines how "curvy" the edges should be
		 *        rendered. Higher values create curvier edges.
		 * @param drawBackgroundGraph whether to draw the background graph.
		 */
		public Settings(ColorMode colorMode,
		                LabelSettings labelSettings, float edgeCurviness,
						boolean drawBackgroundGraph)
		{
			this.colorMode = colorMode;
			this.labelSettings = labelSettings;
			this.edgeCurviness = edgeCurviness;
			this.drawBackgroundGraph = drawBackgroundGraph;
		}

		/**
		 * Creates a new instance of Settings from another instance.
		 */
		public Settings(Settings settings) {
			this(settings.colorMode,
			     settings.labelSettings,
			     settings.edgeCurviness,
				 settings.drawBackgroundGraph);
		}

		/**
		 * Returns the color mode.
		 */
		public ColorMode getColorMode() {
			return colorMode;
		}

		/**
		 * Returns the label settings.
		 */
		public LabelSettings getLabelSettings() {
			return labelSettings;
		}

		/**
		 * Returns the edge curviness.
		 */
		public float getEdgeCurviness() {
			return edgeCurviness;
		}

		/**
		 * Returns whether to draw the background graph.
		 */
		public boolean drawBackgroundGraph() {
			return drawBackgroundGraph;
		}
	}

	private Settings settings;

	private VertexSizeFunction vertexSizeFunction;

	/**
	 * Creates a new instance of CustomRenderer.
	 * @param pickedState the selection model.
	 * @param program the program trace to visualize.
	 */
	public CustomRenderer(PickedState pickedState, ProgramInterface program) {
		super();

		this.pickedState = pickedState;
		this.program = program;
		this.settings = new Settings(ColorMode.STACK, LabelSettings.SHORT, 5.0f, true);

		setVertexStringer(new VertexLabeler());
		setGraphLabelRenderer(new CustomGraphLabelRenderer(Color.BLACK));
	}

	private float getSaturation(Element element) {
		boolean isOnStack;

		if (element instanceof Vertex) {
			VertexData vertexData = (VertexData)((Vertex)element).getUserDatum("tracevis.visualization.ProgramView");
			isOnStack = vertexData.isOnStack();
		} else if (element instanceof Edge) {
			EdgeData edgeData = (EdgeData)((Edge)element).getUserDatum("tracevis.visualization.ProgramView");
			isOnStack = edgeData.isOnStack();
		} else {
			throw new RuntimeException("Illegal graph element.");
		}

		float saturation;
		switch (settings.colorMode) {
			case STACK:
				saturation = isOnStack ? 1.0f : 0.0f;
				break;
			default:
				// Should not happen.
				throw new RuntimeException("Illegal case.");
		}

		return saturation;
	}

	private Color getStackVertexColor(Vertex v) {
		float hue = 0.0f;
		float saturation = getSaturation(v);
		float brightness;

		VertexData vertexData = (VertexData)v.getUserDatum("tracevis.visualization.ProgramView");
		Set<Long> activeThreads = vertexData.getActiveThreads();
		if (activeThreads.size() > 0) {
			// Just pick the first thread.
			long thread = activeThreads.iterator().next();
			SortedSet<Integer> stackEntries = vertexData.getStackEntries(thread);
			int depth = stackEntries.iterator().next();

			int stackDepth = program.getCallStack(thread).depth();
			if (stackDepth > 1) {
				brightness = 1.0f - depth / (stackDepth - 1.0f);
			} else {
				brightness = 1.0f;
			}
		} else {
			brightness = 0.75f;
		}

		return ColorUtils.getHSBAColor(hue, saturation, brightness, 1.00f);
	}

	private Color getCustomVertexColor(Vertex v) {
		VertexData vertexData = (VertexData)v.getUserDatum("tracevis.visualization.ProgramView");
		float hue = vertexData.getHue();

		return ColorUtils.getHSBAColor(hue, 1.0f, 1.0f, 1.0f);
	}

	private Color getVertexColor(Vertex v) {
		// The RGB components of the vertex color are determined by its degree.
		ClassData classData = (ClassData)v.getUserDatum("tracevis.model.Program");

		if (classData.isLoaded()) {
			switch (settings.colorMode) {
				case TIMESTAMP:
				case STACK:
					return getStackVertexColor(v);
				case CUSTOM:
					return getCustomVertexColor(v);
				default:
					throw new RuntimeException();
			}

		} else {
			return new Color(0.0f, 0.0f, 0.0f, 0.0f);
		}
	}

	/**
	 * Draws a vertex.
	 * @param g the canvas to draw on.
	 * @param v the vertex to draw.
	 * @param x the x-coordinate of the location to the draw the vertex.
	 * @param y the y-coordinate of the location to the draw the vertex.
	 */
	@Override
	public void paintVertex(Graphics g, Vertex v, int x, int y) {
		Graphics2D g2d = (Graphics2D)g;

		if (settings.drawBackgroundGraph()) {
			ClassData classData = (ClassData)v.getUserDatum("tracevis.model.Program");
			if (pickedState.isPicked(v) && !classData.isLoaded()) {
				g2d.setPaint(new Color(1.0f, 1.0f, 0.0f, 0.50f));
			} else {
				g2d.setPaint(new Color(0.0f, 0.0f, 0.0f, 0.05f));
			}

			int bgVertexWidth = vertexSizeFunction.getMaxSize(v);
			int bgVertexHeight = vertexSizeFunction.getMaxSize(v);
			// Draw vertex.
			float bgLeftX = x - bgVertexWidth/2;
			float bgLeftY = y - bgVertexHeight/2;
			Shape bgVertexShape = new Ellipse2D.Double(bgLeftX, bgLeftY, bgVertexWidth, bgVertexHeight);

			g2d.fill(bgVertexShape);
		}

		g2d.setColor(getVertexColor(v));

		int vertexWidth = vertexSizeFunction.getSize(v);
		int vertexHeight = vertexSizeFunction.getSize(v);
		// Draw vertex.
		float leftX = x - vertexWidth/2;
		float leftY = y - vertexHeight/2;
		Shape vertexShape = new Ellipse2D.Double(leftX, leftY, vertexWidth, vertexHeight);

		g2d.fill(vertexShape);

		if (pickedState.isPicked(v)) {
			g2d.setStroke(new BasicStroke(2.0f));
			g2d.setPaint(Color.YELLOW);
			g2d.draw(vertexShape);
		} else {
			g2d.setStroke(new BasicStroke(1.0f));
			g2d.setPaint(Color.BLACK);
			g2d.draw(vertexShape);
		}

		// Draw a bullet if this class is on top of a call stack.
		VertexData vertexData = (VertexData)v.getUserDatum("tracevis.visualization.ProgramView");
		for (long threadID : vertexData.getActiveThreads()) {
			SortedSet<Integer> stackEntries = vertexData.getStackEntries(threadID);
			if (stackEntries.contains(0)) {
				// FIXME: this does not handle multiple threads well.
				boolean drawBullet = true;

				for (Edge edge : (Set<Edge>)v.getIncidentEdges()) {
					EdgeData edgeData = (EdgeData)edge.getUserDatum("tracevis.visualization.ProgramView");
					if (edgeData.isInTransition()) {
						drawBullet = false;
					}
				}

				if (drawBullet) {
					final double bs = 5.0f;
					final double bx = x - bs / 2;
					final double by = y - bs / 2;
					Shape bulletShape = new Ellipse2D.Double(bx, by, bs, bs);
					g2d.setPaint(Color.BLACK);
					g2d.fill(bulletShape);
				}
			}
		}

		// Draw label.
		CustomGraphLabelRenderer labelRenderer = (CustomGraphLabelRenderer)getGraphLabelRenderer();
		labelRenderer.setVertexLabelColor(getVertexColor(v));
		labelVertex(g, v, vertexStringer.getLabel(v), x, y);
	}

	private Pair getEdgeColors(DirectedEdge e) {
		Color sourceColor;
		Color destColor;

		switch (settings.colorMode) {
			case TIMESTAMP:
			case STACK:
				float beginBrightness;
				float endBrightness;

				EdgeData edgeData = (EdgeData)e.getUserDatum("tracevis.visualization.ProgramView");
				Set<Long> activeThreads = edgeData.getActiveThreads();
				if (activeThreads.size() > 0) {
					// Just pick the first thread.
					long thread = activeThreads.iterator().next();
					SortedSet<Integer> stackEntries = edgeData.getStackEntries(thread);
					int depth = stackEntries.iterator().next();

					int stackDepth = program.getCallStack(thread).depth();
					beginBrightness = 1.0f - depth / (stackDepth - 1.0f);
					endBrightness = 1.0f - (depth - 1.0f) / (stackDepth - 1.0f);
				} else {
					beginBrightness = 0.75f;
					endBrightness = 0.25f;
				}

				float saturation = getSaturation(e);
				sourceColor = ColorUtils.getHSBAColor(0.0f, saturation, beginBrightness, 0.75f);
				destColor = ColorUtils.getHSBAColor(0.0f, saturation, endBrightness, 0.75f);
				break;
			case CUSTOM:
				sourceColor = ColorUtils.getHSBAColor(0.0f, 0.0f, 0.75f, 0.75f);
				destColor = ColorUtils.getHSBAColor(0.0f, 0.0f, 0.25f, 0.75f);
				break;
			default:
				throw new RuntimeException();
		}

		return new Pair(sourceColor, destColor);
	}

	@Override
	protected void drawSimpleEdge(Graphics2D g2d, Edge e, int x1, int y1, int x2, int y2) {
		// Determine edge colors.

		CallData callData = (CallData)e.getUserDatum("tracevis.model.Program");

		EdgeData edgeData = (EdgeData)e.getUserDatum("tracevis.visualization.ProgramView");

		Pair endpoints = e.getEndpoints();
		Vertex v1 = (Vertex)endpoints.getFirst();
		Vertex v2 = (Vertex)endpoints.getSecond();
		// Determine the shape of the edge.
		boolean isLoop = v1.equals(v2);
		Shape edgeShape;
		Shape bulletShape = null;

		Pair colors = getEdgeColors((DirectedEdge)e);
		Color beginColor = (Color)colors.getFirst();
		Color endColor = (Color)colors.getSecond();

		Paint paint;
		if(isLoop) {
			// This is a self-loop.
			int vertexWidth = vertexSizeFunction.getSize(v1);
			int vertexHeight = vertexSizeFunction.getSize(v1);

			float leftx = x1 - 0.5f * vertexWidth;
			float rightx = x1 + 0.5f * vertexWidth - 1.0f;
			float topy = y1 - vertexHeight;

			paint = new GradientPaint(leftx, y1, beginColor, rightx, y1, endColor);

			edgeShape = new Ellipse2D.Float(leftx, topy, vertexWidth, vertexHeight);

			if (edgeData.isInTransition()) {
				float t = edgeData.getTransition();
				float xangle = (float)((t + 0.25) * 2.0 * Math.PI);
				float bx = 0.5f * vertexWidth * (float)Math.cos(xangle) + x1;

				float cy = y1 - vertexHeight / 2.0f;
				float yangle = (float)((t + 0.25) * 2.0 * Math.PI);
				float by = 0.5f * vertexHeight * (float)Math.sin(yangle) + cy;

				bulletShape = new Ellipse2D.Double(bx - 5.0/2.0, by - 5.0/2.0, 5, 5);
			}

		} else {
			// This is a normal edge.
			paint = new GradientPaint(x1, y1, beginColor, x2, y2, endColor);

			float dx = x2 - x1;
			float dy = y2 - y1;
			float length = (float)Math.sqrt(dx*dx + dy*dy);

			float nx = -dy / length;
			float ny = dx / length;
			float cx = x1 + dx / 2.0f + settings.getEdgeCurviness() * nx;
			float cy = y1 + dy / 2.0f + settings.getEdgeCurviness() * ny;

			edgeShape = new QuadCurve2D.Float(x1, y1, cx, cy, x2, y2);

			if (edgeData.isInTransition()) {
				float t = edgeData.getTransition();

				Point2D p1 = new Point2D.Double(x1, y1);
				Point2D p2 = new Point2D.Double(cx, cy);
				Point2D p3 = new Point2D.Double(x2, y2);

				Point2D bp = MathUtils.quadCurve(p1, p2, p3, t);

				double bx = bp.getX() - 5.0 / 2.0;
				double by = bp.getY() - 5.0 / 2.0;
				bulletShape = new Ellipse2D.Double(bx, by, 5.0, 5.0);
			}
		}

		if (settings.drawBackgroundGraph()) {
			if (pickedState.isPicked(e) && callData.getCallCount() == 0) {
				g2d.setPaint(new Color(1.0f, 1.0f, 0.0f, 0.50f));
			} else {
				g2d.setPaint(new Color(0.0f, 0.0f, 0.0f, 0.05f));
			}

			float bgStrokeWidth = (float)Math.log10(callData.getMaxCallCount() + 1);
			g2d.setStroke(new BasicStroke(bgStrokeWidth));
			g2d.draw(edgeShape);
		}

		if (callData.getCallCount() > 0) {
			float strokeWidth = (float)Math.log10(callData.getCallCount() + 1);
			if (pickedState.isPicked(e)) {
				g2d.setPaint(Color.YELLOW);
				g2d.setStroke(new BasicStroke(strokeWidth + 2.0f));
				g2d.draw(edgeShape);
			}

			// Draw the edge.
			g2d.setPaint(paint);
			g2d.setStroke(new BasicStroke(strokeWidth));
			g2d.draw(edgeShape);

			if (bulletShape != null) {
				g2d.setPaint(Color.BLACK);
				g2d.fill(bulletShape);
			}
		}
	}

	/**
	 * Returns the renderer settings.
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * Sets the renderer settings.
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/**
	 * Get the vertex size function.
	 */
	public VertexSizeFunction getVertexSizeFunction() {
		return vertexSizeFunction;
	}

	/**
	 * Set the vertex size function.
	 */
	public void setVertexSizeFunction(VertexSizeFunction vertexSizeFunction) {
		this.vertexSizeFunction = vertexSizeFunction;
	}

	/**
	 * A VertexStringer (vertex label provider) which returns the name of the
	 * class represented by a vertex.
	 * @author Pieter Deelen
	 */
	private class VertexLabeler implements VertexStringer {
		@Override
		public String getLabel(ArchetypeVertex vertex) {
			ClassData classData = (ClassData)((Vertex)vertex).getUserDatum("tracevis.model.Program");
			switch (settings.getLabelSettings()) {
				case NONE:
					return "";
				case SHORT:
					int index = classData.getName().lastIndexOf(".");
					String shortName = classData.getName().substring(index+1);
					return shortName;
				case LONG:
					return classData.getName();
				default:
					// Should not happen.
					return null;
			}
		}
	}

	private class CustomGraphLabelRenderer extends DefaultGraphLabelRenderer {
		private Color vertexLabelColor;

		public CustomGraphLabelRenderer(Color vertexLabelColor) {
			super(Color.BLACK, Color.BLACK);
			this.vertexLabelColor = vertexLabelColor;
		}

		@Override
		public Component getGraphLabelRendererComponent(JComponent vv, Object value,
			boolean isSelected, Vertex vertex)
		{
			super.setForeground(vertexLabelColor);
			setIcon(null);
			setBorder(noFocusBorder);
			setValue(value);
			return this;
		}

		public void setVertexLabelColor(Color vertexLabelColor) {
			this.vertexLabelColor = vertexLabelColor;
		}
	}
}

