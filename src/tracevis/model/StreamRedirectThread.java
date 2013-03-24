/*
 * StreamRedirectThread.java
 *
 * Author: Pieter Deelen
 * Created: June 3, 2005, 11:40 AM
 *
 */

package tracevis.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;


/**
 * StreamRedirectThread uses its own thread to redirect one stream to another.
 * @author Pieter Deelen
 */
class StreamRedirectThread implements Runnable {

	private final Reader in;
	private final Writer out;

	private static final int BUFFER_SIZE = 2048;

	/**
	 * Creates a new instance of StreamRedirectThread.
	 */
	public StreamRedirectThread(InputStream in, OutputStream out) {
		this.in = new InputStreamReader(in);
		this.out = new OutputStreamWriter(out);
	}

	/**
	 * Stream redirecting loop.
	 */
	@Override
	public void run() {
		try {
			char[] cbuf = new char[BUFFER_SIZE];
			int count;
			while ((count = in.read(cbuf, 0, BUFFER_SIZE)) >= 0) {
				out.write(cbuf, 0, count);
				out.flush();
			}

		} catch(IOException exc) {
			System.err.println("Child I/O Transfer - " + exc);
		}
	}
}
