/*
 * Range.java
 *
 * Author: Pieter Deelen
 * Created: September 30, 2005, 2:41 PM
 *
 */

package tracevis.model.types;

/**
 * A range.
 * @author Pieter Deelen
 */
public class Range {
	private final long begin;
	private final long end;

	/**
	 * Create a new instance of Range.
	 * @param begin the begin of the range.
	 * @param end the end of the range.
	 */
	public Range(long begin, long end) {
		this.begin = begin;
		this.end = end;
	}

	/**
	 * Returns the begin of the range.
	 */
	public long getBegin() {
		return begin;
	}

	/**
	 * Returns the end of the range.
	 */
	public long getEnd() {
		return end;
	}

	/**
	 * Returns the width of the range.
	 */
	public long getWidth() {
		return end - begin;
	}

	/**
	 * Returns whether the specified value is contained in this range.
	 * @param value the specified range.
	 */
	public boolean contains(long value) {
		return begin <= value && value < end;
	}

	/**
	 * Returns whether this range overlaps the specified range.
	 * @param range the specified range.
	 */
	public boolean overlaps(Range range) {
		return getEnd() > range.getBegin() && getBegin() <= range.getEnd();
	}

	@Override
	public String toString() {
		return "[" + begin + "," + end + "]";
	}
}
