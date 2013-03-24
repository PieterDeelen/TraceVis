/*
 * Program.java
 *
 * Author: Pieter Deelen
 * Created: June 2, 2005, 11:54 AM
 *
 */

package tracevis.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import tracevis.model.types.CallAssignment;
import tracevis.model.types.CallData;
import tracevis.model.types.CallStack;
import tracevis.model.types.ClassData;
import tracevis.model.types.ClassLoadEvent;
import tracevis.model.types.Event;
import tracevis.model.types.FramePopEvent;
import tracevis.model.types.MethodEntryEvent;
import tracevis.model.types.MethodExitEvent;
import tracevis.model.types.ObjectAllocationEvent;
import tracevis.model.types.ObjectFreeEvent;
import tracevis.model.types.ProgramListener;
import tracevis.model.types.ThreadStartEvent;
import tracevis.model.types.ThreadStopEvent;
import tracevis.model.types.VMDeathEvent;
import tracevis.model.types.VMInitEvent;
import tracevis.utilities.StreamUtilities;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;


/**
 * Program forms the main interface of the model. It maintains the
 * call-relation graph and the list of execution events.
 * @author Pieter Deelen
 */
public class Program implements ProgramInterface, ProgramEventInterface {
	private final List<ProgramListener> listeners;

	private final Graph callGraph;

	private long startTime;
	private long endTime;
	private long currentTime;
	private long metricStartTime;

	private ArrayList<Event> events;
	private int eventIndex;

	private ConstantPool constants;

	private Map<Long, CallStack> callStacks;
	private Set<Long> activeThreads;

	private Map<String, Vertex> vertexMap;

	private Map<Event, ClassData> callerMap;
	private Map<Event, ClassData> calleeMap;

	private final ForwardStateUpdater forwardStateUpdater;
	private final ReverseStateUpdater reverseStateUpdater;

	private ClassMethodFilter classMethodFilter;

	private CallAssignment callAssignment;
	private boolean hideInnerClasses;

	/**
	 * Creates a new instance of Program.
	 */
	public Program() {
		listeners = new LinkedList<ProgramListener>();

		callGraph = new DirectedSparseGraph();

		forwardStateUpdater = new ForwardStateUpdater(this);
		reverseStateUpdater = new ReverseStateUpdater(this);
	}

	public void loadTrace(String fileName, boolean hideInnnerClasses,
	                      CallAssignment callAssignment) throws Exception
	{
		// Clear the call graph. Just creating a new graph is not possible,
		// because other classes keep a reference to the call graph.
		callGraph.removeAllVertices();
		callGraph.removeUserDatum("tracevis.model.Program");

		events = new ArrayList<Event>();
		eventIndex = 0;

		startTime = 0;
		endTime = 0;
		currentTime = 0;
		metricStartTime = 0;

		constants = new ConstantPool();

		this.callAssignment = callAssignment;

		ZipFile zipFile = new ZipFile(fileName);
		ZipEntry zipEntry = zipFile.getEntry("trace");
		InputStream trace = zipFile.getInputStream(zipEntry);

		// Determine the number of events in the trace.
		int eventCount = StreamUtilities.lineCount(trace);
		events.ensureCapacity(eventCount);
		System.out.println(eventCount);

		trace = zipFile.getInputStream(zipEntry);
		TraceReader reader = new TraceReader(this);
		reader.readTrace(trace);
		zipFile.close();

		GraphBuilder graphBuilder = new GraphBuilder(callGraph, hideInnnerClasses, callAssignment);
		graphBuilder.buildGraph(events, startTime, endTime);
		vertexMap = graphBuilder.getVertexMap();
		callerMap = graphBuilder.getCallerMap();
		calleeMap = graphBuilder.getCalleeMap();

		System.out.println(callGraph.getVertices().size());

		callStacks = new HashMap<Long, CallStack>();
		activeThreads = new HashSet<Long>();

		MetricComputer metricComputer = new MetricComputer(this);
		metricComputer.computeMetrics(events, true, startTime, endTime);

		classMethodFilter = new ClassMethodFilter(callAssignment);

		currentTime = startTime;
		metricStartTime = startTime;

		fireTraceLoaded();
	}

	private void applyFilter(EventFilter eventFilter) {
		eventFilter.filter(events);

		MetricComputer metricComputer = new MetricComputer(this);
		metricComputer.computeMetrics(events, false, startTime, endTime);

		// Reset state.
		activeThreads = new HashSet<Long>();

		for (Long threadID : callStacks.keySet()) {
			callStacks.put(threadID, new CallStack());
		}

		// Replay all events upto current time to restore state.
		eventIndex = 0;
		while (eventIndex < events.size() - 1) {
			Event event = events.get(eventIndex + 1);
			if (event.getTime() <= currentTime) {
				if (!event.isFiltered()) {
					forwardStateUpdater.visit(event);
				}
				eventIndex++;
			} else {
				break;
			}
		}

		updateMetrics();
		fireTraceFiltered();
	}


	public void addClassFilter(String name) {
		classMethodFilter.addClassFilter(name);
	}


	public void removeClassFilter(String name) {
		classMethodFilter.removeClassFilter(name);
	}

	public boolean isFilteredClass(String name) {
		return classMethodFilter.isFilteredClass(name);
	}

	public void initFilter() {
		applyFilter(new ConstructorFilter());
	}

	public void noInitFilter() {
		classMethodFilter = new ClassMethodFilter(callAssignment);
		Iterator<?> vertexIt = callGraph.getVertices().iterator();
		while (vertexIt.hasNext()) {
			ClassData classData = (ClassData)((Vertex)vertexIt.next()).getUserDatum("tracevis.model.Program");
			classMethodFilter.addMethodFilter(classData.getName(), "<init>");
		}
		applyFilter(classMethodFilter);
	}

	public void filter() {
		applyFilter(classMethodFilter);
	}

	public void unfilter() {
		classMethodFilter = new ClassMethodFilter(callAssignment);
		applyFilter(classMethodFilter);
	}

	/* (non-Javadoc)
	 * @see tracevis.model.ProgramInterface#getCallGraph()
	 */
	@Override
	public Graph getCallGraph() {
		return callGraph;
	}

	/*
	 * Methods related to the user data records of the call graph.
	 */

	@Override
	public ClassData getClassData(String className) {
		Vertex classVertex = vertexMap.get(className);
		return (ClassData)classVertex.getUserDatum("tracevis.model.Program");
	}

	/*
	 * Methods related to program event listeners.
	 */
	public void addListener(ProgramListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(ProgramListener listener) {
		listeners.remove(listener);
	}

	private void fireTimeChanged() {
		for (ProgramListener listener : listeners) {
			listener.timeChanged();
		}
	}

	private void fireTraceLoaded() {
		for (ProgramListener listener : listeners) {
			listener.traceLoaded();
		}
	}

	private void fireTraceFiltered() {
		for (ProgramListener listener : listeners) {
			listener.traceFiltered();
		}
	}

	private void fireMethodEntered(ClassData caller, ClassData callee) {
		for (ProgramListener listener : listeners) {
			listener.methodEntered(caller, callee);
		}
	}

	private void fireMethodExited(ClassData caller, ClassData callee) {
		for (ProgramListener listener : listeners) {
			listener.methodExited(caller, callee);
		}
	}

	/*
	 * Methods related to time.
	 */
	public long getCurrentTime() {
		return currentTime;
	}

	/**
	 * Updates the metrics for all vertices and edges.
	 */
	private void updateMetrics() {
		for (Vertex vertex : (Set<Vertex>)callGraph.getVertices()) {
			ClassData classData = (ClassData)vertex.getUserDatum("tracevis.model.Program");
			classData.updateMetrics(metricStartTime, currentTime);
		}

		for (Edge edge : (Set<Edge>)callGraph.getEdges()) {
			CallData callData = (CallData)edge.getUserDatum("tracevis.model.Program");
			callData.updateMetrics(metricStartTime, currentTime);
		}
	}

	public void setCurrentTime(long newCurrentTime) {
		if (currentTime < newCurrentTime) {
			while (eventIndex < events.size() - 1) {
				Event event = events.get(eventIndex + 1);
				if (event.getTime() <= newCurrentTime) {
					if (!event.isFiltered()) {
						forwardStateUpdater.visit(event);
					}
					eventIndex++;
				} else {
					break;
				}
			}
		} else {
			while (eventIndex > 0) {
				Event event = events.get(eventIndex);
				if (event.getTime() >= newCurrentTime) {
					if (!event.isFiltered()) {
						reverseStateUpdater.visit(event);
					}
					eventIndex--;
				} else {
					break;
				}
			}
		}

		currentTime = newCurrentTime;
		updateMetrics();
		fireTimeChanged();
	}

	public void jumpToNextEvent() {
		// Search for the next unfiltered event.
		while (eventIndex < events.size() - 1) {
			eventIndex++;
			Event event = events.get(eventIndex);
			if (!event.isFiltered()) {
				break;
			}
		}

		// Execute the next unfiltered event.
		if (eventIndex < events.size()) {
			Event event = events.get(eventIndex);

			forwardStateUpdater.visit(event);

			currentTime = event.getTime();
			updateMetrics();
			fireTimeChanged();

			if (event instanceof MethodEntryEvent) {
				ClassData caller = getCaller(event);
				ClassData callee = getCallee(event);
				fireMethodEntered(caller, callee);
			} else if (event instanceof MethodExitEvent) {
				ClassData caller = getCaller(event);
				ClassData callee = getCallee(event);
				fireMethodExited(caller, callee);
			} else if (event instanceof FramePopEvent) {
				ClassData caller = getCaller(event);
				ClassData callee = getCallee(event);
				fireMethodExited(caller, callee);
			}
		}
	}

	public void jumpToPreviousEvent() {
		// Search for the last unfiltered event.
		while (eventIndex > 0) {
			Event event = events.get(eventIndex);
			if (!event.isFiltered()) {
				break;
			}
			eventIndex--;
		}

		// If the current time does not coincide with the time of the last event
		// jump to the last event. Otherwise, jump undo the last event, and jump
		// to the before last unfiltered event.
		Event event = events.get(eventIndex);
		if (event.getTime() < currentTime) {
			currentTime = event.getTime();
			fireTimeChanged();
		} else if (eventIndex > 0) {
			reverseStateUpdater.visit(event);
			eventIndex--;

			while (eventIndex > 0) {
				event = events.get(eventIndex);
				if (!event.isFiltered()) {
					break;
				}
				eventIndex--;
			}

			currentTime = (event).getTime();
			updateMetrics();
			fireTimeChanged();

			if (event instanceof MethodEntryEvent) {
				ClassData caller = getCaller(event);
				ClassData callee = getCallee(event);
				fireMethodExited(caller, callee);
			} else if (event instanceof MethodExitEvent) {
				ClassData caller = getCaller(event);
				ClassData callee = getCallee(event);
				fireMethodEntered(caller, callee);
			} else if (event instanceof FramePopEvent) {
				ClassData caller = getCaller(event);
				ClassData callee = getCallee(event);
				fireMethodEntered(caller, callee);
			}
		}
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public long getMetricStartTime() {
		return metricStartTime;
	}

	public void setMetricStartTime(long metricStartTime) {
		this.metricStartTime = metricStartTime;
		updateMetrics();
		fireTimeChanged();
	}

	/*
	 * Accessors.
	 */
	/* (non-Javadoc)
	 * @see tracevis.model.ProgramInterface#getCallStack(long)
	 */
	@Override
	public CallStack getCallStack(long threadID) {
		CallStack callStack = callStacks.get(threadID);
		if (callStack == null) {
			callStack = new CallStack();
			callStacks.put(threadID, callStack);
		}
		return callStack;
	}

	/* (non-Javadoc)
	 * @see tracevis.model.ProgramInterface#getActiveThreads()
	 */
	@Override
	public Set<Long> getActiveThreads() {
		return activeThreads;
	}

	public CallAssignment getCallAssignment() {
		return callAssignment;
	}

	@Override
	public boolean getHideInnerClasses() {
		return hideInnerClasses;
	}

	/* (non-Javadoc)
	 * @see tracevis.model.ProgramInterface#getCaller(tracevis.model.types.Event)
	 */
	@Override
	public ClassData getCaller(Event event) {
		return callerMap.get(event);
	}

	/* (non-Javadoc)
	 * @see tracevis.model.ProgramInterface#getCallee(tracevis.model.types.Event)
	 */
	@Override
	public ClassData getCallee(Event event) {
		return calleeMap.get(event);
	}

	/*
	 * Event handling methods. Call-backs for TraceReader.
	 */

	@Override
	public void handleMethodEntry(long timeStamp, long threadID, String className,
	                       String methodName, long objectID)
	{
		String classNameConstant = constants.getConstant(className);
		String methodNameConstant = constants.getConstant(methodName);

		Event event = new MethodEntryEvent(timeStamp,
		                                   threadID, classNameConstant,
		                                   methodNameConstant, objectID);
		events.add(event);

	}

	@Override
	public void handleMethodExit(long timeStamp, long threadID, String className,
	                      String methodName)
	{
		String classNameConstant = constants.getConstant(className);
		String methodNameConstant = constants.getConstant(methodName);

		Event event = new MethodExitEvent(timeStamp,
		                                  threadID, classNameConstant,
		                                  methodNameConstant);
		events.add(event);
	}

	@Override
	public void handleFramePop(long timeStamp, long threadID, String className, String methodName) {
			String classNameConstant = constants.getConstant(className);
			String methodNameConstant = constants.getConstant(methodName);

			Event event = new FramePopEvent(timeStamp, threadID,
			                                classNameConstant,
			                                methodNameConstant);
			events.add(event);
	}

	@Override
	public void handleThreadStart(long timeStamp, long threadID) {
		Event event = new ThreadStartEvent(timeStamp, threadID);
		events.add(event);
	}

	@Override
	public void handleThreadStop(long timeStamp, long threadID) {
		Event event = new ThreadStopEvent(timeStamp, threadID);
		events.add(event);
	}

	@Override
	public void handleClassLoad(long timeStamp, String className) {
		String classNameConstant = constants.getConstant(className);

		Event event = new ClassLoadEvent(timeStamp,
		                                 classNameConstant);
		events.add(event);
	}

	@Override
	public void handleObjectAllocation(long timeStamp, String className, long objectID) {
		String classNameConstant = constants.getConstant(className);

		Event event = new ObjectAllocationEvent(timeStamp,
		                                        classNameConstant, objectID);
		events.add(event);
	}

	@Override
	public void handleObjectFree(long timeStamp, String className, long objectID) {
		String classNameConstant = constants.getConstant(className);

		Event event = new ObjectFreeEvent(timeStamp,
		                                  classNameConstant, objectID);
		events.add(event);
	}

	@Override
	public void handleVMStart(long timeStamp) {
//			Event event = new VMStartEvent(sharedData, timeStamp);
//			events.add(event);
	}

	@Override
	public void handleVMInit(long timeStamp) {
		startTime = timeStamp;
		currentTime = timeStamp;
		endTime = timeStamp;

		Event event = new VMInitEvent(timeStamp);
		events.add(event);
	}

	@Override
	public void handleVMDeath(long timeStamp) {
		currentTime = timeStamp;
		endTime = timeStamp;

		Event event = new VMDeathEvent(timeStamp);
		events.add(event);
	}
}