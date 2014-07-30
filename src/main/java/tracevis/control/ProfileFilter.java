/*
 * ProfileFilter.java
 *
 * Author: Pieter Deelen
 * Created: September 16, 2005, 10:03 AM
 *
 */

package tracevis.control;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * A FileFilter which only accepts files with the extension ".profile".
 */
class ProfileFilter extends FileFilter {
	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		} else {
			String extension = getExtension(file);
			if (extension != null && extension.equals("profile")) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public String getDescription() {
		return "Profiles (*.profile)";
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
