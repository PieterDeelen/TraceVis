/*
 * TraceGenerator.java
 *
 * Author: Pieter Deelen
 * Created: September 16, 2005, 9:26 AM
 *
 */

package tracevis.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import tracevis.utilities.StringUtils;

/**
 * Generates a trace from a program's execution.
 * @author Pieter Deelen
 */
public class TraceGenerator {
	/**
	 * Starts the program specified by options and saves a trace in the
	 * specified trace file.
	 * @param options specifies the program to start and the options to start
	 *                it with.
	 * @param traceFile the specified trace file.
	 * @param stdout the OutputStream to write the program's standard output to.
	 * @param stderr the OutputStream to write the program's standard error
	 *               output to.
	 * @throws IOException if the trace could not be saved.
	 */
	public static void generateTrace(ProgramOptions options, File traceFile,
									 OutputStream stdout, OutputStream stderr)
		throws IOException
	{
		try {
			ServerSocket serverSocket = new ServerSocket(0);
			launchProgram(options, serverSocket.getLocalPort(), stdout, stderr);
			Socket dataSocket = serverSocket.accept();
			serverSocket.close();

			FileOutputStream outputStream = new FileOutputStream(traceFile);
			ZipOutputStream zipFile = new ZipOutputStream(outputStream);
			zipFile.putNextEntry(new ZipEntry("trace"));

			Runnable mtraceRedirector = new StreamRedirectThread(dataSocket.getInputStream(), zipFile);
			Thread mtraceThread = new Thread(mtraceRedirector, "mtrace redirector");
			mtraceThread.setPriority(Thread.MAX_PRIORITY-1);
			mtraceThread.start();
			mtraceThread.join();
			zipFile.closeEntry();
			zipFile.close();
		} catch (InterruptedException exc) {
			// Ignore.
		}
	}

	/**
	 * Returns a command line which starts a program.
	 * @param options specifies the program and its options.
	 * @param port the port to transmit events over.
	 */
	private static String getCommandLine(ProgramOptions options, int port) {
		StringBuffer commandLine = new StringBuffer();

		// Set executable.
		commandLine.append(System.getProperty("java.home"));
		commandLine.append(File.separator);
		commandLine.append("bin");
		commandLine.append(File.separator);
		commandLine.append("java");

		// Set MTrace options.
		String mtracePath = System.getProperty("mtrace.path");

		File mtraceJAR = new File(mtracePath + File.separator + "mtrace.jar");
		commandLine.append(" -Xbootclasspath/a:" + mtraceJAR.getAbsolutePath());

		File mtraceLibrary = new File(mtracePath + File.separator + System.mapLibraryName("mtrace"));
		commandLine.append(" -agentpath:" + mtraceLibrary.getAbsolutePath() + "=");

		commandLine.append("port=" + port + ",");

		String inner = Boolean.toString(!options.getExcludeInnerClasses());
		commandLine.append("inner=" + inner +  ",");

		List<String> filters = new LinkedList<String>();
		// Set class exclusion filters.
		for (String filter : options.getExclusionPatterns()) {
			filters.add("exclude=" + filter.replace(".", "/"));
		}

		// Set class inclusion filters.
		for (String filter : options.getInclusionPatterns()) {
			filters.add("include=" + filter.replace(".", "/"));
		}

		commandLine.append(StringUtils.joinStrings(filters.iterator(), ","));

		// Set user options.
		commandLine.append(" " + options.getVMOptions());
		String classPath = StringUtils.joinStrings(options.getClassPath().iterator(), ":");
		commandLine.append(" -classpath " + classPath);

		commandLine.append(" " + options.getMainClass());
		commandLine.append(" " + options.getArguments());

		return commandLine.toString();
	}

	/**
	 * Launches the program.
	 * @param port the port (on localhost) the mtrace agent should connect to.
	 * @param options the program's options.
	 * @param stdout the OutputStream to write the program's standard output to.
	 * @param stderr the OutputStream to write the program's standard error
	 *               output to.
	 */
	private static void launchProgram(ProgramOptions options, int port, OutputStream stdout, OutputStream stderr) {
		String commandLine = getCommandLine(options, port);

		File workingDirectory;
		if (!options.getWorkingDirectory().equals("")) {
			workingDirectory = new File(options.getWorkingDirectory());
		} else {
			workingDirectory = null;
		}

		try {
			// Execute program.
			Runtime runtime = Runtime.getRuntime();
			String[] environment = null;

			Process process = runtime.exec(commandLine, environment, workingDirectory);

			// Redirect target's error stream.
			Runnable errorRedirector = new StreamRedirectThread(process.getErrorStream(), stderr);
			Thread errorThread = new Thread(errorRedirector, "error redirector");
			errorThread.setPriority(Thread.MAX_PRIORITY-1);
			errorThread.start();

			// Redirect target's output stream.
			Runnable outputRedirector = new StreamRedirectThread(process.getInputStream(), stdout);
			Thread outputThread = new Thread(outputRedirector, "output redirector");
			outputThread.setPriority(Thread.MAX_PRIORITY-1);
			outputThread.start();

		} catch (IOException exc) {
			throw new Error("Error launching program: " + exc.getMessage());
		}
	}
}
