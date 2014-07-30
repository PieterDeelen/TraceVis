/*
 * TimeLineViewMouse.java
 *
 * Author: Pieter Deelen
 * Created: April 12, 2006, 3:08 PM
 *
 */

package tracevis.visualization.timeline;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import tracevis.model.Program;
import tracevis.model.types.ClassData;
import tracevis.model.types.Event;
import tracevis.model.types.Range;
import tracevis.visualization.utilities.BrushMultiPickedState;
import edu.uci.ics.jung.graph.Vertex;

/**
 * The mouse handler for the time line view.
 * @author Pieter Deelen
 */
public class TimeLineViewMouse implements MouseListener, MouseWheelListener, MouseMotionListener {
	private Point2D down;
	private final TimeLineView timeLineView;
	private final Program program;
	private final BrushMultiPickedState pickedState;

	/**
	 * Creates a new instance of TimeLineViewMouse.
	 * @param timeLineView instance of TimeLineViewMouse to handle mouse events
	 *                     for.
	 * @param program the program to control.
	 * @param pickedState the selection model.
	 */
	public TimeLineViewMouse(TimeLineView timeLineView, Program program,
	                         BrushMultiPickedState pickedState)
	{
		this.down = null;
		this.timeLineView = timeLineView;
		this.program = program;
		this.pickedState = pickedState;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		down = e.getPoint();
		if (SwingUtilities.isLeftMouseButton(e)) {
			double currentX = timeLineView.transformTimeToX(program.getCurrentTime());
			double metricX = timeLineView.transformTimeToX(program.getMetricStartTime());
			// Try to draw one of the time indicators.
			if (Math.abs(currentX - down.getX()) < 3) {
				timeLineView.setCurrentTimeIndicatorSelected(true);
			} else if (Math.abs(metricX - down.getX()) < 3) {
				timeLineView.setMetricTimeIndicatorSelected(true);
			}
		} else if (SwingUtilities.isRightMouseButton(e)) {
			// Start a new selection.
			Range newSelection = new Range((long)down.getX(), (long)down.getX());
			timeLineView.setSelection(newSelection);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			// Disable any selection of the time indicators.
			timeLineView.setCurrentTimeIndicatorSelected(false);
			timeLineView.setMetricTimeIndicatorSelected(false);
		} else if (SwingUtilities.isRightMouseButton(e)) {
			// Zoom into selection.
			Range selection = timeLineView.getSelection();
			Dimension size = timeLineView.getSize();
			// Only zoom into selections with positive size.
			if (selection != null && selection.getWidth() > 0) {
				long startTime = timeLineView.transformXToTime(selection.getBegin());
				long endTime = timeLineView.transformXToTime(selection.getEnd());

				// Don't allow infinite zoom-in.
				// TODO: implement this more subtly.
				if (endTime - startTime > size.width) {
					timeLineView.setViewRange(new Range(startTime, endTime));
				}
			}
			timeLineView.setSelection(null);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (timeLineView.isCurrentTimeIndicatorSelected()) {
				// Drag the current time indicator.
				Point2D p = e.getPoint();

				long currentTime = program.getCurrentTime();
				long metricStartTime = program.getMetricStartTime();
				long newTime = timeLineView.transformXToTime(p.getX());
				newTime = Math.min(program.getEndTime(), newTime);
				newTime = Math.max(program.getStartTime(), newTime);

				// If the time line indicators are linked, move them together.
				if (timeLineView.getLinkIndicators()) {
					program.setMetricStartTime(metricStartTime + newTime - currentTime);
				}

				if (newTime < program.getMetricStartTime()) {
					program.setMetricStartTime(newTime);
				}
				program.setCurrentTime(newTime);
			} else if (timeLineView.isMetricTimeIndicatorSelected()) {
				// Drag the metric time indicator.
				Point2D p = e.getPoint();

				long newTime = timeLineView.transformXToTime(p.getX());
				newTime = Math.min(program.getCurrentTime(), newTime);
				newTime = Math.max(program.getStartTime(), newTime);
				program.setMetricStartTime(newTime);
			}
		} else if (SwingUtilities.isRightMouseButton(e)) {
			Point2D p = e.getPoint();
			long leftx = Math.min((long)down.getX(), (long)p.getX());
			long rightx = Math.max((long)down.getX(), (long)p.getX());
			timeLineView.setSelection(new Range(leftx, rightx));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			Point2D p = e.getPoint();

			Collection vertices = timeLineView.getVerticesOnLine((int)p.getY());
			if (vertices != null) {
				Vertex classVertex = (Vertex)vertices.iterator().next();

				if (e.isControlDown()) {
					// Toggle selection of vertex.
					boolean isPicked = pickedState.isPicked(classVertex);
					pickedState.pick(classVertex, !isPicked);
				} else {
					// Deselect all vertices and edges.
					pickedState.clearPickedVertices();
					pickedState.clearPickedEdges();

					// Select the edge.
					pickedState.pick(classVertex, true);
				}
			}
		} else if (SwingUtilities.isMiddleMouseButton(e)) {
			Point2D p = e.getPoint();

			long newTime = timeLineView.transformXToTime(p.getX());
			newTime = Math.min(program.getEndTime(), newTime);
			newTime = Math.max(program.getStartTime(), newTime);

			if (!e.isControlDown()) {
				if (newTime < program.getMetricStartTime()) {
					program.setMetricStartTime(newTime);
				}
				program.setCurrentTime(newTime);
			} else {
				if (newTime > program.getCurrentTime()) {
					program.setCurrentTime(newTime);
				}
				program.setMetricStartTime(newTime);
			}
		} else if (SwingUtilities.isRightMouseButton(e)) {
			if (e.getClickCount() == 2) {
				timeLineView.setViewRange(new Range(program.getStartTime(), program.getEndTime()));
			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int amount = e.getWheelRotation();

		if (amount != 0) {
			Point p = e.getPoint();

			long tp = timeLineView.transformXToTime(p.getX());
			Range viewRange = timeLineView.getViewRange();
			if (amount > 0) {
				// Zoom out if wheel is turned towards the user.
				long start = (long)(1.1 * (viewRange.getBegin() - tp) + tp);
				long end = (long)(1.1 * (viewRange.getEnd() - tp) + tp);

				// TODO: implement a better range limiter.
				timeLineView.setViewRange(new Range(
					Math.max(program.getStartTime(), start),
					Math.min(program.getEndTime(), end)
				));

			} else if (amount < 0)  {
				// Zoom in if wheel is turned away from the user.
				long start = (long)(0.9 * (viewRange.getBegin() - tp) + tp);
				long end = (long)(0.9 * (viewRange.getEnd() - tp) + tp);

				Dimension size = timeLineView.getSize();
				if (end - start > size.width) {
					timeLineView.setViewRange(new Range(start, end));
				}
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		Collection vertices = timeLineView.getVerticesOnLine((int)p.getY());
		if (vertices != null) {
			Vertex classVertex = (Vertex)vertices.iterator().next();
			pickedState.setBrushed(classVertex);

			long x0 = timeLineView.transformXToTime(p.getX());
			long x1 = timeLineView.transformXToTime(p.getX() + 1);
			ClassData classData = (ClassData)classVertex.getUserDatum("tracevis.model.Program");
			Range pixelRange = new Range(x0, x1);
			List<Range> activityRanges = classData.getActivityRanges(pixelRange);

			List<Event> showableEvents = new LinkedList<Event>();
			if (activityRanges.size() == 1) {
				Range range = activityRanges.get(0);
				List<Event> events = classData.getEvents(range);
				showableEvents.addAll(events);
			} else if (activityRanges.size() > 1) {
				List<Event> events = classData.getEvents(pixelRange);
				showableEvents.addAll(events);
			}
			timeLineView.setShowableEvents(showableEvents);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {
		pickedState.setBrushed((Vertex)null);
		timeLineView.setShowableEvents(new LinkedList<Event>());
	}
}
