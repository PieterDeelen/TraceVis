/*
 * ProgramOptions.java
 *
 * Author: Pieter Deelen
 * Created: June 13, 2005, 11:33 AM
 *
 */

package tracevis.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import tracevis.utilities.StringUtils;

/**
 * A class that holds all the information necessary to load a program.
 * @author Pieter Deelen
 */
public class ProgramOptions {

	private String mainClass;
	private String arguments;
	private String workingDirectory;
	private String vmOptions;
	private List<String> classPath;
	private List<String> exclusionPatterns;
	private List<String> inclusionPatterns;
	private boolean excludeInnerClasses;

	/**
	 * Creates a new instance of ProgramOptions.
	 * @param mainClass the program's main class.
	 * @param arguments a space separated list containing the program's command
	 *        line arguments.
	 * @param workingDirectory the program's working directory.
	 * @param vmOptions options to pass to the virtual machine.
	 * @param exclusionPatterns indicates which classes to exclude.
	 * @param inclusionPatterns indicates which classes to include.
	 * @param excludeInnerClasses don't inspect inner classes.
	 */
	public ProgramOptions(String mainClass, String arguments,
	                      String workingDirectory, String vmOptions,
						  List<String> classPath,
	                      List<String> exclusionPatterns,
						  List<String> inclusionPatterns,
	                      boolean excludeInnerClasses)
	{
		this.mainClass = mainClass;
		this.arguments = arguments;
		this.workingDirectory = workingDirectory;
		this.vmOptions = vmOptions;
		this.classPath = classPath;
		this.exclusionPatterns = exclusionPatterns;
		this.inclusionPatterns = inclusionPatterns;
		this.excludeInnerClasses = excludeInnerClasses;
	}

	/**
	 * Generates a ProgramOptions by reading the file propertiesFile.
	 */
	public static ProgramOptions load(File propertiesFile) throws IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(propertiesFile.getPath()));

		ProgramOptions options = new ProgramOptions(
			properties.getProperty("mainClass", ""),
			properties.getProperty("arguments", ""),
			properties.getProperty("workingDirectory", ""),
			properties.getProperty("vmOptions", ""),
			parseList(properties.getProperty("classPath")),
			parseList(properties.getProperty("exclusionPatterns")),
			parseList(properties.getProperty("inclusionPatterns")),
			properties.getProperty("excludeInnerClasses", "true").equals("true")
		);

		return options;
	}

	private static List<String> parseList(String stringList) {
		List<String> result = new ArrayList<String>();
		if (stringList != null) {
			if (!stringList.equals("")) {
				for (String item : stringList.split(":")) {
					result.add(item);
				}
			}
		}

		return result;
	}

	/**
	 * Saves this ProgramOptions to the file propertiesFile.
	 */
	public void save(File propertiesFile) throws IOException {
		Properties properties = new Properties();
		properties.setProperty("mainClass", mainClass);
		properties.setProperty("arguments", arguments);
		properties.setProperty("workingDirectory", workingDirectory);
		properties.setProperty("vmOptions", vmOptions);

		String classPath = formatList(this.classPath);
		properties.setProperty("classPath", classPath);

		String exclusionPatterns = formatList(this.exclusionPatterns);
		properties.setProperty("exclusionPatterns", exclusionPatterns);

		String inclusionPatterns = formatList(this.inclusionPatterns);
		properties.setProperty("inclusionPatterns", inclusionPatterns);

		properties.setProperty("excludeInnerClasses", Boolean.toString(excludeInnerClasses));

		properties.store(new FileOutputStream(propertiesFile.getPath()), "");
	}

	private static String formatList(List<String> stringList) {
		return StringUtils.joinStrings(stringList.iterator(), ":");
	}

	/**
	 * Returns the name of the main class.
	 */
	public String getMainClass() {
		return mainClass;
	}

	/**
	 * Sets the name of the main class.
	 */
	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}

	/**
	 * Returns a space separated list of arguments.
	 */
	public String getArguments() {
		return arguments;
	}

	/**
	 * Sets the list of arguments.
	 */
	public void setArguments(String arguments) {
		this.arguments = arguments;
	}

	/**
	 * Returns the program's working directory.
	 */
	public String getWorkingDirectory() {
		return workingDirectory;
	}

	/**
	 * Sets the program's working directory.
	 */
	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}

	/**
	 * Returns the virtual machine options.
	 */
	public String getVMOptions() {
		return vmOptions;
	}

	/**
	 * Sets the virtual machine options.
	 */
	public void setVMOptions(String vmOptions) {
		this.vmOptions = vmOptions;
	}

	/**
	 * Returns the list of exclusion patterns.
	 */
	public List<String> getExclusionPatterns() {
		return exclusionPatterns;
	}

	/**
	 * Sets the list of exclusion patterns.
	 */
	public void setExclusionPatterns(List<String> exclusionPatterns) {
		this.exclusionPatterns = exclusionPatterns;
	}

	/**
	 * Returns a boolean indicating whether to exclude inner classes.
	 */
	public boolean getExcludeInnerClasses() {
		return excludeInnerClasses;
	}

	/**
	 * Sets a boolean indicating whether to exclude inner classes.
	 */
	public void setExcludeInnerClasses(boolean excludeInnerClasses) {
		this.excludeInnerClasses = excludeInnerClasses;
	}

	/**
	 * Returns the list of inclusion patterns.
	 */
	public List<String> getInclusionPatterns() {
		return inclusionPatterns;
	}

	/**
	 * Sets the list of inclusion patterns.
	 */
	public void setInclusionPatterns(List<String> inclusionPatterns) {
		this.inclusionPatterns = inclusionPatterns;
	}

	/**
	 * Returns the list with class path entries.
	 */
	public List<String> getClassPath() {
		return classPath;
	}

	/**
	 * Sets the list with class path entries.
	 */
	public void setClassPath(List<String> classPath) {
		this.classPath = classPath;
	}

}
