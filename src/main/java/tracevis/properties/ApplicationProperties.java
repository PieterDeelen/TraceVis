/*
 * ApplicationProperties.java
 *
 * Author: Pieter Deelen
 * Created: December 13, 2005, 2:25 PM
 *
 */

package tracevis.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ApplicationProperties manages application-wide properties. These properties
 * can be saved when the application is shut down and restored when the
 * application is loaded again. ApplicationProperties is a singleton class
 * (see GoF book), i.e., a class which can have at most one instance. This
 * instance can be retrieved using the method {@link #getInstance}.
 * @author Pieter Deelen
 */
public class ApplicationProperties {

	private static ApplicationProperties instance = null;
	private final Properties properties;
	private final File propertiesFile;

	/**
	 * Creates a new instance of ApplicationProperties.
	 */
	private ApplicationProperties() {
		String homeDir = System.getProperty("user.home");
		String dirSep = System.getProperty("file.separator");
		propertiesFile = new File(homeDir + dirSep + ".tracevis");

		properties = new Properties();
		try {
			properties.load(new FileInputStream(propertiesFile));
		} catch (IOException e) {
			// Use default properties.
			properties.setProperty("tracesDir", homeDir);
			properties.setProperty("profilesDir", homeDir);
		}
	}

	/**
	 * Returns the instance of ApplicationProperties.
	 */
	public static synchronized ApplicationProperties getInstance() {
		if (instance == null) {
			instance = new ApplicationProperties();
		}
		return instance;
	}

	/**
	 * Stores the application properties to a configuration file.
	 */
	public void save() {
		String comments = "CallGraph configuration file.";
		try {
			properties.store(new FileOutputStream(propertiesFile), comments);
		} catch (IOException e) {
			// Ignore.
		}
	}

	/**
	 * Returns the directory where the profiles are stored by default.
	 */
	public String getProfilesDirectory() {
		return properties.getProperty("profilesDir");
	}

	/**
	 * Returns the directory where the profiles are stored by default.
	 */
	public String getTracesDirectory() {
		return properties.getProperty("tracesDir");
	}
}
