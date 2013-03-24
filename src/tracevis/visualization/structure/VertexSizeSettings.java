/*
 * VertexSizeSettings.java
 *
 * Author: Pieter Deelen
 * Created: September 23, 2005, 4:17 PM
 *
 */

package tracevis.visualization.structure;

import tracevis.visualization.functions.CallsReceivedVertexSizeFunction;
import tracevis.visualization.functions.CallsSentVertexSizeFunction;
import tracevis.visualization.functions.InstanceVertexSizeFunction;
import tracevis.visualization.structure.StructuralView.SelectedVertexSizeFunction;

/**
 * A class which aggregates all vertex size settings.
 * @author Pieter Deelen
 */
public class VertexSizeSettings {
	private final CallsReceivedVertexSizeFunction.Settings callsReceivedSettings;
	private final CallsSentVertexSizeFunction.Settings callsSentSettings;
	private final InstanceVertexSizeFunction.Settings instanceSettings;

	private final SelectedVertexSizeFunction selectedFunction;

	/**
	 * Creates a new instance of VertexSizeSettings.
	 */
	public VertexSizeSettings(
		CallsReceivedVertexSizeFunction.Settings callsReceivedSettings,
		CallsSentVertexSizeFunction.Settings callsSentSettings,
		InstanceVertexSizeFunction.Settings instanceSettings,
		SelectedVertexSizeFunction selectedFunction)
	{
		this.callsReceivedSettings = callsReceivedSettings;
		this.callsSentSettings = callsSentSettings;
		this.instanceSettings = instanceSettings;
		this.selectedFunction = selectedFunction;
	}

	public CallsReceivedVertexSizeFunction.Settings getCallsReceivedSettings() {
		return callsReceivedSettings;
	}

	public CallsSentVertexSizeFunction.Settings getCallsSentSettings() {
		return callsSentSettings;
	}

	public InstanceVertexSizeFunction.Settings getInstanceSettings() {
		return instanceSettings;
	}

	public SelectedVertexSizeFunction getSelectedFunction() {
		return selectedFunction;
	}

}
