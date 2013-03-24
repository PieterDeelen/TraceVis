/*
 * CustomSpringLayout.java
 *
 * Author: Pieter Deelen
 * Created: June 15, 2005, 1:29 PM
 *
 */

package tracevis.visualization.structure;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.Set;

import tracevis.visualization.functions.VertexSizeFunction;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.visualization.Coordinates;
import edu.uci.ics.jung.visualization.SpringLayout;

/**
 * A custom spring layout.
 * @author Pieter Deelen
 */
public class CustomSpringLayout extends SpringLayout {
	/**
	 * The layout settings.
	 */
	public static class Settings {
		private final double repulsionConstant;
		private final double forceMultiplier;

		/**
		 * Creates a new instance of Settings.
		 * @param repulsionConstant the repulsion constant.
		 * @param forceMultiplier the force multiplier (or spring constant.
		 */
		public Settings(double repulsionConstant, double forceMultiplier)
		{
			this.repulsionConstant = repulsionConstant;
			this.forceMultiplier = forceMultiplier;
		}

		/**
		 * Returns the repulsion constant.
		 */
		public double getRepulsionConstant() {
			return repulsionConstant;
		}

		/**
		 * Returns the force multiplier.
		 */
		public double getForceMultiplier() {
			return forceMultiplier;
		}
	}

	private Settings settings;

	private VertexSizeFunction vertexSizeFunction;
	private LengthFunction lengthFunction;

	private double sizeMultiplier;

	private Dimension bounds;

	/**
	 * Creates a new instance of CustomSpringLayout.
	 * @param graph the graph to create a layout for.
	 */
	public CustomSpringLayout(Graph graph) {
		super(graph);

		settings = new Settings(100.0, 0.40);
		bounds = null;
	}

	/**
	 * Returns the desired length of a specified edge.
	 * @param e the specified edge.
	 */
	@Override
	public double getLength(Edge e) {
		return getLengthFunction().getLength(e);
	}

	/**
	 * Computes an iteration.
	 */
	@Override
	public void advancePositions() {
		// Initialize vertex data records.
		for (Iterator iter = getGraph().getVertices().iterator(); iter.hasNext();) {
			Vertex v = (Vertex) iter.next();
			SpringVertexData svd = getSpringData(v);
			if (svd == null) {
				System.out.println("How confusing!");
				continue;
			}
			svd.dx = 0.0;
			svd.dy = 0.0;
			svd.edgedx = svd.edgedy = 0;
			svd.repulsiondx = svd.repulsiondy = 0;
		}

		relaxEdges();
		calculateRepulsion();
		moveNodes();
	}

	/**
	 * Recompute the layout and compute a new bounding box.
	 */
	public void recompute() {
		// Start with zero-sized vertices and slowly increase their sizes. This
		// should improve convergence.
		final int iterationCount = 100;
		sizeMultiplier = 0.0;
		for (int i = 0; i < iterationCount; i++) {
			advancePositions();
			sizeMultiplier += 1.0 / iterationCount;
		}

		for (int i = 0; i < iterationCount; i++) {
			advancePositions();
		}

		// Determine top and left boundaries;
		double lx = Double.POSITIVE_INFINITY;
		double uy = Double.POSITIVE_INFINITY;
		Vertex lv = null;
		Vertex uv = null;
		for (Vertex v : (Set<Vertex>)getGraph().getVertices()) {
			Coordinates xyd = getCoordinates(v);

			double vx = xyd.getX() - vertexSizeFunction.getMaxSize(v) / 2.0;
			if (vx < lx) {
				lv = v;
				lx = vx;
			}

			double vy = xyd.getY() - vertexSizeFunction.getMaxSize(v) / 2.0;
			if (vy < uy) {
				uv = v;
				uy = vy;
			}
		}

		double tx = vertexSizeFunction.getMaxSize(lv) / 2.0 - getCoordinates(lv).getX();
		double ty = vertexSizeFunction.getMaxSize(uv) / 2.0 - getCoordinates(uv).getY();

		// Translate all vertices.
		for (Vertex v : (Set<Vertex>)getGraph().getVertices()) {
			Coordinates xyd = getCoordinates(v);
			xyd.addX(tx);
			xyd.addY(ty);
		}

		// Compute new size.
		double rx = Double.NEGATIVE_INFINITY;
		double ly = Double.NEGATIVE_INFINITY;
		for (Vertex v : (Set<Vertex>)getGraph().getVertices()) {
			Coordinates xyd = getCoordinates(v);
			double vx = xyd.getX() + vertexSizeFunction.getMaxSize(v) / 2.0;
			rx = Math.max(rx, vx);

			double vy = xyd.getY() + vertexSizeFunction.getMaxSize(v) / 2.0;
			ly = Math.max(ly, vy);
		}

		bounds = new Dimension((int)Math.ceil(rx), (int)Math.ceil(ly));
	}

	/**
	 * Compute spring forces.
	 */
	@Override
	protected void relaxEdges() {
		for (Iterator i = getGraph().getEdges().iterator(); i.hasNext();) {
			Edge e = (Edge) i.next();

			Vertex v1 = getAVertex(e);
			Vertex v2 = e.getOpposite(v1);

			double vx = getX(v1) - getX(v2);
			double vy = getY(v1) - getY(v2);
			double len = Math.sqrt(vx * vx + vy * vy);

			// The *actual* desired length of the edge e (the distance between
			// the centers of v1 and v2) is the desired length of e plus the
			// radii of v1 and v2. This compensates for vertex sizes which are
			// not infinitely small.
			int v1Size = vertexSizeFunction.getMaxSize(v1);
			int v2Size = vertexSizeFunction.getMaxSize(v2);
			double desiredLen = getLength(e) + sizeMultiplier * (v1Size / 2.0 + v2Size / 2.0);

			// Round from zero, if needed (zero would be bad).
			len = (len == 0) ? .0001 : len;

			// The spring force is according to Hooke's law.
			double f = settings.forceMultiplier * (desiredLen - len) / len;

			// The actual movement distance 'dx' is the force multiplied by the
			// distance to go.
			double dx = f * vx;
			double dy = f * vy;
			SpringVertexData v1D, v2D;
			v1D = getSpringData(v1);
			v2D = getSpringData(v2);

			SpringEdgeData sed = getSpringData(e);
			sed.f = f;

			// Add the calculated force to the sum of edge forces for v1.
			// Divide by the degree of v1 to prevent jittering.
			v1D.edgedx += dx / v1.degree();
			v1D.edgedy += dy / v1.degree();

			// Add the calculated force to the sum of edge forces for v2.
			// Divide by the degree of v2 to prevent jittering.
			v2D.edgedx += -dx / v2.degree();
			v2D.edgedy += -dy / v2.degree();
		}
	}

	/**
	 * Compute repelling forces.
	 */
	@Override
	protected void calculateRepulsion() {
		Iterator iter = getGraph().getVertices().iterator();
		while (iter.hasNext()) {
			Vertex v = (Vertex) iter.next();
			if (dontMove(v)) continue;

			SpringVertexData svd = getSpringData(v);
			double dx = 0, dy = 0;

			for (Iterator iter2 = getGraph().getVertices().iterator(); iter2.hasNext();) {
				Vertex v2 = (Vertex) iter2.next();

				// Don't let the vertex interact with itself.
				if (v == v2) continue;

				// Connected vertices don't repel each other.
				if (v.findEdge(v2) != null) continue;
				if (v2.findEdge(v) != null) continue;

				double vx = getX(v) - getX(v2);
				double vy = getY(v) - getY(v2);

				double vSize = vertexSizeFunction.getMaxSize(v);
				double v2Size = vertexSizeFunction.getMaxSize(v2);

				double distance = Math.sqrt(vx*vx + vy*vy) - sizeMultiplier * (vSize/2 - v2Size/2);
				if (distance <= 0) {
					distance = 0.0001;
				}

				// The repulsion force is according to Eades (1984).
				dx += settings.repulsionConstant * vx / (distance * distance);
				dy += settings.repulsionConstant * vy / (distance * distance);

			}

			svd.repulsiondx += dx;
			svd.repulsiondy += dy;
		}
	}

	/**
	 * Move all nodes according to the forces exerted on them.
	 */
	@Override
	protected void moveNodes() {
		Iterator i = getGraph().getVertices().iterator();
		while (i.hasNext()) {
			Vertex v = (Vertex) i.next();
			if (dontMove(v)) continue;
			SpringVertexData vd = getSpringData(v);
			Coordinates xyd = getCoordinates(v);

			// Add repulsion and edge forces.
			vd.dx += vd.repulsiondx + vd.edgedx;
			vd.dy += vd.repulsiondy + vd.edgedy;

			// Move vertex.
			xyd.addX(vd.dx);
			xyd.addY(vd.dy);
		}
	}

	@Override
	public boolean incrementsAreDone() {
		return true;
	}

	/**
	 * Returns the bounding box of the layout.
	 */
	public Dimension getBounds() {
		return bounds;
	}

	/**
	 * Returns the vertex size function.
	 */
	public VertexSizeFunction getVertexSizeFunction() {
		return vertexSizeFunction;
	}

	/**
	 * Sets the vertex size function.
	 */
	public void setVertexSizeFunction(VertexSizeFunction vertexSizeFunction) {
		this.vertexSizeFunction = vertexSizeFunction;
	}

	/**
	 * Gets the edge length function.
	 */
	public LengthFunction getLengthFunction() {
		return lengthFunction;
	}

	/**
	 * Sets the edge length function.
	 */
	public void setLengthFunction(LengthFunction lengthFunction) {
		this.lengthFunction = lengthFunction;
	}

	/**
	 * Returns the layout settings.
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * Sets the layout settings.
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
}
