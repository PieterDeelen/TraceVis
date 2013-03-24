/*
 * VertexData.java
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
 *
 * @author Pieter Deelen
 */
public class VertexData {
	private Map<Long, SortedSet<Integer>> stackEntries;

	private float hue;

	/**
	 * Creates a new instance of VertexData.
	 */
	public VertexData() {
		stackEntries = new HashMap<Long, SortedSet<Integer>>();
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

	public float getHue() {
		return hue;
	}

	public void setHue(float hue) {
		this.hue = hue;
	}
}
