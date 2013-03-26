/*
 * BinarySearch.java
 *
 * Author: Pieter Deelen
 * Created: April 5, 2006, 8:56 PM
 *
 */

package tracevis.utilities;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Pieter Deelen
 */
public class BinarySearchTest {

  @Test
  public void test() {
	  BinarySearch.Condition condition = new BinarySearch.Condition() {		
		@Override
		public boolean isTrue(Object o) {
			Integer integer = (Integer)o;
			return integer.intValue() < 2;		
		}
	  };
	  
	  Assert.assertEquals(-1, BinarySearch.search(Collections.emptyList(), condition));
	  Assert.assertEquals(0, BinarySearch.search(Arrays.asList(0), condition));
	  Assert.assertEquals(0, BinarySearch.search(Arrays.asList(1), condition));	  
	  Assert.assertEquals(1, BinarySearch.search(Arrays.asList(0, 1), condition));
	  Assert.assertEquals(1, BinarySearch.search(Arrays.asList(0, 1, 2, 3, 4), condition));
  }
 
}
