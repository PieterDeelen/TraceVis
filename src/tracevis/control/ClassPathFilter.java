/*
 * ClassPathFilter.java
 *
 * Author: Pieter Deelen
 * Created: October 19, 2005, 11:28 AM
 *
 */

package tracevis.control;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * A FileFilter which only accepts files with the extension ".jar".
 */
class ClassPathFilter extends FileFilter {
	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		} else {
			String extension = getExtension(file);
			if (extension != null && extension.equals("jar")) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public String getDescription() {
		return "Class Path Entry (directory or JAR file)";
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