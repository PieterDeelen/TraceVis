/*
 * MathUtils.java
 *
 * Author: Pieter Deelen
 * Created: November 14, 2005, 7:50 PM
 *
 */

package tracevis.utilities;

import static java.lang.Math.pow;

import java.awt.geom.Point2D;

/**
 * A class with utility math methods.
 * @author Pieter Deelen
 */
public class MathUtils {
	/**
	 * Returns the factorial of the specified number.
	 * @param n the specified number.
	 */
	public static int fac(int n) {
		if (n == 0) {
			return 1;
		} else {
			int result = 1;
			for (int i = 1; i <= n; i++) {
				result *= i;
			}

			return result;
		}
	}

	/**
	 * Returns the value of the Bernstein polynomial for the specified
	 * parameters.
	 */
	public static double bernsteinPolynomial(int i, int N, double t) {
		return fac(N) / (float)(fac(N - i) * fac(i)) * pow(1 - t, N - i) * pow(t, i);
	}

	/**
	 * Evaluates the quadric Bezier polynomial specified by the points p1, p2,
	 * and p3 for point t (0 <= t <= 1).
	 */
	public static Point2D quadCurve(Point2D p1, Point2D p2, Point2D p3, float t) {
		double b1 = bernsteinPolynomial(0, 2, t);
		double b2 = bernsteinPolynomial(1, 2, t);
		double b3 = bernsteinPolynomial(2, 2, t);

		double x = b1 * p1.getX() + b2 * p2.getX() + b3 * p3.getX();
		double y = b1 * p1.getY() + b2 * p2.getY() + b3 * p3.getY();

		return new Point2D.Double(x, y);
	}
}
