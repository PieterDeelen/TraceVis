/*
 * ClassMethodFilter.java
 *
 * Author: Pieter Deelen
 * Created: April 26, 2006, 7:33 PM
 *
 */

package tracevis.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tracevis.model.types.CallAssignment;
import tracevis.model.types.CallStack;
import tracevis.model.types.ClassData;
import tracevis.model.types.ClassLoadEvent;
import tracevis.model.types.Event;
import tracevis.model.types.Frame;
import tracevis.model.types.FramePopEvent;
import tracevis.model.types.MethodEntryEvent;
import tracevis.model.types.MethodExitEvent;
import tracevis.model.types.ObjectAllocationEvent;
import tracevis.model.types.ObjectFreeEvent;
import tracevis.utilities.Pair;

/**
 * An event filter which filters out classes and methods.
 * @author Pieter Deelen
 */
public class ClassMethodFilter extends EventVisitor implements EventFilter {
	private final CallAssignment callAssignment;

	private final Set<String> filteredClasses;
	private final Set<Pair<String, String>> filteredMethods;
	private Map<Long, CallStack> callStacks;

	/**
	 * Creates a new instance of ClassMethodFilter.
	 * @param callAssignment how to assign calls to classes.
	 */
	public ClassMethodFilter(CallAssignment callAssignment) {
		this.callAssignment = callAssignment;

		filteredClasses = new HashSet<String>();
		filteredMethods = new HashSet<Pair<String, String>>();
	}

	/**
	 * Adds the specified class to the list of classes which will be filtered
	 * out.
	 * @param className the name of the specified class.
	 */
	public void addClassFilter(String className) {
		filteredClasses.add(className);
	}

	/**
	 * Removes the specified class from  the list of classes which will be
	 * filtered out.
	 * @param className the name of the specified class.
	 */
	public void removeClassFilter(String className) {
		filteredClasses.remove(className);
	}

	/**
	 * Returns whether the specified class is filtered out.
	 * @param className the name of the specified class.
	 */
	public boolean isFilteredClass(String className) {
		return filteredClasses.contains(className);
	}

	/**
	 * Adds the specified method to the list of methods which will be filtered
	 * out.
	 * @param className the name of the class.
	 * @param methodName the name of the specified method.
	 */
	public void addMethodFilter(String className, String methodName) {
		filteredMethods.add(new Pair<String, String>(className, methodName));
	}

	/**
	 * Removes the specified method from the list of methods which will be
	 * filtered out.
	 * @param className the name of the specified method's class.
	 * @param methodName the name of the specified method.
	 */
	public void removeClassFilter(String className, String methodName) {
		filteredMethods.remove(new Pair<String, String>(className, methodName));
	}

	/**
	 * Returns whether the specified method is filtered out.
	 * @param className the name of the specified method's class.
	 * @param methodName the name of the specified method.
	 */
	public boolean isFilteredMethod(String className, String methodName) {
		return filteredMethods.contains(new Pair<String, String>(className, methodName));
	}

	@Override
	public void filter(List<Event> events) {
		callStacks = new HashMap<Long, CallStack>();

		for (Event event : events) {
			visit(event);
		}
	}

	@Override
	public void visit(ClassLoadEvent event) {
		if (filteredClasses.contains(event.getClassName())) {
			event.setFiltered(true);
		} else {
			event.setFiltered(false);
		}
	}

	@Override
	public void visit(FramePopEvent event) {
		CallStack callStack = getCallStack(event.getThreadID());
		event.setFiltered(stackContainsFilteredFrame(callStack));
		callStack.pop();
	}

	@Override
	public void visit(MethodEntryEvent event) {
		CallStack callStack = getCallStack(event.getThreadID());
		callStack.push(event.getFrame());
		event.setFiltered(stackContainsFilteredFrame(callStack));
	}

	@Override
	public void visit(MethodExitEvent event) {
		CallStack callStack = getCallStack(event.getThreadID());
		event.setFiltered(stackContainsFilteredFrame(callStack));
		callStack.pop();
	}

	@Override
	public void visit(ObjectAllocationEvent event) {
		if (filteredClasses.contains(event.getClassName())) {
			event.setFiltered(true);
		} else {
			event.setFiltered(false);
		}
	}

	@Override
	public void visit(ObjectFreeEvent event) {
		if (filteredClasses.contains(event.getClassName())) {
			event.setFiltered(true);
		} else {
			event.setFiltered(false);
		}
	}

	private boolean stackContainsFilteredFrame(CallStack callStack) {
		for (Frame frame : callStack) {
			ClassData classData = null;
			switch (callAssignment) {
				case DEFINING_CLASS:
					classData = frame.getDefiningClass();
					break;
				case OBJECT_CLASS:
					classData = frame.getActualClass();
					break;
			}

			if (filteredClasses.contains(classData.getName())) {
				return true;
			}

			Pair<String, String> classMethodPair =
				new Pair<String, String>(classData.getName(), frame.getMethod());

			if (filteredMethods.contains(classMethodPair)) {
				return true;
			}
		}

		return false;
	}

	private CallStack getCallStack(long threadID) {
		CallStack callStack = callStacks.get(threadID);
		if (callStack == null) {
			callStack = new CallStack();
			callStacks.put(threadID, callStack);
		}
		return callStack;
	}
}
