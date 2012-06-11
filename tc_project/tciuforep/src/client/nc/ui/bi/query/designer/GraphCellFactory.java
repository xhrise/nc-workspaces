/*
 * 创建日期 2005-6-8
 *
 */
package nc.ui.bi.query.designer;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import nc.vo.iuforeport.businessquery.JoinCondVO;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

/**
 * @author zjb
 * 
 * 图元创建工厂
 */
public class GraphCellFactory {

	/**
	 * 创建缺省图元
	 */
	public static DefaultGraphCell createCell(Map<DefaultGraphCell, Map<?, ?>> attr, Object cellVO,
			Rectangle2D cellBounds) {

		// Create Vertex
		DefaultGraphCell cell = new DefaultGraphCell(cellVO);

		// Create Vertex attr
		Map cellAttrib = new Hashtable();
		attr.put(cell, cellAttrib);

		// Set bounds
		GraphConstants.setBounds(cellAttrib, cellBounds);

		// Set fill color
		//GraphConstants.setBackground(cellAttrib, Color.orange);
		//GraphConstants.setOpaque(cellAttrib, true);

		// Set raised border
		GraphConstants.setBorderColor(cellAttrib, Color.black);
		//GraphConstants.setBorder(cellAttrib,
		// BorderFactory.createRaisedBevelBorder());

		//zjb test
		//GraphConstants.setIcon(cellAttrib, new ImageIcon("e://1.gif"));

		return cell;
	}

	/**
	 * 创建缺省端口
	 */
	public static DefaultPort createPort(DefaultGraphCell cell) {

		// Add a Port
		DefaultPort port = new DefaultPort();
		cell.add(port);

		return port;
	}

	/**
	 * 创建缺省边
	 */
	public static DefaultEdge createEdge(Map<DefaultEdge, Map<?, ?>> attr, Object cellVO) {

		JoinCondVO jc = (JoinCondVO) cellVO;

		//强制转换，只为博千金一笑
		//		JoinCondDispVO jcd = new JoinCondDispVO();
		//		jcd.setLefttable(jc.getLefttable());
		//		jcd.setRighttable(jc.getRighttable());
		//		jcd.setPrimaryKey(jc.getPrimaryKey());
		//		jcd.setOrdernum(jc.getOrdernum());
		//		jcd.setExpression0(jc.getExpression0());
		//		jcd.setTypeflag(jc.getTypeflag());
		//		jcd.setrelationflag(jc.getRelationflag());
		//		jcd.setLeftfld(jc.getLeftfld());
		//		String rightfld = (jc.getRightfld() == null) ? null :
		// jc.getRightfld()
		//				.toString();
		//		jcd.setRightfld(rightfld);
		//		jcd.setOperator(jc.getOperator());
		//		jcd.setCertain(jc.getCertain());
		//		jcd.setColtype(jc.getColtype());

		// Create Edge
		DefaultEdge edge = new DefaultEdge(jc);

		// Create Edge Attributes
		Map edgeAttrib = new Hashtable();
		attr.put(edge, edgeAttrib);
		// Set Arrow
		int arrow = GraphConstants.ARROW_CLASSIC;
		GraphConstants.setLineEnd(edgeAttrib, arrow);
		GraphConstants.setEndFill(edgeAttrib, true);
		// 设置颜色(内连接-蓝,左连接-红,右连接-绿)
		setEdgeColor(edgeAttrib, jc);

		return edge;
	}

	/**
	 * 获得图元的中心点坐标
	 */
	public static Point2D getCenter(Object cell, JGraph graph) {
		if (cell == null) {
			return null;
		}
		Rectangle2D r = getBounds(cell, null, graph);
		return new Point2D.Double(r.getX() + (r.getWidth() / 2), r.getY()
				+ (r.getHeight() / 2));
	}

	/**
	 * 得到Cell的Bounds边框。 <br>
	 * Gets bounding rectangle of given cell. The rectangle is either current
	 * rectangle of cellView either from propertyMap where are held bounding
	 * rectangles of various cells during multiple resizing and/or translating
	 * of cells.
	 */
	public static Rectangle2D getBounds(Object cell, Map propertyMap,
			JGraph graph) {
		if (propertyMap != null && propertyMap.containsKey(cell)) {
			Map map = (Map) propertyMap.get(cell);
			return GraphConstants.getBounds(map);
		} else {
			CellView view = getView(cell, graph);
			return view.getBounds();
		}
	}

	/**
	 * 获得某个Cell的View对象
	 */
	public static CellView getView(Object cell, JGraph graph) {
		return graph.getGraphLayoutCache().getMapping(cell, false);
	}

	/**
	 * 根据顶点获得端口
	 */
	public static DefaultPort getPort(DefaultGraphCell cell) {
		for (Enumeration e = cell.children(); e.hasMoreElements();) {
			Object child = e.nextElement();
			if (child instanceof DefaultPort) {
				return (DefaultPort) child;
			}
		}
		return null;
	}

	/**
	 * 根据连接类型设置边的颜色
	 */
	public static void setEdgeColor(Map edgeAttrib, JoinCondVO jc) {
		// 设置颜色(内连接-蓝,左连接-红,右连接-绿)
		Color color = null;
		if (jc.getTypeflag().equals("L")) {
			color = Color.RED;
		} else if (jc.getTypeflag().equals("R")) {
			color = Color.GREEN;
		} else {
			color = Color.BLUE;
		}
		GraphConstants.setLineColor(edgeAttrib, color);
	}
}
