/*
 * ConstructorFilter.java
 *
 * Author: Pieter Deelen
 * Created: May 7, 2006, 7:26 PM
 *
 */

package tracevis.model;

import java.util.List;

import tracevis.model.types.Event;
import tracevis.model.types.FramePopEvent;
import tracevis.model.types.MethodEntryEvent;
import tracevis.model.types.MethodExitEvent;


/**
 *
 * @author Pieter Deelen
 */
public class ConstructorFilter extends EventVisitor implements EventFilter {
	@Override
	public void filter(List<Event> events) {
		for (Event event : events) {
			event.setFiltered(false);
			visit(event);
		}
	}

	@Override
	public void visit(FramePopEvent event) {
		if (event.getMethodName().equals("<init>")) {
			event.setFiltered(false);
		} else {
			event.setFiltered(true);
		}
	}

	@Override
	public void visit(MethodEntryEvent event) {
		if (event.getMethodName().equals("<init>")) {
			event.setFiltered(false);
		} else {
			event.setFiltered(true);
		}
	}

	@Override
	public void visit(MethodExitEvent event) {
		if (event.getMethodName().equals("<init>")) {
			event.setFiltered(false);
		} else {
			event.setFiltered(true);
		}
	}
}
