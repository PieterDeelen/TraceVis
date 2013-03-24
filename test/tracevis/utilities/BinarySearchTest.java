/*
 * BinarySearch.java
 *
 * Author: Pieter Deelen
 * Created: April 5, 2006, 8:56 PM
 *
 */

package tracevis.utilities;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import tracevis.utilities.BinarySearch.Condition;

/**
 *
 * @author Pieter Deelen
 */
public class BinarySearchTest {

  @Test
  public void test() {
	  Condition condition = new BinarySearch.Condition() {		
		@Override
		public boolean isTrue(Object o) {
			Integer integer = (Integer)o;
			return integer.intValue() == 1;		
		}
	  };
	  Assert.assertEquals(0, BinarySearch.search(Arrays.asList(1), condition));
  }

}
