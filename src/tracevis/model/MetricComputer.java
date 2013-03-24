/*
 * MetricUpdater.java
 *
 * Author: Pieter Deelen
 * Created: April 25, 2006, 1:43 PM
 *
 */

package tracevis.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tracevis.model.types.CallData;
import tracevis.model.types.ClassData;
import tracevis.model.types.Event;
import tracevis.model.types.FramePopEvent;
import tracevis.model.types.MethodEntryEvent;
import tracevis.model.types.MethodExitEvent;
import tracevis.model.types.ObjectAllocationEvent;
import tracevis.model.types.ObjectFreeEvent;

import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.utils.GraphUtils;

/**
 *
 * @author Pieter Deelen
 */
public class MetricComputer extends EventVisitor {
	private final ProgramInterface program;

	/**
	 * Creates a new instance of MetricUpdater.
	 */
	public MetricComputer(ProgramInterface program) {
		this.program = program;
	}

	public void computeMetrics(List<Event> events, boolean firstTime,
	                           long startTime, long endTime)
	{
		Graph callGraph = program.getCallGraph();

		for (Vertex vertex : (Set<Vertex>)callGraph.getVertices()) {
			ClassData classData = (ClassData)vertex.getUserDatum("tracevis.model.Program");
			classData.startComputation(firstTime);
		}

		for (Edge edge : (Set<Edge>)callGraph.getEdges()) {
			CallData callData = (CallData)edge.getUserDatum("tracevis.model.Program");
			callData.startComputation(firstTime);
		}

		for (Event event : events) {
			if (!event.isFiltered()) {
				visit(event);
			}
		}

		Set<Vertex> unconnectedVertices = new HashSet<Vertex>();
		for (Vertex vertex : (Set<Vertex>)callGraph.getVertices()) {
			ClassData classData = (ClassData)vertex.getUserDatum("tracevis.model.Program");
			classData.finishComputation(endTime, firstTime);

			if (vertex.degree() == 0) {
				unconnectedVertices.add(vertex);
			}
		}

		GraphUtils.removeVertices(callGraph, unconnectedVertices);

		for (Edge edge : (Set<Edge>)callGraph.getEdges()) {
			CallData callData = (CallData)edge.getUserDatum("tracevis.model.Program");
			callData.finishComputation(firstTime);
		}
	}

	@Override
	public void visit(FramePopEvent event) {
		ClassData callerData = program.getCaller(event);
		ClassData calleeData = program.getCallee(event);

		if (callerData != null) {
			callerData.increaseActivityCount(event.getTime());
			callerData.registerEvent(event);

			Vertex callerVertex = callerData.getVertex();
			Vertex calleeVertex = calleeData.getVertex();
			Edge edge = callerVertex.findEdge(calleeVertex);
			CallData callData = (CallData)edge.getUserDatum("tracevis.model.Program");
			callData.registerEvent(event);
		}

		calleeData.decreaseActivityCount(event.getTime());
		calleeData.registerEvent(event);
	}

	@Override
	public void visit(MethodEntryEvent event) {
		ClassData callerData = program.getCaller(event);
		ClassData calleeData = program.getCallee(event);

		calleeData.increaseActivityCount(event.getTime());
		calleeData.registerEvent(event);

		if (callerData != null) {
			CallData callData = callerData.getCallData(calleeData);
			String className = event.getClassName();
			String methodName = event.getMethodName();
			if (ProgramUtilities.isInnerClass(className) && program.getHideInnerClasses()) {
				String innerClassName = ProgramUtilities.getInnerClassName(className);
				innerClassName = innerClassName.replace('$', '.');
				callData.registerCall(innerClassName + "." + methodName, event.getTime());
			} else {
				callData.registerCall(methodName, event.getTime());
			}
			callData.registerEvent(event);

			callerData.increaseCallsSent(event.getTime());
			callerData.decreaseActivityCount(event.getTime());
			callerData.registerEvent(event);

			calleeData.increaseCallsReceived(event.getTime());
		}
	}

	@Override
	public void visit(MethodExitEvent event) {
		ClassData callerData = program.getCaller(event);
		ClassData calleeData = program.getCallee(event);

		if (callerData != null) {
			callerData.increaseActivityCount(event.getTime());
			callerData.registerEvent(event);

			Vertex callerVertex = callerData.getVertex();
			Vertex calleeVertex = calleeData.getVertex();
			Edge edge = callerVertex.findEdge(calleeVertex);
			CallData callData = (CallData)edge.getUserDatum("tracevis.model.Program");
			callData.registerEvent(event);
		}

		calleeData.decreaseActivityCount(event.getTime());
		calleeData.registerEvent(event);
	}

	@Override
	public void visit(ObjectAllocationEvent event) {
		String className = event.getClassName();
		ClassData classData = program.getClassData(className);

		classData.increaseInstanceCount(event.getTime());
	}

	@Override
	public void visit(ObjectFreeEvent event) {
		String className = event.getClassName();
		ClassData classData = program.getClassData(className);

		classData.decreaseInstanceCount(event.getTime());
	}
}
