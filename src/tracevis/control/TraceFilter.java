/*
 * TraceFilter.java
 *
 * Author: Pieter Deelen
 * Created: September 16, 2005, 10:04 AM
 *
 */

package tracevis.control;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * A FileFilter which only accepts files with the extension ".trace".
 */
class TraceFilter extends FileFilter {
	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		} else {
			String extension = getExtension(file);
			if (extension != null && extension.equals("trace")) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public String getDescription() {
		return "Trace file (*.trace)";
	}

	private String getExtension(File file) {
		String fileName = file.getName();
		int index = fileName.lastIndexOf('.');
		if (index > 0 && index < fileName.length() - 1) {
			return fileName.substring(index + 1);
		} else {
			return null;
		}
	}
}