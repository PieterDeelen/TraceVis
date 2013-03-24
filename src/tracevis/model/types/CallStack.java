/*
 * CallStack.java
 *
 * Author: Pieter Deelen
 * Created: January 3, 2006, 10:57 AM
 *
 */

package tracevis.model.types;

import java.util.Iterator;
import java.util.LinkedList;



/**
 * Stores a call stack.
 * @author Pieter Deelen
 */
public class CallStack implements Iterable<Frame> {

	private final LinkedList<Frame> frames;

	/**
	 * Creates a new (empty) CallStack.
	 */
	public CallStack() {
		frames = new LinkedList<Frame>();
	}

	/**
	 * Push a frame on top of the stack.
	 * @param newFrame the new frame.
	 */
	public void push(Frame newFrame) {
		frames.addFirst(newFrame);
	}

	/**
	 * Pop the frame from the top of the stack.
	 */
	public void pop() {
		frames.removeFirst();
	}

	/**
	 * Returns the frame on top of the stack.
	 */
	public Frame top() {
		return frames.getFirst();
	}

	/**
	 * Returns an iterator which iterates over the stack from top to bottom.
	 */
	@Override
	public Iterator<Frame> iterator() {
		return frames.iterator();
	}

	/**
	 * Returns the depth of the stack.
	 */
	public int depth() {
		return frames.size();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (Frame frame : frames) {
			result.append(frame.toString());
		}
		return result.toString();
	}
}
