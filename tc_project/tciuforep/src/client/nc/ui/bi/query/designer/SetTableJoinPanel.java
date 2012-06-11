/*
 * �������� 2005-6-6
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
 * ��������ģ�����ý���
 */
public class SetTableJoinPanel extends AbstractQueryDesignSetPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0058";//����ģ��

	//GRAPH���
	private TableJoinGraph m_graphEd = null;

	//��������
	private JScrollPane m_sclPn = null;

	//GRAPHӳ��
	private Map m_attr = new Hashtable();

	/**
	 *  
	 */
	public SetTableJoinPanel() {
		super();
		initUI();
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getPanelTitle()
	 */
	public String getPanelTitle() {
		return TAB_TITLE;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getResultFromPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void getResultFromPanel(BIQueryModelDef qmd) {

		//��ñ���
		Object[] objs = getTableJoinGraph();
		FromTableVO[] fts = (FromTableVO[]) objs[0];
		JoinCondVO[] jcs = (JoinCondVO[]) objs[1];
		GraphViewVO[] gvs = (GraphViewVO[]) objs[2];
		//����
		qmd.getQueryBaseVO().setFromTables(fts);
		qmd.getQueryBaseVO().setJoinConds(jcs);
		qmd.setGraphviews(gvs);
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		//test
		//GraphModel model = new DefaultGraphModel();
		//changeGraphModel(model, getGraphViews());
		//getSclPn().setViewportView(new UIGraph(model));

		//������ǰ�Ĳ�ѯ�����������
		FromTableVO[] fts = qmd.getQueryBaseVO().getFromTables();
		int iLenFt = (fts == null) ? 0 : fts.length;
		GraphViewVO[] gvs = qmd.getGraphviews();
		int iLenGv = (gvs == null) ? 0 : gvs.length;
		if (iLenFt != 0 && iLenGv == 0) {
			JoinCondVO[] jcs = qmd.getQueryBaseVO().getJoinConds();
			//���ݱ�����Ӷ��崴������ģ��
			gvs = makeGraphViews(fts, jcs);
			//����
			qmd.setGraphviews(gvs);
		}

		//����UI
		GraphModel model = getGraphEd().getGraph().getModel();
		changeGraphModel(model, qmd.getGraphviews());
		getGraphEd().getGraph().clearSelection();
		getSclPn().setViewportView(getGraphEd());
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#check()
	 */
	public String check() {
		return null;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	private void initUI() {
		try {
			//����
			setLayout(new BorderLayout());
			add(getSclPn(), BorderLayout.CENTER);
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}

	/*
	 * ���� Javadoc��
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
		//����׼��
		Vector<DefaultGraphCell> vecVertex = new Vector<DefaultGraphCell>();
		FromTableVO[] fts = getTabPn().getQueryBaseDef().getFromTables();
		JoinCondVO[] jcs = getTabPn().getQueryBaseDef().getJoinConds();
		//��¼ID�붥��Ĺ�ϣ��
		Hashtable<String, DefaultGraphCell> hashIdCell = new Hashtable<String, DefaultGraphCell>();
		//��¼ID��˿ڵĹ�ϣ��
		Hashtable<String, DefaultPort> hashIdPort = new Hashtable<String, DefaultPort>();

		//�ֱ��¼
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
				//�����ϣ��
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
				//�ҵ���ض˿�
				String keyStart = String.valueOf(gvs[i].getSourceId());
				String keyEnd = String.valueOf(gvs[i].getTargetId());
				if (hashIdCell.containsKey(keyStart)
						&& hashIdCell.containsKey(keyEnd)) {
					//��ö���
					DefaultGraphCell startCell = (DefaultGraphCell) hashIdCell
							.get(keyStart);
					DefaultGraphCell endCell = (DefaultGraphCell) hashIdCell
							.get(keyEnd);
					//��ö˿�
					DefaultPort startPort = (DefaultPort) hashIdPort
							.get(keyStart);
					DefaultPort endPort = (DefaultPort) hashIdPort.get(keyEnd);

					//�۵㴦��
					int[][] iBreakPoints = gvs[i].getBreakPoints();
					int iLenPoint = (iBreakPoints == null) ? 0
							: iBreakPoints.length;
					if (iLenPoint != 0) {
						ArrayList<Point2D> list = new ArrayList<Point2D>();
						//����۵�
						for (int j = 0; j < iLenPoint; j++) {
							Point p = new Point(iBreakPoints[j][0],
									iBreakPoints[j][1]);
							list.add(p);
						}
						//�����۵�
						GraphConstants.setPoints((Map) getAttr().get(edge),
								list);
						//�˵㴦��
						Point2D startP = GraphCellFactory.getCenter(startCell,
								graph);
						Point2D endP = GraphCellFactory.getCenter(endCell,
								graph);
						Point2D p1 = graph.fromScreen(startP);
						Point2D p2 = graph.fromScreen(endP);
						//��ʼ�˵�
						list.add(0, p1);
						//�����˵�
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
	 * ���GRAPH���
	 */
	public TableJoinGraph getGraphEd() {
		if (m_graphEd == null) {
			m_graphEd = new TableJoinGraph();
			m_graphEd.setOuterPanel(this);
		}
		return m_graphEd;
	}

	/*
	 * ���ݱ�����Ӷ��崴������ģ����ͼ
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	private GraphViewVO[] makeGraphViews(FromTableVO[] fts, JoinCondVO[] jcs) {
		int iLenFt = (fts == null) ? 0 : fts.length;
		int iLenJc = (jcs == null) ? 0 : jcs.length;

		GraphViewVO[] gvs = new GraphViewVO[iLenFt + iLenJc];
		if (iLenFt != 0) {

			//����
			double theta = 2 * Math.PI / iLenFt;
			int x0 = 240;
			int y0 = 120;
			int r1 = 240;
			int r2 = 120;
			Hashtable<String, Integer> hashAliasIndex = new Hashtable<String, Integer>();
			for (int i = 0; i < iLenFt; i++) {
				//�ȷ�Բ��
				int x = x0 + (int) (r1 * Math.cos(theta * i));
				int y = y0 - (int) (r2 * Math.sin(theta * i));
				//System.out.println("<" + i + ">: " + x + ", " + y);
				//��������ģ����ͼVO
				gvs[i] = new GraphViewVO(i, i, x, y, 100, 40);
				//��¼����-��Ź�ϣ��
				String key = fts[i].getTablecode() + " as "
						+ fts[i].getTablealias();
				hashAliasIndex.put(key, new Integer(i));
			}

			//��
			for (int i = 0; i < iLenJc; i++) {
				//�������ұ�
				String leftTable = jcs[i].getLefttable();
				String rightTable = jcs[i].getRighttable();
				//���Դ�����
				int iSourceId = -1;
				if (hashAliasIndex.containsKey(leftTable)) {
					iSourceId = ((Integer) hashAliasIndex.get(leftTable))
							.intValue();
				}
				//���Ŀ������
				int iTargetId = -1;
				if (hashAliasIndex.containsKey(rightTable)) {
					iTargetId = ((Integer) hashAliasIndex.get(rightTable))
							.intValue();
				}
				//��������ģ����ͼVO
				gvs[i + iLenFt] = new GraphViewVO(i + iLenFt, i, iSourceId,
						iTargetId, null);
			}
		}

		return gvs;
	}

	/**
	 * @return ���� sclPn��
	 */
	public JScrollPane getSclPn() {
		if (m_sclPn == null) {
			m_sclPn = new UIScrollPane();
		}
		return m_sclPn;
	}

	/**
	 * ִ�в���
	 */
	public FromTableVO inputFromTable() {
		FromTableVO ft = null;
		TableInfoDlg dlg = new TableInfoDlg(this);
		//���������ж��ظ��Ĺ�ϣ��
		//dlg.setHashTableId(getHashTableId());
		dlg.setHashTableId(getHashTableId());
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			//���ѡ�б�
			ft = dlg.getSelTableVO();
			//���Ӵ���
			//doAdd(ft);
		}
		return ft;
	}

	/**
	 * �ֹ�¼����������
	 */
	public void doJoin(DefaultEdge edge) {

		JoinCondVO jcOld = (JoinCondVO) edge.getUserObject();
		//������
		DefaultPort sourcePort = (DefaultPort) edge.getSource();
		DefaultGraphCell sourceCell = (DefaultGraphCell) sourcePort.getParent();
		FromTableVO ftLeft = (FromTableVO) sourceCell.getUserObject();
		//����ұ�
		DefaultPort targetPort = (DefaultPort) edge.getTarget();
		DefaultGraphCell targetCell = (DefaultGraphCell) targetPort.getParent();
		FromTableVO ftRight = (FromTableVO) targetCell.getUserObject();

		//�����Ի���
		JoinCondSetDlg dlg = new JoinCondSetDlg(this);
		dlg.setDataDict(getTabPn().getDatadict());
		dlg.setQueryBaseDef(getTabPn().getQueryBaseDef());
		dlg.setJoinTable(ftLeft, ftRight);
		dlg.setJoindCond(jcOld);
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			//��÷��ؽ��
			JoinCondVO jc = dlg.getJoindCond();
			//���
			String tableLeftCode = ftLeft.getTablecode() + " as "
					+ ftLeft.getTablealias();
			jc.setLefttable(tableLeftCode);
			//����ұ�
			String tableRightCode = ftRight.getTablecode() + " as "
					+ ftRight.getTablealias();
			jc.setRighttable(tableRightCode);
			//��ɫ
			Map edgeAttrib = (Map) getAttr().get(edge);
			if (edgeAttrib != null) {
				GraphCellFactory.setEdgeColor(edgeAttrib, jc);
				getGraphEd().getGraph().repaint();
			}
			//����
			edge.setUserObject(jc);
		}
		return;
	}

	/**
	 * ���ӱ�һ���Դ���
	 */
	public void afterAdd(FromTableVO ft) {
		/* ����Ϊһ���Դ��� */
		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		//�ϲ�VO����
		ValueObject[] vos = BIModelUtil.addToVOs(qbd.getFromTables(), ft);
		FromTableVO[] newfts = new FromTableVO[vos.length];
		for (int i = 0; i < vos.length; i++)
			newfts[i] = (FromTableVO) vos[i];
		qbd.setFromTables(newfts);

		//�����ֶ����ɿ�
		SetColumnPanel columnPanel = ((SetColumnPanel) getTabPn().getSetPanel(
				SetColumnPanel.TAB_TITLE));
		columnPanel.initTree();
		columnPanel.getFldPanel().setFldGenDlgAsNull();
		//��ʼ��where�������Ԫ�༭��
		((SetCondPanel) getTabPn().getSetPanel(SetCondPanel.TAB_TITLE))
				.initEditorValue();
	}

	/**
	 * ɾ����һ���Դ���
	 */
	public void afterDelete(FromTableVO ft) {
		/* ����Ϊһ���Դ��� */
		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		//����ţ���Ч��
		int iSelIndex = -1;
		FromTableVO[] fts = qbd.getFromTables();
		for (int i = 0; i < fts.length; i++) {
			if (fts[i].getTablealias().equals(ft.getTablealias())) {
				iSelIndex = i;
				break;
			}
		}
		if( iSelIndex >=0 ){
			//�ϲ�VO����
			ValueObject[] vos = BIModelUtil.delFromVOs(fts, iSelIndex);
			FromTableVO[] newfts = new FromTableVO[vos.length];
			for (int i = 0; i < vos.length; i++)
				newfts[i] = (FromTableVO) vos[i];
			qbd.setFromTables(newfts);
	
			//�����ֶ����ɿ�
			SetColumnPanel columnPanel = ((SetColumnPanel) getTabPn().getSetPanel(
					SetColumnPanel.TAB_TITLE));
			columnPanel.initTree();
			columnPanel.getFldPanel().setFldGenDlgAsNull();
			//��ʼ��where�����������������Ԫ�༭��
			((SetCondPanel) getTabPn().getSetPanel(SetCondPanel.TAB_TITLE))
					.initEditorValue();
		}
	}

	/**
	 * ͨ��ͼԪö�ٻ�ñ��塢���Ӷ��������ģ��
	 */
	private void enumerateGraph(Vector<FromTableVO> vecFt, Vector<JoinCondVO> vecJc, Vector<GraphViewVO> vecGv) {
		//���ö��
		JGraph graph = getGraphEd().getGraph();
		DefaultGraphModel model = (DefaultGraphModel) graph.getModel();
		Object[] cells = DefaultGraphModel.getRoots(model);
		List list = DefaultGraphModel.getDescendants(model, cells);

		//����׼��
		int iLen = list.size();
		int s = 0;
		int t = 0;
		Hashtable<String, Integer> hashAliasIndex = new Hashtable<String, Integer>();

		//����
		for (int i = 0; i < iLen; i++) {
			Object obj = list.get(i);
			if (!(obj instanceof DefaultEdge) && !(obj instanceof DefaultPort)) {
				DefaultGraphCell cell = (DefaultGraphCell) obj;
				if( cell.getUserObject() instanceof FromTableVO){
					FromTableVO ft = (FromTableVO) cell.getUserObject();
	
					//��ñ߽�
					AttributeMap am = cell.getAttributes();
					Rectangle2D cellBound = (Rectangle2D) am
							.get(GraphConstants.BOUNDS);
					//��������ģ����ͼVO
					GraphViewVO gv = new GraphViewVO();
					gv.setId(s);
					gv.setVoIndex(s);
					gv.setX(cellBound.getX());
					gv.setY(cellBound.getY());
					gv.setWidth(cellBound.getWidth());
					gv.setHeight(cellBound.getHeight());
					//��¼����-��Ź�ϣ��
					String key = ft.getTablecode() + " as " + ft.getTablealias();
					hashAliasIndex.put(key, new Integer(s));
					//�ۼ�
					s++;
					//��¼
					vecGv.addElement(gv);
					vecFt.addElement(ft);
				}
			}
		}

		//��
		for (int i = 0; i < iLen; i++) {
			Object obj = list.get(i);
			if (obj instanceof DefaultEdge) {
				DefaultEdge edge = (DefaultEdge) obj;
				//AttributeMap am = edge.getAttributes();
				JoinCondVO jc = (JoinCondVO) edge.getUserObject();
				if (jc == null) {
					continue;
				}
				//�������ұ�
				String leftTable = jc.getLefttable();
				String rightTable = jc.getRighttable();
				//���Դ�����
				int iSourceId = -1;
				if (hashAliasIndex.containsKey(leftTable)) {
					iSourceId = ((Integer) hashAliasIndex.get(leftTable))
							.intValue();
				}
				//���Ŀ������
				int iTargetId = -1;
				if (hashAliasIndex.containsKey(rightTable)) {
					iTargetId = ((Integer) hashAliasIndex.get(rightTable))
							.intValue();
				}
				//��¼�ϵ�
				CellView cv = graph.getGraphLayoutCache().getMapping(edge,
						false);
				EdgeView ev = (EdgeView) cv;
				int noOfPoints = ev.getPointCount();
				Vector<int[]> vec = new Vector<int[]>();
				//�ߵ������˵㲻��ΪBreakPoint����
				for (int j = 1; j < noOfPoints - 1; j++) {
					Point2D point = ev.getPoint(j);
					int[] iTemp = new int[] { (int) point.getX(),
							(int) point.getY() };
					vec.add(iTemp);
				}
				int[][] iBreakPoints = new int[vec.size()][];
				vec.copyInto(iBreakPoints);
				//��������ģ����ͼVO
				GraphViewVO gv = new GraphViewVO(s, t, iSourceId, iTargetId,
						iBreakPoints);
				//�ۼ�
				s++;
				t++;
				//��¼
				vecGv.addElement(gv);
				vecJc.addElement(jc);
			}
		}
	}

	/**
	 * @return ���� map��
	 */
	public Map getAttr() {
		return m_attr;
	}

	/**
	 * ��ñ߽�
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
	 * ��ñ��塢���Ӷ��������ģ��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getResultFromPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	private Object[] getTableJoinGraph() {

		Vector<FromTableVO> vecFt = new Vector<FromTableVO>();
		Vector<JoinCondVO> vecJc = new Vector<JoinCondVO>();
		Vector<GraphViewVO> vecGv = new Vector<GraphViewVO>();
		//ͨ��ͼԪö�ٻ�ñ��塢���Ӷ��������ģ��
		enumerateGraph(vecFt, vecJc, vecGv);
		//������
		FromTableVO[] fts = new FromTableVO[vecFt.size()];
		vecFt.copyInto(fts);
		JoinCondVO[] jcs = new JoinCondVO[vecJc.size()];
		vecJc.copyInto(jcs);
		GraphViewVO[] gvs = new GraphViewVO[vecGv.size()];
		vecGv.copyInto(gvs);
		//����������������
		jcs = reOrderJoinCond(jcs);
		//��֯����ֵ
		return new Object[] { fts, jcs, gvs };
	}

	/**
	 * �����ж��ظ��Ĺ�ϣ�� �������ڣ�(2003-10-28 9:04:53)
	 */
	public Hashtable getHashTableId() {
		//��ñ���
		Object[] objs = getTableJoinGraph();
		FromTableVO[] fts = (FromTableVO[]) objs[0];
		//
		Hashtable<String, String> hashTableId = new Hashtable<String, String>();
		for (int i = 0; i < fts.length; i++)
			hashTableId.put(fts[i].getTablealias(), fts[i].getTabledisname());
		return hashTableId;
	}

	/**
	 * ˢ�²�ѯ��������
	 */
	public void refreshQbd() {
		//��ñ�����Ӷ���
		Object[] objs = getTableJoinGraph();
		FromTableVO[] fts = (FromTableVO[]) objs[0];
		JoinCondVO[] jcs = (JoinCondVO[]) objs[1];
		//ˢ��
		getTabPn().getQueryBaseDef().setFromTables(fts);
		getTabPn().getQueryBaseDef().setJoinConds(jcs);
	}

	/**
	 * ������������
	 */
	private JoinCondVO[] reOrderJoinCond(JoinCondVO[] jcs) {
		//ͨ���������������Թ������Ա�����˳��
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
		//��������
		if (hashLeftRightJoincond.size() == 0) {
			return jcs;
		}
		//���ö��
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
					//����
					Object objJoincond = hashLeftRightJoincond.get(parentKey
							+ "$" + childKey);
					vec.addElement(objJoincond);
				}
			}
		}
		//���ź���
		JoinCondVO[] newJcs = new JoinCondVO[vec.size()];
		vec.copyInto(newJcs);
		return newJcs;
	}

	/**
	 * ͨ���������������Թ������Ա�����˳�򣬷�������
	 */
	private static DefaultMutableTreeNode changeOrder(JoinCondVO[] jcs,
			Hashtable<String, JoinCondVO> hashLeftRightJoincond) throws IllegalArgumentException,
			Exception {

		DefaultMutableTreeNode root = null;
		int iLen = (jcs == null) ? 0 : jcs.length;
		if (iLen != 0) {
			//��������
			DefaultMutableTreeNode parent = null;
			DefaultMutableTreeNode child = null;
			//�����-���ڵ��ϣ��
			Hashtable<String, DefaultMutableTreeNode> hashTablealiasNode = new Hashtable<String, DefaultMutableTreeNode>();
			//���ƶ��ű����ӵ�һ�ű�Ĺ�ϣ��
			Hashtable<String, String> hashRightLeft = new Hashtable<String, String>();
			try {
				for (int i = 0; i < iLen; i++) {
					//���ڵ�
					String leftTable = jcs[i].getLefttable();
					if (hashTablealiasNode.containsKey(leftTable)) {
						parent = (DefaultMutableTreeNode) hashTablealiasNode
								.get(leftTable);
					} else {
						parent = new DefaultMutableTreeNode(leftTable);
						hashTablealiasNode.put(leftTable, parent);
					}
					//�ұ�ڵ�
					String rightTable = jcs[i].getRighttable();
					if (hashTablealiasNode.containsKey(rightTable)) {
						child = (DefaultMutableTreeNode) hashTablealiasNode
								.get(rightTable);
					} else {
						child = new DefaultMutableTreeNode(rightTable);
						hashTablealiasNode.put(rightTable, child);
					}
					//�������ӹ�ϵ
					parent.add(child);
					//У���Ƿ���ڶ���һ������
					if (hashRightLeft.containsKey(rightTable)) {
						throw new Exception(StringResource.getStringResource(QueryModelException.ERR1010));
					}
					hashRightLeft.put(rightTable, leftTable);
					//��¼���ұ�-����������ϣ��
					hashLeftRightJoincond.put(leftTable + "$" + rightTable,
							jcs[i]);
				}
			} catch (IllegalArgumentException e) {
				throw e;
			}
			//������
			root = new DefaultMutableTreeNode("ROOT");
			for (int i = 0; i < iLen; i++) {
				//���ڵ�
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
	 * ��ʾ����������
	 */
	public void showJoinCondTree() {
		//������Ӷ���
		Object[] objs = getTableJoinGraph();
		JoinCondVO[] jcs = (JoinCondVO[]) objs[1];
		//�������
		DefaultMutableTreeNode root = null;
		try {
			root = changeOrder(jcs, new Hashtable<String, JoinCondVO>());
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		//����
		if (root != null) {
			ShowJoinTreeDlg dlg = new ShowJoinTreeDlg(this);
			dlg.initTree(root);
			dlg.showModal();
			dlg.destroy();
		}
	}
}
