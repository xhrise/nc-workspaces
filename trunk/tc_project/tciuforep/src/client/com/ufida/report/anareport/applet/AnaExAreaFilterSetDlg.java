package com.ufida.report.anareport.applet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.vo.iufo.datasetmanager.DataSetDefVO;
import com.ufida.dataset.descriptor.FilterDescriptor;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.rep.applet.exarea.ExAreaFilterSetPanel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;
import com.ufsoft.table.exarea.IExData;

public class AnaExAreaFilterSetDlg extends UfoDialog implements ItemListener, ActionListener {
	private JPanel jTopPanel = null;
	private JPanel jTypePanel = null;
	private ButtonGroup buttonGroup=null;
	private JRadioButton jrbExArea = null;
	private JRadioButton jrbDataSet = null;
	private JPanel jExAreaInfoPanel = null;
	private JComboBox jcmbExArea = null;
	private JComboBox jcmbDataSet = null;
	private JPanel jCmdPanel = null;
	private JButton btnOK;
	private JButton btnCancel;
	private JButton btnApply;
	private ExAreaFilterSetPanel filterPanel;

	private AnaReportPlugin m_plugin=null;
	private ExAreaModel exAreaModel = null;
	private transient ExAreaCell[] allExArea = null;
	private transient DataSetDefVO[] allDataSet = null;
	private transient Hashtable<ExAreaCell,FilterDescriptor> exAreaCache=new Hashtable<ExAreaCell,FilterDescriptor>();
	private transient Hashtable<DataSetDefVO,FilterDescriptor> dataSetCache=new Hashtable<DataSetDefVO,FilterDescriptor>();

	public AnaExAreaFilterSetDlg(Container parent, AnaReportPlugin plugin) {
		super(parent, StringResource.getStringResource("miufo00429"), true);
		this.m_plugin=plugin;
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getTopPanel(), BorderLayout.NORTH);
		getContentPane().add(getFilterSetPanel().getContentPanel(), BorderLayout.CENTER);
		getContentPane().add(getCmdPanel(m_plugin.getModel()), BorderLayout.SOUTH);
		pack();
	}

	private JPanel getTopPanel() {
		if (jTopPanel == null) {
			jTopPanel = new UIPanel();
			jTopPanel.setLayout(new BoxLayout(jTopPanel, BoxLayout.Y_AXIS));
			jTopPanel.add(getTypePanel());
			jTopPanel.add(getExAreaInfoPanel());
		}
		return jTopPanel;
	}

	private JPanel getExAreaInfoPanel() {
		if (jExAreaInfoPanel == null) {
			jExAreaInfoPanel=new UIPanel();
			jExAreaInfoPanel.setLayout(new BoxLayout(jExAreaInfoPanel,BoxLayout.Y_AXIS));
			JPanel exPanel=new UIPanel(new FlowLayout(FlowLayout.LEADING));
			JLabel jlExArea=new UILabel(MultiLang.getString("miufo00103"));
			jlExArea.setPreferredSize(new Dimension(100,20));
			exPanel.add(jlExArea);
			exPanel.add(getCmbExArea());
			jExAreaInfoPanel.add(exPanel);
			JPanel dataPanel=new UIPanel(new FlowLayout(FlowLayout.LEADING));
			JLabel jlDataset=new UILabel(StringResource.getStringResource("miufo00241"));
			jlDataset.setPreferredSize(new Dimension(100,20));
			dataPanel.add(jlDataset);
			dataPanel.add(getCmbDataSet());
			jExAreaInfoPanel.add(dataPanel);
		}
		return jExAreaInfoPanel;
	}

	private JComboBox getCmbExArea() {
		if (jcmbExArea == null) {
			jcmbExArea = new UIComboBox(){

				/**
				 * @i18n iufobi00023=设置不合法请重新设置
				 * @i18n miufo1000155=错误
				 */
				@Override
				public void setSelectedItem(Object anObject) {
					if(updataInfo()){
						super.setSelectedItem(anObject);
					}else{
						UfoPublic.showErrorDialog(AnaExAreaFilterSetDlg.this, StringResource.getStringResource("iufobi00023"), StringResource.getStringResource("miufo1000155"));
					}
					
				}
				
			};
			jcmbExArea.addItemListener(this);
			DefaultComboBoxModel model=(DefaultComboBoxModel)jcmbExArea.getModel();
			for(int i=0;i<getAllExArea().length;i++){
				model.addElement(getAllExArea()[i]);
			}
			ExAreaCell selectedEx=getSelectedEx();
			if(selectedEx!=null){
				jcmbExArea.setSelectedItem(selectedEx);
			}
			
		}
		return jcmbExArea;
	}

	private JComboBox getCmbDataSet() {
		if (jcmbDataSet == null) {
			jcmbDataSet =new UIComboBox(){

				/**
				 * @i18n iufobi00023=设置不合法请重新设置
				 * @i18n miufo1000155=错误
				 */
				@Override
				public void setSelectedItem(Object anObject) {
					if(updataInfo()){
						super.setSelectedItem(anObject);
					}else{
						UfoPublic.showErrorDialog(AnaExAreaFilterSetDlg.this, StringResource.getStringResource("iufobi00023"), StringResource.getStringResource("miufo1000155"));
					}
					
				}
				
			};
			jcmbDataSet.addItemListener(this);
			DefaultComboBoxModel model=(DefaultComboBoxModel)jcmbDataSet.getModel();
			for(int i=0;i<getAllDataSet().length;i++){
				model.addElement(getAllDataSet()[i]);
			}
		}
		return jcmbDataSet;
	}

	private ExAreaFilterSetPanel getFilterSetPanel() {
		if (filterPanel == null) {
			filterPanel = new ExAreaFilterSetPanel();
		}
		return filterPanel;
	}

	/**
	 * @i18n iufobi00024=可扩展区域
	 */
	private JRadioButton getExAreaRb() {
		if (jrbExArea == null) {
			jrbExArea = new UIRadioButton(StringResource.getStringResource("iufobi00024"));
			jrbExArea.addItemListener(this);
		}
		return jrbExArea;
	}

	private JRadioButton getDataSetRb() {
		if (jrbDataSet == null) {
			jrbDataSet = new UIRadioButton(StringResource.getStringResource("miufo00241")){
				
			};
			jrbDataSet.addItemListener(this);
		}
		return jrbDataSet;
	}

	/**
	 * @i18n iufobi00025=筛选范围
	 */
	private JPanel getTypePanel() {
		if (jTypePanel == null) {
			jTypePanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
			JLabel jlFilterType=new UILabel(StringResource.getStringResource("iufobi00025"));
			jlFilterType.setPreferredSize(new Dimension(100,20));
			jTypePanel.add(jlFilterType);
			getTypeButtonGroup().add(getExAreaRb());
			jTypePanel.add(getExAreaRb());
			getTypeButtonGroup().add(getDataSetRb());
			jTypePanel.add(getDataSetRb());
			getExAreaRb().setSelected(true);
		}
		return jTypePanel;
	}

	private JPanel getCmdPanel(AnaReportModel anaModel) {
		if (jCmdPanel == null) {
			jCmdPanel = new UIPanel(new FlowLayout(FlowLayout.TRAILING));
			jCmdPanel.addMouseListener(new PageSetListener(anaModel));
			jCmdPanel.add(getOKButton());
			jCmdPanel.add(getCancleButton());
			jCmdPanel.add(getApplyButton());
		}
		return jCmdPanel;
	}
    private ButtonGroup getTypeButtonGroup(){
    	if(buttonGroup==null){
    		buttonGroup= new ButtonGroup(){

				/**
				 * @i18n iufobi00023=设置不合法请重新设置
				 * @i18n miufo1000155=错误
				 */
				@Override
				public void setSelected(ButtonModel m, boolean b) {
					if(updataInfo()){
						super.setSelected(m, b);
					}else{
						UfoPublic.showErrorDialog(AnaExAreaFilterSetDlg.this, StringResource.getStringResource("iufobi00023"), StringResource.getStringResource("miufo1000155"));
					}
					
				}
    			
    		};
    	}
    	return buttonGroup;
    }
	private JButton getOKButton() {
		if (btnOK == null) {
			btnOK = new UIButton(MultiLang.getString("ok"));
			btnOK.addActionListener(this);
		}
		return btnOK;
	}

	private JButton getCancleButton() {
		if (btnCancel == null) {
			btnCancel = new UIButton(MultiLang.getString("cancel"));
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}

	/**
	 * @i18n miufo1001112=应用
	 */
	private JButton getApplyButton() {
		if (btnApply == null) {
			btnApply = new UIButton(StringResource.getStringResource("miufo1001112"));
			btnApply.addActionListener(this);
		}
		return btnApply;
	}

	/**
	 * @i18n iufobi00023=设置不合法请重新设置
	 * @i18n miufo1000155=错误
	 */
	public void actionPerformed(ActionEvent e) {
		// 更新所有的设置
		if (e.getSource() == getOKButton()) {//保存所有的并关闭			
			if(updataInfo()){
				doOK();
				setResult(ID_OK);
				close();
				if(!m_plugin.getModel().isFormatState()){
					m_plugin.setDirty(true);
					m_plugin.refreshDataModel(true);
				}
			}else{
				UfoPublic.showErrorDialog(this, StringResource.getStringResource("iufobi00023"), StringResource.getStringResource("miufo1000155"));
				return ;
			}
			getFilterSetPanel().getFilterDescPanel().setDirty(false);
		}
		// 更新当前的设置
		if (e.getSource() == getApplyButton()) {//保存当前的并从cache中移除
			if (updataInfo()) {
				if (getFilterSetPanel().getFilterDescPanel().isDirty()) {
					doApply();
					if (!m_plugin.getModel().isFormatState()) {
						m_plugin.setDirty(true);
						m_plugin.refreshDataModel(true);
					}
				}
			}else{
				UfoPublic.showErrorDialog(this, StringResource.getStringResource("iufobi00023"), StringResource.getStringResource("miufo1000155"));
				return ;
			}
			getFilterSetPanel().getFilterDescPanel().setDirty(false);
		}

		if (e.getSource() == getCancleButton()) {
			setResult(ID_CANCEL);
			close();
			getFilterSetPanel().getFilterDescPanel().setDirty(false);
		}

	}

	private void  doOK(){
		Enumeration<ExAreaCell> exareas=exAreaCache.keys();
		while(exareas.hasMoreElements()){
			ExAreaCell selExArea =exareas.nextElement();
			if (selExArea != null
					&& selExArea.getModel() instanceof AreaDataModel) {
				AreaDataModel areaData = (AreaDataModel) selExArea
						.getModel();
				FilterDescriptor filter = exAreaCache.get(selExArea);
				if(filter!=null){
					areaData.setAreaFilter(filter);
				}
				
			}
		}
		Enumeration<DataSetDefVO> datasets=dataSetCache.keys();
		while(datasets.hasMoreElements()){
			DataSetDefVO selDataDef=datasets.nextElement();
			if (selDataDef != null) {
				FilterDescriptor filter = dataSetCache.get(selDataDef);
				if (filter != null) {
					m_plugin.getModel().getDataSource().setReportFilter(
							selDataDef.getPk_datasetdef(), filter);
					m_plugin.getModel().setDirty(true);
				}
			}
		}
	}
	
	private void doApply(){
		if (getExAreaRb().isSelected()) {
			ExAreaCell selExArea = (ExAreaCell) getCmbExArea()
					.getSelectedItem();
			if (selExArea != null
					&& selExArea.getModel() instanceof AreaDataModel) {
				AreaDataModel areaData = (AreaDataModel) selExArea
						.getModel();
				FilterDescriptor filter = exAreaCache.get(selExArea);
				if(filter!=null){
					areaData.setAreaFilter(filter);
					exAreaCache.remove(selExArea);
				}
				
			}
		} else {
			DataSetDefVO selDataDef = (DataSetDefVO) getCmbDataSet()
					.getSelectedItem();
			if (selDataDef != null) {
				FilterDescriptor filter = dataSetCache.get(selDataDef);
				if (filter != null) {
					m_plugin.getModel().getDataSource().setReportFilter(
							selDataDef.getPk_datasetdef(), filter);
					dataSetCache.remove(selDataDef);
					m_plugin.getModel().setDirty(true);
				}
			}
		}
	}
	private boolean updataInfo() {
		if (getFilterSetPanel().getFilterDescPanel().getDescriptor() == null
				|| getFilterSetPanel().getFilterDescPanel().getDataSet() == null) {
			getFilterSetPanel().getFilterDescPanel().setDirty(false);
			return true;
		}
		getFilterSetPanel().getFilterDescPanel().stopTableEdit();
		if (getFilterSetPanel().getFilterDescPanel().isDirty()) {
			if (!getFilterSetPanel().updateInfo()) {
				return false;
			}
//		if (selectItem instanceof ExAreaCell) {
//			ExAreaCell selExArea = (ExAreaCell) selectItem;
//			if (selExArea != null && selExArea.getModel() instanceof AreaDataModel) {
//				AreaDataModel areaData = (AreaDataModel) selExArea.getModel();
//				if (getFilterSetPanel().updateInfo()) {
//					FilterDescriptor filter = getFilterSetPanel().getFilterDescPanel().getDescriptor();
//					areaData.setAreaFilter(filter);
//				}else{
//					return false;
//				}
//
//			}
//		} else if (selectItem instanceof DataSetDefVO) {
//			// 数据集处理重新筛选了吗??
//			DataSetDefVO selDataDef = (DataSetDefVO) selectItem;
//			if (selDataDef != null) {
//				if (getFilterSetPanel().updateInfo()) {
//					FilterDescriptor filter = getFilterSetPanel()
//							.getFilterDescPanel().getDescriptor();
//					m_plugin.getModel().getDataSource().setReportFilter(selDataDef.getPk_datasetdef(), filter);
//					m_plugin.getModel().setDirty(true);
//				}else{
//					return false;
//				}
//			}
//		}
		}
		return true;
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == getExAreaRb()) {
				if (getExAreaRb().isSelected()) {
					getCmbDataSet().setEnabled(false);
					getCmbExArea().setEnabled(true);
				} else {
					getCmbExArea().setEnabled(false);
					getCmbDataSet().setEnabled(true);				
			}
			refreshUI();
		}
		if (e.getSource() == getCmbExArea() && getCmbExArea().isEnabled() && e.getStateChange() == ItemEvent.SELECTED&&getExAreaRb().isSelected()) {
			refreshUI();
		}

		if (e.getSource() == getCmbDataSet() && getCmbDataSet().isEnabled() && e.getStateChange() == ItemEvent.SELECTED&&getDataSetRb().isSelected()) {
			refreshUI();
		}

	}

	private void refreshUI() {
		if (getExAreaRb().isSelected()) {
			ExAreaCell selExArea = (ExAreaCell) getCmbExArea().getSelectedItem();
			if (selExArea != null
					&& selExArea.getModel() instanceof AreaDataModel) {
				AreaDataModel areaData = (AreaDataModel) selExArea.getModel();
				if (areaData.getDSTool() != null
						&& areaData.getDSTool().getDSDef() != null) {
					getCmbDataSet().setSelectedItem(areaData.getDSInfo());
					FilterDescriptor filter = exAreaCache.get(selExArea);
					if (filter == null) {
						if (areaData.getAreaFilter() == null) {
							filter = new FilterDescriptor();
						} else {
							filter = (FilterDescriptor) areaData
									.getAreaFilter().clone();
						}
						exAreaCache.put(selExArea, filter);
					}
					getFilterSetPanel().initData(
							areaData.getDSTool().getDSDef(), filter);
				}
			}
		} else {
			DataSetDefVO selDataDef = (DataSetDefVO) getCmbDataSet().getSelectedItem();
			if (selDataDef != null) {
				FilterDescriptor filter =dataSetCache.get(selDataDef);
				if (filter == null) {
					filter = m_plugin.getModel().getDataSource()
							.getReportFilter(selDataDef.getPk_datasetdef());
					if (filter == null) {
						filter = new FilterDescriptor();
					} else {
						filter = (FilterDescriptor) filter.clone();
					}
					dataSetCache.put(selDataDef,filter);
				}
				getFilterSetPanel().initData(selDataDef.getDataSetDef(), filter);
			}
		}
	}

	public ExAreaModel getExAreaModel() {
		if (exAreaModel == null) {
			exAreaModel = ExAreaModel.getInstance(m_plugin.getModel().getFormatModel());
		}
		return exAreaModel;
	}

	private ExAreaCell[] getAllExArea() {
		if (allExArea == null) {
			ArrayList<ExAreaCell> temp = new ArrayList<ExAreaCell>();
			ExAreaCell[] all = getExAreaModel().getExAreaCells();
			for (int i = 0; i < all.length; i++) {
				if (all[i] != null && all[i].getModel() instanceof AreaDataModel) {
					AreaDataModel areaData = (AreaDataModel) all[i].getModel();
					if (areaData.getDSTool() != null && areaData.getDSTool().getDSDef() != null) {
//						if (areaData.getDSTool().getDSDef().supportDescriptorFunc(DescriptorType.FilterDescriptor)) {
							temp.add(all[i]);
//						}
					}
				}
			}
			allExArea = temp.toArray(new ExAreaCell[0]);
		}
		return allExArea;
	}
	private ExAreaCell getSelectedEx() {
		ExAreaCell cell = null;
		CellsModel cellsModel = m_plugin.getModel().getCellsModel();
		AreaPosition[] selCells =cellsModel.getSelectModel().getSelectedAreas();
		if (!m_plugin.getModel().isFormatState()) {
			CellPosition[] anaCells=m_plugin.getModel().getFormatPoses(cellsModel, selCells);
			if(anaCells!=null&&anaCells.length>0&&anaCells[0]!=null){
				selCells[0]=AreaPosition.getInstance(anaCells[0], anaCells[0]);
			}else{
				selCells[0]=null;
			}
		}
		if (selCells[0] != null) {
			ExAreaCell[] cells = getAllExArea();
			for (int i = 0; i < cells.length; i++) {
				AreaPosition area = cells[i].getArea();
				if (selCells[0].intersection(area)) {
					cell = cells[i];
					break;
				}
			}
		}
		return cell;
	}
	
	private DataSetDefVO[] getAllDataSet() {
		if (allDataSet == null) {
			ArrayList<DataSetDefVO> temp = new ArrayList<DataSetDefVO>();
			String[] allPKs = m_plugin.getModel().getDataSource().getAllDSPKs();
			DataSetDefVO def = null;
			for (int i = 0; i < allPKs.length; i++) {
				def = m_plugin.getModel().getDataSource().getDataSetDefVO(allPKs[i]);
				if (def != null
				// &&
				// def.getDataSetDef().supportDescriptorFunc(DescriptorType.FilterDescriptor)
				) {
					temp.add(def);
				}

			}
			allDataSet = temp.toArray(new DataSetDefVO[0]);
		}
		return allDataSet;
	}

	private class PageSetListener implements MouseListener {
		// private AreaDataModel m_model = null;
		private AnaReportModel m_anaModel;

		public PageSetListener(AnaReportModel anaModel) {
			// m_model = model;
			m_anaModel = anaModel;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		/**
		 * @i18n iufobi00026=还要使用默认的分页么？
		 */
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			if (e.getClickCount() < 3)
				return;
			CellsModel cells = m_anaModel.getFormatModel();
			ExAreaCell[] exCells = ExAreaModel.getInstance(cells).getExAreaCells();
			if (exCells == null)
				return;
			int res = MessageDialog.showYesNoCancelDlg(AnaExAreaFilterSetDlg.this, null, StringResource.getStringResource("iufobi00026"));
			if (res == MessageDialog.ID_NO) {
				setPageInfo(false);
			} else if (res == MessageDialog.ID_YES) {
				setPageInfo(true);
			}
		}

		private void setPageInfo(boolean bPage) {
			CellsModel cells = m_anaModel.getFormatModel();
			ExAreaCell[] exCells = ExAreaModel.getInstance(cells).getExAreaCells();
			for (int i = 0; i < exCells.length; i++) {
				IExData model = exCells[i].getModel();
				if (model != null && model instanceof AreaDataModel) {
					((AreaDataModel) model).setPage(bPage);
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

}
 