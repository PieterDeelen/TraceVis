/*
 * TraceReader.java
 *
 * Author: Pieter Deelen
 * Created: September 12, 2005, 1:36 PM
 *
 */

package tracevis.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;




/**
 * Parser for a trace file. Calls back into Program for the handling of events.
 * @author Pieter Deelen
 */
class TraceReader {
	private final ProgramEventInterface program;

	/**
	 * Creates a new instance of TraceReader.
	 * @param program the Program instance to call back to for event handling.
	 */
	public TraceReader(ProgramEventInterface program) {
		this.program = program;
	}

	/**
	 * Reads the trace file from the specified input stream.
	 * @param inputStream the specified input stream.
	 * @throws IOException if the trace could not be read.
	 */
	public void readTrace(InputStream inputStream) throws IOException {
		InputStreamReader inputReader= new InputStreamReader(inputStream);
		BufferedReader in = new BufferedReader(inputReader);

		String input;
		// Events are separated by newlines.
		while ((input = in.readLine()) != null) {
			// Fields are separated by colons.
			String[] event = input.split(":");

			// The first two fields of every event are the event type and a
			// time stamp.
			String eventType = event[0];
			Long timeStamp = new Long(event[1]);

			// Check which kind of event it is.
			if (eventType.equals("MN")) {
				// Method entry.
				Long threadID = new Long(event[2]);
				String className = (event[3]).replace("/", ".");
				String methodName = event[4];
				Long objectID = new Long(event[5]);
				program.handleMethodEntry(timeStamp, threadID, className, methodName, objectID);
			} else if (eventType.equals("MX")) {
				// Method exit.
				Long threadID = new Long(event[2]);
				String className = (event[3]).replace("/", ".");
				String methodName = event[4];
				program.handleMethodExit(timeStamp, threadID, className, methodName);
			} else if (eventType.equals("FP")) {
				// Frame pop (caused by exception).
				Long threadID = new Long(event[2]);
				String className = (event[3]).replace("/", ".");
				String methodName = event[4];
				program.handleFramePop(timeStamp, threadID, className, methodName);
			} else if (eventType.equals("CL")) {
				// Class load.
				String className = (event[2]).replace("/", ".");
				program.handleClassLoad(timeStamp, className);
			} else if (eventType.equals("TB")) {
				// Thread begin.
				Long threadID = new Long(event[2]);
				program.handleThreadStart(timeStamp, threadID);
			} else if (eventType.equals("TE")) {
				// Thread end.
				Long threadID = new Long(event[2]);
				program.handleThreadStop(timeStamp, threadID);
			} else if (eventType.equals("OA")) {
				// Object allocation.
				String className = (event[2]).replace("/", ".");
				Long objectID = new Long(event[3]);
				program.handleObjectAllocation(timeStamp, className, objectID);
			} else if (eventType.equals("OF")) {
				String className = (event[2]).replace("/", ".");
				Long objectID = new Long(event[3]);
				program.handleObjectFree(timeStamp, className, objectID);
			} else if (eventType.equals("VS")) {
				program.handleVMStart(timeStamp);
			} else if (eventType.equals("VI")) {
				program.handleVMInit(timeStamp);
			} else if (eventType.equals("VD")) {
				program.handleVMDeath(timeStamp);
			}
		}

		in.close();
	}

}
