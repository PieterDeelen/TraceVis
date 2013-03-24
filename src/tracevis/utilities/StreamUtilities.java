/*
 * StreamUtilities.java
 *
 * Author: Pieter Deelen
 * Created: September 14, 2005, 1:26 PM
 *
 */

package tracevis.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A class with stream utility methods.
 * @author Pieter Deelen
 */
public class StreamUtilities {

	/**
	 * Returns the number of lines in the specified input stream.
	 * @param input the specified input stream.
	 * @throws IOException if the input stream could not be read.
	 */
	public static int lineCount(InputStream input) throws IOException {
		InputStreamReader streamReader = new InputStreamReader(input);
		BufferedReader bufferedReader = new BufferedReader(streamReader);

		int lineCount = 0;
		while (bufferedReader.readLine() != null) {
			lineCount++;
		}

		return lineCount;
	}
}
