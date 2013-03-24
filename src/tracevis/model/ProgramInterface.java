package tracevis.model;

import java.util.Set;

import tracevis.model.types.CallStack;
import tracevis.model.types.ClassData;
import tracevis.model.types.Event;
import edu.uci.ics.jung.graph.Graph;

public interface ProgramInterface {

	public abstract Graph getCallGraph();

	/*
	 * Accessors.
	 */
	public abstract CallStack getCallStack(long threadID);

	public abstract Set<Long> getActiveThreads();

	public abstract ClassData getCaller(Event event);

	public abstract ClassData getCallee(Event event);

	public abstract ClassData getClassData(String className);

	public abstract boolean getHideInnerClasses();

}