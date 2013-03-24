/*
 * ReverseStateUpdater.java
 *
 * Author: Pieter Deelen
 * Created: April 24, 2006, 10:49 PM
 *
 */

package tracevis.model;

import tracevis.model.types.CallStack;
import tracevis.model.types.ClassData;
import tracevis.model.types.ClassLoadEvent;
import tracevis.model.types.FramePopEvent;
import tracevis.model.types.MethodEntryEvent;
import tracevis.model.types.MethodExitEvent;
import tracevis.model.types.ObjectAllocationEvent;
import tracevis.model.types.ObjectFreeEvent;
import tracevis.model.types.ThreadStartEvent;
import tracevis.model.types.ThreadStopEvent;


class ReverseStateUpdater extends EventVisitor {
	public ReverseStateUpdater(ProgramInterface program) {
		this.program = program;
	}

	private final ProgramInterface program;

	@Override
	public void visit(ClassLoadEvent event) {
		ClassData classData = program.getClassData(event.getClassName());

		classData.decreaseLoadCount();
	}

	@Override
	public void visit(FramePopEvent event) {
		long threadID = event.getThreadID();
		CallStack callStack = program.getCallStack(threadID);
		callStack.push(event.getFrame());
	}

	@Override
	public void visit(MethodEntryEvent event) {
		long threadID = event.getThreadID();
		CallStack callStack = program.getCallStack(threadID);
		callStack.pop();
	}

	@Override
	public void visit(MethodExitEvent event) {
		long threadID = event.getThreadID();
		CallStack callStack = program.getCallStack(threadID);
		callStack.push(event.getFrame());
	}

	@Override
	public void visit(ObjectAllocationEvent event) {
		ClassData classData = program.getClassData(event.getClassName());
		classData.unregisterInstance(event.getObjectID());
	}

	@Override
	public void visit(ObjectFreeEvent event) {
		ClassData classData = program.getClassData(event.getClassName());
		classData.registerInstance(event.getObjectID());
	}

	@Override
	public void visit(ThreadStartEvent event) {
		this.program.getActiveThreads().remove(event.getThreadID());
	}

	@Override
	public void visit(ThreadStopEvent event) {
		this.program.getActiveThreads().add(event.getThreadID());
	}
}