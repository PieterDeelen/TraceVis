/*
 * Main.java
 *
 * Author: Pieter Deelen
 * Created: June 6, 2005, 2:43 PM
 *
 */

package tracevis;

import java.awt.EventQueue;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import tracevis.control.MainFrame;
import tracevis.model.Program;
import tracevis.properties.ApplicationProperties;
import tracevis.visualization.ProgramView;

/**
 * Main class.
 * @author Pieter Deelen
 */
public class Main {
	/**
	 * A thread which saves application properties when the application is shut
	 * down.
	 */
	private static class PropertySaver extends Thread {
		@Override
		public void run() {
			ApplicationProperties applicationProperties = ApplicationProperties.getInstance();
			applicationProperties.save();
		}
	}

	/**
	 * Customizes certain Swing properties.
	 */
	private static void customizeSwing() {
		// Disable the annoying single-click renaming in file choosers.
		UIManager.put("FileChooser.readOnly", Boolean.TRUE);

		// Use a nicer default font.
		FontUIResource font = new FontUIResource("Dialog", Font.PLAIN, 11);
		Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, font);
			}
		}
	}

	/**
	 * The main method.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Runtime runtime = Runtime.getRuntime();
				runtime.addShutdownHook(new PropertySaver());

				customizeSwing();

				Program program = new Program();
				ProgramView view = new ProgramView(program);
				MainFrame mainFrame = new MainFrame(program, view);
				mainFrame.setVisible(true);
			}
		});
	}
}
