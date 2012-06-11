package com.ufida.report.anareport.edit;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UITextField;
import nc.vo.pub.lang.UFDouble;

import com.ufida.dataset.DataSet;
import com.ufida.dataset.metadata.CalField;
import com.ufida.dataset.metadata.Field;
import com.ufida.report.anareport.model.AnaDataSetTool;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.anareport.model.FieldCountDef;
import com.ufida.report.crosstable.CrossHeader;
import com.ufida.report.crosstable.CrossTableCellElement;
import com.ufida.report.crosstable.CrossTableHeader;
import com.ufida.report.crosstable.CrossTableModel;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 查找数据对话框
 * @author guogang
 *
 */
public class AnaReportFindDlg extends UfoDialog implements ItemListener{
	private AnaReportModel model=null;
	private ExAreaModel exAreamodel=null;
	private ExAreaCell[] listExArea = null;
	private UfoReport report=null;
	private JComboBox jcmbExArea=null;//扩展区域
	private JTextField jTxDataSetValue = null;//数据集
	private JComboBox jCmbFindCol = null;//查找列
	private JPanel jpnFindColName=null;//查找列名panel
	private JLabel jlFindColName=null;
	private JComboBox jCmbFindColName=null;
	private JPanel jpnAlternate = null;//查找值panel
	private JTextField jTxFindValue = null;
	private JComboBox jCmbFindValue = null;
	private JPanel jpnFindSet=null;//选项
	private JRadioButton jraUserDefined;//用户自定义
	private JRadioButton jraEnumType;//枚举值
	private JCheckBox jcbWholeString;//整个字符串
	private JCheckBox jcbCase;//匹配大小写
	private JButton findNextButton = null;
	private JButton closeButton = null;
	
	private CellPosition beginPos=null;//查找开始的位置


	public AnaReportFindDlg(UfoReport container,AnaReportModel model){
		super(container);
		this.report=container;
		this.model=model;
		initialize();
		refreshUI();
	}
	
	private void initialize() {
		this.setTitle(MultiLang.getString("find"));
		this.setContentPane(getJContentPane());
		this.setResizable(false);
		pack();

	}
	private JPanel getJContentPane() {
		JPanel jContentPane = new UIPanel();
		jContentPane.setLayout(new BorderLayout());
		jContentPane.add(getFindPanel(), BorderLayout.CENTER);
		jContentPane.add(getFindOperPanel(), BorderLayout.EAST);

		return jContentPane;
	}
	
	/**
	 * @i18n iufobi00024=可扩展区域
	 * @i18n miufo00241=数据集
	 * @i18n iufobi00035=查找值
	 */
	private JPanel getFindPanel(){
		JPanel jFindPanel=new UIPanel();
		GridBagLayout gridbaglayout = new GridBagLayout();
		GridBagConstraints gridbagconstraints = new GridBagConstraints();
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty=1.0D;
		gridbagconstraints.fill = GridBagConstraints.HORIZONTAL;
		gridbagconstraints.insets=new Insets(0,2,0,2);
		jFindPanel.setLayout(gridbaglayout);
		JLabel jlExArea=new UILabel(StringResource.getStringResource("iufobi00024"));
		gridbagconstraints.gridwidth = GridBagConstraints.RELATIVE;
		gridbaglayout.setConstraints(jlExArea, gridbagconstraints);
		jFindPanel.add(jlExArea);
		gridbagconstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridbaglayout.setConstraints(getCmbExArea(), gridbagconstraints);
		jFindPanel.add(getCmbExArea());
		JLabel jlDataset=new UILabel(StringResource.getStringResource("miufo00241"));
		gridbagconstraints.gridwidth = GridBagConstraints.RELATIVE;
		gridbaglayout.setConstraints(jlDataset, gridbagconstraints);
		jFindPanel.add(jlDataset);
		gridbagconstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridbaglayout.setConstraints(getTxDataSetValue(), gridbagconstraints);
		jFindPanel.add(getTxDataSetValue());
		gridbagconstraints.gridwidth = GridBagConstraints.RELATIVE;
		gridbaglayout.setConstraints(getFindColName(), gridbagconstraints);
		jFindPanel.add(getFindColName());
		gridbagconstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridbaglayout.setConstraints(getCmbFindCol(), gridbagconstraints);
		jFindPanel.add(getCmbFindCol());
		JLabel jlFindText=new UILabel(StringResource.getStringResource("iufobi00035"));
		gridbagconstraints.gridwidth = GridBagConstraints.RELATIVE;
		gridbaglayout.setConstraints(jlFindText, gridbagconstraints);
		jFindPanel.add(jlFindText);
		gridbagconstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridbaglayout.setConstraints(getPnAlternate(), gridbagconstraints);
		jFindPanel.add(getPnAlternate());
		gridbaglayout.setConstraints(getFindSetPanel(), gridbagconstraints);
		jFindPanel.add(getFindSetPanel());
		return jFindPanel;
	}
	
	private JPanel getFindColName(){
		if(jpnFindColName==null){
			jpnFindColName=new UIPanel();
			jpnFindColName.setName("FindColName");
			jpnFindColName.setLayout(new CardLayout());
			jpnFindColName.add(getTxFindColName(), getTxFindColName().getName());
			jpnFindColName.add(getCmbFindColName(),getCmbFindColName().getName());
		}
		return jpnFindColName;
	}
	/**
	 * @i18n iufobi00036=查找列
	 */
	private JLabel getTxFindColName(){
		if(jlFindColName==null){
			jlFindColName=new UILabel(StringResource.getStringResource("iufobi00036"));
			jlFindColName.setName("TxFindColName");
		}
		return jlFindColName;
	}
	/**
	 * @i18n iufobi00037=交叉行
	 * @i18n iufobi00038=交叉列
	 */
	private JComboBox getCmbFindColName(){
		if(jCmbFindColName==null){
			jCmbFindColName=new UIComboBox(new String[]{StringResource.getStringResource("iufobi00037"), StringResource.getStringResource("iufobi00038")});
			jCmbFindColName.setName("CmbFindColName");
			jCmbFindColName.addItemListener(this);
		}
		return jCmbFindColName;
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
	private JComboBox getCmbFindCol(){
		if(jCmbFindCol==null){
			jCmbFindCol=new UIComboBox();
			jCmbFindCol.setName("CmbFindCol");
			jCmbFindCol.addItemListener(this);
		}
		return jCmbFindCol;
	}
	private JPanel getPnAlternate(){
		if(jpnAlternate==null){
			jpnAlternate=new UIPanel();
			jpnAlternate.setName("PnAlternate");
			jpnAlternate.setLayout(new CardLayout());
			jpnAlternate.add(getTxFindValue(), getTxFindValue().getName());
			jpnAlternate.add(getCmbFindValue(), getCmbFindValue().getName());
		}
		return jpnAlternate;
	}
	
	private JTextField getTxFindValue(){
		if(jTxFindValue==null){
			jTxFindValue=new UITextField();
			jTxFindValue.setName("TxFindValue");
		}
		return jTxFindValue;
	}
	
	private JComboBox getCmbFindValue(){
		if(jCmbFindValue==null){
			jCmbFindValue=new UIComboBox();
			jCmbFindValue.setName("CmbFindValue");
		}
		return jCmbFindValue;
	}
	
	/**
	 * @i18n iufobi00039=用户自定义
	 */
	private JRadioButton getRaUserDefine(){
		if(jraUserDefined==null){
			jraUserDefined=new UIRadioButton(StringResource.getStringResource("iufobi00039"));
			jraUserDefined.addItemListener(this);
			jraUserDefined.setSelected(true);
		}
		return jraUserDefined;
	}
	/**
	 * @i18n iufobi00040=枚举值
	 */
	private JRadioButton getRaEnumValue(){
		if(jraEnumType==null){
			jraEnumType=new UIRadioButton(StringResource.getStringResource("iufobi00040"));
		}
		return jraEnumType;
	}
	/**
	 * @i18n iufobi00041=整个字符串
	 */
	private JCheckBox getUICheckBoxWhole(){
		if(jcbWholeString==null){
			jcbWholeString=new UICheckBox(StringResource.getStringResource("iufobi00041"));
		}
		return jcbWholeString;
	}
	/**
	 * @i18n iufobi00042=匹配大小写
	 */
	private JCheckBox getUICheckBoxCase(){
		if(jcbCase==null){
			jcbCase=new UICheckBox(StringResource.getStringResource("iufobi00042"));
		}
		return jcbCase;
	}
	/**
	 * @i18n iufobi00043=选项
	 */
	private JPanel getFindSetPanel(){
		if(jpnFindSet==null){
			jpnFindSet=new UIPanel();
			jpnFindSet.setBorder(new TitledBorder(new EtchedBorder(), StringResource.getStringResource("iufobi00043"), TitledBorder.LEFT, TitledBorder.TOP,new java.awt.Font("dialog", 0, 14)));
			jpnFindSet.setLayout(new GridLayout(2,2));
			ButtonGroup radioGroup=new ButtonGroup();
			radioGroup.add(getRaUserDefine());
			jpnFindSet.add(getRaUserDefine());
			radioGroup.add(getRaEnumValue());
			jpnFindSet.add(getRaEnumValue());
			jpnFindSet.add(getUICheckBoxWhole());
			jpnFindSet.add(getUICheckBoxCase());
		}
		return jpnFindSet;
	}
	private JPanel getFindOperPanel(){
		JPanel jFindOperPanel=new UIPanel();
		jFindOperPanel.setLayout(new BoxLayout(jFindOperPanel,BoxLayout.Y_AXIS));
		jFindOperPanel.add(Box.createVerticalStrut(10));
		JPanel panel1=new UIPanel();
		panel1.add(getFindNextButton());
		jFindOperPanel.add(panel1);
		jFindOperPanel.add(Box.createVerticalStrut(10));
		JPanel panel2=new UIPanel();
		panel2.add(getCloseButton());
		jFindOperPanel.add(panel2);
		return jFindOperPanel;
	}
	private JButton getFindNextButton() {
		if (findNextButton == null) {
			findNextButton = new nc.ui.pub.beans.UIButton();
			findNextButton.setText(MultiLang.getString("find"));
			findNextButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String filterValue;
					if (getRaUserDefine().isSelected()) {
						filterValue = getTxFindValue().getText().trim();
					}else {
						filterValue = getCmbFindValue().getSelectedItem()!=null?getCmbFindValue().getSelectedItem().toString():"";
					}
					
					findNext(getCmbFindCol().getSelectedItem(),filterValue,getUICheckBoxCase().isSelected(),getUICheckBoxWhole().isSelected());
					
				}
			});
		}
		return findNextButton;
	}
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new nc.ui.pub.beans.UIButton();
			closeButton.setText(MultiLang.getString("close"));
			closeButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					close();
				}
			});
		}
		return closeButton;
	}
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource()==getCmbExArea()&&e.getStateChange()==ItemEvent.SELECTED){
			beginPos=null;
			refreshUI();
		}
		if(e.getSource()==getRaUserDefine()){
			boolean userDefineSelected = getRaUserDefine().isSelected();
			CardLayout cardLayout = (CardLayout)getPnAlternate().getLayout();
			String compName =
				userDefineSelected
					? getTxFindValue().getName()
					: getCmbFindValue().getName();
			cardLayout.show(getPnAlternate(),compName);
			if (!userDefineSelected) {
				getAllEnumValues();
			}
		}
		if(e.getSource()==getCmbFindCol()&&e.getStateChange()==ItemEvent.SELECTED){
			
				if (e.getItem() instanceof CalField||e.getItem() instanceof FieldCountDef) {
					getRaEnumValue().setEnabled(false);
					getRaUserDefine().setSelected(true);
				} else {
					getRaEnumValue().setEnabled(true);
				}
			
			if (getRaEnumValue().isEnabled()&&getRaEnumValue().isSelected())
				getAllEnumValues();
		}
		if(e.getSource()==getCmbFindColName()&&e.getStateChange()==ItemEvent.SELECTED){//装载交叉查找列的值
			reLoadCmbFindColModel();
		}
	}
	private void refreshUI(){

		ExAreaCell selectedArea=(ExAreaCell)this.getCmbExArea().getSelectedItem();
		if(selectedArea!=null){
			if (selectedArea.getModel() instanceof AreaDataModel) {
				AreaDataModel areaData = (AreaDataModel) selectedArea
						.getModel();
				CardLayout cardLayout = (CardLayout)getFindColName().getLayout();
				String compName =null;
				if(areaData.isCross()){
					compName=getCmbFindColName().getName();
				}else{
					compName=getTxFindColName().getName();
				}
				cardLayout.show(getFindColName(),compName);
				
				if (areaData.getDSTool() != null) {
					getCmbFindCol().setEnabled(true);
					getTxFindValue().setEnabled(true);
                    getRaEnumValue().setEnabled(true);					
                    getRaUserDefine().setEnabled(true);
					getUICheckBoxCase().setEnabled(true);
					getUICheckBoxWhole().setEnabled(true);
					
					
					this.getTxDataSetValue().setText(areaData.getDSInfo().getName());
					
					//不应该是所有的字段而应该是扩展区域包含的字段
					if (compName.equals(getTxFindColName().getName())) {
						DefaultComboBoxModel cmbModel = (DefaultComboBoxModel) getCmbFindCol()
						.getModel();
						cmbModel.removeAllElements();
						ArrayList<AnaRepField> allflds=areaData.getAreaFields().getAllAnaFields();
						AnaRepField exFld=null;
						for (int i = 0; i < allflds.size(); i++) {
							exFld=allflds.get(i);
							cmbModel.addElement(exFld.getField());
						}
					} else {
						reLoadCmbFindColModel();
					}
				    if(getRaEnumValue().isSelected()){
				    	getAllEnumValues();
				    }
				}else{
					getTxDataSetValue().setText("");
					((DefaultComboBoxModel)getCmbFindCol().getModel()).removeAllElements();
					getCmbFindCol().setEnabled(false);
					getTxFindValue().setEnabled(false);
					getRaEnumValue().setEnabled(false);
					getRaUserDefine().setEnabled(false);
					getUICheckBoxCase().setEnabled(false);
					getUICheckBoxWhole().setEnabled(false);
					
				}
			}
		}
	}
	
	/**
	 * 加载交叉查找行
	 * @i18n iufobi00037=交叉行
	 */
	private void reLoadCmbFindColModel() {
		ExAreaCell selectedArea = (ExAreaCell) this.getCmbExArea()
				.getSelectedItem();
		if (selectedArea != null) {
			if (selectedArea.getModel() instanceof AreaDataModel) {
				AreaDataModel areaData = (AreaDataModel) selectedArea
						.getModel();
				CrossFindCol cfc = null;
				DefaultComboBoxModel cmbModel = (DefaultComboBoxModel) getCmbFindCol()
				.getModel();
		         cmbModel.removeAllElements();
				if (areaData.getDSTool() != null) {
					AnaDataSetTool dsTool = areaData.getDSTool();

					
					Field[] multiFld = null;
					String[] fldNames = null;
					FieldCountDef[] measFld = null;
//					FieldCountDef[] aggrFld = null;
					if (getCmbFindColName().getSelectedItem().equals(StringResource.getStringResource("iufobi00037"))) {
						multiFld = areaData.getCrossSet().getCrossSet()
								.getRowFlds();
					} else {
						multiFld = areaData.getCrossSet().getCrossSet()
								.getColFlds();
					}
					fldNames = new String[multiFld.length];
					for (int i = 0; i < fldNames.length; i++) {
						fldNames[i] = multiFld[i].getFldname();
					}
					DataSet dataset=(DataSet)dsTool.getDSDef().clone();
					areaData.setDSParametersValue(dataset);
					Object[][] disObjs = dsTool
							.getDistinctValue(dataset,fldNames, null);
				
					measFld = areaData.getCrossSet().getCrossSet()
							.getMeasureFlds();
//					aggrFld = areaData.getCrossSet().getCrossSet()
//							.getAggrFields();
					Object[] headerValues=new Object[fldNames.length];
					for (int i = 0; i < disObjs.length; i++) {
						System.arraycopy(disObjs[i], 0, headerValues, 0, headerValues.length);
						for (int j = 0; j < measFld.length; j++) {
							cfc = new CrossFindCol(headerValues, measFld[j]);
							cmbModel.addElement(cfc);
						}
//						for (int k = 0; k < aggrFld.length; k++) {
//							cfc = new CrossFindCol(disObjs[i], aggrFld[k]);
//							cmbModel.addElement(cfc);
//						}
					}
				}
				CrossTableModel dataModel=null;
				if(areaData.getCrossDataModel()!=null){
					dataModel=areaData.getCrossDataModel().getCrossTabel();
				}
				if(dataModel!=null){
					CrossTableHeader tableHeader=null;
					CrossHeader header=null;
					boolean isRow=getCmbFindColName().getSelectedItem().equals(StringResource.getStringResource("iufobi00037"));
					tableHeader=isRow?dataModel.getRowHeader():dataModel.getColHeader();
					for(int i=0;i<tableHeader.size();i++){
						header=tableHeader.get(i);
						if(header.isAggr()){
							for(int j=0;j<header.getAggrField().size();j++){
								cfc = new CrossFindCol(header.getHeaderValues(), header.getAggrField().get(j));
								cmbModel.addElement(cfc);
							}
						}
					}
					
				}
			}
		}
	}
	private void getAllEnumValues(){
		ExAreaCell selectedArea=(ExAreaCell)this.getCmbExArea().getSelectedItem();
		if(selectedArea!=null){
			if (selectedArea.getModel() instanceof AreaDataModel) {
				AreaDataModel areaData = (AreaDataModel) selectedArea
						.getModel();
				if(areaData.getDSTool()!=null){
					AnaDataSetTool dsTool=areaData.getDSTool();
					Object selectFld=getCmbFindCol().getSelectedItem();
					if(selectFld instanceof Field){//是普通字段
						DefaultComboBoxModel cmbModel = (DefaultComboBoxModel)getCmbFindValue().getModel();
						cmbModel.removeAllElements();
						if (!(selectFld instanceof CalField)) {
							DataSet dataset=(DataSet)dsTool.getDSDef().clone();
							areaData.setDSParametersValue(dataset);
							Object[][] enumValues = dsTool.getDistinctValue(dataset,new String[] { ((Field) selectFld).getFldname() }, null);
							if (enumValues != null && enumValues.length > 0) {
								Object data = null;
								for (int i = 0; i < enumValues.length; i++) {
									data = enumValues[i][0];
									if(data instanceof BigDecimal){
										data=((BigDecimal)data).doubleValue();
									}
									if (data instanceof Double) {
										cmbModel.addElement(new UFDouble(
												(Double) data, 2));
									} else {
										cmbModel.addElement(data);
									}
								}
							}
						}
					}
					//如果是交叉表的??
					if(selectFld instanceof CrossFindCol){
						getCrossHeaderEnumValues((CrossFindCol)selectFld);
					}
				}
			}
		}
	}
	/**
	 * 获取交叉枚举值
	 * @i18n iufobi00037=交叉行
	 */
	private void getCrossHeaderEnumValues(CrossFindCol selectFld){
		ExAreaCell selectedArea=(ExAreaCell)this.getCmbExArea().getSelectedItem();
		if(selectedArea!=null){
			if (selectedArea.getModel() instanceof AreaDataModel) {
				AreaDataModel areaData = (AreaDataModel) selectedArea
						.getModel();
				CrossTableModel dataModel=null;
				if(areaData.getCrossDataModel()!=null){
					dataModel=areaData.getCrossDataModel().getCrossTabel();
				}
				if(dataModel!=null){
					CrossTableHeader tableHeader=null;
					boolean isRow=getCmbFindColName().getSelectedItem().equals(StringResource.getStringResource("iufobi00037"));
					tableHeader=isRow?dataModel.getRowHeader():dataModel.getColHeader();
					int i=0;
					for(;i<tableHeader.size();i++){
						if(selectFld.isHeaderContain(tableHeader.get(i))){
							break;
						}
					}
					DefaultComboBoxModel cmbModel = (DefaultComboBoxModel)getCmbFindValue().getModel();
					cmbModel.removeAllElements();
					Object data=null;
					if(i<tableHeader.size()){
					if(isRow){
						CrossTableCellElement[] rowData=dataModel.getCrossDatas()[i];
						for(int j=0;j<rowData.length;j++){
							if(rowData[j]!=null&&rowData[j].getMeasureKey().equals(selectFld.getFld())){
								data=rowData[j].getData();
								if(data!=null){
									if(data instanceof Double){
										data=new UFDouble((Double)data,2);
									}
									if(cmbModel.getIndexOf(data)<0)
									    cmbModel.addElement(data);
								}
							}
						}
					}else{
						for(int j=0;j<dataModel.getCrossDatas().length;j++){
							if(dataModel.getCrossDatas()[j][i]!=null&&dataModel.getCrossDatas()[j][i].getMeasureKey().equals(selectFld.getFld())){
								data=dataModel.getCrossDatas()[j][i].getData();
								if(data!=null){
									if(data instanceof Double){
										data=new UFDouble((Double)data,2);
									}
									if(cmbModel.getIndexOf(data)<0)
									  cmbModel.addElement(data);
								}
							}
						}
					}
					}
				}
			}
		}
		
		
	}
	public ExAreaModel getExAreaModel() {
		if (exAreamodel == null) {
			exAreamodel = ExAreaModel.getInstance(model.getCellsModel());
		}
		return exAreamodel;
	}
	private ExAreaCell[] getExAreaCells(){
		if(listExArea==null){
			ArrayList<ExAreaCell> temps=new ArrayList<ExAreaCell>();
			ExAreaCell[] allEx=getExAreaModel().getExAreaCells();
			if(allEx!=null&&allEx.length>0){
				for(int i=0;i<allEx.length;i++){
					if(allEx[i].getModel() instanceof AreaDataModel){
						if(allEx[i].getExAreaType()!=ExAreaCell.EX_TYPE_CHART){
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
		CellsModel cellsModel = model.getCellsModel();
		AreaPosition[] selCells =cellsModel.getSelectModel().getSelectedAreas();
		
		if (selCells!=null&&selCells.length>0&&selCells[0] != null) {
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
	private CellsModel getCellsModel(){
		return this.model.getCellsModel();
	}
	/**
	 * 
	 * @param selectedFld 查找字段
	 * @param findContent 查找字段的内容
	 * @param diffBigSmall 是否区分大小写
	 * @param matchAll 整字符串匹配
	 * @i18n iufobi00044=不存在符合条件的记录
	 */
	public boolean findNext(Object selectedFld,String findContent, boolean diffBigSmall,boolean matchAll) {
		if(selectedFld==null||findContent==null||findContent.equals("")){
			return false;
		}
		 if(beginPos==null){
			 beginPos = ((ExAreaCell)this.getCmbExArea().getSelectedItem()).getArea().getStart();
		 }
		CellPosition nextPos = findNextPos(beginPos, selectedFld,findContent, diffBigSmall,matchAll);
		if(nextPos != null&&getCellsModel()!=null){
			beginPos=nextPos;
			report.getTable().getCells().changeSelectionByUser(nextPos.getRow(), nextPos.getColumn(), false, false, false);
			return true;
		}else{
			UfoPublic.sendMessage(StringResource.getStringResource("iufobi00044"), this);
		}
		return false;
	}
	
	
	private CellPosition findNextPos(CellPosition beginPos,Object selectedFld, String findContent, boolean diffBigSmall,boolean matchAll){
		CellPosition nextPos = beginPos;
		while(true){
			nextPos = getNextPos(nextPos);	
			if(isMatch(nextPos,selectedFld,findContent,diffBigSmall,matchAll)){
				return nextPos;
			}			
			if(nextPos.equals(beginPos)){
				return null;
			}
		}	
	}
	private CellPosition getNextPos(CellPosition curPos){
		AreaPosition allArea = ((ExAreaCell)this.getCmbExArea().getSelectedItem()).getArea();
		ArrayList allPos = getCellsModel().getSeperateCellPos(allArea);
		int index = allPos.indexOf(curPos);
		index ++;
		if(index == allPos.size()){
			index = 0;
		}
		return (CellPosition) allPos.get(index);
	}
	/**
	 * 单元位置是否匹配查找字符串.
	 * @param nextPos
	 * @param findContent
	 * @param diffBigSmall 
	 * @return
	 * @i18n iufobi00037=交叉行
	 */
	private boolean isMatch(CellPosition curPos, Object selectedFld,String findContent, boolean diffBigSmall,boolean matchAll) {
		
		Cell cell= this.model.getFormatCell(getCellsModel(), curPos);
		if(cell==null){
			return false;
		}
		AnaRepField anaFld=(AnaRepField)cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
		if(anaFld==null){
			return false;
		}
		Object objValue=null;
		if(selectedFld instanceof Field){
			if(!anaFld.getField().equals((Field)selectedFld)){
				return false;
			}
			objValue= getCellsModel().getCellValue(curPos);
			
		}
		if (selectedFld instanceof CrossFindCol) {
			if (getCellsModel().getCellValue(curPos) instanceof CrossTableCellElement) {
				CrossFindCol findCol = (CrossFindCol) selectedFld;
				boolean isRow = getCmbFindColName().getSelectedItem().equals(
						StringResource.getStringResource("iufobi00037"));
				CrossTableCellElement cellData = (CrossTableCellElement) getCellsModel()
						.getCellValue(curPos);
				if (findCol.getFld().equals(cellData.getMeasureKey())) {
					if (findCol.isHeaderContain(isRow ? cellData.getRowHeader()
							: cellData.getColHeader())) {
						objValue=cellData.getData();
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		
		if(!isValidateType(objValue)){
			return false;
		}
		String value="";
		IufoFormat iufoFormat = (IufoFormat) cell.getFormat();
		if(objValue instanceof BigDecimal){
			objValue=((BigDecimal)objValue).doubleValue();
		}
		if(iufoFormat!=null&&iufoFormat.getCellType()==TableConstant.CELLTYPE_NUMBER&&objValue instanceof Double){
			value=iufoFormat.getString((Double)objValue);
		}else{
			value=objValue.toString();
		}
		
		
		if(isNull(value)){
			if(isNull(findContent)){
				return true;
			}else{
				return false;
			}
		}else{
			//value不是空值.
			if(isNull(findContent)){
				return false;
			}
			String strValue = (String) value;
			if(!diffBigSmall){
				strValue = strValue.toLowerCase();
				findContent = findContent.toLowerCase();
			}
			if (matchAll) {
				return strValue.equals(findContent);
			} else {
				return strValue.indexOf(findContent) > -1;
			}
				
		}
	}
	private boolean isNull(String str){
		return str == null || str.equals("");
	}
	private boolean isValidateType(Object selectValue){
		boolean isType=false;
		if(selectValue instanceof String||selectValue instanceof Double||selectValue instanceof BigDecimal||selectValue instanceof Integer){
			isType=true;
		}
		
		return isType;
	}
	
	private class CrossFindCol{
		private Object[] headerValue=null;
		private FieldCountDef fld=null;
		public CrossFindCol(Object[] headervalue,FieldCountDef fld){
			this.headerValue=new Object[headervalue.length+1];
			System.arraycopy(headervalue, 0, this.headerValue, 0, headervalue.length);
			this.headerValue[headervalue.length]=fld.getFldname();
			this.fld=fld;
		}
		public FieldCountDef getFld() {
			return fld;
		}
		
		public boolean isHeaderContain(CrossHeader header){
			boolean isContain=false;
			if(header!=null&&header.getHeaderValues()!=null&&headerValue!=null){
				int equalNum=0;
				for(int i=0;i<header.getHeaderValues().length;i++){
					for(int j=0;j<headerValue.length;j++){
						if(headerValue[j].equals(header.getHeaderValues()[i])){
							equalNum++;
							break;
						}
					}
				}
				if(equalNum==header.getHeaderValues().length){
					isContain=true;
				}
			}
			return isContain;
		}
		@Override
		public String toString() {
			StringBuffer result = new StringBuffer();
			boolean hasMeas=false;
			for (int i = 0; i < headerValue.length; i++) {
				if(headerValue[i].equals(fld.getSubTotalName())){
					result.append(fld.getCaption());
					hasMeas=true;
				}else{
					result.append(headerValue[i]);
				}
				
				if (i < headerValue.length - 1) {
					result.append(",");
				}
			}
			if(!hasMeas){
				result.append(",").append(fld.getCaption());
			}
			return result.toString();
		}
		@Override
		public boolean equals(Object obj) {
			if(obj==this)
				return true;
			if(obj instanceof CrossFindCol){
				return obj.toString().equals(this.toString());
			}
			return false;
		}
		
	}
	public static void main(String[] args){
		AnaReportFindDlg dlg=new AnaReportFindDlg(null,null);
		dlg.show();
	}
}
 