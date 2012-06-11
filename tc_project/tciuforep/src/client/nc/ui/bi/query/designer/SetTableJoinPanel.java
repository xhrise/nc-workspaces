/*
 * 创建日期 2005-6-6
 *
 */
package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.GraphViewVO;
import nc.vo.bi.query.manager.QueryModelException;
import nc.vo.iuforeport.businessquery.FromTableVO;
import nc.vo.iuforeport.businessquery.JoinCondVO;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.pub.ValueObject;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellView;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zjb
 * 
 * 可视连接模型设置界面
 */
public class SetTableJoinPanel extends AbstractQueryDesignSetPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0058";//连接模型

	//GRAPH面板
	private TableJoinGraph m_graphEd = null;

	//滚动窗格
	private JScrollPane m_sclPn = null;

	//GRAPH映射
	private Map m_attr = new Hashtable();

	/**
	 *  
	 */
	public SetTableJoinPanel() {
		super();
		initUI();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getPanelTitle()
	 */
	public String getPanelTitle() {
		return TAB_TITLE;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getResultFromPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void getResultFromPanel(BIQueryModelDef qmd) {

		//获得表定义
		Object[] objs = getTableJoinGraph();
		FromTableVO[] fts = (FromTableVO[]) objs[0];
		JoinCondVO[] jcs = (JoinCondVO[]) objs[1];
		GraphViewVO[] gvs = (GraphViewVO[]) objs[2];
		//回设
		qmd.getQueryBaseVO().setFromTables(fts);
		qmd.getQueryBaseVO().setJoinConds(jcs);
		qmd.setGraphviews(gvs);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		//test
		//GraphModel model = new DefaultGraphModel();
		//changeGraphModel(model, getGraphViews());
		//getSclPn().setViewportView(new UIGraph(model));

		//对于以前的查询定义进行升级
		FromTableVO[] fts = qmd.getQueryBaseVO().getFromTables();
		int iLenFt = (fts == null) ? 0 : fts.length;
		GraphViewVO[] gvs = qmd.getGraphviews();
		int iLenGv = (gvs == null) ? 0 : gvs.length;
		if (iLenFt != 0 && iLenGv == 0) {
			JoinCondVO[] jcs = qmd.getQueryBaseVO().getJoinConds();
			//根据表和连接定义创建连接模型
			gvs = makeGraphViews(fts, jcs);
			//回设
			qmd.setGraphviews(gvs);
		}

		//加入UI
		GraphModel model = getGraphEd().getGraph().getModel();
		changeGraphModel(model, qmd.getGraphviews());
		getGraphEd().getGraph().clearSelection();
		getSclPn().setViewportView(getGraphEd());
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#check()
	 */
	public String check() {
		return null;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	private void initUI() {
		try {
			//加入
			setLayout(new BorderLayout());
			add(getSclPn(), BorderLayout.CENTER);
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	private void changeGraphModel(GraphModel model, GraphViewVO[] gvs) {

		int iLen = (gvs == null) ? 0 : gvs.length;
		if (iLen == 0) {
			return;
		}

		JGraph graph = getGraphEd().getGraph();
		//graph.setPortsVisible(false);
		//变量准备
		Vector<DefaultGraphCell> vecVertex = new Vector<DefaultGraphCell>();
		FromTableVO[] fts = getTabPn().getQueryBaseDef().getFromTables();
		JoinCondVO[] jcs = getTabPn().getQueryBaseDef().getJoinConds();
		//记录ID与顶点的哈希表
		Hashtable<String, DefaultGraphCell> hashIdCell = new Hashtable<String, DefaultGraphCell>();
		//记录ID与端口的哈希表
		Hashtable<String, DefaultPort> hashIdPort = new Hashtable<String, DefaultPort>();

		//分别记录
		for (int i = 0; i < iLen; i++) {
			if (gvs[i].isVertex()) {
				// Create a Cell
				Rectangle2D bound = new Rectangle2D.Double(gvs[i].getX(),
						gvs[i].getY(), gvs[i].getWidth(), gvs[i].getHeight());
				FromTableVO ft = fts[gvs[i].getVoIndex()];
				DefaultGraphCell cell = GraphCellFactory.createCell(getAttr(),
						ft, bound);
				vecVertex.addElement(cell);
				// Add a Port
				DefaultPort port = GraphCellFactory.createPort(cell);
				//记入哈希表
				hashIdCell.put(String.valueOf(gvs[i].getId()), cell);
				hashIdPort.put(String.valueOf(gvs[i].getId()), port);
			}
		}

		// Set Cells
		Object[] cells = new Object[vecVertex.size()];
		vecVertex.copyInto(cells);
		// Insert into Model
		model.insert(cells, getAttr(), null, null, null);

		for (int i = 0; i < iLen; i++) {
			if (!gvs[i].isVertex()) {
				// Create Edge
				JoinCondVO jc = jcs[gvs[i].getVoIndex()];
				DefaultEdge edge = GraphCellFactory.createEdge(getAttr(), jc);
				//找到相关端口
				String keyStart = String.valueOf(gvs[i].getSourceId());
				String keyEnd = String.valueOf(gvs[i].getTargetId());
				if (hashIdCell.containsKey(keyStart)
						&& hashIdCell.containsKey(keyEnd)) {
					//获得顶点
					DefaultGraphCell startCell = (DefaultGraphCell) hashIdCell
							.get(keyStart);
					DefaultGraphCell endCell = (DefaultGraphCell) hashIdCell
							.get(keyEnd);
					//获得端口
					DefaultPort startPort = (DefaultPort) hashIdPort
							.get(keyStart);
					DefaultPort endPort = (DefaultPort) hashIdPort.get(keyEnd);

					//折点处理
					int[][] iBreakPoints = gvs[i].getBreakPoints();
					int iLenPoint = (iBreakPoints == null) ? 0
							: iBreakPoints.length;
					if (iLenPoint != 0) {
						ArrayList<Point2D> list = new ArrayList<Point2D>();
						//获得折点
						for (int j = 0; j < iLenPoint; j++) {
							Point p = new Point(iBreakPoints[j][0],
									iBreakPoints[j][1]);
							list.add(p);
						}
						//绘制折点
						GraphConstants.setPoints((Map) getAttr().get(edge),
								list);
						//端点处理
						Point2D startP = GraphCellFactory.getCenter(startCell,
								graph);
						Point2D endP = GraphCellFactory.getCenter(endCell,
								graph);
						Point2D p1 = graph.fromScreen(startP);
						Point2D p2 = graph.fromScreen(endP);
						//起始端点
						list.add(0, p1);
						//结束端点
						list.add(p2);
					}

					// Connect Edge
					ConnectionSet cs = new ConnectionSet(edge, startPort,
							endPort);
					// Set Cells
					cells = new Object[] { edge };
					// Insert into Model
					model.insert(cells, getAttr(), cs, null, null);
				}
			}
		}
	}

	/*
	 * 获得GRAPH面板
	 */
	public TableJoinGraph getGraphEd() {
		if (m_graphEd == null) {
			m_graphEd = new TableJoinGraph();
			m_graphEd.setOuterPanel(this);
		}
		return m_graphEd;
	}

	/*
	 * 根据表和连接定义创建连接模型视图
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	private GraphViewVO[] makeGraphViews(FromTableVO[] fts, JoinCondVO[] jcs) {
		int iLenFt = (fts == null) ? 0 : fts.length;
		int iLenJc = (jcs == null) ? 0 : jcs.length;

		GraphViewVO[] gvs = new GraphViewVO[iLenFt + iLenJc];
		if (iLenFt != 0) {

			//顶点
			double theta = 2 * Math.PI / iLenFt;
			int x0 = 240;
			int y0 = 120;
			int r1 = 240;
			int r2 = 120;
			Hashtable<String, Integer> hashAliasIndex = new Hashtable<String, Integer>();
			for (int i = 0; i < iLenFt; i++) {
				//等分圆周
				int x = x0 + (int) (r1 * Math.cos(theta * i));
				int y = y0 - (int) (r2 * Math.sin(theta * i));
				//System.out.println("<" + i + ">: " + x + ", " + y);
				//构造连接模型视图VO
				gvs[i] = new GraphViewVO(i, i, x, y, 100, 40);
				//记录别名-序号哈希表
				String key = fts[i].getTablecode() + " as "
						+ fts[i].getTablealias();
				hashAliasIndex.put(key, new Integer(i));
			}

			//边
			for (int i = 0; i < iLenJc; i++) {
				//获得左表右表
				String leftTable = jcs[i].getLefttable();
				String rightTable = jcs[i].getRighttable();
				//获得源表序号
				int iSourceId = -1;
				if (hashAliasIndex.containsKey(leftTable)) {
					iSourceId = ((Integer) hashAliasIndex.get(leftTable))
							.intValue();
				}
				//获得目标表序号
				int iTargetId = -1;
				if (hashAliasIndex.containsKey(rightTable)) {
					iTargetId = ((Integer) hashAliasIndex.get(rightTable))
							.intValue();
				}
				//构造连接模型视图VO
				gvs[i + iLenFt] = new GraphViewVO(i + iLenFt, i, iSourceId,
						iTargetId, null);
			}
		}

		return gvs;
	}

	/**
	 * @return 返回 sclPn。
	 */
	public JScrollPane getSclPn() {
		if (m_sclPn == null) {
			m_sclPn = new UIScrollPane();
		}
		return m_sclPn;
	}

	/**
	 * 执行插入
	 */
	public FromTableVO inputFromTable() {
		FromTableVO ft = null;
		TableInfoDlg dlg = new TableInfoDlg(this);
		//传入用于判断重复的哈希表
		//dlg.setHashTableId(getHashTableId());
		dlg.setHashTableId(getHashTableId());
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			//获得选中表
			ft = dlg.getSelTableVO();
			//增加处理
			//doAdd(ft);
		}
		return ft;
	}

	/**
	 * 手工录入连接条件
	 */
	public void doJoin(DefaultEdge edge) {

		JoinCondVO jcOld = (JoinCondVO) edge.getUserObject();
		//获得左表
		DefaultPort sourcePort = (DefaultPort) edge.getSource();
		DefaultGraphCell sourceCell = (DefaultGraphCell) sourcePort.getParent();
		FromTableVO ftLeft = (FromTableVO) sourceCell.getUserObject();
		//获得右表
		DefaultPort targetPort = (DefaultPort) edge.getTarget();
		DefaultGraphCell targetCell = (DefaultGraphCell) targetPort.getParent();
		FromTableVO ftRight = (FromTableVO) targetCell.getUserObject();

		//弹出对话框
		JoinCondSetDlg dlg = new JoinCondSetDlg(this);
		dlg.setDataDict(getTabPn().getDatadict());
		dlg.setQueryBaseDef(getTabPn().getQueryBaseDef());
		dlg.setJoinTable(ftLeft, ftRight);
		dlg.setJoindCond(jcOld);
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			//获得返回结果
			JoinCondVO jc = dlg.getJoindCond();
			//左表
			String tableLeftCode = ftLeft.getTablecode() + " as "
					+ ftLeft.getTablealias();
			jc.setLefttable(tableLeftCode);
			//获得右表
			String tableRightCode = ftRight.getTablecode() + " as "
					+ ftRight.getTablealias();
			jc.setRighttable(tableRightCode);
			//颜色
			Map edgeAttrib = (Map) getAttr().get(edge);
			if (edgeAttrib != null) {
				GraphCellFactory.setEdgeColor(edgeAttrib, jc);
				getGraphEd().getGraph().repaint();
			}
			//回设
			edge.setUserObject(jc);
		}
		return;
	}

	/**
	 * 增加表一致性处理
	 */
	public void afterAdd(FromTableVO ft) {
		/* 以下为一致性处理 */
		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		//合并VO数组
		ValueObject[] vos = BIModelUtil.addToVOs(qbd.getFromTables(), ft);
		FromTableVO[] newfts = new FromTableVO[vos.length];
		for (int i = 0; i < vos.length; i++)
			newfts[i] = (FromTableVO) vos[i];
		qbd.setFromTables(newfts);

		//更新字段生成框
		SetColumnPanel columnPanel = ((SetColumnPanel) getTabPn().getSetPanel(
				SetColumnPanel.TAB_TITLE));
		columnPanel.initTree();
		columnPanel.getFldPanel().setFldGenDlgAsNull();
		//初始化where条件表格单元编辑器
		((SetCondPanel) getTabPn().getSetPanel(SetCondPanel.TAB_TITLE))
				.initEditorValue();
	}

	/**
	 * 删除表一致性处理
	 */
	public void afterDelete(FromTableVO ft) {
		/* 以下为一致性处理 */
		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		//找序号（低效）
		int iSelIndex = -1;
		FromTableVO[] fts = qbd.getFromTables();
		for (int i = 0; i < fts.length; i++) {
			if (fts[i].getTablealias().equals(ft.getTablealias())) {
				iSelIndex = i;
				break;
			}
		}
		if( iSelIndex >=0 ){
			//合并VO数组
			ValueObject[] vos = BIModelUtil.delFromVOs(fts, iSelIndex);
			FromTableVO[] newfts = new FromTableVO[vos.length];
			for (int i = 0; i < vos.length; i++)
				newfts[i] = (FromTableVO) vos[i];
			qbd.setFromTables(newfts);
	
			//更新字段生成框
			SetColumnPanel columnPanel = ((SetColumnPanel) getTabPn().getSetPanel(
					SetColumnPanel.TAB_TITLE));
			columnPanel.initTree();
			columnPanel.getFldPanel().setFldGenDlgAsNull();
			//初始化where条件和连接条件表格单元编辑器
			((SetCondPanel) getTabPn().getSetPanel(SetCondPanel.TAB_TITLE))
					.initEditorValue();
		}
	}

	/**
	 * 通过图元枚举获得表定义、连接定义和连接模型
	 */
	private void enumerateGraph(Vector<FromTableVO> vecFt, Vector<JoinCondVO> vecJc, Vector<GraphViewVO> vecGv) {
		//获得枚举
		JGraph graph = getGraphEd().getGraph();
		DefaultGraphModel model = (DefaultGraphModel) graph.getModel();
		Object[] cells = DefaultGraphModel.getRoots(model);
		List list = DefaultGraphModel.getDescendants(model, cells);

		//变量准备
		int iLen = list.size();
		int s = 0;
		int t = 0;
		Hashtable<String, Integer> hashAliasIndex = new Hashtable<String, Integer>();

		//顶点
		for (int i = 0; i < iLen; i++) {
			Object obj = list.get(i);
			if (!(obj instanceof DefaultEdge) && !(obj instanceof DefaultPort)) {
				DefaultGraphCell cell = (DefaultGraphCell) obj;
				if( cell.getUserObject() instanceof FromTableVO){
					FromTableVO ft = (FromTableVO) cell.getUserObject();
	
					//获得边界
					AttributeMap am = cell.getAttributes();
					Rectangle2D cellBound = (Rectangle2D) am
							.get(GraphConstants.BOUNDS);
					//构造连接模型视图VO
					GraphViewVO gv = new GraphViewVO();
					gv.setId(s);
					gv.setVoIndex(s);
					gv.setX(cellBound.getX());
					gv.setY(cellBound.getY());
					gv.setWidth(cellBound.getWidth());
					gv.setHeight(cellBound.getHeight());
					//记录别名-序号哈希表
					String key = ft.getTablecode() + " as " + ft.getTablealias();
					hashAliasIndex.put(key, new Integer(s));
					//累加
					s++;
					//记录
					vecGv.addElement(gv);
					vecFt.addElement(ft);
				}
			}
		}

		//边
		for (int i = 0; i < iLen; i++) {
			Object obj = list.get(i);
			if (obj instanceof DefaultEdge) {
				DefaultEdge edge = (DefaultEdge) obj;
				//AttributeMap am = edge.getAttributes();
				JoinCondVO jc = (JoinCondVO) edge.getUserObject();
				if (jc == null) {
					continue;
				}
				//获得左表右表
				String leftTable = jc.getLefttable();
				String rightTable = jc.getRighttable();
				//获得源表序号
				int iSourceId = -1;
				if (hashAliasIndex.containsKey(leftTable)) {
					iSourceId = ((Integer) hashAliasIndex.get(leftTable))
							.intValue();
				}
				//获得目标表序号
				int iTargetId = -1;
				if (hashAliasIndex.containsKey(rightTable)) {
					iTargetId = ((Integer) hashAliasIndex.get(rightTable))
							.intValue();
				}
				//记录断点
				CellView cv = graph.getGraphLayoutCache().getMapping(edge,
						false);
				EdgeView ev = (EdgeView) cv;
				int noOfPoints = ev.getPointCount();
				Vector<int[]> vec = new Vector<int[]>();
				//线的两个端点不作为BreakPoint保存
				for (int j = 1; j < noOfPoints - 1; j++) {
					Point2D point = ev.getPoint(j);
					int[] iTemp = new int[] { (int) point.getX(),
							(int) point.getY() };
					vec.add(iTemp);
				}
				int[][] iBreakPoints = new int[vec.size()][];
				vec.copyInto(iBreakPoints);
				//构造连接模型视图VO
				GraphViewVO gv = new GraphViewVO(s, t, iSourceId, iTargetId,
						iBreakPoints);
				//累加
				s++;
				t++;
				//记录
				vecGv.addElement(gv);
				vecJc.addElement(jc);
			}
		}
	}

	/**
	 * @return 返回 map。
	 */
	public Map getAttr() {
		return m_attr;
	}

	/**
	 * 获得边界
	 */
	public Rectangle2D getBounds(Object cell, Map propertyMap) {
		if (propertyMap != null && propertyMap.containsKey(cell)) {
			Map map = (Map) propertyMap.get(cell);
			return GraphConstants.getBounds(map);
		} else {
			//CellView view = getView(cell);
			//return view.getBounds();
			return null;
		}
	}

	/*
	 * 获得表定义、连接定义和连接模型
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getResultFromPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	private Object[] getTableJoinGraph() {

		Vector<FromTableVO> vecFt = new Vector<FromTableVO>();
		Vector<JoinCondVO> vecJc = new Vector<JoinCondVO>();
		Vector<GraphViewVO> vecGv = new Vector<GraphViewVO>();
		//通过图元枚举获得表定义、连接定义和连接模型
		enumerateGraph(vecFt, vecJc, vecGv);
		//整理结果
		FromTableVO[] fts = new FromTableVO[vecFt.size()];
		vecFt.copyInto(fts);
		JoinCondVO[] jcs = new JoinCondVO[vecJc.size()];
		vecJc.copyInto(jcs);
		GraphViewVO[] gvs = new GraphViewVO[vecGv.size()];
		vecGv.copyInto(gvs);
		//重排连接条件数组
		jcs = reOrderJoinCond(jcs);
		//组织返回值
		return new Object[] { fts, jcs, gvs };
	}

	/**
	 * 用于判断重复的哈希表 创建日期：(2003-10-28 9:04:53)
	 */
	public Hashtable getHashTableId() {
		//获得表定义
		Object[] objs = getTableJoinGraph();
		FromTableVO[] fts = (FromTableVO[]) objs[0];
		//
		Hashtable<String, String> hashTableId = new Hashtable<String, String>();
		for (int i = 0; i < fts.length; i++)
			hashTableId.put(fts[i].getTablealias(), fts[i].getTabledisname());
		return hashTableId;
	}

	/**
	 * 刷新查询基本定义
	 */
	public void refreshQbd() {
		//获得表和连接定义
		Object[] objs = getTableJoinGraph();
		FromTableVO[] fts = (FromTableVO[]) objs[0];
		JoinCondVO[] jcs = (JoinCondVO[]) objs[1];
		//刷新
		getTabPn().getQueryBaseDef().setFromTables(fts);
		getTabPn().getQueryBaseDef().setJoinConds(jcs);
	}

	/**
	 * 重排连接条件
	 */
	private JoinCondVO[] reOrderJoinCond(JoinCondVO[] jcs) {
		//通过连接条件数组试构造树以便重排顺序
		DefaultMutableTreeNode root = null;
		Hashtable<String, JoinCondVO> hashLeftRightJoincond = new Hashtable<String, JoinCondVO>();
		try {
			root = changeOrder(jcs, hashLeftRightJoincond);
		} catch (IllegalArgumentException e) {
			AppDebug.debug(e);//@devTools System.out.println(e);
			MessageDialog.showWarningDlg(this, "UFBI",
					StringResource.getStringResource(QueryModelException.ERR1009));
			return jcs;
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools System.out.println(e);
			MessageDialog.showWarningDlg(this, "UFBI", e.getMessage());
			return jcs;
		}
		//无须重排
		if (hashLeftRightJoincond.size() == 0) {
			return jcs;
		}
		//获得枚举
		Vector<Object> vec = new Vector<Object>();
		Enumeration enums = root.preorderEnumeration();
		while (enums.hasMoreElements()) {
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) enums
					.nextElement();
			if (!parent.isRoot()) {
				String parentKey = parent.getUserObject().toString();
				for (int i = 0; i < parent.getChildCount(); i++) {
					DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent
							.getChildAt(i);
					String childKey = child.getUserObject().toString();
					//查找
					Object objJoincond = hashLeftRightJoincond.get(parentKey
							+ "$" + childKey);
					vec.addElement(objJoincond);
				}
			}
		}
		//重排后结果
		JoinCondVO[] newJcs = new JoinCondVO[vec.size()];
		vec.copyInto(newJcs);
		return newJcs;
	}

	/**
	 * 通过连接条件数组试构造树以便重排顺序，返回树根
	 */
	private static DefaultMutableTreeNode changeOrder(JoinCondVO[] jcs,
			Hashtable<String, JoinCondVO> hashLeftRightJoincond) throws IllegalArgumentException,
			Exception {

		DefaultMutableTreeNode root = null;
		int iLen = (jcs == null) ? 0 : jcs.length;
		if (iLen != 0) {
			//建立树林
			DefaultMutableTreeNode parent = null;
			DefaultMutableTreeNode child = null;
			//表别名-树节点哈希表
			Hashtable<String, DefaultMutableTreeNode> hashTablealiasNode = new Hashtable<String, DefaultMutableTreeNode>();
			//控制多张表连接到一张表的哈希表
			Hashtable<String, String> hashRightLeft = new Hashtable<String, String>();
			try {
				for (int i = 0; i < iLen; i++) {
					//左表节点
					String leftTable = jcs[i].getLefttable();
					if (hashTablealiasNode.containsKey(leftTable)) {
						parent = (DefaultMutableTreeNode) hashTablealiasNode
								.get(leftTable);
					} else {
						parent = new DefaultMutableTreeNode(leftTable);
						hashTablealiasNode.put(leftTable, parent);
					}
					//右表节点
					String rightTable = jcs[i].getRighttable();
					if (hashTablealiasNode.containsKey(rightTable)) {
						child = (DefaultMutableTreeNode) hashTablealiasNode
								.get(rightTable);
					} else {
						child = new DefaultMutableTreeNode(rightTable);
						hashTablealiasNode.put(rightTable, child);
					}
					//建立父子关系
					parent.add(child);
					//校验是否存在多向一的连接
					if (hashRightLeft.containsKey(rightTable)) {
						throw new Exception(StringResource.getStringResource(QueryModelException.ERR1010));
					}
					hashRightLeft.put(rightTable, leftTable);
					//记录左右表-连接条件哈希表
					hashLeftRightJoincond.put(leftTable + "$" + rightTable,
							jcs[i]);
				}
			} catch (IllegalArgumentException e) {
				throw e;
			}
			//建立树
			root = new DefaultMutableTreeNode("ROOT");
			for (int i = 0; i < iLen; i++) {
				//左表节点
				String leftTable = jcs[i].getLefttable();
				parent = (DefaultMutableTreeNode) hashTablealiasNode
						.get(leftTable);
				if (parent.getParent() == null) {
					root.add(parent);
				}
			}
		}
		return root;
	}

	/**
	 * 显示连接条件树
	 */
	public void showJoinCondTree() {
		//获得连接定义
		Object[] objs = getTableJoinGraph();
		JoinCondVO[] jcs = (JoinCondVO[]) objs[1];
		//获得树根
		DefaultMutableTreeNode root = null;
		try {
			root = changeOrder(jcs, new Hashtable<String, JoinCondVO>());
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		//弹框
		if (root != null) {
			ShowJoinTreeDlg dlg = new ShowJoinTreeDlg(this);
			dlg.initTree(root);
			dlg.showModal();
			dlg.destroy();
		}
	}
}
