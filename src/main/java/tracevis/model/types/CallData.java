/*
 * CallData.java
 *
 * Author: Pieter Deelen
 * Created: June 9, 2005, 1:49 PM
 *
 */

package tracevis.model.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tracevis.utilities.BinarySearch;
import tracevis.utilities.Condition;
import edu.uci.ics.jung.graph.Edge;

/**
 * A JUNG user data record to be stored with an edge, which stores information
 * related to the calls this edge represents.
 * @author Pieter Deelen
 */
public class CallData {
	private final Edge edge;

	private int callCountTotal;
	private int maxCallCountTotal;
	private Map<String, Integer> callCount;
	private Map<String, TimeFunction<Integer>> callCountFunctions;

	private Event lastEvent;
	private List<Event> events;

	/**
	 * Creates a new instance of CallData.
	 * @param edge the edge this CallData is associated with.
	 */
	public CallData(Edge edge) {
		this.edge = edge;

		this.callCountTotal = 0;

		this.callCount = new HashMap<String, Integer>();
		this.callCountFunctions = new HashMap<String, TimeFunction<Integer>>();

		this.lastEvent = null;
		this.events = new ArrayList<Event>();
	}

	public void startComputation(boolean firstTime) {
		if (firstTime) {
			maxCallCountTotal = 0;
			callCount = new HashMap<String, Integer>();
			callCountFunctions = new HashMap<String, TimeFunction<Integer>>();
		} else {
			for (String method : callCount.keySet()) {
				callCount.put(method, 0);
				callCountFunctions.put(method, new TimeFunction<Integer>(0));
			}
		}

		callCountTotal = 0;

		lastEvent = null;
		events = new ArrayList<Event>();
	}

	/**
	 * Completes the building of the graph. Should be called after all events
	 * in the trace have been replayed.
	 */
	public void finishComputation(boolean firstTime) {
		callCountTotal = 0;
		callCount = new HashMap<String, Integer>();
		lastEvent = null;

		if (firstTime) {
			maxCallCountTotal = 0;
			for (String method : callCountFunctions.keySet()) {
				TimeFunction<Integer> timeFunction = callCountFunctions.get(method);
				Integer value = timeFunction.get(timeFunction.lastTime());
				maxCallCountTotal += value;
			}
		}
	}

	/**
	 * Registers the call of method methodName on the time specified by
	 * timeStamp.
	 */
	public void registerCall(String methodName, long timeStamp) {
		Integer value = callCount.get(methodName);
		if (value == null) {
			value = 1;
		} else {
			value += 1;
		}
		callCount.put(methodName, value);

		TimeFunction<Integer> timeFunction = callCountFunctions.get(methodName);
		if (timeFunction == null) {
			timeFunction = new TimeFunction<Integer>(0);
			callCountFunctions.put(methodName, timeFunction);
		}

		timeFunction.put(timeStamp, value);
	}

	/**
	 * Registers an event with this edge.
	 * @param event the event to register.
	 */
	public void registerEvent(Event event) {
		events.add(event);
	}

	/**
	 * Updates the metrics to only include the events between the metric start
	 * time and the current time.
	 * @param metricStartTime the metrics start time.
	 * @param currentTime the current time.
	 */
	public void updateMetrics(long metricStartTime, final long currentTime) {
		callCountTotal = 0;
		callCount.clear();
		for (String method : callCountFunctions.keySet()) {
			TimeFunction<Integer> timeFunction = callCountFunctions.get(method);
			Integer value = timeFunction.get(currentTime) -
			                timeFunction.get(metricStartTime);
			callCountTotal += value;
			callCount.put(method, value);
		}

		lastEvent = null;
//		if (events.size() > 0 && currentTime < events.get(0).getTime()) {
//			lastEvent = null;
//		} else {
//			int index = BinarySearch.search(events, new Condition() {
//				public boolean isTrue(Object o) {
//					return ((Event)o).getTime() <= currentTime;
//				}
//			});
//			lastEvent = events.get(index);
//		}
	}

	/**
	 * Returns the associated edge.
	 */
	public Edge getEdge() {
		return edge;
	}

	public Event getLastEvent() {
		return lastEvent;
	}

	/**
	 * Returns the list of events
	 */
	public List<Event> getEvents(final Range range) {
		int lowIndex = 1 + BinarySearch.search(events, new Condition() {
			@Override
			public boolean isTrue(Object o) {
				return ((Event)o).getTime() < range.getBegin();
			}
		});

		int highIndex = 1 + BinarySearch.search(events, new Condition() {
			@Override
			public boolean isTrue(Object o) {
				return ((Event)o).getTime() <= range.getEnd();
			}
		});

		return events.subList(lowIndex, highIndex);
	}

	/**
	 * Returns the number of calls (counted between the metric start time and
	 * the current time) associated with this edge.
	 */
	public int getCallCount() {
		return callCountTotal;
	}

	/**
	 * Returns the maximum number (counted over the whole of the execution) of
	 * calls associated with this edge.
	 */
	public int getMaxCallCount() {
		return maxCallCountTotal;
	}

	/**
	 * Returns the set of methods associated with this edge.
	 */
	public Set<String> getMethods() {
		return callCountFunctions.keySet();
	}

	/**
	 * Returns the number of calls (counted between the metric start time and
	 * the current time) of a method from the source of this edge to the
	 * destination.
	 * @param method the name of the method.
	 */
	public int getMethodCallCount(String method) {
		Integer count = callCount.get(method);
		if (count != null) {
			return count;
		} else {
			return 0;
		}
	}

}
