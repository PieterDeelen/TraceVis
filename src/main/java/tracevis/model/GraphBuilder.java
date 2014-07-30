/*
 * GraphBuilder.java
 *
 * Author: Pieter Deelen
 * Created: April 21, 2006, 12:14 PM
 *
 */

package tracevis.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tracevis.model.types.CallAssignment;
import tracevis.model.types.CallData;
import tracevis.model.types.CallStack;
import tracevis.model.types.ClassData;
import tracevis.model.types.ClassLoadEvent;
import tracevis.model.types.Event;
import tracevis.model.types.Frame;
import tracevis.model.types.FramePopEvent;
import tracevis.model.types.MethodEntryEvent;
import tracevis.model.types.MethodExitEvent;
import tracevis.model.types.ObjectAllocationEvent;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseVertex;
import edu.uci.ics.jung.utils.GraphUtils;
import edu.uci.ics.jung.utils.UserData;

/**
 *
 * @author Pieter Deelen
 */
public class GraphBuilder extends EventVisitor {
	private final Graph callGraph;
	private final boolean hideInnerClasses;
	private final CallAssignment callAssignment;

	private final Map<String, Vertex> vertexMap;
	private final Map<Long, ClassData> objectMap;
	private final Map<Long, CallStack> callStacks;

	private final Map<Event, ClassData> callerMap;
	private final Map<Event, ClassData> calleeMap;

	/**
	 * Creates a new instance of GraphBuilder.
	 */
	public GraphBuilder(Graph callGraph, boolean hideInnerClasses,
	                    CallAssignment callAssignment)
	{
		this.callGraph = callGraph;
		this.hideInnerClasses = hideInnerClasses;
		this.callAssignment = callAssignment;

		this.vertexMap = new HashMap<String, Vertex>();
		this.objectMap = new HashMap<Long, ClassData>();
		this.callStacks = new HashMap<Long, CallStack>();

		this.callerMap = new HashMap<Event, ClassData>();
		this.calleeMap = new HashMap<Event, ClassData>();
	}

	public void buildGraph(List<Event> events, long beginTime, long endTime) {
		for (Event event: events) {
			visit(event);
		}

		Set<Vertex> unconnectedVertices = new HashSet<Vertex>();
		for (Vertex vertex : (Set<Vertex>)callGraph.getVertices()) {

			if (vertex.degree() == 0) {
				unconnectedVertices.add(vertex);
			}
		}

		GraphUtils.removeVertices(callGraph, unconnectedVertices);
	}

	@Override
	public void visit(ClassLoadEvent event) {
		String className = event.getClassName();

		if (ProgramUtilities.isInnerClass(className) && hideInnerClasses) {
			String enclosingClassName = ProgramUtilities.getEnclosingClassName(className);
			Vertex classVertex = vertexMap.get(enclosingClassName);
			if (classVertex == null) {
				// Apparently inner classes can be loaded before their enclosing
				// class.
				classVertex = callGraph.addVertex(new DirectedSparseVertex());
				ClassData classData = new ClassData(classVertex, enclosingClassName);

				classVertex.addUserDatum("tracevis.model.Program", classData, UserData.SHARED);
				vertexMap.put(enclosingClassName, classVertex);
			}

			vertexMap.put(className, classVertex);
		} else {
			Vertex classVertex = vertexMap.get(className);
			if (classVertex == null) {
				classVertex = callGraph.addVertex(new DirectedSparseVertex());
				ClassData classData = new ClassData(classVertex, className);

				classVertex.addUserDatum("tracevis.model.Program", classData, UserData.SHARED);
			}

			vertexMap.put(className, classVertex);
		}
	}

	@Override
	public void visit(FramePopEvent event) {
		ClassData calleeData = null;
		ClassData callerData = null;
		long threadID = event.getThreadID();

		CallStack callStack = getCallStack(threadID);
		Frame frame = callStack.top();
		switch (callAssignment) {
			case DEFINING_CLASS:
				calleeData = frame.getDefiningClass();
				break;
			case OBJECT_CLASS:
				calleeData = frame.getActualClass();
				break;
		}
		event.setFrame(frame);

		callStack.pop();

		if (callStack.depth() > 0) {
			Frame callerFrame = callStack.top();
			switch (callAssignment) {
				case DEFINING_CLASS:
					callerData = callerFrame.getDefiningClass();
					break;
				case OBJECT_CLASS:
					callerData = callerFrame.getActualClass();
					break;
			}


			callerMap.put(event, callerData);
		}
		calleeMap.put(event, calleeData);
	}

	@Override
	public void visit(MethodEntryEvent event) {
		ClassData callerData  = null;
		ClassData calleeData = null;

		String className = event.getClassName();
		String methodName = event.getMethodName();
		long threadID = event.getThreadID();
		long objectID = event.getObjectID();

		CallStack callStack = getCallStack(threadID);

		// Determine defining class for call.
		Vertex definingCalleeVertex = vertexMap.get(className);
		ClassData definingCalleeData = (ClassData)definingCalleeVertex.getUserDatum("tracevis.model.Program");

		// Determine actual (object) class for call.
		ClassData actualCalleeData = objectMap.get(objectID);
		Vertex actualCalleeVertex;
		if (actualCalleeData != null) {
			actualCalleeVertex = actualCalleeData.getVertex();
		} else {
			// Assume this method is static.
			actualCalleeVertex = definingCalleeVertex;
			actualCalleeData = definingCalleeData;
		}

		// Determine callee.
		Vertex calleeVertex = null;
		switch (callAssignment) {
			case DEFINING_CLASS:
				calleeData = definingCalleeData;
				calleeVertex = definingCalleeVertex;
				break;
			case OBJECT_CLASS:
				calleeData = actualCalleeData;
				calleeVertex = actualCalleeVertex;
				break;
		}


		calleeMap.put(event, calleeData);

		if (callStack.depth() > 0) {
			// Determine caller.
			Frame callerFrame = callStack.top();
			switch (callAssignment) {
				case DEFINING_CLASS:
					callerData = callerFrame.getDefiningClass();
					break;
				case OBJECT_CLASS:
					callerData = callerFrame.getActualClass();
					break;
			}

			// Check if there's an edge between caller and callee.
			Vertex callerVertex = callerData.getVertex();
			Edge edge = callerVertex.findEdge(calleeVertex);
			if (edge == null) {
				edge = callGraph.addEdge(new DirectedSparseEdge(callerVertex, calleeVertex));
				CallData callData = new CallData(edge);
				edge.addUserDatum("tracevis.model.Program", callData, UserData.SHARED);
			}

			callerMap.put(event, callerData);
		}

		Frame frame = new Frame(definingCalleeData, actualCalleeData, objectID, methodName);
		callStack.push(frame);

		event.setFrame(frame);
	}

	@Override
	public void visit(MethodExitEvent event) {
		ClassData calleeData = null;
		ClassData callerData = null;
		long threadID = event.getThreadID();

		CallStack callStack = getCallStack(threadID);
		Frame frame = callStack.top();
		switch (callAssignment) {
			case DEFINING_CLASS:
				calleeData = frame.getDefiningClass();
				break;
			case OBJECT_CLASS:
				calleeData = frame.getActualClass();
				break;
		}
		event.setFrame(frame);

		callStack.pop();

		if (callStack.depth() > 0) {
			Frame callerFrame = callStack.top();
			switch (callAssignment) {
				case DEFINING_CLASS:
					callerData = callerFrame.getDefiningClass();
					break;
				case OBJECT_CLASS:
					callerData = callerFrame.getActualClass();
					break;
			}

			callerMap.put(event, callerData);
		}

		calleeMap.put(event, calleeData);
	}

	@Override
	public void visit(ObjectAllocationEvent event) {
		Vertex classVertex = vertexMap.get(event.getClassName());
		ClassData classData = (ClassData)classVertex.getUserDatum("tracevis.model.Program");
		long objectID = event.getObjectID();
		objectMap.put(objectID, classData);
	}

	private CallStack getCallStack(long threadID) {
		CallStack callStack = callStacks.get(threadID);
		if (callStack == null) {
			callStack = new CallStack();
			callStacks.put(threadID, callStack);
		}
		return callStack;
	}

	public Map<String, Vertex> getVertexMap() {
		return vertexMap;
	}

	public Map<Long, ClassData> getObjectMap() {
		return objectMap;
	}

	public Map<Event, ClassData> getCallerMap() {
		return callerMap;
	}

	public Map<Event, ClassData> getCalleeMap() {
		return calleeMap;
	}
}
