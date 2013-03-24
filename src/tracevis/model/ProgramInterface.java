package tracevis.model;

import java.util.Set;

import tracevis.model.types.CallStack;
import tracevis.model.types.ClassData;
import tracevis.model.types.Event;
import edu.uci.ics.jung.graph.Graph;

public interface ProgramInterface {

	Graph getCallGraph();

	/*
	 * Accessors.
	 */
	CallStack getCallStack(long threadID);

	Set<Long> getActiveThreads();

	ClassData getCaller(Event event);

	ClassData getCallee(Event event);

	ClassData getClassData(String className);

	boolean getHideInnerClasses();

}