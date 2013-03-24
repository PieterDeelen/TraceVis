/*
 * EventFilter.java
 *
 * Author: Pieter Deelen
 * Created: April 26, 2006, 7:20 PM
 *
 */

package tracevis.model;

import java.util.List;

import tracevis.model.types.Event;


/**
 * An EventFilter filters events.
 * @author Pieter Deelen
 */
public interface EventFilter {
	/**
	 * Filters the specified list of events.
	 * @param events the specified list of events.
	 */
	public void filter(List<Event> events);
}
