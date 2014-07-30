/*
 * EdgeData.java
 *
 * Author: Pieter Deelen
 * Created: October 25, 2005, 10:49 AM
 *
 */

package tracevis.visualization.structure;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A JUNG user data record for edges which stores information used by multiple
 * views.
 * @author Pieter Deelen
 */
public class EdgeData {
	private Map<Long, SortedSet<Integer>> stackEntries;

	private boolean inTransition;
	private float transition;

	/**
	 * Creates a new instance of EdgeData.
	 */
	public EdgeData() {
		stackEntries = new HashMap<Long, SortedSet<Integer>>();
		inTransition = false;
		transition = 0.0f;
	}

	public void addStackEntry(long thread, int depth) {
		SortedSet<Integer> entries = stackEntries.get(thread);
		if (entries == null) {
			entries = new TreeSet();
			stackEntries.put(thread, entries);
		}
		entries.add(depth);
	}

	public Set<Long> getActiveThreads() {
		return stackEntries.keySet();
	}

	public SortedSet<Integer> getStackEntries(long thread) {
		return stackEntries.get(thread);
	}

	public void resetStackEntries() {
		stackEntries = new HashMap<Long, SortedSet<Integer>>();
	}

	public boolean isOnStack() {
		return !stackEntries.isEmpty();
	}

	public boolean isInTransition() {
		return inTransition;
	}

	public void setInTransition(boolean inTransition) {
		this.inTransition = inTransition;
	}

	public float getTransition() {
		return transition;
	}

	public void setTransition(float transition) {
		this.transition = transition;
	}
}
