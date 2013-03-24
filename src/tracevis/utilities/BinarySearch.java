/*
 * BinarySearch.java
 *
 * Author: Pieter Deelen
 * Created: April 5, 2006, 8:56 PM
 *
 */

package tracevis.utilities;

import java.util.List;

/**
 *
 * @author Pieter Deelen
 */
public class BinarySearch {
	public static interface Condition {
		public boolean isTrue(Object o);
	}

	public static int search(List list, Condition condition) {
		if (list.size() == 0) {
			return -1;
		} else if (!condition.isTrue(list.get(0))) {
			return -1;
		} else if (condition.isTrue(list.get(list.size() - 1))) {
			return list.size() - 1;
		} else {

			int x = 0;
			int y = list.size() - 1;

			while (x + 1 != y) {
				int h = (x + y) / 2;
				if (condition.isTrue(list.get(h))) {
					x = h;
				} else {
					y = h;
				}
			}

			return x;
		}
	}
}
