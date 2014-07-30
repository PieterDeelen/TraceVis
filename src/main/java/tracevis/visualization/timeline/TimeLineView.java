/*
 * TimeLineView.java
 *
 * Author: Pieter Deelen
 * Created: October 4, 2005, 12:54 PM
 *
 */

package tracevis.visualization.timeline;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;

import tracevis.model.Program;
import tracevis.model.types.CallData;
import tracevis.model.types.ClassData;
import tracevis.model.types.Event;
import tracevis.model.types.MethodEntryEvent;
import tracevis.model.types.MethodExitEvent;
import tracevis.model.types.ProgramListener;
import tracevis.model.types.Range;
import tracevis.model.types.TimeFunction;
import tracevis.visualization.structure.VertexData;
import tracevis.visualization.utilities.BrushEventListener;
import tracevis.visualization.utilities.BrushMultiPickedState;
import tracevis.visualization.utilities.ColorModel;
import tracevis.visualization.utilities.ColorModelListener;
import edu.uci.ics.jung.graph.ArchetypeEdge;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.DirectedEdge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.utils.UserData;
import edu.uci.ics.jung.visualization.PickEventListener;

/**
 * Renders the time line view.
 * @author Pieter Deelen
 */
public class TimeLineView extends JComponent {
	private final Program program;
	private final BrushMultiPickedState pickedState;

	private Range viewRange;
	private Range selection;

	private BufferedImage buffer;
	private boolean bufferIsValid;

	private boolean isCurrentTimeIndicatorSelected;
	private boolean isMetricTimeIndicatorSelected;
	private boolean linkIndicators;

	private Map<String, Vertex> classMap;

	private double activityRatiosTotal;

	private final MultiMap lineToVertexMap;

	private List<Event> showableEvents;

	private TimeLineSettings settings;

	private final List<TimeLineViewListener> listeners;

	private final TimeLineViewMouse viewMouse;

	/**
	 * Creates a new instance of TimeLineView.
	 */
	public TimeLineView(Program program, BrushMultiPickedState pickedState,
	                    ColorModel colorModel, TimeLineSettings settings)
	{
		this.program = program;
		program.addListener(new ProgramListenerImpl());

		this.pickedState = pickedState;
		PickBrushListener pickBrushListener = new PickBrushListener();
		pickedState.addListener(pickBrushListener);
		pickedState.addBrushEventListener(pickBrushListener);

		colorModel.addListener(new ColorModelListenerImpl());

		this.settings = settings;

		this.viewRange = null;
		this.selection = null;

		this.buffer = null;
		this.bufferIsValid = false;

		this.isCurrentTimeIndicatorSelected = false;
		this.isMetricTimeIndicatorSelected = false;
		this.linkIndicators = false;

		this.activityRatiosTotal = 0;

		this.lineToVertexMap = new MultiHashMap();

		this.showableEvents = new LinkedList<Event>();

		this.classMap = new TreeMap<String, Vertex>();

		this.listeners = new ArrayList<TimeLineViewListener>();

		ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
		toolTipManager.registerComponent(this);

		this.viewMouse = new TimeLineViewMouse(this, program, pickedState);

		addComponentListener(new ResizeHandler());
	}

	//
	// Draw methods.
	//

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;

		if (classMap.size() > 0) {
			switch (settings.getMode()) {
				case SHOW_ACTIVITY:
					drawActivityView(g2d);
					drawCalls(g2d);
					drawEvents(g2d);
					break;
				case SHOW_INSTANCES:
					drawInstanceView(g2d);
					break;
			}

			drawTimeIndicators(g2d);

			drawPicks(g2d);
		} else {
			Dimension size = getSize();
			g2d.setPaint(Color.WHITE);
			g2d.fill(new Rectangle2D.Double(0, 0, size.width, size.height));
		}

		drawSelection(g2d);
	}

	private void drawActivityViewSpan(int y) {
		List<Double> weights = new ArrayList<Double>();
		List<TimeVertexData> vertexDatas = new ArrayList<TimeVertexData>();
		List<Float> hues = new ArrayList<Float>();

		Collection vertices = (Collection)lineToVertexMap.get(y);
		if (vertices != null) {
			for (Iterator it = vertices.iterator(); it.hasNext();) {
				Vertex vertex = (Vertex)it.next();

				TimeVertexData timeLineVertexData = TimeLineView.getVertexData(vertex);
				VertexData vertexData = (VertexData)vertex.getUserDatum("tracevis.visualization.ProgramView");

				weights.add(timeLineVertexData.getWeight(y));
				vertexDatas.add(timeLineVertexData);
				hues.add(vertexData.getHue());
			}
		}

		float b = 1.0f;
		for (int x = 0; x < buffer.getWidth(); x++) {
			float h = 0.0f;
			float s = 0.0f;

			for (int i = 0; i < vertexDatas.size(); i++) {
				TimeVertexData vertexData = vertexDatas.get(i);

				double weight = vertexData.getWeight(y);
				float hue = hues.get(i);
				h += weight * hue;
				s += weight * vertexData.getCacheValue(x);
			}

			int color = Color.HSBtoRGB(h, s, b);
			buffer.setRGB(x, y, color);
		}
	}

	private void drawActivityView(Graphics2D g2d) {
		if (!bufferIsValid) {
			for (int y = 0; y < buffer.getHeight(); y++) {
				drawActivityViewSpan(y);
			}

			bufferIsValid = true;
		}

		g2d.drawImage(buffer, null, 0, 0);
	}

	private void drawInstanceViewSpan(int classIndex, Vertex vertex, int maxValue) {
		Dimension size = getSize();

		long viewWidth = viewRange.getEnd() - viewRange.getBegin();
		long increment = viewWidth / size.width;

		ClassData classData = (ClassData)vertex.getUserDatum("tracevis.model.Program");
		VertexData vertexData = (VertexData)vertex.getUserDatum("tracevis.visualization.ProgramView");
		long leftTime = viewRange.getBegin();
		long rightTime = leftTime + increment;
		TimeFunction<Integer> instances = classData.getInstanceCountFunction();

		float hue = vertexData.getHue();
		for (int x = 0; x < size.width; x++) {
			int value = instances.get(rightTime);

			float saturation;
			switch (settings.getScale()) {
				case LINEAR:
					saturation = value / (float)maxValue;
					break;
				case SQUARE_ROOT:
					saturation = (float)(Math.sqrt(value) / Math.sqrt(maxValue));
					break;
				case LOGARITHMIC:
					saturation = (float)(Math.log(value + 1) / Math.log(maxValue + 1));
					break;
				default:
					throw new RuntimeException();
			}

			int color = Color.HSBtoRGB(hue, saturation, 1.00f);
			buffer.setRGB(x, classIndex, color);

			leftTime += increment;
			rightTime += increment;
		}
	}

	private void drawInstanceView(Graphics2D g2d) {
		Graph callGraph = program.getCallGraph();
		int maxValue = 0;
		for (Vertex v : (Set<Vertex>)callGraph.getVertices()) {
			ClassData classData = (ClassData)v.getUserDatum("tracevis.model.Program");
			maxValue = Math.max(maxValue, classData.getMaxInstanceCount());
		}

		int row = 0;
		for (Vertex vertex : classMap.values()) {
			drawInstanceViewSpan(row, vertex, maxValue);
			row++;
		}

		AffineTransform oldTransform = g2d.getTransform();

		g2d.scale(1.0f, getHeight() / (float)buffer.getHeight());
		g2d.drawImage(buffer, null, 0, 0);

		g2d.setTransform(oldTransform);
	}

	private void drawCalls(Graphics2D g2d) {
		for (DirectedEdge edge : (Set<DirectedEdge>)pickedState.getPickedEdges()) {
			CallData callData = (CallData)edge.getUserDatum("tracevis.model.Program");
			Vertex caller = edge.getSource();
			Vertex callee = edge.getDest();

			double y0 = TimeLineView.getVertexData(caller).getCenterHeight();
			double y1 = TimeLineView.getVertexData(callee).getCenterHeight();

			for (int x = 0; x < getWidth(); x++) {
				long t0 = transformXToTime(x);
				long t1 = transformXToTime(x + 1);

				Range tRange = new Range(t0, t1);
				List<Event> events = callData.getEvents(tRange);

				int count = 0;
				for (Event event : events) {
					count++;
				}

				if (count > 0) {
					Color source = new Color(0.0f, 0.0f, 0.0f, 0.25f);
					Color dest = new Color(0.0f, 0.0f, 0.0f, 0.75f);
					Paint gradient = new GradientPaint(x, (float)y0, source,
						                               x, (float)y1, dest);
					g2d.setPaint(gradient);
					g2d.draw(new Line2D.Double(x, y0, x, y1));
				}
			}
		}
	}

	private void drawEvents(Graphics2D g2d) {
		for (Event event : showableEvents) {
			if (event instanceof MethodEntryEvent || event instanceof MethodExitEvent) {
				ClassData callerData;
				ClassData calleeData;

				callerData = program.getCaller(event);
				calleeData = program.getCallee(event);

				if (callerData == null) {
					continue;
				}
				Vertex callerVertex = callerData.getVertex();
				Vertex calleeVertex = calleeData.getVertex();

				long timeStamp = event.getTime();
				double x = transformTimeToX(timeStamp);

				double y0 = TimeLineView.getVertexData(callerVertex).getCenterHeight();
				double y1 = TimeLineView.getVertexData(calleeVertex).getCenterHeight();

				Color source = new Color(0.0f, 0.0f, 0.0f, 0.25f);
				Color dest = new Color(0.0f, 0.0f, 0.0f, 0.75f);
				Paint gradient = new GradientPaint((float)x, (float)y0, source,
					(float)x, (float)y1, dest);
				g2d.setPaint(gradient);
				g2d.draw(new Line2D.Double(x, y0, x, y1));
			}
		}
	}

	private void drawTimeIndicators(Graphics2D g2d) {
		Dimension size = getSize();

		long metricStartTime = program.getMetricStartTime();
		double metricX = transformTimeToX(metricStartTime);
		if (isMetricTimeIndicatorSelected) {
			g2d.setPaint(new Color(0.0f, 1.0f, 0.0f, 1.0f));
		} else {
			g2d.setPaint(new Color(0.0f, 1.0f, 0.0f, 0.5f));
		}
		g2d.draw(new Rectangle2D.Double(metricX, 0, 1, size.height));


		long currentTime = program.getCurrentTime();
		double currentX = transformTimeToX(currentTime);
		if (isCurrentTimeIndicatorSelected) {
			g2d.setPaint(new Color(1.0f, 0.0f, 0.0f, 1.0f));
		} else {
			g2d.setPaint(new Color(1.0f, 0.0f, 0.0f, 0.5f));
		}
		g2d.draw(new Rectangle2D.Double(currentX, 0, 1, size.height));

	}

	private void drawPicks(Graphics2D g2d) {
		Iterator pickIt = pickedState.getPickedVertices().iterator();
		while (pickIt.hasNext()) {
			Vertex vertex = (Vertex)pickIt.next();
			TimeVertexData vertexData = TimeLineView.getVertexData(vertex);
			g2d.setPaint(new Color(0.0f, 0.0f, 0.0f, 0.20f));
			double y0 = vertexData.getStartHeight();
			double y1 = vertexData.getEndHeight() - y0;
			g2d.fill(new Rectangle2D.Double(0.0, y0, getWidth(), y1));
		}

		Vertex brushedVertex = (Vertex)pickedState.getBrushedVertex();
		if (brushedVertex != null) {
			TimeVertexData vertexData = TimeLineView.getVertexData(brushedVertex);
			g2d.setPaint(new Color(0.0f, 0.0f, 0.0f, 0.10f));
			double y0 = vertexData.getStartHeight();
			double y1 = vertexData.getEndHeight() - y0;
			g2d.fill(new Rectangle2D.Double(0.0, y0, getWidth(), y1));
		}
	}

	private void drawSelection(Graphics2D g2d) {
		Dimension size = getSize();

		Color selectionBorderColor = UIManager.getDefaults().getColor("textHighlight");
		Color selectionColor = new Color(selectionBorderColor.getRed(),
		                                 selectionBorderColor.getGreen(),
		                                 selectionBorderColor.getBlue(), 16);

		if (selection != null) {
			long x = selection.getBegin();
			long y = 0;
			long width = selection.getEnd() - selection.getBegin();
			long height = size.height;

			if (width > 0) {
				Shape selectionRect = new Rectangle2D.Double(x, y, width, height);

				g2d.setPaint(selectionBorderColor);
				g2d.draw(selectionRect);
				g2d.setPaint(selectionColor);
				g2d.fill(selectionRect);
			}
		}
	}

	@Override
	public String getToolTipText(MouseEvent event) {
		if (lineToVertexMap != null) {
			Point p = event.getPoint();
			Collection vertices = (Collection)lineToVertexMap.get((int)p.getY());
			if (vertices != null) {
				StringBuilder text = new StringBuilder();
				Iterator it = vertices.iterator();
				text.append("<html>");
				while(it.hasNext()) {
					Vertex classVertex = (Vertex)it.next();
					ClassData classData = (ClassData)classVertex.getUserDatum("tracevis.model.Program");
					text.append(classData.getName() + "<br>");
				}
				text.append("</html>");

				return text.toString();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private void initializeBuffers() {
		Dimension size = getSize();
		switch (settings.getMode()) {
			case SHOW_ACTIVITY:
				buffer = new BufferedImage(size.width, size.height,
										   BufferedImage.TYPE_INT_ARGB);
				break;
			case SHOW_INSTANCES:
				buffer = new BufferedImage(size.width, classMap.size(),
										   BufferedImage.TYPE_INT_ARGB);
				break;
		}
	}

	//
	// Computation methods.
	//

	private void updateClassHeights() {
		if (settings.getMode() == TimeLineSettings.Mode.SHOW_ACTIVITY) {
			long totalTime = program.getEndTime() - program.getStartTime();

			activityRatiosTotal = 0.0;
			for (Vertex vertex : classMap.values()) {
				TimeVertexData vertexData = TimeLineView.getVertexData(vertex);
				vertexData.updateActivityRatio(totalTime);

				double activityRatio = vertexData.getActivityRatio();
				activityRatiosTotal += Math.pow(activityRatio, settings.getActivityExponent());
			}
		}

		lineToVertexMap.clear();
		double totalHeight = 0.0;
		for (Vertex vertex : classMap.values()) {
			double heightRatio = 0.0;
			TimeVertexData timeLineVertexData = TimeLineView.getVertexData(vertex);
			switch (settings.getMode()) {
				case SHOW_ACTIVITY:
					double activityRatio = timeLineVertexData.getActivityRatio();
					heightRatio = Math.pow(activityRatio, settings.getActivityExponent()) /
												  activityRatiosTotal;
					break;
				case SHOW_INSTANCES:
					heightRatio = 1.0 / classMap.size();
					break;
			}

			double classHeight = getHeight() * heightRatio;

			timeLineVertexData.setStartHeight(totalHeight);
			int y0 = (int)Math.floor(totalHeight);
			totalHeight += classHeight;
			timeLineVertexData.setEndHeight(totalHeight);
			int y1 = (int)Math.ceil(totalHeight);

			for (int y = y0; y < y1; y++) {
				lineToVertexMap.put(y, vertex);
			}
		}
	}

	private void updateTimeLineCaches() {
		if (settings.getMode() == TimeLineSettings.Mode.SHOW_ACTIVITY) {
			for (Vertex vertex : classMap.values()) {
				TimeVertexData vertexData = TimeLineView.getVertexData(vertex);
				vertexData.updateTimeLineCache(buffer.getWidth(), viewRange);
			}

			bufferIsValid = false;
		}
	}

	//
	// Size related methods.
	//

	private class ResizeHandler extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			if (classMap.size() > 0) {
				initializeBuffers();
				updateTimeLineCaches();
				updateClassHeights();
			}
			repaint();
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(1000, 100);
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	//
	// Listener for program events.
	//

	private class ProgramListenerImpl implements ProgramListener {
		@Override
		public void traceLoaded() {
			setViewRange(new Range(program.getStartTime(), program.getEndTime()));

			Graph callGraph = program.getCallGraph();

			classMap = new TreeMap<String, Vertex>();
			for (Vertex vertex : (Set<Vertex>)callGraph.getVertices()) {
				ClassData classData = (ClassData)vertex.getUserDatum("tracevis.model.Program");
				classMap.put(classData.getName(), vertex);
			}

			for (Vertex vertex : (Set<Vertex>)callGraph.getVertices()) {
				TimeVertexData vertexData = new TimeVertexData(vertex);
				vertex.addUserDatum(getKey(), vertexData, UserData.SHARED);
			}

			if (classMap.size() > 0) {
				initializeBuffers();
				updateTimeLineCaches();
				updateClassHeights();
			}

			addMouseListener(viewMouse);
			addMouseMotionListener(viewMouse);
			addMouseWheelListener(viewMouse);

			repaint();
		}

		@Override
		public void traceFiltered() {
			if (classMap.size() > 0) {
				initializeBuffers();
				updateTimeLineCaches();
				updateClassHeights();

				repaint();
			}
		}

		@Override
		public void timeChanged() {
			repaint();
		}

		@Override
		public void methodEntered(ClassData caller, ClassData callee) {}
		@Override
		public void methodExited(ClassData caller, ClassData callee) {}
	}

	//
	// Listener for pick and brush events.
	//

	private class PickBrushListener implements PickEventListener, BrushEventListener {
		@Override
		public void vertexPicked(ArchetypeVertex v) {
			repaint();
		}

		@Override
		public void vertexUnpicked(ArchetypeVertex v) {
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

		@Override
		public void edgePicked(ArchetypeEdge e) {
			repaint();
		}

		@Override
		public void edgeUnpicked(ArchetypeEdge e) {
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
	}

	private class ColorModelListenerImpl implements ColorModelListener {
		@Override
		public void colorsChanged() {
			bufferIsValid = false;
			repaint();
		}
	}

	//
	// Event listener methods.
	//

	public void addListener(TimeLineViewListener listener) {
		listeners.add(listener);
	}

	public void removeListener(TimeLineViewListener listener) {
		listeners.remove(listener);
	}

	private void fireViewRangeChanged() {
		for (TimeLineViewListener listener : listeners) {
			listener.viewRangeChanged();
		}
	}

	//
	// Transformation methods.
	//

	public long transformXToTime(double x) {
		Dimension size = getSize();
		double viewWidth = viewRange.getEnd() - viewRange.getBegin();
		double xscale = size.width / viewWidth;
		return (long)(x / xscale + viewRange.getBegin());
	}

	public double transformTimeToX(long time) {
		Dimension size = getSize();
		double viewWidth = viewRange.getEnd() - viewRange.getBegin();
		double xscale = size.width / viewWidth;
		return (long)((time - viewRange.getBegin()) * xscale);
	}

	public Collection getVerticesOnLine(int y) {
		return (Collection)lineToVertexMap.get(y);
	}

	//
	// Accessors.
	//

	public static String getKey() {
		return "tracevis.visualization.timeline.TimeLineView";
	}

	public static TimeVertexData getVertexData(Vertex vertex) {
		return (TimeVertexData)vertex.getUserDatum(getKey());
	}

	public TimeLineSettings getSettings() {
		return settings;
	}

	public void setSettings(TimeLineSettings settings) {
		this.settings = settings;
		switch (settings.getMode()) {
			case SHOW_ACTIVITY:
				initializeBuffers();
				updateTimeLineCaches();
				updateClassHeights();
				break;
			case SHOW_INSTANCES:
				initializeBuffers();
				updateClassHeights();
				break;
		}
		repaint();
	}

	public Range getViewRange() {
		return viewRange;
	}

	public void setViewRange(Range viewRange) {
		this.viewRange = viewRange;
		updateTimeLineCaches();

		fireViewRangeChanged();

		repaint();
	}

	public boolean getLinkIndicators() {
		return linkIndicators;
	}

	public void setLinkIndicators(boolean linkIndicators) {
		this.linkIndicators = linkIndicators;
	}

	public Range getSelection() {
		return selection;
	}

	public void setSelection(Range selection) {
		this.selection = selection;
		repaint();
	}

	public boolean isCurrentTimeIndicatorSelected() {
		return isCurrentTimeIndicatorSelected;
	}

	public void setCurrentTimeIndicatorSelected(boolean isCurrentTimeIndicatorSelected) {
		this.isCurrentTimeIndicatorSelected = isCurrentTimeIndicatorSelected;
		repaint();
	}

	public boolean isMetricTimeIndicatorSelected() {
		return isMetricTimeIndicatorSelected;
	}

	public void setMetricTimeIndicatorSelected(boolean isMetricTimeIndicatorSelected) {
		this.isMetricTimeIndicatorSelected = isMetricTimeIndicatorSelected;
		repaint();
	}

	public List<Event> getShowableEvents() {
		return showableEvents;
	}

	public void setShowableEvents(List<Event> showableEvents) {
		this.showableEvents = showableEvents;
		repaint();
	}
}
