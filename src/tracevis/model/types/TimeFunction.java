/*
 * TimeFunction.java
 *
 * Author: Pieter Deelen
 * Created: November 10, 2005, 10:21 AM
 *
 */

package tracevis.model.types;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import tracevis.utilities.Pair;

/**
 * A function which maps times to values. This function is specified by
 * data points, which are time-value pairs, and an initial value. The value of
 * the function at a certain time <i>t</i> is determined as follows:
 * <ul>
 *   <li> If <i>t</i> is smaller than the time of the data point with the
 *        lowest time, return the initial value. </li>
 *   <li> Otherwise, the value of the function is the value of the data point
 *        with the highest time below <i>t</i>. </li>
 * </ul>
 * Times are represented by long integers. The type of values is specified by
 * the type parameter V.
 * @author Pieter Deelen
 */
public class TimeFunction<V> {
	private class FunctionIterator implements Iterator<Pair<Long, V>> {
		private final SortedMap<Long, V> subFunction;
		private final Iterator<Map.Entry<Long, V>> entryIterator;

		private FunctionIterator(Range range) {
			subFunction = function.subMap(range.getBegin(), range.getEnd() + 1);
			entryIterator = subFunction.entrySet().iterator();
		}

		@Override
		public boolean hasNext() {
			return entryIterator.hasNext();
		}

		@Override
		public Pair<Long, V> next() {
			Map.Entry<Long, V> next = entryIterator.next();
			return new Pair<Long, V>(next.getKey(), next.getValue());
		}

		@Override
		public void remove() {
			// Not supported.
		}
	}

	private final SortedMap<Long, V> function;
	private final V initialValue;

	/**
	 * Creates a new instance of TimeFunction.
	 * @param initialValue the initial value.
	 */
	public TimeFunction(V initialValue) {
		this.function = new TreeMap<Long, V>();
		this.initialValue = initialValue;
	}

	/**
	 * Adds a new data point to the function.
	 * @param time the time.
	 * @param value the value.
	 */
	public void put(long time, V value) {
		function.put(time, value);
	}

	/**
	 * Returns the value of the function at the specified time.
	 * @param time the specified time.
	 */
	public V get(long time) {
		// Restrict the view of function to keys t with t < time + 1 ==
		// t <= time.
		SortedMap<Long, V> head = function.headMap(time + 1);

		if (head.isEmpty()) {
			return initialValue;
		} else {
			long key = head.lastKey();
			return head.get(key);
		}
	}

	/**
	 * Returns the time of the last data point.
	 */
	public long lastTime() {
		return function.lastKey();
	}

	/**
	 * Returns all data points of which the times fall in the specified range.
	 * @param range the specified range.
	 */
	public Iterator<Pair<Long, V>> getEntryIterator(Range range) {
		return new FunctionIterator(range);
	}
}
