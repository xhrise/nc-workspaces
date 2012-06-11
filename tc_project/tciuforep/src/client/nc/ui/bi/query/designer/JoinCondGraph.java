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
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import nc.ui.pub.beans.UIFrame;
import nc.ui.pub.beans.UIPopupMenu;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.iuforeport.businessquery.FromTableVO;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.ddc.datadict.Datadict;

import org.jgraph.JGraph;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.BasicMarqueeHandler;
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
import org.jgraph.graph.Port;
import org.jgraph.graph.PortView;

/**
 * 连接条件设置界面（此类未使用）
 * 
 * @author：朱俊彬
 */
public class JoinCondGraph extends nc.ui.pub.beans.UIPanel implements GraphSelectionListener,
		KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// JGraph instance
	protected JGraph graph;

	// Actions which Change State
	protected Action remove;

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
		frame.getContentPane().add(new JoinCondGraph());
		// Set Default Size
		frame.setSize(520, 390);
		// Show Frame
		frame.show();
	}

	//
	// Editor Panel
	//

	// Construct an Editor Panel
	public JoinCondGraph() {
		// Use Border Layout
		setLayout(new BorderLayout());
		// Construct the Graph
		graph = new MyGraph(new MyModel());

		// Construct Command History
		//

		// Add Listeners to Graph
		//
		// Update ToolBar based on Selection Changes
		graph.getSelectionModel().addGraphSelectionListener(this);
		// Listen for Delete Keystroke when the Graph has Focus
		graph.addKeyListener(this);

		// Construct Panel
		//
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
		Hashtable<DefaultGraphCell, Map<?, ?>> attributes = new Hashtable<DefaultGraphCell, Map<?, ?>>();
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
		Hashtable<DefaultGraphCell, Map<?, ?>> attributes = new Hashtable<DefaultGraphCell, Map<?, ?>>();
		// Associate the Edge with its Attributes
		attributes.put(edge, map);
		// Insert the Edge and its Attributes
		graph.getGraphLayoutCache().insert(new Object[] { edge }, attributes,
				cs, null, null);
		//zjb+
		return edge;
	}

	// Returns the total number of cells in a graph
	protected int getCellCount(JGraph graph) {
		Object[] cells = graph.getDescendants(graph.getRoots());
		return cells.length;
	}

	//
	// Listeners
	//

	// From GraphSelectionListener Interface
	public void valueChanged(GraphSelectionEvent e) {
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
		if (e.getKeyCode() == KeyEvent.VK_DELETE)
			// Execute Remove Action on Delete Key Press
			remove.actionPerformed(null);
	}

	//
	// Custom Graph
	//

	// Defines a Graph that uses the Shift-Button (Instead of the Right
	// Mouse Button, which is Default) to add/remove point to/from an edge.
	public class MyGraph extends JGraph {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

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
		 * 
		 */
		private static final long serialVersionUID = 1L;

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
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

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
	@SuppressWarnings("serial")
	public JPopupMenu createPopupMenu(final Point pt, final Object cell) {
		JPopupMenu menu = new UIPopupMenu();
		if (cell != null && cell instanceof DefaultEdge) {
			// Edit
			menu.add(new AbstractAction("Edit") {
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
			menu.add(new AbstractAction("Remove") {
				public void actionPerformed(ActionEvent e) {
					//获得选中图元
					Object[] selObjs = graph.getSelectionCells();
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
					Set set = DefaultGraphModel.getEdges(getGraph().getModel(),
							new Object[] { cell });
					Object[] cells = set.toArray();
					graph.addSelectionCells(cells);
					//删除
					remove.actionPerformed(e);
				}
			});
		}
		//menu.addSeparator();
		return menu;
	}

	// This will change the source of the actionevent to graph.
	@SuppressWarnings("serial")
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
	 * 绘制表字段，一个字段一个CELL（zjb+）
	 */
	public void setFromTable(FromTableVO ft, int xPoint) {
		//获得字段信息
		String tableId = ft.getTablecode();
		String tableAlias = ft.getTablealias();
		Datadict tree = getOuterPanel().getTabPn().getDatadict();
		SelectFldVO[] sfs = BIModelUtil.getFldsFromTable(tableId, tableAlias,
				tree);
		//插入
		int iLen = (sfs == null) ? 0 : sfs.length;
		for (int i = 0; i < iLen; i++) {
			Point pt = new Point(xPoint, 40 * i + 20);
			insert(pt, sfs[i]);
		}
		//一致性处理
		//getOuterPanel().afterAdd(ft);
	}

	/**
	 * 删除表处理（zjb+）
	 */
	public void doDelete(FromTableVO ft) {
		//一致性处理
		getOuterPanel().afterDelete(ft);
	}

	/**
	 * 手工录入连接条件（zjb+）
	 */
	public void doJoin(DefaultEdge edge) {
		getOuterPanel().doJoin(edge);
	}
}
