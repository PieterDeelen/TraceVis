/*
 * EventVisitor.java
 *
 * Author: Pieter Deelen
 * Created: April 21, 2006, 12:04 PM
 *
 */

package tracevis.model;

import tracevis.model.types.ClassLoadEvent;
import tracevis.model.types.Event;
import tracevis.model.types.FramePopEvent;
import tracevis.model.types.MethodEntryEvent;
import tracevis.model.types.MethodExitEvent;
import tracevis.model.types.ObjectAllocationEvent;
import tracevis.model.types.ObjectFreeEvent;
import tracevis.model.types.ThreadStartEvent;
import tracevis.model.types.ThreadStopEvent;
import tracevis.model.types.VMDeathEvent;
import tracevis.model.types.VMInitEvent;
import tracevis.model.types.VMStartEvent;

/**
 *
 * @author Pieter Deelen
 */
public class EventVisitor {
	public void visit(ClassLoadEvent event) {}
	public void visit(FramePopEvent event) {}
	public void visit(MethodEntryEvent event) {}
	public void visit(MethodExitEvent event) {}
	public void visit(ObjectAllocationEvent event) {}
	public void visit(ObjectFreeEvent event) {}
	public void visit(ThreadStartEvent event) {}
	public void visit(ThreadStopEvent event) {}
	public void visit(VMDeathEvent event) {}
	public void visit(VMInitEvent event) {}
	public void visit(VMStartEvent event) {}

	public void visit(Event event) {
		if (event instanceof ClassLoadEvent) {
			visit((ClassLoadEvent)event);
		} else if (event instanceof FramePopEvent) {
			visit((FramePopEvent)event);
		} else if (event instanceof MethodEntryEvent) {
			visit((MethodEntryEvent)event);
		} else if (event instanceof MethodExitEvent) {
			visit((MethodExitEvent)event);
		} else if (event instanceof ObjectAllocationEvent) {
			visit((ObjectAllocationEvent)event);
		} else if (event instanceof ObjectFreeEvent) {
			visit((ObjectFreeEvent)event);
		} else if (event instanceof ThreadStartEvent) {
			visit((ThreadStartEvent)event);
		} else if (event instanceof ThreadStopEvent) {
			visit((ThreadStopEvent)event);
		} else if (event instanceof VMDeathEvent) {
			visit((VMDeathEvent)event);
		} else if (event instanceof VMInitEvent) {
			visit((VMInitEvent)event);
		} else if (event instanceof VMStartEvent) {
			visit((VMStartEvent)event);
		}
	}
}
