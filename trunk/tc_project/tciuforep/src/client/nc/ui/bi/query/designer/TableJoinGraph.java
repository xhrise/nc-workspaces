package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import javax.swing.tree.DefaultMutableTreeNode;

import nc.ui.pub.beans.UIFrame;
import nc.ui.pub.beans.UIPopupMenu;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.iuforeport.businessquery.FromTableVO;

import org.jgraph.JGraph;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.CellView;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Edge;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphContext;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphUndoManager;
import org.jgraph.graph.ParentMap;
import org.jgraph.graph.Port;
import org.jgraph.graph.PortView;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

public class TableJoinGraph extends nc.ui.pub.beans.UIPanel implements GraphSelectionListener,
		KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// JGraph instance
	protected JGraph graph;

	// Undo Manager
	protected GraphUndoManager undoManager;

	// Actions which Change State
	protected Action undo, redo, remove, group, ungroup, tofront, toback, cut,
			copy, paste;

	// 外部面板实例(zjb+)
	private AbstractQueryDesignSetPanel m_outerPanel;

	// 顶点缺省宽度(zjb+)
	private final static int DEFAULT_WIDTH = 100;

	// 顶点缺省高度(zjb+)
	private final static int DEFAULT_HEIGHT = 40;

	// Main Method
	public static void main(String[] args) {
		// Construct Frame
		JFrame frame = new UIFrame("GraphEd");
		// Set Close Operation to Exit
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Add an Editor Panel
		frame.getContentPane().add(new TableJoinGraph());
		// Fetch URL to Icon Resource
		URL jgraphUrl = TableJoinGraph.class.getClassLoader().getResource(
				"org/jgraph/example/resources/jgraph.gif");
		// If Valid URL
		if (jgraphUrl != null) {
			// Load Icon
			ImageIcon jgraphIcon = new ImageIcon(jgraphUrl);
			// Use in Window
			frame.setIconImage(jgraphIcon.getImage());
		}
		// Set Default Size
		frame.setSize(520, 390);
		// Show Frame
		frame.show();
	}

	//
	// Editor Panel
	//

	// Construct an Editor Panel
	public TableJoinGraph() {
		// Use Border Layout
		setLayout(new BorderLayout());
		// Construct the Graph
		graph = new MyGraph(new MyModel());
		graph.setEditable(false);

		// Construct Command History
		//
		// Create a GraphUndoManager which also Updates the ToolBar
		undoManager = new GraphUndoManager() {
			// Override Superclass
			public void undoableEditHappened(UndoableEditEvent e) {
				// First Invoke Superclass
				super.undoableEditHappened(e);
				// Then Update Undo/Redo Buttons
				updateHistoryButtons();
			}
		};

		// Add Listeners to Graph
		//
		// Register UndoManager with the Model
		graph.getModel().addUndoableEditListener(undoManager);
		// Update ToolBar based on Selection Changes
		graph.getSelectionModel().addGraphSelectionListener(this);
		// Listen for Delete Keystroke when the Graph has Focus
		graph.addKeyListener(this);

		// Construct Panel
		//
		// Add a ToolBar
//		JToolBar toolbar = null;
		/* zyjun remark, 老出错,因为没有图标
		try {
			toolbar = createToolBar();
			toolbar.setVisible(false); //zjb+
			add(toolbar, BorderLayout.NORTH);
		} catch (Throwable e) {
			AppDebug.debug(e);//@devTools System.out.println(e);
		}*/
		// Add the Graph as Center Component
		add(new UIScrollPane(graph), BorderLayout.CENTER);

	}

	// Insert a new Vertex at point
	public void insert(Point2D point, Object obj) {
		// Construct Vertex with no Label
		DefaultGraphCell vertex = new DefaultGraphCell(obj);
		// Add one Floating Port
		vertex.add(new DefaultPort());
		// Create a Map that holds the attributes for the Vertex
		Map map = new Hashtable();
		// Snap the Point to the Grid
		point = graph.snap((Point2D) point.clone());
		// Default Size for the new Vertex
		Dimension size = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		// Add a Bounds Attribute to the Map
		GraphConstants.setBounds(map, new Rectangle2D.Double(point.getX(),
				point.getY(), size.width, size.height));
		// Add a Border Color Attribute to the Map
		GraphConstants.setBorderColor(map, Color.black);
		// Add a White Background
		GraphConstants.setBackground(map, Color.white);
		// Make Vertex Opaque
		GraphConstants.setOpaque(map, true);
		// Construct a Map from cells to Maps (for insert)
		Hashtable attributes = new Hashtable();
		// Associate the Vertex with its Attributes
		attributes.put(vertex, map);
		// Insert the Vertex and its Attributes (can also use model)
		graph.getGraphLayoutCache().insert(new Object[] { vertex }, attributes,
				null, null, null);
	}

	// Insert a new Edge between source and target
	public DefaultEdge connect(Port source, Port target) {
		// Connections that will be inserted into the Model
		ConnectionSet cs = new ConnectionSet();
		// Construct Edge with no label
		DefaultEdge edge = new DefaultEdge();
		// Create Connection between source and target using edge
		cs.connect(edge, source, target);
		// Create a Map thath holds the attributes for the edge
		Map map = new Hashtable();
		// Add a Line End Attribute
		GraphConstants.setLineEnd(map, GraphConstants.ARROW_SIMPLE);
		// Add a label along edge attribute
		GraphConstants.setLabelAlongEdge(map, true);
		// Construct a Map from cells to Maps (for insert)
		Hashtable attributes = new Hashtable();
		// Associate the Edge with its Attributes
		attributes.put(edge, map);
		// Insert the Edge and its Attributes
		graph.getGraphLayoutCache().insert(new Object[] { edge }, attributes,
				cs, null, null);
		//zjb+
		return edge;
	}

	// Create a Group that Contains the Cells
	public void group(Object[] cells) {
		// Order Cells by View Layering
		cells = graph.order(cells);
		// If Any Cells in View
		if (cells != null && cells.length > 0) {
			// Create Group Cell
			int count = getCellCount(graph);
			DefaultGraphCell group = new DefaultGraphCell(
					new Integer(count - 1));
			// Create Change Information
			ParentMap map = new ParentMap();
			// Insert Child Parent Entries
			for (int i = 0; i < cells.length; i++)
				map.addEntry(cells[i], group);
			// Insert into model
			graph.getGraphLayoutCache().insert(new Object[] { group }, null,
					null, map, null);
		}
	}

	// Returns the total number of cells in a graph
	protected int getCellCount(JGraph graph) {
		Object[] cells = graph.getDescendants(graph.getRoots());
		return cells.length;
	}

	// Ungroup the Groups in Cells and Select the Children
	public void ungroup(Object[] cells) {
		// If any Cells
		if (cells != null && cells.length > 0) {
			// List that Holds the Groups
			ArrayList groups = new ArrayList();
			// List that Holds the Children
			ArrayList children = new ArrayList();
			// Loop Cells
			for (int i = 0; i < cells.length; i++) {
				// If Cell is a Group
				if (isGroup(cells[i])) {
					// Add to List of Groups
					groups.add(cells[i]);
					// Loop Children of Cell
					for (int j = 0; j < graph.getModel()
							.getChildCount(cells[i]); j++) {
						// Get Child from Model
						Object child = graph.getModel().getChild(cells[i], j);
						// If Not Port
						if (!(child instanceof Port))
							// Add to Children List
							children.add(child);
					}
				}
			}
			// Remove Groups from Model (Without Children)
			graph.getGraphLayoutCache().remove(groups.toArray());
			// Select Children
			graph.setSelectionCells(children.toArray());
		}
	}

	// Determines if a Cell is a Group
	public boolean isGroup(Object cell) {
		// Map the Cell to its View
		CellView view = graph.getGraphLayoutCache().getMapping(cell, false);
		if (view != null)
			return !view.isLeaf();
		return false;
	}

	// Brings the Specified Cells to Front
	public void toFront(Object[] c) {
		graph.getGraphLayoutCache().toFront(c);
	}

	// Sends the Specified Cells to Back
	public void toBack(Object[] c) {
		graph.getGraphLayoutCache().toBack(c);
	}

	// Undo the last Change to the Model or the View
	public void undo() {
		try {
			undoManager.undo(graph.getGraphLayoutCache());
		} catch (Exception ex) {
			System.err.println(ex);
		} finally {
			updateHistoryButtons();
		}
	}

	// Redo the last Change to the Model or the View
	public void redo() {
		try {
			undoManager.redo(graph.getGraphLayoutCache());
		} catch (Exception ex) {
			System.err.println(ex);
		} finally {
			updateHistoryButtons();
		}
	}

	// Update Undo/Redo Button State based on Undo Manager
	protected void updateHistoryButtons() {
		try {
			// The View Argument Defines the Context
			/* zyjun -
			undo.setEnabled(undoManager.canUndo(graph.getGraphLayoutCache()));
			redo.setEnabled(undoManager.canRedo(graph.getGraphLayoutCache()));
			*/
		} catch (Throwable e) {
			//zjb+
			AppDebug.debug(e);//@devTools System.out.println(e);
		}
	}

	//
	// Listeners
	//

	// From GraphSelectionListener Interface
	public void valueChanged(GraphSelectionEvent e) {
		try {
			// Group Button only Enabled if more than One Cell Selected
			if( group != null ){
				group.setEnabled(graph.getSelectionCount() > 1);
				
				// Update Button States based on Current Selection
				/* zyjun,没有工具条
				boolean enabled = !graph.isSelectionEmpty();
				remove.setEnabled(enabled);
				ungroup.setEnabled(enabled);
				tofront.setEnabled(enabled);
				toback.setEnabled(enabled);
				copy.setEnabled(enabled);
				cut.setEnabled(enabled);
				*/
			}
		} catch (Throwable ex) {
			//zjb+
			AppDebug.debug(ex);//@devTools System.out.println(ex);
		}
	}

	//
	// KeyListener for Delete KeyStroke
	//
	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		// Listen for Delete Key Press
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			// Execute Remove Action on Delete Key Press
//			remove.actionPerformed(null);
			removeSelected();

			return;
		}

		//zjb+
		if (e.getKeyCode() == KeyEvent.VK_F12) {
			//显示连接条件生成树
			getOuterPanel().showJoinCondTree();
		}
	}

	//
	// Custom Graph
	//

	// Defines a Graph that uses the Shift-Button (Instead of the Right
	// Mouse Button, which is Default) to add/remove point to/from an edge.
	public class MyGraph extends JGraph {

		// Construct the Graph using the Model as its Data Source
		public MyGraph(GraphModel model) {
			super(model);
			// Use a Custom Marquee Handler
			setMarqueeHandler(new MyMarqueeHandler());
			// Tell the Graph to Select new Cells upon Insertion
			//setSelectNewCells(true);
			// Make Ports Visible by Default
			setPortsVisible(true);
			// Use the Grid (but don't make it Visible)
			setGridEnabled(true);
			// Set the Grid Size to 10 Pixel
			setGridSize(6);
			// Set the Tolerance to 2 Pixel
			setTolerance(2);
			// Accept edits if click on background
			setInvokesStopCellEditing(true);
		}

		// Override Superclass Method to Return Custom EdgeView
//		protected EdgeView createEdgeView(JGraph graph, CellMapper cm,
//				Object cell) {
//			// Return Custom EdgeView
//			return new EdgeView(cell, graph, cm) {
//
//				/**
//				 * Returns a cell handle for the view.
//				 */
//				public CellHandle getHandle(GraphContext context) {
//					return new MyEdgeHandle(this, context);
//				}
//
//			};
//		}
	}

	//
	// Custom Edge Handle
	//

	// Defines a EdgeHandle that uses the Shift-Button (Instead of the Right
	// Mouse Button, which is Default) to add/remove point to/from an edge.
	public class MyEdgeHandle extends EdgeView.EdgeHandle {

		/**
		 * @param edge
		 * @param ctx
		 */
		public MyEdgeHandle(EdgeView edge, GraphContext ctx) {
			super(edge, ctx);
		}

		// Override Superclass Method
		public boolean isAddPointEvent(MouseEvent event) {
			// Points are Added using Shift-Click
			return event.isShiftDown();
		}

		// Override Superclass Method
		public boolean isRemovePointEvent(MouseEvent event) {
			// Points are Removed using Shift-Click
			return event.isShiftDown();
		}

	}

	//
	// Custom Model
	//

	// A Custom Model that does not allow Self-References
	public static class MyModel extends DefaultGraphModel {
		// Override Superclass Method
		public boolean acceptsSource(Object edge, Object port) {
			// Source only Valid if not Equal Target
			return (((Edge) edge).getTarget() != port);
		}

		// Override Superclass Method
		public boolean acceptsTarget(Object edge, Object port) {
			// Target only Valid if not Equal Source
			return (((Edge) edge).getSource() != port);
		}
	}

	//
	// Custom MarqueeHandler

	// MarqueeHandler that Connects Vertices and Displays PopupMenus
	public class MyMarqueeHandler extends BasicMarqueeHandler {

		// Holds the Start and the Current Point
		protected Point2D start, current;

		// Holds the First and the Current Port
		protected PortView port, firstPort;

		// Override to Gain Control (for PopupMenu and ConnectMode)
		public boolean isForceMarqueeEvent(MouseEvent e) {
			if (e.isShiftDown())
				return false;
			// If Right Mouse Button we want to Display the PopupMenu
			if (SwingUtilities.isRightMouseButton(e))
				// Return Immediately
				return true;
			// Find and Remember Port
			port = getSourcePortAt(e.getPoint());
			// If Port Found and in ConnectMode (=Ports Visible)
			if (port != null && graph.isPortsVisible())
				return true;
			// Else Call Superclass
			return super.isForceMarqueeEvent(e);
		}

		// Display PopupMenu or Remember Start Location and First Port
		public void mousePressed(final MouseEvent e) {
			// If Right Mouse Button
			if (SwingUtilities.isRightMouseButton(e)) {
				// Scale From Screen to Model
//				Point2D loc = graph.fromScreen((Point2D) e.getPoint().clone());
				// Find Cell in Model Coordinates
				Object cell = graph.getFirstCellForLocation(e.getX(), e.getY());
				// Create PopupMenu for the Cell
				JPopupMenu menu = createPopupMenu(e.getPoint(), cell);
				// Display PopupMenu
				menu.show(graph, e.getX(), e.getY());
				// Else if in ConnectMode and Remembered Port is Valid
			} else if (port != null && graph.isPortsVisible()) {
				// Remember Start Location
				start = graph.toScreen(port.getLocation(null));
				// Remember First Port
				firstPort = port;
			} else {
				// Call Superclass
				super.mousePressed(e);
			}
		}

		// Find Port under Mouse and Repaint Connector
		public void mouseDragged(MouseEvent e) {
			// If remembered Start Point is Valid
			if (start != null) {
				// Fetch Graphics from Graph
				Graphics g = graph.getGraphics();
				// Xor-Paint the old Connector (Hide old Connector)
				paintConnector(Color.black, graph.getBackground(), g);
				// Reset Remembered Port
				port = getTargetPortAt(e.getPoint());
				// If Port was found then Point to Port Location
				if (port != null)
					current = graph.toScreen(port.getLocation(null));
				// Else If no Port was found then Point to Mouse Location
				else
					current = graph.snap(e.getPoint());
				// Xor-Paint the new Connector
				paintConnector(graph.getBackground(), Color.black, g);
			}
			// Call Superclass
			super.mouseDragged(e);
		}

		public PortView getSourcePortAt(Point2D point) {
			// Scale from Screen to Model
			Point2D tmp = graph.fromScreen((Point2D) point.clone());
			// Find a Port View in Model Coordinates and Remember
			return graph.getPortViewAt(tmp.getX(), tmp.getY());
		}

		// Find a Cell at point and Return its first Port as a PortView
		protected PortView getTargetPortAt(Point2D point) {
			// Find Cell at point (No scaling needed here)
			Object cell = graph.getFirstCellForLocation(point.getX(), point
					.getY());
			// Loop Children to find PortView
			for (int i = 0; i < graph.getModel().getChildCount(cell); i++) {
				// Get Child from Model
				Object tmp = graph.getModel().getChild(cell, i);
				// Get View for Child using the Graph's View as a Cell Mapper
				tmp = graph.getGraphLayoutCache().getMapping(tmp, false);
				// If Child View is a Port View and not equal to First Port
				if (tmp instanceof PortView && tmp != firstPort)
					// Return as PortView
					return (PortView) tmp;
			}
			// No Port View found
			return getSourcePortAt(point);
		}

		// Connect the First Port and the Current Port in the Graph or Repaint
		public void mouseReleased(MouseEvent e) {
			// If Valid Event, Current and First Port
			if (e != null && port != null && firstPort != null
					&& firstPort != port) {
				// Then Establish Connection
				DefaultEdge edge = connect((Port) firstPort.getCell(),
						(Port) port.getCell());
				// zjb+
				doJoin(edge);
			} else
				// Else Repaint the Graph
				graph.repaint();
			// Reset Global Vars
			firstPort = port = null;
			start = current = null;
			// Call Superclass
			super.mouseReleased(e);
		}

		// Show Special Cursor if Over Port
		public void mouseMoved(MouseEvent e) {
			// Check Mode and Find Port
			if (e != null && getSourcePortAt(e.getPoint()) != null
					&& graph.isPortsVisible()) {
				// Set Cusor on Graph (Automatically Reset)
				graph.setCursor(new Cursor(Cursor.HAND_CURSOR));
				// Consume Event
				// Note: This is to signal the BasicGraphUI's
				// MouseHandle to stop further event processing.
				e.consume();
			} else
				// Call Superclass
				super.mouseMoved(e);
		}

		// Use Xor-Mode on Graphics to Paint Connector
		protected void paintConnector(Color fg, Color bg, Graphics g) {
			// Set Foreground
			g.setColor(fg);
			// Set Xor-Mode Color
			g.setXORMode(bg);
			// Highlight the Current Port
			paintPort(graph.getGraphics());
			// If Valid First Port, Start and Current Point
			if (firstPort != null && start != null && current != null)
				// Then Draw A Line From Start to Current Point
				g.drawLine((int) start.getX(), (int) start.getY(),
						(int) current.getX(), (int) current.getY());
		}

		// Use the Preview Flag to Draw a Highlighted Port
		protected void paintPort(Graphics g) {
			// If Current Port is Valid
			if (port != null) {
				// If Not Floating Port...
				boolean o = (GraphConstants.getOffset(port.getAttributes()) != null);
				// ...Then use Parent's Bounds
				Rectangle2D r = (o) ? port.getBounds() : port.getParentView()
						.getBounds();
				// Scale from Model to Screen
				r = graph.toScreen((Rectangle2D) r.clone());
				// Add Space For the Highlight Border
				r.setFrame(r.getX() - 3, r.getY() - 3, r.getWidth() + 6, r
						.getHeight() + 6);
				// Paint Port in Preview (=Highlight) Mode
				graph.getUI().paintCell(g, port, r, true);
			}
		}

	} // End of Editor.MyMarqueeHandler

	//
	// PopupMenu and ToolBar
	//

	//
	// PopupMenu
	//
	public JPopupMenu createPopupMenu(final Point pt, final Object cell) {
		JPopupMenu menu = new UIPopupMenu();
		if (cell != null && cell instanceof DefaultEdge) {
			// Edit
			menu.add(new AbstractAction(StringResource.getStringResource("miufopublic280")) {//edit
				public void actionPerformed(ActionEvent e) {
					//if (cell instanceof DefaultEdge) {
					doJoin((DefaultEdge) cell);
					//}
					//graph.startEditingAtCell(cell);
				}
			});
		}
		// Remove
		if (!graph.isSelectionEmpty()) {
			menu.addSeparator();
			menu.add(new AbstractAction(StringResource.getStringResource("miufopublic243")) {//remove
				public void actionPerformed(ActionEvent e) {
					removeSelected();
				}
			});
		}
		menu.addSeparator();
		// Insert
		menu.add(new AbstractAction(StringResource.getStringResource("miufo1001633") ){//Insert
			public void actionPerformed(ActionEvent ev) {
				//选择数据表
				FromTableVO ft = inputFromTable();
				//插入
				if (ft != null) {
					doAdd(pt, ft);
				}
			}
		});
		return menu;
	}

	//
	// ToolBar
	//
	public JToolBar createToolBar() {
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);

		// Insert
		URL insertUrl = getClass().getClassLoader().getResource(
				"org/jgraph/example/resources/insert.gif");
		ImageIcon insertIcon = new ImageIcon(insertUrl);
		toolbar.add(new AbstractAction("", insertIcon) {
			public void actionPerformed(ActionEvent e) {
				//选择数据表
				FromTableVO ft = inputFromTable();
				//插入
				doAdd(null, ft);
			}
		});

		// Toggle Connect Mode
		URL connectUrl = getClass().getClassLoader().getResource(
				"org/jgraph/example/resources/connecton.gif");
		ImageIcon connectIcon = new ImageIcon(connectUrl);
		toolbar.add(new AbstractAction("", connectIcon) {
			public void actionPerformed(ActionEvent e) {
				graph.setPortsVisible(!graph.isPortsVisible());
				URL connectUrl;
				if (graph.isPortsVisible())
					connectUrl = getClass().getClassLoader().getResource(
							"org/jgraph/example/resources/connecton.gif");
				else
					connectUrl = getClass().getClassLoader().getResource(
							"org/jgraph/example/resources/connectoff.gif");
				ImageIcon connectIcon = new ImageIcon(connectUrl);
				putValue(SMALL_ICON, connectIcon);
			}
		});

		// Undo
		toolbar.addSeparator();
		URL undoUrl = getClass().getClassLoader().getResource(
				"org/jgraph/example/resources/undo.gif");
		ImageIcon undoIcon = new ImageIcon(undoUrl);
		undo = new AbstractAction("", undoIcon) {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		};
		undo.setEnabled(false);
		toolbar.add(undo);

		// Redo
		URL redoUrl = getClass().getClassLoader().getResource(
				"org/jgraph/example/resources/redo.gif");
		ImageIcon redoIcon = new ImageIcon(redoUrl);
		redo = new AbstractAction("", redoIcon) {
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		};
		redo.setEnabled(false);
		toolbar.add(redo);

		//
		// Edit Block
		//
		toolbar.addSeparator();
		Action action;
		URL url;

		// Copy
		action = javax.swing.TransferHandler // JAVA13:
				// org.jgraph.plaf.basic.TransferHandler
				.getCopyAction();
		url = getClass().getClassLoader().getResource(
				"org/jgraph/example/resources/copy.gif");
		action.putValue(Action.SMALL_ICON, new ImageIcon(url));
		toolbar.add(copy = new EventRedirector(action));

		// Paste
		action = javax.swing.TransferHandler // JAVA13:
				// org.jgraph.plaf.basic.TransferHandler
				.getPasteAction();
		url = getClass().getClassLoader().getResource(
				"org/jgraph/example/resources/paste.gif");
		action.putValue(Action.SMALL_ICON, new ImageIcon(url));
		toolbar.add(paste = new EventRedirector(action));

		// Cut
		action = javax.swing.TransferHandler // JAVA13:
				// org.jgraph.plaf.basic.TransferHandler
				.getCutAction();
		url = getClass().getClassLoader().getResource(
				"org/jgraph/example/resources/cut.gif");
		action.putValue(Action.SMALL_ICON, new ImageIcon(url));
		toolbar.add(cut = new EventRedirector(action));

		// Remove
		URL removeUrl = getClass().getClassLoader().getResource(
				"org/jgraph/example/resources/delete.gif");
		ImageIcon removeIcon = new ImageIcon(removeUrl);
		remove = new AbstractAction("", removeIcon) {
			public void actionPerformed(ActionEvent e) {
				if (!graph.isSelectionEmpty()) {
					Object[] cells = graph.getSelectionCells();
					cells = graph.getDescendants(cells);
					graph.getModel().remove(cells);
				}
			}
		};
		remove.setEnabled(false);
		toolbar.add(remove);

		// Zoom Std
		toolbar.addSeparator();
		URL zoomUrl = getClass().getClassLoader().getResource(
				"org/jgraph/example/resources/zoom.gif");
		ImageIcon zoomIcon = new ImageIcon(zoomUrl);
		toolbar.add(new AbstractAction("", zoomIcon) {
			public void actionPerformed(ActionEvent e) {
				graph.setScale(1.0);
			}
		});
		// Zoom In
		URL zoomInUrl = getClass().getClassLoader().getResource(
				"org/jgraph/example/resources/zoomin.gif");
		ImageIcon zoomInIcon = new ImageIcon(zoomInUrl);
		toolbar.add(new AbstractAction("", zoomInIcon) {
			public void actionPerformed(ActionEvent e) {
				graph.setScale(2 * graph.getScale());
			}
		});
		// Zoom Out
		URL zoomOutUrl = getClass().getClassLoader().getResource(
				"org/jgraph/example/resources/zoomout.gif");
		ImageIcon zoomOutIcon = new ImageIcon(zoomOutUrl);
		toolbar.add(new AbstractAction("", zoomOutIcon) {
			public void actionPerformed(ActionEvent e) {
				graph.setScale(graph.getScale() / 2);
			}
		});

		// Group
		toolbar.addSeparator();
		URL groupUrl = getClass().getClassLoader().getResource(
				"org/jgraph/example/resources/group.gif");
		ImageIcon groupIcon = new ImageIcon(groupUrl);
		group = new AbstractAction("", groupIcon) {
			public void actionPerformed(ActionEvent e) {
				group(graph.getSelectionCells());
			}
		};
		group.setEnabled(false);
		toolbar.add(group);

		// Ungroup
		URL ungroupUrl = getClass().getClassLoader().getResource(
				"org/jgraph/example/resources/ungroup.gif");
		ImageIcon ungroupIcon = new ImageIcon(ungroupUrl);
		ungroup = new AbstractAction("", ungroupIcon) {
			public void actionPerformed(ActionEvent e) {
				ungroup(graph.getSelectionCells());
			}
		};
		ungroup.setEnabled(false);
		toolbar.add(ungroup);

		// To Front
		toolbar.addSeparator();
		URL toFrontUrl = getClass().getClassLoader().getResource(
				"org/jgraph/example/resources/tofront.gif");
		ImageIcon toFrontIcon = new ImageIcon(toFrontUrl);
		tofront = new AbstractAction("", toFrontIcon) {
			public void actionPerformed(ActionEvent e) {
				if (!graph.isSelectionEmpty())
					toFront(graph.getSelectionCells());
			}
		};
		tofront.setEnabled(false);
		toolbar.add(tofront);

		// To Back
		URL toBackUrl = getClass().getClassLoader().getResource(
				"org/jgraph/example/resources/toback.gif");
		ImageIcon toBackIcon = new ImageIcon(toBackUrl);
		toback = new AbstractAction("", toBackIcon) {
			public void actionPerformed(ActionEvent e) {
				if (!graph.isSelectionEmpty())
					toBack(graph.getSelectionCells());
			}
		};
		toback.setEnabled(false);
		toolbar.add(toback);

		return toolbar;
	}

	// This will change the source of the actionevent to graph.
	protected class EventRedirector extends AbstractAction {

		protected Action action;

		// Construct the "Wrapper" Action
		public EventRedirector(Action a) {
			super("", (ImageIcon) a.getValue(Action.SMALL_ICON));
			this.action = a;
		}

		// Redirect the Actionevent
		public void actionPerformed(ActionEvent e) {
			e = new ActionEvent(graph, e.getID(), e.getActionCommand(), e
					.getModifiers());
			action.actionPerformed(e);
		}
	}

	//获得Graph（zjb+）
	public JGraph getGraph() {
		return graph;
	}

	/**
	 * @return 返回 outerPanel (zjb+)
	 */
	public SetTableJoinPanel getOuterPanel() {
		return (SetTableJoinPanel) m_outerPanel;
	}

	/**
	 * @param outerPanel
	 *            要设置的 outerPanel (zjb+)
	 */
	public void setOuterPanel(AbstractQueryDesignSetPanel outerPanel) {
		m_outerPanel = outerPanel;
	}

	/**
	 * 增加表处理（zjb+）
	 */
	public void doAdd(Point pt, FromTableVO ft) {
		int x = 20, y = 20;
		if (pt != null) {
			x = (int) pt.getX();
			y = (int) pt.getY();
		}
		while (true) {
			//判断四角是否存在覆盖
			boolean bLeftUp = (getGraph().getFirstCellForLocation(x + 1, y + 1) == null);
			boolean bRightUp = (getGraph().getFirstCellForLocation(
					x + DEFAULT_WIDTH - 1, y + 1) == null);
			boolean bLeftDown = (getGraph().getFirstCellForLocation(x + 1,
					y + DEFAULT_HEIGHT - 1) == null);
			boolean bRightDown = (getGraph().getFirstCellForLocation(
					x + DEFAULT_WIDTH - 1, y + DEFAULT_HEIGHT - 1) == null);
			if (bLeftUp && bRightUp && bLeftDown && bRightDown) {
				//找到合适位置
				break;
			}
			//侧移
			x += 25;
			y += 25;
		}
		pt = new Point(x, y);
		//插入
		insert(pt, ft);
		//一致性处理
		getOuterPanel().afterAdd(ft);
	}

	/**
	 * 删除表处理（zjb+）
	 */
	public void doDelete(FromTableVO ft) {
		//一致性处理
		getOuterPanel().afterDelete(ft);
	}

	/**
	 * 执行插入（zjb+）
	 */
	public FromTableVO inputFromTable() {
		return getOuterPanel().inputFromTable();
	}

	/**
	 * 手工录入连接条件（zjb+）
	 */
	public void doJoin(DefaultEdge edge) {
		getOuterPanel().doJoin(edge);
	}
	/**
	 * 执行删除
	 *
	 */
	private void removeSelected(){
		//获得选中图元
		Object[] selObjs = graph.getSelectionCells();
		if( selObjs != null ){
			for (int i = 0; i < selObjs.length; i++) {
				Object userObj = ((DefaultMutableTreeNode) selObjs[i])
						.getUserObject();
				if (userObj instanceof FromTableVO) {
					FromTableVO ft = (FromTableVO) userObj;
					if (ft != null) {
						doDelete(ft);
					}
				}
			}
			//加选连带边(为了一起删除)
			Set set = DefaultGraphModel.getEdges(getGraph().getModel(),selObjs);
			Object[] cells = set.toArray();
			graph.addSelectionCells(cells);					
			//删除
			cells = graph.getSelectionCells();
			cells = graph.getDescendants(cells);
			graph.getModel().remove(cells);
		}
	}
}
