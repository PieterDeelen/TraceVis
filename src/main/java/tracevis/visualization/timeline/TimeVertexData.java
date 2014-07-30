/*
 * TimeVertexData.java
 *
 * Author: Pieter Deelen
 * Created: April 12, 2006, 1:59 PM
 *
 */

package tracevis.visualization.timeline;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.ListIterator;

import tracevis.model.types.ClassData;
import tracevis.model.types.Range;
import edu.uci.ics.jung.graph.Vertex;

/**
 *
 * @author Pieter Deelen
 */
class TimeVertexData {
	private final Vertex vertex;
	private float[] timeLineCache;
	private double activityRatio;

	private double startHeight;
	private double endHeight;

	/**
	 * Creates a new instance of TimeVertexData.
	 */
	public TimeVertexData(Vertex vertex) {
		this.vertex = vertex;
		timeLineCache = null;
		activityRatio = 0;
	}

	private BigInteger longToBigInteger(long l) {
		// FIXME: Conversion using ByteBuffers is klunky.
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(l);
		return new BigInteger(buffer.array());
	}

	public void updateTimeLineCache(int width, Range viewRange) {
		// NOTE: this method uses 64.64 fixed point arithmetic.
		timeLineCache = new float[width];

		long viewWidth = viewRange.getEnd() - viewRange.getBegin();

		BigInteger viewWidthBig = longToBigInteger(viewWidth).shiftLeft(64);
		BigInteger widthBig = longToBigInteger(width);
		BigInteger incrementBig = viewWidthBig.divide(widthBig);

		long increment = incrementBig.shiftRight(64).longValue();

		BigInteger leftTimeBig = longToBigInteger(viewRange.getBegin()).shiftLeft(64);
		BigInteger rightTimeBig = leftTimeBig.add(incrementBig);

		ClassData classData = (ClassData)vertex.getUserDatum("tracevis.model.Program");
		ListIterator<Range> rangeIt = classData.getActivityRanges().listIterator();

		for (int x = 0; x < width; x++) {
			if (rangeIt.hasPrevious()) {
				rangeIt.previous();
			}

			BigInteger totalTimeBig = BigInteger.ZERO;
			while (rangeIt.hasNext()) {
				Range range = rangeIt.next();

				BigInteger rangeBeginBig = longToBigInteger(range.getBegin()).shiftLeft(64);
				BigInteger rangeEndBig = longToBigInteger(range.getEnd()).shiftLeft(64);

				if (rangeEndBig.compareTo(leftTimeBig) == -1) {
					continue;
				} else if (rightTimeBig.compareTo(rangeBeginBig) == -1) {
					// range is to the right of this pixel:
					//	stop searching and backtrack.
					rangeIt.previous();
					break;
				} else {
					// range overlaps this pixel.
					BigInteger leftBig = leftTimeBig.max(rangeBeginBig);
					BigInteger rightBig = rightTimeBig.min(rangeEndBig);
					totalTimeBig = totalTimeBig.add(rightBig.subtract(leftBig));
				}
			}

			long totalTime = totalTimeBig.shiftRight(64).longValue();

			double ratio = (double)totalTime / (double)increment;

			timeLineCache[x] = (float)ratio;

			leftTimeBig = leftTimeBig.add(incrementBig);
			rightTimeBig = rightTimeBig.add(incrementBig);
		}
	}

	public float getCacheValue(int x) {
		return timeLineCache[x];
	}

	public void updateActivityRatio(long totalTime) {
		double weight = 1.0 / totalTime; ;

		ClassData classData = (ClassData)vertex.getUserDatum("tracevis.model.Program");
		List<Range> ranges = classData.getActivityRanges();
		activityRatio = 0.0;
		for (Range range : ranges) {
			activityRatio += weight * range.getWidth();
		}
	}

	public double getActivityRatio() {
		return activityRatio;
	}

	public double getStartHeight() {
		return startHeight;
	}

	public void setStartHeight(double startHeight) {
		this.startHeight = startHeight;
	}

	public double getEndHeight() {
		return endHeight;
	}

	public void setEndHeight(double endHeight) {
		this.endHeight = endHeight;
	}

	public double getCenterHeight() {
		return (endHeight + startHeight) / 2.0;
	}

	public double getWeight(int height) {
		double y0 = Math.max(startHeight, height);
		double y1 = Math.min(endHeight, height + 1);

		return Math.max(y1 - y0, 0.0);
	}
}
