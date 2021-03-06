/*
 * TreeView.java
 *
 * Author: Huub van de Wetering
 * Created: April 19, 2006, 1:20 PM
 *
 */

package tracevis.visualization.explorer;

import java.awt.Color;
import java.util.Iterator;

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import tracevis.model.Program;
import tracevis.model.ProgramInterface;
import tracevis.model.types.ClassData;
import tracevis.model.types.ProgramListener;
import tracevis.utilities.treenode.NewTreeNodeEditor;
import tracevis.utilities.treenode.TreeNodeRenderer;
import tracevis.visualization.utilities.BrushMultiPickedState;
import tracevis.visualization.utilities.ColorModel;
import tracevis.visualization.utilities.ColorModelListener;
import edu.uci.ics.jung.graph.ArchetypeEdge;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.visualization.PickEventListener;

/**
 *
 * @author  Huub van de Wetering
 */
public class Explorer extends javax.swing.JPanel {
	private final NewTreeNodeEditor<ExplorerWidget, ExplorerValue> cellEditor;
	private final ProgramInterface program;
	private final ColorModel colorModel;
	private final BrushMultiPickedState pickedState;

    /** Creates new form TreeView */
    public Explorer(Program program, ColorModel colorModel, BrushMultiPickedState pickedState) {
        this.program = program;
        program.addListener(new ProgramListenerImpl());
        this.colorModel = colorModel;
        colorModel.addListener(new ColorModelListenerImpl());
        this.pickedState = pickedState;
        pickedState.addListener(new PickEventListenerImpl());

        initComponents();

        ExplorerWidget editorWidget = new ExplorerWidget(true);
        cellEditor = editorWidget.getCellEditor();
        cellEditor.addCellEditorListener(new CellEditorListenerImpl());

        ExplorerWidget renderWidget = new ExplorerWidget();

        jTree.setModel(null); // throw away the default model cotaining pizza etc.
        jTree.setCellRenderer(new TreeNodeRenderer<ExplorerWidget,ExplorerValue>(renderWidget));
        jTree.setCellEditor(cellEditor);
        jTree.setEditable(true);

        jTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListenerImpl());
    }

    /** adds a node to the tree with the given root.
     *  The added node has path[start] path[start+1] ... as root path
     *  and as color the HSV color (hue,1,1).
     **/


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree = new javax.swing.JTree();

        setLayout(new java.awt.BorderLayout());

        jTree.setExpandsSelectedPaths(false);
        jTree.setRootVisible(false);
        jTree.setShowsRootHandles(true);
        jScrollPane1.setViewportView(jTree);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree jTree;
    // End of variables declaration//GEN-END:variables

    public void addCellEditorListener(CellEditorListener l) {
        cellEditor.addCellEditorListener(l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
        cellEditor.removeCellEditorListener(l);
    }

    private class ProgramListenerImpl implements ProgramListener {
        private  void addToTree(DefaultMutableTreeNode root, Vertex v, String name, String[] path, float hue, int start) {
            if (path.length-start==1) {
                DefaultMutableTreeNode node=new DefaultMutableTreeNode();
                node.setUserObject(new ExplorerValue(v, name+path[start],Color.getHSBColor(hue,1,1),false));
                root.insert(node, root.getChildCount());
            }  else {
                int n=root.getChildCount();
                int i=0;
                DefaultMutableTreeNode found=null;
                while(i<n) {
                    DefaultMutableTreeNode child=(DefaultMutableTreeNode)root.getChildAt(i);
                    String label = ((ExplorerValue)child.getUserObject()).getShortText();
                    if (label.equals(path[start])) {
                        found=child;
                        break;
                    }
                    i=i+1;
                }
                if (found==null) {
                    found=new DefaultMutableTreeNode(path[start]);
                    found.setUserObject(new ExplorerValue(v, name+path[start],Color.getHSBColor(hue,1,1),false));
                    root.insert(found, n);
                }
                addToTree(found, v, name+path[start]+".",path, hue, start+1);
            }
        }

        private void buildTree() {
            // setup new Tree model
            DefaultMutableTreeNode root=new DefaultMutableTreeNode("packages"); // not visible due to settings of jTree
            root.setUserObject(new ExplorerValue(null, "root", Color.green, true));     // should have some value
            DefaultTreeModel treeModel=new DefaultTreeModel(root);              // create new model

            // Iterate over all vertices/classes
            Graph callGraph = program.getCallGraph();
            Iterator<Vertex> vertexIt = callGraph.getVertices().iterator();
            while (vertexIt.hasNext()) {
                Vertex vertex = vertexIt.next();

                // get the data of the class belonging to vertex
                ClassData classData = (ClassData)vertex.getUserDatum("tracevis.model.Program");

                // pick up canonical classname
                String className = classData.getName();
                String[] path = className.split("\\.");

                // Add class name to tree model.
                addToTree(root,vertex, "", path, colorModel.getHue(className), 0);
            }

            // apply tree model
            jTree.setModel(treeModel);
        }

        @Override
		public void traceLoaded() {
            buildTree();
        }

		@Override
		public void traceFiltered() { }

        @Override
		public void timeChanged() { }  // for now explorer is time independent

        @Override
		public void methodEntered(ClassData caller, ClassData callee) { } // no method information in explorer

        @Override
		public void methodExited(ClassData caller, ClassData callee)  { } // no method information in explorer
    }

    private class ColorModelListenerImpl implements ColorModelListener {
        private void updateTreeColors(DefaultMutableTreeNode root, boolean colorRoot) {
            if (colorRoot) {
                ExplorerValue v= (ExplorerValue)root.getUserObject();
                float hue = colorModel.getHue(v.getText());
                v.setColor(Color.getHSBColor(hue,1,1));
            }
            // colour the children of node
            int n = root.getChildCount();
            if (n!=0){
                for(int i=0;i<n;i++) {
                    updateTreeColors((DefaultMutableTreeNode)root.getChildAt(i),true);;
                }
            }
        }
        @Override
		public void colorsChanged() {
            DefaultTreeModel model = (DefaultTreeModel)jTree.getModel();
            DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
            updateTreeColors(root, false);
        }
    }

    private class CellEditorListenerImpl implements CellEditorListener {
        @Override
		public void editingStopped(ChangeEvent e) {
            ExplorerValue value = cellEditor.getCellEditorValue();
            Color c = value.getColor();
            float[] hsb=Color.RGBtoHSB(c.getRed(),c.getGreen(),c.getBlue(),null);
            colorModel.setHue(value.getText(),hsb[0]);         // set color of node
            colorModel.setHue(value.getText()+".*",hsb[0]);    // and all its children, if any
        }

        @Override
		public void editingCanceled(ChangeEvent e) {
            System.err.println("CANCELLED::CellEditorListener::"+(e.getSource().getClass().getName()));
        }
    }

    private class PickEventListenerImpl implements PickEventListener {

        private <Node extends DefaultMutableTreeNode> Node findNode(Node root, String fullName) {
            // colour the children of node
            int n = root.getChildCount();
            for(int i=0;i<n;i++) {
                Node child = (Node)root.getChildAt(i);
                ExplorerValue value = (ExplorerValue)child.getUserObject();
                if (!value.getText().equals(fullName))
				 {
					child = findNode(child,fullName);  // try to find a descendant that matches
				}
                if (child!=null) {
					return child;
				}
            }
            return null; // we return null, if node is not found
        }

        private void pickNode(ArchetypeVertex v, boolean pick) {
            DefaultTreeModel model = (DefaultTreeModel)jTree.getModel();
            DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
            ClassData classData = (ClassData)((Vertex)v).getUserDatum("tracevis.model.Program");
            DefaultMutableTreeNode node = findNode(root, classData.getName());
            if (pick) {
				jTree.getSelectionModel().addSelectionPath(new TreePath(node.getPath()));
			} else {
				jTree.getSelectionModel().removeSelectionPath(new TreePath(node.getPath()));
			}
        }

        @Override
		public void vertexPicked(ArchetypeVertex v)   { pickNode(v,true); }

        @Override
		public void vertexUnpicked(ArchetypeVertex v) { pickNode(v,false); }

        @Override
		public void edgePicked(ArchetypeEdge e) { }

        @Override
		public void edgeUnpicked(ArchetypeEdge e) { }
    }

    private class TreeSelectionListenerImpl implements TreeSelectionListener {
        @Override
		public void valueChanged(TreeSelectionEvent e) {
            TreePath[] paths = e.getPaths();
            for(TreePath path : paths) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
                ExplorerValue value = (ExplorerValue)node.getUserObject();
                pickedState.pick(value.getVertex(),e.isAddedPath(path));
            }
        }
    }
}
