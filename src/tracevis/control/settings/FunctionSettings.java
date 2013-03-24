/*
 * FunctionSettings.java
 *
 * Author: Pieter Deelen
 * Created: August 24, 2005, 4:21 PM
 *
 */

package tracevis.control.settings;

import javax.swing.JPanel;

import tracevis.visualization.functions.Function;

/**
 * A FunctionSettings is a JPanel which allows the user to configure a function.
 * @author Pieter Deelen
 */
public abstract class FunctionSettings extends JPanel {
	public abstract String getFunctionName();
	public abstract Function getFunction();
}
