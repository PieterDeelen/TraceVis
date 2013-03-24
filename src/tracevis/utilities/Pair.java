/*
 * Pair.java
 *
 * Author: Pieter Deelen
 * Created: November 16, 2005, 7:10 PM
 *
 */

package tracevis.utilities;

/**
 * A simple generic pair class. Inspired by JUNG's pair class, but adapted for
 * genericity.
 * @author Pieter Deelen
 */
public class Pair<E1, E2> {
	private final E1 first;
	private final E2 second;

	/**
	 * Creates a new instance of Pair.
	 * @param first the first element of the pair.
	 * @param second the second element of the pair.
	 */
	public Pair(E1 first, E2 second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Returns the first element of the pair.
	 */
	public E1 getFirst() {
		return first;
	}

	/**
	 * Returns the second element of the pair.
	 */
	public E2 getSecond() {
		return second;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Pair) {
			Pair other = (Pair)o;
			return ((this.first == other.first || this.first.equals(other.first)) &&
				(this.second == other.second || this.second.equals(other.second)));
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return first.hashCode() + second.hashCode();
	}
}
