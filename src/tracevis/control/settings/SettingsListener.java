/*
 * SettingsListener.java
 *
 * Author: Pieter Deelen
 * Created: September 23, 2005, 3:08 PM
 *
 */

package tracevis.control.settings;

/**
 * A SettingsListener listens to the events generated by a settings dialog or
 * panel.
 * @author Pieter Deelen
 */
public interface SettingsListener {
	/**
	 * Indicates that one or more settings have changed.
	 * @param source the panel from which the settings change is originated.
	 */
	public void settingsChanged(Object source);
}
