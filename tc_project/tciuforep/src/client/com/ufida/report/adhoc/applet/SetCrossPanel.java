package com.ufida.report.adhoc.applet;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nc.ui.pub.beans.UICheckBoxMenuItem;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIPopupMenu;

import com.ufida.report.adhoc.model.AdhocCrossTableSet;
import com.ufida.report.adhoc.model.AdhocModel;
import com.ufida.report.adhoc.model.IFldCountType;
import com.ufida.report.rep.model.DefaultReportField;
import com.ufsoft.iufo.data.IMetaData;
import com.ufsoft.iufo.fmtplugin.freequery.UniqueList;
import com.ufsoft.iufo.resource.StringResource;

public class SetCrossPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JScrollPane jScrollPaneRowDims = null;

	private UniqueList jListRowDims = null;

	private JPanel jPanelMeasure = null;

	private JLabel jLabelMeasure = null;

	private JScrollPane jScrollPaneMeasures = null;

	private UniqueList jListMeasures = null;

	private JPanel jPanelColDim = null;

	private JLabel jLabelColDim = null;

	private JScrollPane jScrollPaneColDims = null;

	private UniqueList jListColDims = null;

	private JPanel jPanelExpand = null;

	private JCheckBox jCheckBoxHorSum = null;

	private JCheckBox jCheckBoxVerSum = null;

	private JCheckBox jCheckBoxHorExpand = null;

//	private JCheckBox jCheckBoxVerExpand = null;

	private JPanel jPanelRowDim = null;

	private JLabel jLabelRowDim = null;

	private JPanel jPanelSumFlag = null;

	private AdhocCrossTableSet m_crossInfo = null;

	private JMenuItem[] m_measMenuItems = null;

	private JMenuItem[] m_dimRowMenuItems = null;
	private JMenuItem[] m_dimColMenuItems = null;
	private int[] m_measMenuIndex = null;

	private int[] m_dimMenuIndex = null;

	private UIPopupMenu m_popupMenu = null;

	public static final int MENU_TYPE_MEASURE = 0;
	public static final int MENU_TYPE_ROW = 1;
	public static final int MENU_TYPE_COL = 2;
//	private static final int MENU_TYPE_DIMENSION = 1;
    /** 在成员设置固定成员加载数据的时候需要该模型*/
	private AdhocModel m_adhocModel = null;
	/**
	 * This method initializes
	 * 
	 */
	public SetCrossPanel() {
		super();
		initialize();
	}

	public SetCrossPanel(AdhocModel model) {
		this();
		m_adhocModel=model;
	}
	
	private AdhocModel getAdhocModel() {
		return m_adhocModel;
	}
	
	public void setAdhocModel(AdhocModel model) {
		m_adhocModel = model;
	}
	/**
	 * This method initializes this
	 * 
	 */

	private void initialize() {
		this.setLayout(new BorderLayout());
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new GridLayout(2,2));
		centerPanel.add(new JPanel());
		centerPanel.add(getJPanelColDim());
		centerPanel.add(getJPanelRowDim());
		centerPanel.add(getJPanelMeasure());
//		this.add(getJPanelColDim(), BorderLayout.NORTH);
//		this.add(getJPanelMeasure(), BorderLayout.CENTER);
//		this.add(getJPanelExpand(), BorderLayout.EAST);
//		this.add(getJPanelRowDim(), BorderLayout.WEST);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(getJPanelSumFlag(), BorderLayout.SOUTH);
//		setSize(580, 454);
	}

	/**
	 * This method initializes jScrollPaneRowDims
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneRowDims() {
		if (jScrollPaneRowDims == null) {
			jScrollPaneRowDims = new JScrollPane();
			jScrollPaneRowDims.setViewportView(getJListRowDims());
		}
		return jScrollPaneRowDims;
	}

	/**
	 * This method initializes jListRowDims
	 * 
	 * @return javax.swing.JList
	 */
	UniqueList getJListRowDims() {
		if (jListRowDims == null) {
			jListRowDims = new UniqueList(){
				@Override
				public int getObjIndex(Object obj) {
					return ((DefaultListModel)getModel()).indexOf(obj);
				}
			};
			jListRowDims.setModel(new DefaultListModel());
			jListRowDims.addMouseListener(new ListMouseListener(MENU_TYPE_ROW));
		}
		return jListRowDims;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelMeasure() {
		if (jPanelMeasure == null) {
			jLabelMeasure = new JLabel();
			jLabelMeasure.setText(StringResource.getStringResource("uifreequery0011"));// 统计字段
			jPanelMeasure = new JPanel();
			jPanelMeasure.setLayout(new BorderLayout());
			jPanelMeasure.add(jLabelMeasure, java.awt.BorderLayout.NORTH);
			jPanelMeasure.add(getJScrollPaneMeasures(), java.awt.BorderLayout.CENTER);
		}
		return jPanelMeasure;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneMeasures() {
		if (jScrollPaneMeasures == null) {
			jScrollPaneMeasures = new JScrollPane();
			jScrollPaneMeasures.setViewportView(getJListMeasures());
		}
		return jScrollPaneMeasures;
	}

	/**
	 * This method initializes jList
	 * 
	 * @return javax.swing.JList
	 */
	UniqueList getJListMeasures() {
		if (jListMeasures == null) {
			jListMeasures = new UniqueList(){
				@Override
				public int getObjIndex(Object obj) {
					return ((DefaultListModel)getModel()).indexOf(obj);
				}
			};
			jListMeasures.setModel(new DefaultListModel());
			jListMeasures.addMouseListener(new ListMouseListener(MENU_TYPE_MEASURE));

		}
		return jListMeasures;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelColDim() {
		if (jPanelColDim == null) {
			jLabelColDim = new JLabel();
			jLabelColDim.setText(StringResource.getStringResource("ubimultidim0070"));// 列维度
			jPanelColDim = new JPanel();
			jPanelColDim.setLayout(new BorderLayout());
			jPanelColDim.add(jLabelColDim, java.awt.BorderLayout.NORTH);
			jPanelColDim.add(getJScrollPaneColDims(), java.awt.BorderLayout.CENTER);
		}
		return jPanelColDim;
	}

	/**
	 * This method initializes jScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneColDims() {
		if (jScrollPaneColDims == null) {
			jScrollPaneColDims = new JScrollPane();
			jScrollPaneColDims.setViewportView(getJListColDims());
		}
		return jScrollPaneColDims;
	}

	/**
	 * This method initializes jList1
	 * 
	 * @return javax.swing.JList
	 */
	UniqueList getJListColDims() {
		if (jListColDims == null) {
			jListColDims = new UniqueList(){
				@Override
				public int getObjIndex(Object obj) {
					return ((DefaultListModel)getModel()).indexOf(obj);
				}
			};
			jListColDims.setModel(new DefaultListModel());
			jListColDims.addMouseListener(new ListMouseListener(MENU_TYPE_COL));

		}
		return jListColDims;
	}
	
	public UniqueList getCrossFieldList(int pos) {
		switch (pos) {
		case SetCrossPanel.MENU_TYPE_COL:
			return getJListColDims();
		case SetCrossPanel.MENU_TYPE_ROW:
			return getJListRowDims();
		case SetCrossPanel.MENU_TYPE_MEASURE:
			return getJListMeasures();
		}
		return null;
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
//	private JPanel getJPanelExpand() {
//		if (jPanelExpand == null) {
//			jPanelExpand = new JPanel();
//			jPanelExpand.setLayout(new BorderLayout());
//			ButtonGroup btnGroup = new ButtonGroup();
//			btnGroup.add(getJCheckBoxHorExpand());
//			btnGroup.add(getJCheckBoxVerExpand());
//			jPanelExpand.add(getJCheckBoxHorExpand(), BorderLayout.NORTH);
//			jPanelExpand.add(getJCheckBoxVerExpand(), BorderLayout.CENTER);
//		}
//		return jPanelExpand;
//	}

	/**
	 * This method initializes jCheckBoxHorSum
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBoxHorSum() {
		if (jCheckBoxHorSum == null) {
			jCheckBoxHorSum = new JCheckBox();
			jCheckBoxHorSum.setText(StringResource.getStringResource("uifreequery0012"));// 横向合计
		}
		return jCheckBoxHorSum;
	}

	/**
	 * This method initializes jCheckBoxVerSum
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBoxVerSum() {
		if (jCheckBoxVerSum == null) {
			jCheckBoxVerSum = new JCheckBox();
			jCheckBoxVerSum.setText(StringResource.getStringResource("uifreequery0013"));// 纵向合计
		}
		return jCheckBoxVerSum;
	}

	/**
	 * This method initializes jCheckBoxHorExpand
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBoxHorExpand() {
		if (jCheckBoxHorExpand == null) {
			jCheckBoxHorExpand = new JCheckBox();
			jCheckBoxHorExpand.setSelected(true);
			jCheckBoxHorExpand.setText(StringResource.getStringResource("uifreequery0009"));// 横向展现
		}
		return jCheckBoxHorExpand;
	}

	/**
	 * This method initializes jCheckBoxVerExpand
	 * 
	 * @return javax.swing.JCheckBox
	 */
//	private JCheckBox getJCheckBoxVerExpand() {
//		if (jCheckBoxVerExpand == null) {
//			jCheckBoxVerExpand = new JCheckBox();
//			jCheckBoxVerExpand.setText(StringResource.getStringResource("uifreequery0010"));// 纵向展现
//
//		}
//		return jCheckBoxVerExpand;
//	}

	/**
	 * This method initializes jPanel4
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelRowDim() {
		if (jPanelRowDim == null) {
			jLabelRowDim = new JLabel();
			jLabelRowDim.setText(StringResource.getStringResource("ubimultidim0069"));// 行维度
			jPanelRowDim = new JPanel();
			jPanelRowDim.setLayout(new BorderLayout());
			jPanelRowDim.add(jLabelRowDim, java.awt.BorderLayout.NORTH);
			jPanelRowDim.add(getJScrollPaneRowDims(), java.awt.BorderLayout.CENTER);
		}
		return jPanelRowDim;
	}

	/**
	 * This method initializes jPanelCalcPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelSumFlag() {
		if (jPanelSumFlag == null) {
			jPanelSumFlag = new JPanel();
			jPanelSumFlag.add(getJCheckBoxHorExpand(), null);
			JLabel lbl = new JLabel();
			lbl.setText("                 ");
			lbl.setSize(200, 20);
			jPanelSumFlag.add(lbl, null);
			jPanelSumFlag.add(getJCheckBoxVerSum(), null);
			jPanelSumFlag.add(getJCheckBoxHorSum(), null);
		}
		return jPanelSumFlag;
	}

	public void setCrossTableInfo(AdhocCrossTableSet crossInfo) {
		m_crossInfo = crossInfo;
		DefaultListModel model = (DefaultListModel) getJListRowDims().getModel();
		model.clear();
		model = (DefaultListModel) getJListColDims().getModel();
		model.clear();
		model = (DefaultListModel) getJListMeasures().getModel();
		model.clear();
		if (crossInfo == null) {
			return;
		} else {
			IMetaData[] colDims = crossInfo.getColFlds();
			if (colDims != null) {
				model = (DefaultListModel) getJListColDims().getModel();
				for (IMetaData fld : colDims) {
					model.addElement(fld);
				}
			}
			IMetaData[] rowDims = crossInfo.getRowFlds();
			if (rowDims != null) {
				model = (DefaultListModel) getJListRowDims().getModel();
				for (IMetaData fld : rowDims) {
					model.addElement(fld);
				}
			}
			IMetaData[] measures = crossInfo.getMeasureFlds();
			if (measures != null) {
				model = (DefaultListModel) getJListMeasures().getModel();
				for (IMetaData fld : measures) {
					model.addElement(fld);
				}
			}
			getJCheckBoxHorExpand().setSelected(crossInfo.isHorExpand());
//			getJCheckBoxVerExpand().setSelected(!crossInfo.isHorExpand());
			getJCheckBoxHorSum().setSelected(crossInfo.isHorSum());
			getJCheckBoxVerSum().setSelected(crossInfo.isVerSum());

		}
	}

	public AdhocCrossTableSet getCrossTableInfo() {
		if (m_crossInfo == null)
			m_crossInfo = new AdhocCrossTableSet();
		DefaultListModel listModel = (DefaultListModel) getJListRowDims().getModel();
		if (listModel.size() > 0) {
			DefaultReportField[] rows = new DefaultReportField[listModel.size()];
			listModel.copyInto(rows);
			m_crossInfo.setRowFlds(rows);
		} else
			m_crossInfo.setRowFlds(null);
		listModel = (DefaultListModel) getJListColDims().getModel();
		if (listModel.size() > 0) {
			DefaultReportField[] cols = new DefaultReportField[listModel.size()];
			listModel.copyInto(cols);
			m_crossInfo.setColFlds(cols);
		} else
			m_crossInfo.setColFlds(null);

		listModel = (DefaultListModel) getJListMeasures().getModel();
		if (listModel.size() > 0) {
			DefaultReportField[] measures = new DefaultReportField[listModel.size()];
			listModel.copyInto(measures);
			m_crossInfo.setMeasureFlds(measures);
		} else
			m_crossInfo.setMeasureFlds(null);

		m_crossInfo.setIsHorExpand(getJCheckBoxHorExpand().isSelected());
		m_crossInfo.setIsVerSum(getJCheckBoxVerSum().isSelected());
		m_crossInfo.setIsHorSum(getJCheckBoxHorSum().isSelected());

		return m_crossInfo;
	}

	private JMenuItem[] getMeasuerMenuItems() {
		if (m_measMenuItems == null) {
			m_measMenuItems = new UICheckBoxMenuItem[] {
					new UICheckBoxMenuItem(StringResource
							.getStringResource(IFldCountType.TYPE_NAME_SUM)),
					new UICheckBoxMenuItem(StringResource
							.getStringResource(IFldCountType.TYPE_NAME_COUNT)),
					new UICheckBoxMenuItem(StringResource
							.getStringResource(IFldCountType.TYPE_NAME_AVAGE)),
					new UICheckBoxMenuItem(StringResource
							.getStringResource(IFldCountType.TYPE_NAME_MAX)),
					new UICheckBoxMenuItem(StringResource
							.getStringResource(IFldCountType.TYPE_NAME_MIN)) };
			MenuItemListener itemListener = new MenuItemListener(
					MENU_TYPE_MEASURE);
			for (int i = 0; i < m_measMenuItems.length; i++) {
				m_measMenuItems[i].addActionListener(itemListener);
			}
			m_measMenuIndex = new int[] { IFldCountType.TYPE_SUM,
					IFldCountType.TYPE_COUNT, IFldCountType.TYPE_AVAGE,
					IFldCountType.TYPE_MAX, IFldCountType.TYPE_MIN };
		}
		return m_measMenuItems;
	}

//	private JCheckBoxMenuItem[] getDimensionMenuItems() {
//		if (m_dimMenuItems == null) {
//			m_dimMenuItems = new UICheckBoxMenuItem[] {
//					new UICheckBoxMenuItem(StringResource.getStringResource(IFldCountType.ORDER_TYPE_NAME_NONE)),
//					new UICheckBoxMenuItem(StringResource.getStringResource(IFldCountType.ORDER_TYPE_NAME_ASCEDING)),
//					new UICheckBoxMenuItem(StringResource.getStringResource(IFldCountType.ORDER_TYPE_NAME_DESCENDING)), };
//			m_dimMenuIndex = new int[] { IFldCountType.ORDER_TYPE_NONE, IFldCountType.ORDER_TYPE_ASCEDING,
//					IFldCountType.ORDER_TYPE_DESCENDING };
//
//		}
//		return m_dimMenuItems;
//	}
    
	
	/**
	 * @i18n miufo00261=行纬度成员设置
	 */
	private JMenuItem[] getRowDimensionMenuItems() {
		if(m_dimRowMenuItems==null){
			m_dimRowMenuItems = new UIMenuItem[] {new UIMenuItem(StringResource.getStringResource("miufo00261"))};
			m_dimMenuIndex = null;
			MenuItemListener itemListener = new MenuItemListener(
					MENU_TYPE_ROW);
			for (int i = 0; i < m_dimRowMenuItems.length; i++) {
				m_dimRowMenuItems[i].addActionListener(itemListener);
			}
		}
			

		return m_dimRowMenuItems;
	}
	/**
	 * @i18n miufo00262=列纬度成员设置
	 */
	private JMenuItem[] getColDimensionMenuItems() {
		if(m_dimColMenuItems==null){
			m_dimColMenuItems = new UIMenuItem[] {new UIMenuItem(StringResource.getStringResource("miufo00262"))};
			m_dimMenuIndex = null;
			MenuItemListener itemListener = new MenuItemListener(
					MENU_TYPE_COL);
			for (int i = 0; i < m_dimColMenuItems.length; i++) {
				m_dimColMenuItems[i].addActionListener(itemListener);
			}
		}
			

		return m_dimColMenuItems;
	}
	private JMenuItem[] getMenuItems(int menuType) {
		if (menuType == MENU_TYPE_MEASURE){
			return getMeasuerMenuItems();
		}else if (menuType == MENU_TYPE_ROW){
			return getRowDimensionMenuItems();
		}else if(menuType == MENU_TYPE_COL){
			return getColDimensionMenuItems();
		}
			
		return null;
	}

	private int[] getMenuIndex(int menuType) {
		if (menuType == MENU_TYPE_MEASURE)
			return m_measMenuIndex;
		else if (menuType == MENU_TYPE_ROW||menuType == MENU_TYPE_COL)
			return m_dimMenuIndex;
		return null;

	}
    
	/**
	 * 根据鼠标所在的位置不同重新构造右键菜单
	 * @param menuType
	 * @return
	 */
	private UIPopupMenu getPopUpMenu(int menuType) {
		    m_popupMenu = new UIPopupMenu();
		    JMenuItem[] items = getMenuItems(menuType);
			for (int i = 0; i < items.length; i++) {
				m_popupMenu.add(items[i]);
			}

		return m_popupMenu;
	}

	private class ListMouseListener implements MouseListener {

		private int m_menuType;

		public ListMouseListener(int menuType) {
			m_menuType = menuType;
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
			mouseReleasedMeasureList(e, m_menuType);
		}

	}

	private void mouseReleasedMeasureList(MouseEvent e, int menuType) {
		if (!e.isPopupTrigger())
			return;

		JList measureList = getCrossFieldList(menuType);
		Point p = e.getPoint();
		if (!measureList.contains(p) || measureList.getModel().getSize() == 0)
			return;
		
		JMenuItem[] menuItems = getMenuItems(menuType);
		int[] menuTypes = getMenuIndex(menuType);
		int index = measureList.getSelectedIndex();
		// 如果没有选中成员，则将弹出菜单位置设置为选中
		if (index < 0 || index >= measureList.getModel().getSize()) {
			index = measureList.locationToIndex(p);
		}
		// 目前处理是为选中的成员设置属性
		if (index >= 0 && index < measureList.getModel().getSize()) {
			measureList.setSelectedIndex(index);
			DefaultReportField selMem = (DefaultReportField) measureList.getModel().getElementAt(index);
			int type = selMem.getCountType();

			for (int i = 0; i < menuItems.length; i++) {
				if (menuItems[i] instanceof JCheckBoxMenuItem) {
					if (menuTypes != null && i < menuTypes.length
							&& menuTypes[i] == type) {
						menuItems[i].setSelected(true);
					} else {
						menuItems[i].setSelected(false);
					}
				}
			}
			
			getPopUpMenu(menuType).show((Component) e.getSource(), e.getX(), e.getY());
		}
	}

	// 菜单监听
	private class MenuItemListener implements ActionListener {
		private int m_menuType;

		public MenuItemListener(int menuType) {
			m_menuType = menuType;
		}

		public void actionPerformed(ActionEvent e) {
			onMenuItemClick(e, m_menuType);
		};
	}

	private void onMenuItemClick(ActionEvent e, int menuType) {
		if(menuType==MENU_TYPE_MEASURE)
		{
			JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();

			JMenuItem[] menuItems = getMenuItems(menuType);
			int[] menuValues = getMenuIndex(menuType);
			int type = -1;

			if (!item.isSelected()) {
				menuItems[0].setSelected(true);
				type = menuValues[0];
			} else {
				for (int i = 0; i < menuItems.length; i++) {
					if (item == menuItems[i]) {
						menuItems[i].setSelected(true);
						type = menuValues[i];
					} else {
						menuItems[i].setSelected(false);
					}
				}
			}
			if (type == -1)
				return;

			int index = getJListMeasures().getSelectedIndex();
			DefaultListModel listModel = (DefaultListModel) getJListMeasures()
					.getModel();
			if (index >= 0 && index < listModel.size()) {
				((DefaultReportField) listModel.elementAt(index))
						.setCountType(type);
			}
		}else{
			JList measureList = getCrossFieldList(menuType);
			int selected=measureList.getSelectedIndex();
			DefaultReportField selField = (DefaultReportField) measureList.getModel().getElementAt(selected);
			FieldSetDlg dlg=new FieldSetDlg(null,selField,getAdhocModel());
			dlg.show();
		}
	}
	
	

} // @jve:decl-index=0:visual-constraint="10,10"

 