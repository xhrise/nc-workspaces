package com.ufida.report.anareport.edit;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.dsmanager.SortDescPanel;

import com.ufida.dataset.DataSet;
import com.ufida.dataset.descriptor.DescriptorType;
import com.ufida.dataset.descriptor.SortDescriptor;
import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;
import com.ufsoft.iufo.resource.StringResource;

public class AnaOrderManageDlg extends UfoDialog implements ItemListener,ActionListener{

	private AnaReportPlugin m_plugin=null;
	private ExAreaCell[] listExArea = null;
	private JPanel jDatasetInfo;
	private JComboBox jcmbExArea=null;//扩展区域
	private JTextField jTxDataSetValue = null;//数据集
	private JPanel jOrderTypePanel;
	private JRadioButton rbDefaultField;
	private JRadioButton rbUserDefineField;
	private AnaReportSortDescPanel jsortPanel;
	private JPanel jCmdPanel = null;
	private JButton btnOK;
	private JButton btnCancel;
	private JButton btnApply;
	/**
	 * 
	 * @param container
	 * @param exCell
	 * @param fmtModel
	 */
	public AnaOrderManageDlg(Container container,AnaReportPlugin plugin){
		super(container);
		this.m_plugin=plugin;
		initialize();
		refreshUI();
	}
	
	/**
	 * @i18n iufobi00027=排序管理
	 */
	private void initialize() {
		this.setTitle(StringResource.getStringResource("iufobi00027"));
		this.setContentPane(getJContentPane());
		this.setResizable(false);
		pack();
	}
	
	private JPanel getJContentPane(){
		JPanel jContentPane = new UIPanel();
		GridBagLayout gridbaglayout = new GridBagLayout();
		GridBagConstraints gridbagconstraints = new GridBagConstraints();
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty=1.0D;
		gridbagconstraints.fill = GridBagConstraints.HORIZONTAL;
		gridbagconstraints.insets=new Insets(0,2,0,2);
		jContentPane.setLayout(gridbaglayout);
		gridbagconstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridbaglayout.setConstraints(getDataSetInfoPanel(), gridbagconstraints);
		jContentPane.add(getDataSetInfoPanel());
		gridbaglayout.setConstraints(getOrderTypePanel(), gridbagconstraints);
		jContentPane.add(getOrderTypePanel());
		gridbaglayout.setConstraints(getSortDescPanel(), gridbagconstraints);
		jContentPane.add(getSortDescPanel());
		gridbaglayout.setConstraints(getCmdPanel(), gridbagconstraints);
		jContentPane.add(getCmdPanel());
		return jContentPane;
	}
	
	/**
	 * @i18n iufobi00024=可扩展区域
	 * @i18n miufo00241=数据集
	 */
	private JPanel getDataSetInfoPanel(){
		if(jDatasetInfo==null){
			jDatasetInfo=new UIPanel();
			jDatasetInfo.setLayout(new BoxLayout(jDatasetInfo,BoxLayout.Y_AXIS));
			JPanel exPanel=new UIPanel(new FlowLayout(FlowLayout.LEADING));
			JLabel jlExArea=new UILabel(StringResource.getStringResource("iufobi00024"));
			jlExArea.setPreferredSize(new Dimension(80,20));
			exPanel.add(jlExArea);
			exPanel.add(getCmbExArea());
			jDatasetInfo.add(exPanel);
			JPanel dataPanel=new UIPanel(new FlowLayout(FlowLayout.LEADING));
			JLabel jlDataset=new UILabel(StringResource.getStringResource("miufo00241"));
			jlDataset.setPreferredSize(new Dimension(80,20));
			dataPanel.add(jlDataset);
			getTxDataSetValue().setPreferredSize(new Dimension(100,20));
			dataPanel.add(getTxDataSetValue());
			jDatasetInfo.add(dataPanel);
		}
		return jDatasetInfo;
	}
	private JComboBox getCmbExArea(){
		if(jcmbExArea==null){
			jcmbExArea=new UIComboBox(getExAreaCells());
			ExAreaCell selectedEx=getSelectedEx();
			if(selectedEx!=null){
				jcmbExArea.setSelectedItem(selectedEx);
			}
			jcmbExArea.addItemListener(this);
		}
		return jcmbExArea;
	}
	private JTextField getTxDataSetValue(){
		if(jTxDataSetValue==null){
			jTxDataSetValue=new UITextField();
			jTxDataSetValue.setName("TxFindValue");
			jTxDataSetValue.setEditable(false);
		}
		return jTxDataSetValue;
	}
	
	/**
	 * @i18n iufobi00028=排序类型
	 * @i18n iufobi00029=按界面顺序
	 * @i18n iufobi00030=按自定义顺序
	 */
	private JPanel getOrderTypePanel(){
		if(jOrderTypePanel==null){
			jOrderTypePanel=new UIPanel();
			jOrderTypePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
			JLabel jlOrderType=new UILabel(StringResource.getStringResource("iufobi00028"));
			jlOrderType.setPreferredSize(new Dimension(80,20));
			jOrderTypePanel.add(jlOrderType);
			ButtonGroup setgroup = new ButtonGroup();
			rbDefaultField = new JRadioButton(StringResource.getStringResource("iufobi00029"));
			rbDefaultField.addItemListener(this);
			setgroup.add(rbDefaultField);
			jOrderTypePanel.add(rbDefaultField);
			rbUserDefineField = new JRadioButton(StringResource.getStringResource("iufobi00030"));
			rbUserDefineField.addItemListener(this);
			setgroup.add(rbUserDefineField);
			jOrderTypePanel.add(rbUserDefineField);
		}
		return jOrderTypePanel;
	}
	
	private AnaReportSortDescPanel getSortDescPanel(){
		if(jsortPanel==null){
			jsortPanel=new AnaReportSortDescPanel(null);
		}
		return jsortPanel;
	}
	private JPanel getCmdPanel(){
		   if(jCmdPanel==null){
			   jCmdPanel=new UIPanel(new FlowLayout(FlowLayout.TRAILING));
			   jCmdPanel.add(getOKButton());
			   jCmdPanel.add(getCancleButton());
			   jCmdPanel.add(getApplyButton());
		   }
		   return jCmdPanel;
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
	 * 格式态的扩展区域
	 * @return
	 */
	private ExAreaCell[] getExAreaCells(){
		if(listExArea==null){
			ArrayList<ExAreaCell> temps=new ArrayList<ExAreaCell>();
			ExAreaCell[] allEx=ExAreaModel.getInstance(this.m_plugin.getModel().getFormatModel()).getExAreaCells();
			if(allEx!=null&&allEx.length>0){
				AreaDataModel areaData=null;
				for(int i=0;i<allEx.length;i++){
					if(allEx[i].getModel() instanceof AreaDataModel){
						areaData=(AreaDataModel)allEx[i].getModel();
						if (!areaData.isCross()
								&& allEx[i].getExAreaType() != ExAreaCell.EX_TYPE_CHART
								&& areaData.getDSTool() != null
								&& areaData.getDSTool().isSupport(
										DescriptorType.SortDescriptor)){
							temps.add(allEx[i]);
						}
					}
				}
				listExArea=temps.toArray(new ExAreaCell[0])	;
			}
		}
		return listExArea;
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
			ExAreaCell[] cells = getExAreaCells();
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
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == this.rbDefaultField
				|| e.getSource() == this.rbUserDefineField) {
			getSortDescPanel().setDirty(true);
		}
		if (e.getSource() == getCmbExArea()
				&& e.getStateChange() == ItemEvent.SELECTED) {
			refreshUI();
		}
		if (e.getSource() == getCmbExArea()
				&& e.getStateChange() == ItemEvent.DESELECTED) {
			ExAreaCell selectedArea = (ExAreaCell) e.getItem();
			updateInfo(selectedArea);
		}
	}
	private boolean updateInfo(ExAreaCell selectedArea){
		if(getSortDescPanel().getTable().getCellEditor()!=null){
			getSortDescPanel().getTable().getCellEditor().stopCellEditing();
		}
		if (getSortDescPanel().isDirty()) {
			if (selectedArea != null) {
				if (selectedArea.getModel() instanceof AreaDataModel) {
					AreaDataModel areaData = (AreaDataModel) selectedArea
							.getModel();
					if (getSortDescPanel().getDescriptor() != null
							&& getSortDescPanel().getDescriptor().validate()) {
						areaData.setSortDesMng(getSortDescPanel()
								.getDescriptor());
						areaData.setUserDefineSort(rbUserDefineField
								.isSelected());
					} else {
						return false;
					}
				}
			}
		}
		return true;
	}
	/**
	 * @i18n iufobi00023=设置不合法请重新设置
	 * @i18n miufo1000155=错误
	 */
	public void actionPerformed(ActionEvent e) {
		ExAreaCell selectedArea = (ExAreaCell) this.getCmbExArea().getSelectedItem();
		if (e.getSource() == getOKButton()) {
			if(updateInfo(selectedArea)){
				setResult(ID_OK);
				close();
				if (m_plugin.getModel().isFormatState()) {
					if (selectedArea != null) {
						m_plugin.getModel().getCellsModel().fireExtPropChanged(
								selectedArea.getArea());
					}
				}else{
					m_plugin.refreshDataModel(true);
				}
				
			}else{
				UfoPublic.showErrorDialog(this, StringResource.getStringResource("iufobi00023"), StringResource.getStringResource("miufo1000155"));
			}
			getSortDescPanel().setDirty(false);
		}
		if (e.getSource() == getApplyButton()) {
			if(updateInfo(selectedArea)){
				if (m_plugin.getModel().isFormatState()) {
					if (selectedArea != null) {
						m_plugin.getModel().getCellsModel().fireExtPropChanged(
								selectedArea.getArea());
					}
				}else{
					m_plugin.refreshDataModel(true);
				}
			}else{
				UfoPublic.showErrorDialog(this, StringResource.getStringResource("iufobi00023"), StringResource.getStringResource("miufo1000155"));
			}
			getSortDescPanel().setDirty(false);
		}
		if (e.getSource() == getCancleButton()) {
			setResult(ID_CANCEL);
			close();
			getSortDescPanel().setDirty(false);
		}
	}
	
	private void refreshUI(){
		ExAreaCell selectedArea=(ExAreaCell)this.getCmbExArea().getSelectedItem();
		if(selectedArea!=null){
			if (selectedArea.getModel() instanceof AreaDataModel) {
				AreaDataModel areaData = (AreaDataModel) selectedArea
						.getModel();
					if (areaData.isUserDefineSort()) {
						this.rbUserDefineField.setSelected(true);
					} else {
						this.rbDefaultField.setSelected(true);
					}
					if (areaData.getDSDef() != null) {
						this.getTxDataSetValue().setText(
								areaData.getDSInfo().toString());
						getSortDescPanel().setDataSet(areaData.getDSDef());
						SortDescriptor sort=areaData.getSortDesMng(true);
						getSortDescPanel().setDescriptor(sort==null?new SortDescriptor():(SortDescriptor)sort.clone());
						getSortDescPanel().reload();
					}
			}
		}
	}
	
	public static void main(String[] args){
		AnaOrderManageDlg dlg=new AnaOrderManageDlg(null,null);
		dlg.show();
	}
	
	private class AnaReportSortDescPanel extends SortDescPanel{

		public AnaReportSortDescPanel(DataSet dataSet) {
			super(dataSet, true);
		}

		@Override
		protected void initDescriptor() {
			//不向dataset中添加SortDescriptor,
		}

		@Override
		protected void setDescriptor(SortDescriptor sd) {
			super.setDescriptor(sd);
		}
		
	}

	
}
 