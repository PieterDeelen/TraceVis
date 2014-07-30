/*
 * ClassData.java
 *
 * Author: Pieter Deelen
 * Created: June 9, 2005, 3:14 PM
 *
 */

package tracevis.model.types;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import tracevis.utilities.BinarySearch;
import tracevis.utilities.Condition;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;

/**
 * A JUNG user data record to be stored with a vertex, which stores information
 * related to the class this vertex represents.
 * @author Pieter Deelen
 */
public class ClassData {
	private final Vertex vertex;

	private final String name;

	private int callsSent;
	private int maxCallsSent;
	private TimeFunction<Integer> callsSentFunction;

	private int callsReceived;
	private int maxCallsReceived;
	private TimeFunction<Integer> callsReceivedFunction;

	private int instanceCount;
	private int maxInstanceCount;
	private TimeFunction<Integer> instanceCountFunction;
	private List<Long> instances;

	private int loadCount;

	private int activityCount;
	private List<Range> activityRanges;
	private long beginCurrentActivity;

	private Event lastEvent;
	private List<Event> events;

	/**
	 * Creates a new instance of ClassData.
	 * @param vertex the vertex this ClassData is associated with.
	 * @param name the name of the class.
	 */
	public ClassData(Vertex vertex, String name) {
		this.vertex = vertex;

		this.name = name;

		this.callsSent = 0;
		this.callsSentFunction = new TimeFunction<Integer>(0);

		this.callsReceived = 0;
		this.callsReceivedFunction = new TimeFunction<Integer>(0);

		this.instanceCount = 0;
		this.maxInstanceCount = 0;
		this.instanceCountFunction = new TimeFunction<Integer>(0);
		this.instances = new LinkedList<Long>();

		this.loadCount = 0;

		this.activityCount = 0;
		this.beginCurrentActivity = -1;
		this.activityRanges = new ArrayList<Range>();

		this.lastEvent = null;
		this.events = new ArrayList<Event>();
	}

	public void startComputation(boolean firstTime) {
		if (firstTime) {
			maxCallsSent = 0;
			maxCallsReceived = 0;
			maxInstanceCount = 0;
		}

		callsSent = 0;
		callsSentFunction = new TimeFunction<Integer>(0);

		callsReceived = 0;
		callsReceivedFunction = new TimeFunction<Integer>(0);

		instanceCount = 0;
		instanceCountFunction = new TimeFunction<Integer>(0);
		instances = new LinkedList<Long>();

		activityCount = 0;
		beginCurrentActivity = -1;
		activityRanges = new ArrayList<Range>();

		loadCount = 0;

		lastEvent = null;
		events = new ArrayList<Event>();
	}

	/**
	 * Completes the building of the graph. Should be called after all events
	 * in the trace have been replayed.
	 * @param timeStamp the time of the end of execution.
	 */
	public void finishComputation(long timeStamp, boolean firstTime) {
		if (firstTime) {
			maxCallsSent = callsSent;
			maxCallsReceived = callsReceived;
		}

		if (beginCurrentActivity != -1) {
			activityRanges.add(new Range(beginCurrentActivity, timeStamp));
		}

		callsSent = 0;
		callsReceived = 0;
		instanceCount = 0;
		loadCount = 0;
	}

	public void increaseCallsSent(long timeStamp) {
		callsSent += 1;
		callsSentFunction.put(timeStamp, callsSent);
	}

	public void increaseCallsReceived(long timeStamp) {
		callsReceived += 1;
		callsReceivedFunction.put(timeStamp, callsReceived);
	}

	public void increaseInstanceCount(long timeStamp) {
		instanceCount += 1;
		maxInstanceCount = Math.max(maxInstanceCount, instanceCount);
		instanceCountFunction.put(timeStamp, instanceCount);
	}

	public void decreaseInstanceCount(long timeStamp) {
		instanceCount -= 1;
		instanceCountFunction.put(timeStamp, instanceCount);
	}

	public void increaseActivityCount(long timeStamp) {
		if (activityCount == 0) {
			beginCurrentActivity = timeStamp;
		}
		activityCount += 1;
	}

	public void decreaseActivityCount(long timeStamp) {
		activityCount -= 1;
		if (activityCount == 0) {
			activityRanges.add(new Range(beginCurrentActivity, timeStamp));
			beginCurrentActivity = -1;
		}
	}

	public void increaseLoadCount() {
		loadCount++;
	}

	public void decreaseLoadCount() {
		loadCount--;
	}

	/**
	 * Registers an event with this vertex.
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
		callsSent = callsSentFunction.get(currentTime) -
		            callsSentFunction.get(metricStartTime);
		callsReceived = callsReceivedFunction.get(currentTime) -
		                callsReceivedFunction.get(metricStartTime);
		instanceCount = instanceCountFunction.get(currentTime);

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

	public void registerInstance(long objectID) {
		instances.add(objectID);
	}

	public void unregisterInstance(long objectID) {
		instances.remove(objectID);
	}

	public List<Long> getInstances() {
		return instances;
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
	 * Returns whether this class is loaded at the current time.
	 */
	public boolean isLoaded() {
		return loadCount > 0;
	}

	/**
	 * Returns the vertex associated with this ClassData.
	 */
	public Vertex getVertex() {
		return vertex;
	}

	/**
	 * Returns the canonical name of the class.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the short (or simple) name of this class.
	 */
	public String getShortName() {
		int index = name.lastIndexOf(".");
		String shortName = name.substring(index+1);
		return shortName;
	}

	/**
	 * Returns the name of the enclosing package.
	 */
	public String getPackageName() {
		int index = name.lastIndexOf(".");
		String packageName = name.substring(0, index);
		return packageName;
	}

	/** Returns a list containing the identifiers the name of the enclosing
	 * package is composed of. For instance, for the class "java.util.Map" this
	 * method would return the list ["java", "util"].
	 */
	public List<String> getPackageIdentifiers() {
		String[] splitIdentifiers = name.split("\\.");
		List<String> packageIdentifiers = new ArrayList<String>();
		for (int i = 0; i < splitIdentifiers.length - 1; i++) {
			packageIdentifiers.add(splitIdentifiers[i]);
		}

		return packageIdentifiers;
	}

	/**
	 * Returns the number of calls (counted between the metric start time and
	 * the current time) this class has sent to other classes.
	 */
	public int getCallsSent() {
		return callsSent;
	}

	/**
	 * Returns the maximum number (counted over the whole of the execution) of
	 * calls this class has sent to other classes.
	 */
	public int getMaxCallsSent() {
		return maxCallsSent;
	}

	/**
	 * Returns the number of calls (counted between the metric start time and
	 * the current time) this class has received from other classes.
	 */
	public int getCallsReceived() {
		return callsReceived;
	}

	/**
	 * Returns the maximum number (counted over the whole of the execution) of
	 * calls this class has received from other classes.
	 */
	public int getMaxCallsReceived() {
		return maxCallsReceived;
	}

	/**
	 * Returns the current number of instances of this class.
	 */
	public int getInstanceCount() {
		return instanceCount;
	}

	/**
	 * Returns the maximum number (counted over the whole of the execution) of
	 * instances of this class.
	 */
	public int getMaxInstanceCount() {
		return maxInstanceCount;
	}

	/**
	 * Returns the list of activity ranges. An activity range specifies a part
	 * of the execution where this class was "active": on top of the stack.
	 */
	public List<Range> getActivityRanges() {
		return activityRanges;
	}

	/**
	 * Returns the list of activity ranges which fall in a selected range.
	 * @param selectedRange the selected range.
	 * @see #getActivityRanges()
	 */
	public List<Range> getActivityRanges(final Range selectedRange) {
		int lowIndex = 1 + BinarySearch.search(activityRanges, new Condition() {
			@Override
			public boolean isTrue(Object o) {
				return ((Range)o).getEnd() <= selectedRange.getBegin();
			}
		});

		int highIndex = BinarySearch.search(activityRanges, new Condition() {
			@Override
			public boolean isTrue(Object o) {
				return ((Range)o).getBegin() <= selectedRange.getEnd();
			}
		});

		return activityRanges.subList(lowIndex, highIndex + 1);
	}

	public TimeFunction<Integer> getInstanceCountFunction() {
		return instanceCountFunction;
	}

	public TimeFunction<Integer> getCallsSentFunction() {
		return callsSentFunction;
	}

	public TimeFunction<Integer> getCallsReceivedFunction() {
		return callsReceivedFunction;
	}

	/**
	 * Returns the CallData record which represents the calls from this class
	 * to another class.
	 * @param calleeData the other class.
	 */
	public CallData getCallData(ClassData calleeData) {
		Vertex callerVertex = vertex;
		Vertex calleeVertex = calleeData.getVertex();
		Edge edge = callerVertex.findEdge(calleeVertex);
		if (edge == null) {
			return null;
		} else {
			return (CallData)edge.getUserDatum("tracevis.model.Program");
		}
	}
}
