package com.ufida.report.spreedsheet.applet;
import com.ufida.iufo.pub.tools.AppDebug;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nc.itf.iufo.freequery.IMember;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextField;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.bi.query.manager.QueryModelSrv;
import nc.vo.bi.query.manager.QueryModelVO;

import com.ufida.report.adhoc.model.PageDimField;
import com.ufida.report.multidimension.applet.SelDimSetPanel;
import com.ufida.report.multidimension.model.DimMemberCombinationVO;
import com.ufida.report.multidimension.model.IMultiDimConst;
import com.ufida.report.multidimension.model.MultiDimensionUtil;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufida.report.multidimension.model.SelDimensionVO;
import com.ufida.report.spreedsheet.model.SpreadCellPropertyVO;
import com.ufida.report.spreedsheet.model.SpreadSheetModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;

public class SelQueryAreaDlg extends UIDialog implements ActionListener, SelectListener, ChangeListener {// AreaSelectDlg

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private QueryModelVO[] m_queryModels = null;

	private SelQueryAreaInfo m_selInfo = null;

	private JPanel jPanelMain = null;

	private JPanel jPanelButton = null;

	private nc.ui.pub.beans.UITabbedPane jTabbedPane = null;

	private JPanel jPanelQuery = null;

	private JScrollPane jScrollPane = null;

	private JPanel jPanelDimMember = null;

	private JScrollPane jScrollPaneDimMember = null;

	private SelDimSetPanel jPanelSelDim = null;

	private JPanel jPanelCellPos = null;

	private JList jListQuery = null;

	private JButton jButtonPreStep = null;

	private JButton jButtonNextStep = null;

	private JLabel jLabelPosition = null;

	private JTextField jTextFieldPos = null;

	private JTextField jTextFieldSelPos = null;

	private JButton jButtonPosRef = null;

	private JButton jButtonPosFold = null;

	private JLabel jLabelNote = null;

	private JLabel jLabelImage = null;

	private JButton jButtonOK = null;

	private JButton jButtonCancel = null;

	private int m_iLastSelTabIndex = 0;

	private String m_userID = null;

	private CellsModel m_cellsModel = null;

	/**
	 * This method initializes
	 * 
	 */
	public SelQueryAreaDlg(Container cont) {
		super(cont);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setSize(new java.awt.Dimension(531, 500));
		this.setContentPane(getJPanelMain());

	}

	public void setShowParams(QueryModelVO[] queryModels, QueryModelVO selQuery, CellsModel cellsModel,
			CellPosition selPos, String userID,SpreadSheetModel spreadModel) {
		// 根据参数决定显示那个页面及页面内容
		if (queryModels == null || queryModels.length == 0)
			return;
		m_userID = userID;
		m_cellsModel = cellsModel;

		getJListQuery().setListData(queryModels);
		int queryIndex = -1;
		if (selQuery != null) {
			for (int i = 0; i < queryModels.length; i++) {
				if (queryModels[i].getPrimaryKey().equals(selQuery.getPrimaryKey())) {
					queryIndex = i;
					break;
				}
			}
		}
		if (queryIndex >= 0)
			getJListQuery().setSelectedIndex(queryIndex);

		if (selPos != null)
			getJTextFieldPos().setText(selPos.toString());

		if (queryIndex >= 0) {
			SelDimModel dimModel = new SelDimModel(null);
			dimModel.setQueryModel(selQuery);
			PageDimField[] pageDims = spreadModel.getPageDimFields();
			if(pageDims != null && pageDims.length>0){
				setPageDims(pageDims, dimModel);// 对于本查询中引用的已经被设置为模型页维度的纬度，直接将其设置成页维度
			}
			getJPanelSelDim().setSelModel(dimModel, m_userID);
			showPages(1);
		} else {
			showPages(0);
		}
	}

	private void setPageDims(PageDimField[] pageDims, SelDimModel dimMode){
		// 获取查询中的所有字段定义
		MetaDataVO[] metadatas = QueryModelSrv.getSelectFlds(dimMode.getQueryModel().getPrimaryKey());
		if(metadatas == null || metadatas.length == 0)
			return;
		Hashtable<String, MetaDataVO> ht_metadatas = new Hashtable<String, MetaDataVO>();
		for (int i = 0; i < metadatas.length; i++) {
			if(MultiDimensionUtil.isDimMetaData(metadatas[i])){
				// 只处理引用了公共维度的字段
				 ht_metadatas.put(metadatas[i].getPk_dimdef(), metadatas[i]);
			}
		}

		ArrayList<SelDimensionVO> al_pages = new ArrayList<SelDimensionVO>();
		//收集已经设置过的页维度
		for (int i = 0; i < pageDims.length; i++) {
			if(ht_metadatas.containsKey(pageDims[i].getPageDimID())){
				SelDimensionVO selDim = new SelDimensionVO((MetaDataVO)ht_metadatas.get(pageDims[i].getPageDimID()));
				selDim.setAllMembers(pageDims[i].getAllValues());
				selDim.setCurrentMembers(new IMember[]{pageDims[i].getSelectedValue()});
//				selDim.setCanBeMoved(false);
				
				al_pages.add(selDim);
			}
		}
		if(al_pages.size()>0){
			SelDimensionVO[] pages = (SelDimensionVO[])al_pages.toArray(new SelDimensionVO[al_pages.size()]);
			dimMode.setSelDimVOs(IMultiDimConst.POS_PAGE, pages);
		}
		
	}
	private void createSelInfo() {
		if (m_selInfo == null)
			m_selInfo = new SelQueryAreaInfo();

		m_selInfo.setCellPosition(CellPosition.getInstance(getJTextFieldPos().getText()));
		m_selInfo.setSelDimModel(getJPanelSelDim().getSelModel());
		m_selInfo.setSelQuery((QueryModelVO) getJListQuery().getSelectedValue());

	}

	QueryModelVO[] getQueryModels() {
		return m_queryModels;
	}

	public void setQueryModels(QueryModelVO[] models) {
		m_queryModels = models;
	}

	public SelQueryAreaInfo getSelInfo() {
		return m_selInfo;
	}

	/**
	 * This method initializes jPanelMain
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelMain() {
		if (jPanelMain == null) {
			jPanelMain = new UIPanel();
			jPanelMain.setLayout(null);
			jPanelMain.add(getJPanelButton(), null);
			jPanelMain.add(getJTabbedPane(), null);
			jPanelMain.add(getJTextFieldSelPos(), null);
			jPanelMain.add(getJButtonPosFold(), null);
		}
		return jPanelMain;
	}

	/**
	 * This method initializes jPanelButton
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelButton() {
		if (jPanelButton == null) {
			jPanelButton = new UIPanel();
			jPanelButton.setBounds(new java.awt.Rectangle(29, 408, 452, 46));
			jPanelButton.setLayout(null);
			jPanelButton.add(getJButtonPreStep(), null);
			jPanelButton.add(getJButtonNextStep(), null);
			jPanelButton.add(getJButtonOK(), null);
			jPanelButton.add(getJButtonCancel(), null);
		}
		return jPanelButton;
	}

	/**
	 * This method initializes jTabbedPane
	 * 
	 * @return nc.ui.pub.beans.UITabbedPane
	 */
	private nc.ui.pub.beans.UITabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new nc.ui.pub.beans.UITabbedPane();
			jTabbedPane.setBounds(new java.awt.Rectangle(29, 15, 458, 362));
			jTabbedPane.addTab(StringResource.getStringResource("ubispreadsheet0007"), getJPanelQuery());
			jTabbedPane.addTab(StringResource.getStringResource("ubispreadsheet0008"), getJPanelDimMember());
			jTabbedPane.addTab(StringResource.getStringResource("miufopublic476"), getJPanelCellPos());

			jTabbedPane.addChangeListener(this);
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes jPanelQuery
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelQuery() {
		if (jPanelQuery == null) {
			jPanelQuery = new UIPanel();
			jPanelQuery.add(getJScrollPane(), null);
			jPanelQuery.setLayout(null);
		}
		return jPanelQuery;
	}

	private JPanel getJPanelDimMember() {
		if (jPanelDimMember == null) {
			jPanelDimMember = new UIPanel();
			jPanelDimMember.add(getJScrollPaneDimMember(), null);
			jPanelDimMember.setLayout(null);
		}
		return jPanelDimMember;
	}

	/**
	 * This method initializes jPanelSelDim
	 * 
	 * @return javax.swing.JPanel
	 */
	private SelDimSetPanel getJPanelSelDim() {
		if (jPanelSelDim == null) {
			jPanelSelDim = new SelDimSetPanel();
			jPanelSelDim.setPreferredSize(jPanelSelDim.getSize());
		}
		return jPanelSelDim;
	}

	/**
	 * This method initializes jPanelCellPos
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelCellPos() {
		if (jPanelCellPos == null) {
			jLabelImage = new nc.ui.pub.beans.UILabel();
			jLabelImage.setBounds(new java.awt.Rectangle(24, 25, 155, 250));
			jLabelNote = new nc.ui.pub.beans.UILabel();
			jLabelNote.setBounds(new java.awt.Rectangle(197, 235, 216, 27));
			jLabelNote.setText(StringResource.getStringResource("ubispreadsheet0010"));
			jLabelPosition = new nc.ui.pub.beans.UILabel();
			jLabelPosition.setBounds(new java.awt.Rectangle(196, 108, 152, 27));
			jLabelPosition.setText(StringResource.getStringResource("ubispreadsheet0009"));
			jPanelCellPos = new UIPanel();
			jPanelCellPos.setLayout(null);
			jPanelCellPos.add(jLabelPosition, null);
			jPanelCellPos.add(getJTextFieldPos(), null);
			jPanelCellPos.add(getJTextFieldSelPos(), null);
			jPanelCellPos.add(getJButtonPosRef(), null);
			jPanelCellPos.add(getJButtonPosFold(), null);
			jPanelCellPos.add(jLabelNote, null);
			jPanelCellPos.add(jLabelImage, null);
		}
		return jPanelCellPos;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setSize(new java.awt.Dimension(439, 320));
			jScrollPane.setViewportView(getJListQuery());
			jScrollPane.setLocation(new java.awt.Point(5, 7));
		}
		return jScrollPane;
	}

	private JScrollPane getJScrollPaneDimMember() {
		if (jScrollPaneDimMember == null) {
			jScrollPaneDimMember = new UIScrollPane();
			jScrollPaneDimMember.setSize(new java.awt.Dimension(439, 321));
			jScrollPaneDimMember.setViewportView(getJPanelSelDim());
			jScrollPaneDimMember.setLocation(new java.awt.Point(5, 7));
		}
		return jScrollPaneDimMember;
	}

	/**
	 * This method initializes jListQuery
	 * 
	 * @return javax.swing.JList
	 */
	private JList getJListQuery() {
		if (jListQuery == null) {
			jListQuery = new UIList();
			jListQuery.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return jListQuery;
	}

	/**
	 * This method initializes jButtonPreStep
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonPreStep() {
		if (jButtonPreStep == null) {
			jButtonPreStep = new nc.ui.pub.beans.UIButton();
			jButtonPreStep.setBounds(new java.awt.Rectangle(18, 10, 75, 22));
			jButtonPreStep.setText(StringResource.getStringResource("miufopublic260"));
			jButtonPreStep.addActionListener(this);
		}
		return jButtonPreStep;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonNextStep() {
		if (jButtonNextStep == null) {
			jButtonNextStep = new nc.ui.pub.beans.UIButton();
			jButtonNextStep.setBounds(new java.awt.Rectangle(127, 10, 75, 22));
			jButtonNextStep.setText(StringResource.getStringResource("miufopublic261"));
			jButtonNextStep.addActionListener(this);
		}
		return jButtonNextStep;
	}

	/**
	 * This method initializes jTextFieldPos
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldPos() {
		if (jTextFieldPos == null) {
			jTextFieldPos = new UITextField();
			jTextFieldPos.setBounds(new java.awt.Rectangle(196, 145, 124, 28));
		}
		return jTextFieldPos;
	}

	private JTextField getJTextFieldSelPos() {
		if (jTextFieldSelPos == null) {
			jTextFieldSelPos = new UITextField();
			jTextFieldSelPos.setBounds(new java.awt.Rectangle(5, 4, 408, 28));

			jTextFieldSelPos.setVisible(false);
			jTextFieldSelPos.setEditable(false);
		}
		return jTextFieldSelPos;
	}

	/**
	 * This method initializes jButtonPosRef
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonPosRef() {
		if (jButtonPosRef == null) {
			jButtonPosRef = new nc.ui.pub.beans.UIButton();
			jButtonPosRef.setBounds(new java.awt.Rectangle(326, 144, 26, 22));
			jButtonPosRef.addActionListener(this);
		}
		return jButtonPosRef;
	}

	private JButton getJButtonPosFold() {
		if (jButtonPosFold == null) {
			jButtonPosFold = new nc.ui.pub.beans.UIButton();
			jButtonPosFold.setBounds(new java.awt.Rectangle(420, 4, 26, 22));
			jButtonPosFold.addActionListener(this);
			jButtonPosFold.setVisible(false);
		}
		return jButtonPosFold;
	}

	/**
	 * This method initializes jButtonOK
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new nc.ui.pub.beans.UIButton();
			jButtonOK.setBounds(new java.awt.Rectangle(238, 10, 75, 22));
			jButtonOK.setText(StringResource.getStringResource("miufo1000064"));
			jButtonOK.addActionListener(this);
		}
		return jButtonOK;
	}

	/**
	 * This method initializes jButtonCancel
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new nc.ui.pub.beans.UIButton();
			jButtonCancel.setBounds(new java.awt.Rectangle(354, 10, 75, 22));
			jButtonCancel.setText(StringResource.getStringResource("miufo1000274"));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getJButtonPreStep()) {
			int index = getJTabbedPane().getSelectedIndex();
			showPages(index - 1);
		} else if (e.getSource() == getJButtonNextStep()) {
			int index = getJTabbedPane().getSelectedIndex();
			if (index == 0)
				if (!validateSelQuery())
					return;
			if(index == 1)
				if(!validateSelDim())
					return;
			showPages(index + 1);
		} else if (e.getSource() == getJButtonPosRef()) {
			fold(true);

		} else if (e.getSource() == getJButtonPosFold()) {
			fold(false);

		} else if (e.getSource() == getJButtonOK()) {
			int index = getJTabbedPane().getSelectedIndex();
			if(index == 1){
				if(!validateSelDim())
					return;
			}
			createSelInfo();
			boolean isCoverArea = checkIsCoverArea(getSelInfo(), m_cellsModel);
			if (isCoverArea) {
				if (MessageDialog.showYesNoDlg(this, null, StringResource.getStringResource("mbispreadsheet0004")) != ID_YES)
					return;
			}
			closeOK();
		} else if (e.getSource() == getJButtonCancel()) {
			closeCancel();
		}

	}

	/**
	 * 检查查询将要展开的区域是否已经有数据，以便于提示是否覆盖
	 * 
	 * @param selInfo
	 * @param cellsModel
	 * @return
	 */
	private boolean checkIsCoverArea(SelQueryAreaInfo selInfo, CellsModel cellsModel) {
		boolean hasData = false;
		CellPosition pos = selInfo.getCellPosition();

		SelDimModel dimModel = selInfo.getSelDimModel();
		DimMemberCombinationVO[] columns = MultiDimensionUtil.getAllCombination(dimModel
				.getSelDimVOs(IMultiDimConst.POS_COLUMN));
		DimMemberCombinationVO[] rows = MultiDimensionUtil.getAllCombination(dimModel
				.getSelDimVOs(IMultiDimConst.POS_ROW));
		if (columns == null || columns.length == 0 || rows == null || rows.length == 0)
			return hasData;

		int width = rows[0].getMembers().length + columns.length;
		int height = columns[0].getMembers().length + rows.length;
		for (int i = pos.getRow(); i < pos.getRow() + height; i++) {
			for (int j = pos.getColumn(); j < pos.getColumn() + width; j++) {
				Cell cell = cellsModel.getCell(i, j);
				if (cell != null) {
					if (cell.getExtFmt(SpreadCellPropertyVO.KEY_CELL_SPREAD_PROP) != null)
						return true;
				}
			}
		}

		return hasData;
	}

	// 检查是否选择了有效的查询
	private boolean validateSelQuery() {
		if (getJListQuery().getSelectedIndex() >= 0) {
			QueryModelVO selQuery = (QueryModelVO) getJListQuery().getSelectedValue();

			SelDimModel dimModel = getJPanelSelDim().getSelModel();
			if (dimModel != null) {
				String oldQueryID = dimModel.getQueryModel().getPrimaryKey();
				if (oldQueryID.equals(selQuery))// 选中的查询没有变化时，对于原来的维度成员选择不做清除
					return true;
			}

			dimModel = new SelDimModel(null);
			dimModel.setQueryModel(selQuery);
			getJPanelSelDim().setSelModel(dimModel, m_userID);

			return true;
		} else {
			MessageDialog.showHintDlg(this, null, StringResource.getStringResource("mbispreadsheet0001"));
			return false;
		}
	}

	private void showPages(int index) {
		if (index < 0 || index > 2)
			return;// 无效的页面序号
		getJTabbedPane().setSelectedIndex(index);

		m_iLastSelTabIndex = index;

		switch (index) {
		case 0:
			getJButtonPreStep().setVisible(false);
			getJButtonNextStep().setVisible(true);
			getJButtonOK().setVisible(false);
			getJButtonCancel().setVisible(true);
			break;
		case 1:
			getJButtonPreStep().setVisible(true);
			getJButtonNextStep().setVisible(true);
			getJButtonOK().setVisible(true);
			getJButtonCancel().setVisible(true);
			break;
		case 2:
			getJButtonPreStep().setVisible(true);
			getJButtonNextStep().setVisible(false);
			getJButtonOK().setVisible(true);
			getJButtonCancel().setVisible(true);
			break;

		default:
			break;
		}
	}

	/**
	 * 区域参照时，对窗口折叠或恢复。 创建日期：(2001-1-15 9:29:52)
	 * 
	 * @param FoldType
	 *            boolean
	 */
	private void fold(boolean FoldType) {
		// setFold(FoldType);
		if (FoldType) {
			getJTextFieldSelPos().setVisible(true);
			getJButtonPosFold().setVisible(true);

			getJTabbedPane().setVisible(false);
			Rectangle r = getBounds();
			r.height = 65;
			setBounds(r);
			getJTextFieldSelPos().setText(getJTextFieldPos().getText());
		} else {
			getJTextFieldSelPos().setVisible(false);
			getJButtonPosFold().setVisible(false);

			getJTabbedPane().setVisible(true);
			// ivjJLabel1.setVisible(true);
			Rectangle r = getBounds();
			r.height = 500;
			setBounds(r);
			getJTextFieldPos().setText(getJTextFieldSelPos().getText());
			getJTextFieldPos().requestFocus();
		}
	}

	private boolean isNowSelPos() {
		return getJTabbedPane().getSelectedIndex() == 2;
	}

	public void selectedChanged(SelectEvent e) {
		if (e.getProperty() == SelectEvent.ANCHOR_CHANGED) {
			if (isShowing() && isNowSelPos()) {
				CellPosition anchorPos = m_cellsModel.getSelectModel().getAnchorCell();
				String strAreaName = anchorPos.toString();
				// String strAreaName = getViewAreaName(mevt); // 得到视图选中区域

				getJTextFieldSelPos().setText(strAreaName);
			}
		}
	}

	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == getJTabbedPane()) {
			AppDebug.debug("tabbedPane changed");//@devTools System.out.println("tabbedPane changed");
			int iSelIndex = getJTabbedPane().getSelectedIndex();
			if (m_iLastSelTabIndex >= iSelIndex) {// 选中的之前已经显示过的页签
				m_iLastSelTabIndex = iSelIndex;
				showPages(iSelIndex);
				return;
			} else {// 直接选中之后的页签，视同“下一步”的操作
				if (m_iLastSelTabIndex == 0) {
					if (!validateSelQuery()) {
						iSelIndex = m_iLastSelTabIndex;
					}
				}
				if (iSelIndex == 2) {
					if (!validateSelDim()) {
						iSelIndex = m_iLastSelTabIndex;
					}
				}
				showPages(iSelIndex);
			}
		}
	}

	// 检查是否选择了有效的查询
	private boolean validateSelDim() {
		String validMsg = getJPanelSelDim().validateDim();
		if (validMsg != null) {
			MessageDialog.showErrorDlg(this, null, validMsg);
			return false;
		}
		return true;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
