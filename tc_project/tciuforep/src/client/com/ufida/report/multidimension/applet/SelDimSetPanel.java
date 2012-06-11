/*
 * Created on 2005-6-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.multidimension.applet;

import java.awt.dnd.DnDConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import nc.itf.iufo.freequery.IMember;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.bi.integration.dimension.DimRescource;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.integration.dimension.MeasureVO;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.bi.query.manager.QueryModelSrv;
import nc.vo.bi.query.manager.QueryModelVO;

import com.ufida.report.multidimension.model.IMultiDimConst;
import com.ufida.report.multidimension.model.MultiDimensionUtil;
import com.ufida.report.multidimension.model.MultiReportSrvUtil;
import com.ufida.report.multidimension.model.SelDimMemberVO;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufida.report.multidimension.model.SelDimensionVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.sysplugin.dnd.DndHandler;

/**
 * @author ll
 * 
 * 多维表的维度选择界面
 */
public class SelDimSetPanel extends UIPanel implements ActionListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getJButtonClearAll()) {
			doClearSelDim();
		}else if(e.getSource() == getJButtonUpdate()) {
			doRefreshSelDims();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {

	}

	private JList jList = null;

	private JList jList1 = null;

	private JList jList2 = null;

	private JList jList3 = null;

	private JLabel jLabelUnsel = null;

	private JLabel jLabelPage = null;

	private JLabel jLabelCol = null;

	private JLabel jLabelRow = null;

	private JButton jButtonDeleteAll = null;
	
	private JButton jButtonUpdate = null;

	private JScrollPane jScrollPane = null;

	private JScrollPane jScrollPane1 = null;

	private JScrollPane jScrollPane2 = null;

	private JScrollPane jScrollPane3 = null;

	private SelDimModel m_selModel = null;

	private String m_userID = null;

	// 根据查询构建的指标维度成员
	private ArrayList<MeasureVO> m_alMeasureMembers = null;

	// 成员选择对话框
	private SelMemberDialog m_selMemberDlg = null;

	/**
	 * This method initializes
	 * 
	 */
	public SelDimSetPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		jLabelRow = new nc.ui.pub.beans.UILabel();
		jLabelCol = new nc.ui.pub.beans.UILabel();
		jLabelPage = new nc.ui.pub.beans.UILabel();
		jLabelUnsel = new nc.ui.pub.beans.UILabel();
		this.setSize(495, 400);
		this.setLayout(null);
		jLabelUnsel.setBounds(24, 17, 101, 26);
		jLabelUnsel.setText(StringResource.getStringResource("ubimultidim0003"));
		jLabelPage.setBounds(218, 17, 101, 26);
		jLabelPage.setText(StringResource.getStringResource("ubimultidim0004"));
		jLabelCol.setBounds(330, 132, 101, 26);
		jLabelCol.setText(StringResource.getStringResource("ubimultidim0005"));
		jLabelRow.setBounds(210, 223, 101, 26);
		jLabelRow.setText(StringResource.getStringResource("ubimultidim0006"));
		this.add(jLabelUnsel, null);
		this.add(jLabelPage, null);
		this.add(jLabelCol, null);
		this.add(jLabelRow, null);
		this.add(getJButtonClearAll(), null);
		this.add(getJButtonUpdate(), null);
		this.add(getJScrollPane(), null);
		this.add(getJScrollPane1(), null);
		this.add(getJScrollPane2(), null);
		this.add(getJScrollPane3(), null);

		JList list = getJListCol();
		DndHandler.enableDndDrag(list, new SelDimDndAdapter(list, this), DnDConstants.ACTION_MOVE);
		DndHandler.enableDndDrop(list, new SelDimDndAdapter(list, this));
		list = getJListRow();
		DndHandler.enableDndDrag(list, new SelDimDndAdapter(list, this), DnDConstants.ACTION_MOVE);
		DndHandler.enableDndDrop(list, new SelDimDndAdapter(list, this));
		list = getJListPage();
		DndHandler.enableDndDrag(list, new SelDimDndAdapter(list, this), DnDConstants.ACTION_MOVE);
		DndHandler.enableDndDrop(list, new SelDimDndAdapter(list, this));
		list = getJListUnsel();
		DndHandler.enableDndDrag(list, new SelDimDndAdapter(list, this), DnDConstants.ACTION_MOVE);
		DndHandler.enableDndDrop(list, new SelDimDndAdapter(list, this));

		this.addMouseListener(this);
	}

	/**
	 * This method initializes jList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getJListUnsel() {
		if (jList == null) {
			jList = new UIList();
			// jList.setPreferredSize(new Dimension(200, 100));
		}
		return jList;
	}

	/**
	 * This method initializes jList1
	 * 
	 * @return javax.swing.JList
	 */
	private JList getJListPage() {
		if (jList1 == null) {
			jList1 = new UIList();

			// 用name来约定不能放指标维度
			jList1.setName(StringResource.getStringResource("ubimultidim0004"));
			// jList1.setPreferredSize(new Dimension(200, 100));
		}
		return jList1;
	}

	/**
	 * This method initializes jList2
	 * 
	 * @return javax.swing.JList
	 */
	private JList getJListCol() {
		if (jList2 == null) {
			jList2 = new UIList();
			// jList2.setPreferredSize(new Dimension(200, 100));
		}
		return jList2;
	}

	/**
	 * This method initializes jList3
	 * 
	 * @return javax.swing.JList
	 */
	private JList getJListRow() {
		if (jList3 == null) {
			jList3 = new UIList();
			// jList3.setPreferredSize(new Dimension(200, 100));
		}
		return jList3;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getJButtonClearAll() {
		if (jButtonDeleteAll == null) {
			jButtonDeleteAll = new nc.ui.pub.beans.UIButton();
			jButtonDeleteAll.setBounds(23, 362, 72, 22);
			jButtonDeleteAll.setText(StringResource.getStringResource("ubimultidim0017"));
			jButtonDeleteAll.addActionListener(this);
		}
		return jButtonDeleteAll;
	}
	
	public JButton getJButtonUpdate() {
		if (jButtonUpdate == null) {
			jButtonUpdate = new nc.ui.pub.beans.UIButton();
			jButtonUpdate.setBounds(105, 362, 72, 22);
			jButtonUpdate.setText(StringResource.getStringResource("ubimultidim0065"));
			jButtonUpdate.addActionListener(this);
		}
		return jButtonUpdate;
	}
	
	

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setBounds(23, 49, 160, 305);
			jScrollPane.setViewportView(getJListUnsel());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new UIScrollPane();
			jScrollPane1.setBounds(217, 49, 266, 81);
			jScrollPane1.setViewportView(getJListPage());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes jScrollPane2
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane2() {
		if (jScrollPane2 == null) {
			jScrollPane2 = new UIScrollPane();
			jScrollPane2.setBounds(330, 161, 153, 93);
			jScrollPane2.setViewportView(getJListCol());
		}
		return jScrollPane2;
	}

	/**
	 * This method initializes jScrollPane3
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane3() {
		if (jScrollPane3 == null) {
			jScrollPane3 = new UIScrollPane();
			jScrollPane3.setBounds(210, 258, 120, 96);
			jScrollPane3.setViewportView(getJListRow());
		}
		return jScrollPane3;
	}

	/**
	 * 设置维度选择界面
	 * 
	 */
	public void setSelModel(SelDimModel selModel, String userID) {
		m_selModel = selModel;
		m_userID = userID;
		
		//判断维度选择模型是否为空，认为如果行列维度没有定义，则认为模型为空
		boolean   bUndefined = true;
		for( int pos = IMultiDimConst.POS_ROW; pos <= IMultiDimConst.POS_UNSEL; pos++){
			if( m_selModel.getSelDimVOs(pos) != null ){
				bUndefined = false;
				break;
			}
		}
		if( bUndefined ){
			m_alMeasureMembers = MultiDimensionUtil.processMetaData(m_selModel);
		}else{
			m_alMeasureMembers = getAllMeasureMembers(m_selModel);
		}
		setListDatas(m_selModel);

	}
	private void setListDatas(SelDimModel selModel){
		SelDimensionVO[] unselDims = selModel.getSelDimVOs(IMultiDimConst.POS_UNSEL);
		getJListUnsel().setModel(createListModel(unselDims));

		SelDimensionVO[] pageDims = selModel.getSelDimVOs(IMultiDimConst.POS_PAGE);
		getJListPage().setModel(createListModel(pageDims));
		SelDimensionVO[] colDims = selModel.getSelDimVOs(IMultiDimConst.POS_COLUMN);
		getJListCol().setModel(createListModel(colDims));
		SelDimensionVO[] rowDims = selModel.getSelDimVOs(IMultiDimConst.POS_ROW);
		getJListRow().setModel(createListModel(rowDims));

	}

	public SelDimModel getSelModel() {
		return m_selModel;
	}
	private void getSelModelFromUI(){
		if (m_selModel == null)
			return;
		
		m_selModel.setSelDimVOs(IMultiDimConst.POS_PAGE, getListData(getJListPage()));
		m_selModel.setSelDimVOs(IMultiDimConst.POS_COLUMN, getListData(getJListCol()));
		m_selModel.setSelDimVOs(IMultiDimConst.POS_ROW, getListData(getJListRow()));
		m_selModel.setSelDimVOs(IMultiDimConst.POS_UNSEL, getListData(getJListUnsel()));

	}

	private DefaultListModel createListModel(SelDimensionVO[] datas) {
		DefaultListModel lModel = new DefaultListModel();
		if (datas != null) {
			for (int i = 0; i < datas.length; i++) {
				lModel.addElement(datas[i]);
			}
		}
		return lModel;
	}

	private SelDimensionVO[] getListData(JList list) {
		int count = list.getModel().getSize();
		if (count == 0)
			return null;
		SelDimensionVO[] dims = new SelDimensionVO[count];

		for (int i = 0; i < count; i++) {
			dims[i] = (SelDimensionVO) list.getModel().getElementAt(i);
		}
		return dims;
	}

	public void addMouseListener(MouseListener listener) {
		getJListUnsel().addMouseListener(listener);
		getJListPage().addMouseListener(listener);
		getJListCol().addMouseListener(listener);
		getJListRow().addMouseListener(listener);
	}

	private void doClearSelDim() {
		Vector<SelDimensionVO> 			vec = new Vector<SelDimensionVO>();
		SelDimModel		model = new SelDimModel(null);
		model.setQueryModel(getSelModel().getQueryModel());
		for (int i = 0; i < SelDimModel.DIMTYPE_COUNT; i++) {
			SelDimensionVO[] dims = getSelModel().getSelDimVOs(i);
			if (dims != null) {
				for (int j = 0; j < dims.length; j++) {
					vec.addElement(dims[j]);
				}
			}
		}
		SelDimensionVO[] allDims = new SelDimensionVO[vec.size()];
		vec.copyInto(allDims);
		model.setSelDimVOs(IMultiDimConst.POS_UNSEL, allDims);
		setListDatas(model);
	}
	
	private void doRefreshSelDims(){
		//更新查询设置
		String			strQueryID = m_selModel.getQueryModel().getPrimaryKey();
		QueryModelVO	queryVO = QueryModelSrv.getByIDs(new String[]{strQueryID})[0];
		if( queryVO != null ){
			//更新选择模型
			getSelModel().setQueryModel(queryVO);
			SelDimModel		selModel = new SelDimModel(null);
			selModel.setQueryModel(queryVO);
			getSelModel().setQueryModel(queryVO);
			
			selModel.setSelDimVOs(IMultiDimConst.POS_PAGE, getListData(getJListPage()));
			selModel.setSelDimVOs(IMultiDimConst.POS_COLUMN, getListData(getJListCol()));
			selModel.setSelDimVOs(IMultiDimConst.POS_ROW, getListData(getJListRow()));
			selModel.setSelDimVOs(IMultiDimConst.POS_UNSEL, getListData(getJListUnsel()));
			
			m_alMeasureMembers = MultiDimensionUtil.processMetaData(selModel);
			setListDatas(selModel);
			
		}
	}

	boolean doSelMember(SelDimensionVO seldimVO) {
		IMember[] allMembers = getMembers(seldimVO.getDimDef(), m_userID);
		getSelMemberDlg().setParams(allMembers, seldimVO.getSelMembers());

		if (getSelMemberDlg().showModal() == UIDialog.ID_OK) {
			SelDimMemberVO[] selmembers = getSelMemberDlg().getSelMembers();
			seldimVO.setSelMembers(selmembers);
		}
		return !(seldimVO.getSelMembers() == null);

	}

	private SelMemberDialog getSelMemberDlg() {
		if (m_selMemberDlg == null) {
			m_selMemberDlg = new SelMemberDialog(this);
		}
		return m_selMemberDlg;
	}

	/**
	 * 维度所有成员（参与成员选择）
	 * 
	 * @param pk_dim
	 * @return
	 */
	private IMember[] getMembers(DimensionVO dimdefVO, String userID) {
		// 指标维度
		if (dimdefVO.getType().intValue() == DimRescource.INT_DIM_MEASURE) {
			IMember[]	members = new IMember[m_alMeasureMembers.size()];
			m_alMeasureMembers.toArray(members);
			return members;
		}
		// 其他维度
		else {
			IMember[] members = (IMember[]) MultiReportSrvUtil.getAllMember(dimdefVO, userID);
			return members;
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() >= 2) {
			if (e.getSource() instanceof JList) {
				Object data = ((JList) e.getSource()).getSelectedValue();
				if (data != null && data instanceof SelDimensionVO) {
					// 维度成员的选择处理
					SelDimensionVO seldimVO = (SelDimensionVO) data;
					doSelMember(seldimVO);
				}
			}

		}

	}

	/**
	 * 确定时进行数据的合法性校验
	 * 需要检验维度的成员是否选择
	 * 
	 * @return
	 * @i18n mbimulti00019={0}没有选择成员
	 */
	public String validateDim() {
		getSelModelFromUI();
		if (getSelModel().getSelDimVOs(IMultiDimConst.POS_COLUMN) == null
				|| getSelModel().getSelDimVOs(IMultiDimConst.POS_ROW) == null) {
			return StringResource.getStringResource("mbimultidim0007");
		}
		SelDimensionVO[] unsel = getSelModel().getSelDimVOs(IMultiDimConst.POS_UNSEL);
		if (unsel != null) {
			for (int i = 0; i < unsel.length; i++) {
				if (unsel[i].getDimDef().getPrimaryKey().equals(IMultiDimConst.PK_MEASURE_DIMDEF))
					return StringResource.getStringResource("mbimultidim0008");
			}
		}
		
		for( int pos = IMultiDimConst.POS_PAGE; pos <=IMultiDimConst.POS_COLUMN; pos++ ){
			SelDimensionVO[]	dimVOs = getSelModel().getSelDimVOs(pos);
			if( dimVOs != null ){
				for( int i=0; i<dimVOs.length; i++ ){
					if( dimVOs[i].getSelMembers() == null ){
						return StringResource.getStringResource(StringResource.getStringResource("mbimulti00019"), new String[]{dimVOs[i].getShowName()});
					}
				}
			}
		}
		return null;
	}
	
	private  ArrayList<MeasureVO>	getAllMeasureMembers(SelDimModel model){
		ArrayList<MeasureVO> al_measureMember = new ArrayList<MeasureVO>();

		// 获取查询中的所有字段定义
		MetaDataVO[] metadatas = QueryModelSrv.getSelectFlds(model.getQueryModel().getPrimaryKey());
		for (int i = 0; i < metadatas.length; i++) {
			if (!metadatas[i].getDimflag()) {//是指标
				al_measureMember.add(new MeasureVO(metadatas[i]));
			}
		}
		// 增加指标成员的根节点
		al_measureMember.add(new MeasureVO(null));
		return al_measureMember;

	}

} // @jve:decl-index=0:visual-constraint="21,13"
 