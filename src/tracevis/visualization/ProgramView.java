/*
 * ProgramView.java
 *
 * Author: Pieter Deelen
 * Created: June 10, 2005, 11:33 AM
 *
 */

package tracevis.visualization;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import tracevis.model.Program;
import tracevis.model.types.CallStack;
import tracevis.model.types.ClassData;
import tracevis.model.types.Frame;
import tracevis.model.types.ProgramListener;
import tracevis.visualization.detail.InformationPanel;
import tracevis.visualization.explorer.Explorer;
import tracevis.visualization.structure.CustomRenderer;
import tracevis.visualization.structure.CustomSpringLayout;
import tracevis.visualization.structure.EdgeData;
import tracevis.visualization.structure.StructuralView;
import tracevis.visualization.structure.VertexData;
import tracevis.visualization.structure.VertexSizeSettings;
import tracevis.visualization.timeline.TimeLine;
import tracevis.visualization.timeline.TimeLineSettings;
import tracevis.visualization.utilities.BrushMultiPickedState;
import tracevis.visualization.utilities.ColorModel;
import tracevis.visualization.utilities.ColorModelListener;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.utils.UserData;

/**
 * Main visualization component.
 * @author Pieter Deelen
 */
public class ProgramView extends JPanel {
	private final Program program;
	private final BrushMultiPickedState pickedState;
	private final ColorModel colorModel;

	private final StructuralView structuralView;
	private final TimeLine timeLine;

	/**
	 * Creates a new instance of ProgramView.
	 * @param program the program instance to visualize.
	 */
	public ProgramView(Program program) {
		this.program = program;
		program.addListener(new ProgramListenerImpl());

		pickedState = new BrushMultiPickedState();

		colorModel = new ColorModel(0.66f);
		colorModel.addListener(new ColorModelListenerImpl());

		// Place widgets in this panel.
		setLayout(new BorderLayout());

		structuralView = new StructuralView(program, pickedState);
		JTabbedPane tabbedPane = new JTabbedPane();
		InformationPanel informationPanel = new InformationPanel(program, colorModel, pickedState);
		Explorer explorer = new Explorer(program, colorModel, pickedState);
        tabbedPane.addTab("info",informationPanel);
        tabbedPane.addTab("explorer",explorer);

		timeLine = new TimeLine(program, pickedState, colorModel);

		JSplitPane topPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		topPane.setTopComponent(structuralView);
		topPane.setBottomComponent(tabbedPane);
		topPane.setContinuousLayout(true);
		topPane.setOneTouchExpandable(true);
		topPane.setResizeWeight(1);

		JSplitPane mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		mainPane.setTopComponent(topPane);
		mainPane.setBottomComponent(timeLine);
		mainPane.setContinuousLayout(true);
		mainPane.setOneTouchExpandable(true);
		mainPane.setResizeWeight(1);
		add(mainPane);
	}

	private class ProgramListenerImpl implements ProgramListener {
		@Override
		public void traceLoaded() {
			// Reset selections.
			pickedState.clearBrushed();
			pickedState.clearPickedVertices();
			pickedState.clearPickedEdges();

			Graph callGraph = program.getCallGraph();
			// Add vertex data.
			for (Vertex v : (Set<Vertex>)callGraph.getVertices()) {
				VertexData vertexData = new VertexData();
				v.addUserDatum("tracevis.visualization.ProgramView", vertexData, UserData.SHARED);
			}

			// Add edge data.
			for (Edge e : (Set<Edge>)callGraph.getEdges()) {
				EdgeData edgeData = new EdgeData();
				e.addUserDatum("tracevis.visualization.ProgramView", edgeData, UserData.SHARED);
			}

			updateColors();
		}

		private void updateStackEntries() {
			Graph callGraph = program.getCallGraph();
			for (Vertex v : (Set<Vertex>)callGraph.getVertices()) {
				VertexData vertexData = (VertexData)v.getUserDatum("tracevis.visualization.ProgramView");
				vertexData.resetStackEntries();
			}

			for (Edge e : (Set<Edge>)callGraph.getEdges()) {
				EdgeData edgeData = (EdgeData)e.getUserDatum("tracevis.visualization.ProgramView");
				edgeData.resetStackEntries();
			}


			for (long thread : program.getActiveThreads()) {
				CallStack callStack = program.getCallStack(thread);

				Iterator<Frame> callee = callStack.iterator();
				int calleeDepth = 0;

				Iterator<Frame> caller = callStack.iterator();
				if (caller.hasNext()) {
					caller.next();
				}

				while (callee.hasNext()) {
					ClassData calleeClassData = null;
					switch (program.getCallAssignment()) {
						case DEFINING_CLASS:
							calleeClassData = callee.next().getDefiningClass();
							break;
						case OBJECT_CLASS:
							calleeClassData = callee.next().getActualClass();
							break;
					}
					Vertex calleeVertex = calleeClassData.getVertex();

					VertexData calleeData = (VertexData)calleeVertex.getUserDatum("tracevis.visualization.ProgramView");
					calleeData.addStackEntry(thread, calleeDepth);

					if (caller.hasNext()) {
						ClassData callerData = null;
						switch (program.getCallAssignment()) {
							case DEFINING_CLASS:
								callerData = caller.next().getDefiningClass();
								break;
							case OBJECT_CLASS:
								callerData = caller.next().getActualClass();
								break;
						}
						Vertex callerVertex = callerData.getVertex();

						Edge edge = callerVertex.findEdge(calleeVertex);
						if (edge == null) {
							System.out.println(callerVertex + " " + calleeVertex);
							System.out.println(callStack);
						}
						EdgeData edgeData = (EdgeData)edge.getUserDatum("tracevis.visualization.ProgramView");
						edgeData.addStackEntry(thread, calleeDepth);
					}

					calleeDepth += 1;
				}
			}
		}

		@Override
		public void traceFiltered() {
			updateStackEntries();
		}

		@Override
		public void timeChanged() {
			updateStackEntries();
		}

		@Override
		public void methodEntered(ClassData callerData, ClassData calleeData) {}

		@Override
		public void methodExited(ClassData callerData, ClassData calleeData) {}
	}

	private void updateColors() {
		Graph callGraph = program.getCallGraph();
		for (Vertex v : (Set<Vertex>)callGraph.getVertices()) {
			VertexData vertexData = (VertexData)v.getUserDatum("tracevis.visualization.ProgramView");
			ClassData classData = (ClassData)v.getUserDatum("tracevis.model.Program");
			float hue = colorModel.getHue(classData.getName());
			vertexData.setHue(hue);
		}
	}

	private class ColorModelListenerImpl implements ColorModelListener {
		@Override
		public void colorsChanged() {
			updateColors();
			repaint();
		}
	}

	public void recomputeLayout() {
		structuralView.recomputeLayout();
	}

	/*
	 * Settings related methods.
	 */

	public TimeLineSettings getTimeLineSettings() {
		return timeLine.getSettings();
	}

	public void setTimeLineSettings(TimeLineSettings settings) {
		timeLine.setSettings(settings);
	}

	public StructuralView.Settings getStructuralViewSettings() {
		return structuralView.getSettings();
	}

	public void setStructuralViewSettings(StructuralView.Settings settings) {
		structuralView.setSettings(settings);
	}

	public CustomRenderer.Settings getRendererSettings() {
		return structuralView.getRendererSettings();
	}

	public void setRendererSettings(CustomRenderer.Settings settings) {
		structuralView.setRendererSettings(settings);
	}

	public CustomSpringLayout.Settings getLayoutSettings() {
		return structuralView.getLayoutSettings();
	}

	public void setLayoutSettings(CustomSpringLayout.Settings settings) {
		structuralView.setLayoutSettings(settings);
	}

	public VertexSizeSettings getVertexSizeSettings() {
		return structuralView.getVertexSizeSettings();
	}

	public void setVertexSizeSettings(VertexSizeSettings settings) {
		structuralView.setVertexSizeSettings(settings);
	}

	public void loadColorMap(File colorMapFile) throws IOException {
		colorModel.load(colorMapFile);
	}

	public void saveColorMap(File colorMapFile) throws IOException {
		colorModel.save(colorMapFile);
	}
}


