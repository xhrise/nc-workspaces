package com.ufida.report.anareport.edit;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.ibm.db2.jcc.a.ed;
import com.ufida.dataset.descriptor.DescriptorType;
import com.ufida.dataset.metadata.Field;
import com.ufida.report.anareport.applet.AnaReportFieldAction;
import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AnaCrossTableSet;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.crosstable.CrossMeasureOrderSet;
import com.ufida.report.crosstable.CrossOrderSet;
import com.ufida.report.crosstable.CrossTableSet;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;

/**
 * 分析表中字段的排序属性设置
 * add by guogang 2009-3-17 交叉指标的排序功能
 * @author ll
 * 
 */
public class AnaFieldOrderExt extends AbsActionExt {

	/**
	 * @i18n miufo1001305=升序
	 */
	private static final String RESID_SETFIELD_ASCENDING = StringResource.getStringResource("miufo1001305");
	/**
	 * @i18n miufo1001306=降序
	 */
	private static final String RESID_SETFIELD_DESCENDING = StringResource.getStringResource("miufo1001306");
	/**
	 * @i18n miufo00454=取消排序
	 */
	private static final String RESID_SETFIELD_NOORDER = StringResource.getStringResource("miufo00454");
	/**
	 * @i18n miufo00178=字段设置
	 */
	private static final String RESID_SETFIELD = StringResource.getStringResource("miufo00178");
	
	private AnaReportPlugin m_plugin = null;
	private int m_orderType = AnaRepField.ORDERTYPE_NONE;
	
	private ImageIcon desImage=null;
	private ImageIcon aesImage=null;
	private ImageIcon noOrderImage=null;

	private class AnaFieldCommand extends UfoCommand {
		public void execute(Object[] params) {
			if (params == null)
				return;

			UfoReport report = (UfoReport) params[0];
			AreaDataModel areaModel = (AreaDataModel) params[1];
			CellPosition[] selectedPos=(CellPosition[])params[2];
			changeFieldOrderType( m_plugin.getModel().getFormatModel(), areaModel,selectedPos);
		}
	}

	public AnaFieldOrderExt(AnaReportPlugin plugin, int fldType) {
		super();
		this.m_plugin = plugin;
		this.m_orderType = fldType;
	}

	public AnaFieldOrderExt(AnaReportPlugin plugin){
		super();
		this.m_plugin = plugin;
	}
	
	public UfoCommand getCommand() {
		return new AnaFieldCommand();
	}

	public Object[] getParams(UfoReport container) {

		return doGetParams(container);
	}

	public boolean isEnabled(Component focusComp) {
		if(!StateUtil.isAreaSel(m_plugin.getReport(), focusComp))
			return false;
		AnaReportModel model = m_plugin.getModel();
		CellsModel fmtModel = model.getFormatModel();
		CellPosition anchorCellPos=m_plugin.getFormatAnchorPos();
			
		Cell cell = fmtModel.getCell(anchorCellPos);
		if(cell == null)
			return false;
		AreaPosition area = AreaPosition.getInstance(anchorCellPos, anchorCellPos);
		AreaDataModel areaData = m_plugin.getExAreaModel(fmtModel,area);
		if(areaData == null || areaData.getDSTool() == null)
			return false;
		if(!areaData.getDSTool().isSupport(DescriptorType.SortDescriptor))//对于不支持排序的数据集，此功能不可用
			return false;

		AnaRepField fld =(AnaRepField)cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
		
		if( fld== null)
			return false;
		if(!areaData.isCross() && fld.isAggrFld())// @edit by ll at 2009-12-1,下午02:54:05 对于列表中的统计字段，不让设置排序
			return false;
		else
			return true;		 
	}

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes3 = new ActionUIDes();
		uiDes3.setToolBar(true);
		uiDes3.setGroup(StringResource.getStringResource(RESID_SETFIELD));
		uiDes3.setPaths(new String[] {});
		if (m_orderType == AnaRepField.ORDERTYPE_ASCENDING) {
			uiDes3.setName(StringResource.getStringResource(RESID_SETFIELD_ASCENDING));
			uiDes3.setTooltip(StringResource.getStringResource(RESID_SETFIELD_ASCENDING));
			uiDes3.setImageFile("reportcore/ascending.gif");
		} else if (m_orderType == AnaRepField.ORDERTYPE_DESCENDING) {
			uiDes3.setName(StringResource.getStringResource(RESID_SETFIELD_DESCENDING));
			uiDes3.setTooltip(StringResource.getStringResource(RESID_SETFIELD_DESCENDING));
			uiDes3.setImageFile("reportcore/descending.gif");
		} else if (m_orderType == AnaRepField.ORDERTYPE_NONE) {
			uiDes3.setName(StringResource.getStringResource(RESID_SETFIELD_NOORDER));
			uiDes3.setTooltip(StringResource.getStringResource(RESID_SETFIELD_NOORDER));
			uiDes3.setImageFile("reportcore/no_order.gif");
		}
		return new ActionUIDes[] { uiDes3 };
	}

	/**
	 * override
	 * @see AbsActionExt.initListenerByComp();
	 */
	public void initListenerByComp(Component stateChangeComp) {		
		if(stateChangeComp instanceof JButton){
			final JButton toolButton = (JButton)stateChangeComp;
			
			m_plugin.getModel().getCellsModel().getSelectModel().addSelectModelListener(
					new SelectListener(){
						public void selectedChanged(SelectEvent e) {
							AnaReportModel model = m_plugin.getModel();
							CellsModel cellsModel = model.getFormatModel();
							CellPosition cellPos = m_plugin.getFormatAnchorPos();
							Cell cell = cellsModel.getCell(cellPos);
							if(cell == null)
								return;
							AnaRepField fld = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);								
							if (fld == null)
								return;
                            if(fld.getOrderType() == AnaRepField.ORDERTYPE_ASCENDING){
                            	setOrderType(AnaRepField.ORDERTYPE_DESCENDING);
                            	toolButton.setName(StringResource.getStringResource(RESID_SETFIELD_DESCENDING));
                            	toolButton.setToolTipText(StringResource.getStringResource(RESID_SETFIELD_DESCENDING));
                            	toolButton.setIcon(getDesImage());
                            } else if(fld.getOrderType() == AnaRepField.ORDERTYPE_DESCENDING){
                            	setOrderType(AnaRepField.ORDERTYPE_NONE);
                            	toolButton.setName(StringResource.getStringResource(RESID_SETFIELD_NOORDER));
                            	toolButton.setToolTipText(StringResource.getStringResource(RESID_SETFIELD_NOORDER));
                            	toolButton.setIcon(getNoOrderImage());
                            } else if(fld.getOrderType() == AnaRepField.ORDERTYPE_NONE){
                            	setOrderType(AnaRepField.ORDERTYPE_ASCENDING);
                            	toolButton.setName(StringResource.getStringResource(RESID_SETFIELD_ASCENDING));
                            	toolButton.setToolTipText(StringResource.getStringResource(RESID_SETFIELD_ASCENDING));
                            	toolButton.setIcon(getAesImage());
                            }
							
						}
						
					});
			
			toolButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
                if(m_orderType == AnaRepField.ORDERTYPE_ASCENDING){
                	setOrderType(AnaRepField.ORDERTYPE_DESCENDING);
                	toolButton.setName(StringResource.getStringResource(RESID_SETFIELD_DESCENDING));
                	toolButton.setToolTipText(StringResource.getStringResource(RESID_SETFIELD_DESCENDING));
                	toolButton.setIcon(getDesImage());
                } else if(m_orderType == AnaRepField.ORDERTYPE_DESCENDING){
                	setOrderType(AnaRepField.ORDERTYPE_NONE);
                	toolButton.setName(StringResource.getStringResource(RESID_SETFIELD_NOORDER));
                	toolButton.setToolTipText(StringResource.getStringResource(RESID_SETFIELD_NOORDER));
                	toolButton.setIcon(getNoOrderImage());
                } else if(m_orderType == AnaRepField.ORDERTYPE_NONE){
                	setOrderType(AnaRepField.ORDERTYPE_ASCENDING);
                	toolButton.setName(StringResource.getStringResource(RESID_SETFIELD_ASCENDING));
                	toolButton.setToolTipText(StringResource.getStringResource(RESID_SETFIELD_ASCENDING));
                	toolButton.setIcon(getAesImage());
                }
				
                if(!m_plugin.getModel().isFormatState())
        			m_plugin.refreshDataModel(true);//由于插件执行优先于此监听，所以放这里刷新数据
				
				}
				
			});
		}
	}
	
	private ImageIcon getDesImage(){
		if(desImage==null){
			desImage=ResConst.getImageIcon("reportcore/descending.gif");
		}
		return desImage;
	}

	private ImageIcon getAesImage(){
		if(aesImage==null){
			aesImage=ResConst.getImageIcon("reportcore/ascending.gif");
		}
		return aesImage;
	}
	
	private ImageIcon getNoOrderImage(){
		if(noOrderImage==null){
			noOrderImage=ResConst.getImageIcon("reportcore/no_order.gif");
		}
		return noOrderImage;
	}
	// 执行区域字段的属性设置
	private void changeFieldOrderType(CellsModel model, AreaDataModel aModel,CellPosition[] area) {
		if (area == null || area.length == 0)
			return;
		AnaRepField fld=null;
		for (CellPosition pos : area) {

			fld = getRepField(model, pos);
			if (fld == null)
				continue;

			if (fld.getOrderType() == m_orderType)// modify by wangyga 2008-8-21 模型扩展格式发生改变时，通知组件进行绘制
				continue;
			fld.setOrderType(m_orderType);
			//同时修改排序管理的设置 modify by guogang 2009-5-7
			aModel.updateOrderMng(fld);
			
			model.fireExtPropChanged(pos);
			if (aModel.isCross()) {
				if (aModel.getCrossSet() != null)
					aModel.getCrossSet().setDirty(true);
			}

		}
		if(!m_plugin.getModel().isFormatState())
			m_plugin.refreshDataModel(true);
		
		return;
	}

	
	private Object[] doGetParams(UfoReport container) {
		CellsModel fmtModel = m_plugin.getModel().getFormatModel();
		AreaPosition[] area =m_plugin.getFormatSelected();
		if (area == null || area.length == 0)
			return null;
		AreaDataModel areaModel = m_plugin.getExAreaModel(fmtModel, area[0]);
		if (areaModel == null || areaModel.getDSPK() == null)
			return null;
		CellPosition[] selectedPos=m_plugin.getSelectedPos(fmtModel, area[0]);
		if (!AnaReportFieldAction.isSelAnaField(fmtModel,selectedPos)) //modify by wangyga 2008-09-17 没有字段的地方，已经控制按钮不可用
			return null;
		if (selectedPos.length == 1) {// 对交叉指标的排序设置
			AnaRepField repField = getRepField(fmtModel, selectedPos[0]);
			if (repField != null
					&& (repField.getFieldType() == AnaRepField.Type_CROSS_MEASURE || repField
							.getFieldType() == AnaRepField.Type_CROSS_SUBTOTAL)
					&& areaModel.getCrossSet() != null) {
				doCrossMeasureOrder(container,repField,areaModel.getCrossSet());
				fmtModel.fireExtPropChanged(area[0]);
				return null;
			}
		}
		return new Object[] { container, areaModel,selectedPos};
	}
	/**
	 * 处理交叉指标的排序
	 * @param container
	 * @param anaFld
	 * @param crosstab
	 */
	private void doCrossMeasureOrder(Container container,AnaRepField anaFld,AnaCrossTableSet crosstab){

		Field[] rowField = crosstab.getCrossSet().genHeaderField(true);
		Field[] colField = crosstab.getCrossSet().genHeaderField(false);
		CrossMeasureOrderSet rowSet = getInitCrossMeasureOrderSet(true,
				rowField,colField, anaFld, crosstab.getCrossSet());
		CrossMeasureOrderSet colSet = getInitCrossMeasureOrderSet(false,
				colField,rowField, anaFld, crosstab.getCrossSet());
		if (rowSet != null || colSet != null) {
			if (anaFld.getFieldCountDef() != null
					&& anaFld.getFieldCountDef().getRangeFld() != null) {
				CrossOrderSet orderInfo = null;
				if (anaFld.getDimInfo().getOrderSet() instanceof CrossOrderSet) {
					orderInfo = (CrossOrderSet) anaFld.getDimInfo()
							.getOrderSet();
				}
				if (orderInfo == null) {
					orderInfo = new CrossOrderSet();
				}
				if (rowSet != null && !rowSet.isEditable()) {
					rowSet.setOrderType(this.m_orderType);
					orderInfo.setRowOrderSet(rowSet);
					anaFld.getDimInfo().setOrderType(orderInfo);
					m_plugin.setDirty(true);
					crosstab.setDirty(true);
					if (!m_plugin.getModel().isFormatState()) {// 数据态的设置
						// 3。更新数据浏览态的表格模型
						m_plugin.refreshDataModel(true);
					}
					return;
				}
				if (colSet != null && !colSet.isEditable()) {
					colSet.setOrderType(this.m_orderType);
					orderInfo.setColOrderSet(colSet);
					anaFld.getDimInfo().setOrderType(orderInfo);
					m_plugin.setDirty(true);
					crosstab.setDirty(true);
					if (!m_plugin.getModel().isFormatState()) {// 数据态的设置
						// 3。更新数据浏览态的表格模型
						m_plugin.refreshDataModel(true);
					}
					return;
				}

			}
			CrossMeasureOrderSetDlg dlg = new CrossMeasureOrderSetDlg(
					container, rowSet, rowField, colSet, colField,crosstab.getDsTool());
			dlg.show();
			if (dlg.getResult() == UfoDialog.ID_OK) {
				rowSet=dlg.getOrderSetInfo(true);
				colSet=dlg.getOrderSetInfo(false);
				CrossOrderSet orderInfo=null;
				if(anaFld.getDimInfo().getOrderSet() instanceof CrossOrderSet){
					orderInfo=(CrossOrderSet)anaFld.getDimInfo().getOrderSet();
				}
				if(rowSet!=null||colSet!=null){
					if(orderInfo==null){
						orderInfo=new CrossOrderSet();
					}
					orderInfo.setRowOrderSet(rowSet);
					orderInfo.setColOrderSet(colSet);
					anaFld.getDimInfo().setOrderType(orderInfo);
					m_plugin.setDirty(true);
					crosstab.setDirty(true);
				}
				if (!m_plugin.getModel().isFormatState()) {// 数据态的设置
					// 3。更新数据浏览态的表格模型
					m_plugin.refreshDataModel(true);
				}
			
			}
		}
	
	}
	private void setOrderType(int type) {
		this.m_orderType = type;
	}
	private AnaRepField getRepField(CellsModel model,CellPosition cellPos){
		AnaRepField repField=null;
		Cell cell = model.getCell(cellPos);
		if (cell != null) {
			repField = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
		}
		return repField;
	}
	/**
     * 根据指标排序的规则,获取指标排序设置
     * @create by guogang at Feb 28, 2009,7:31:42 PM
     *
     * @param isRow
     * @param measure
     * @param crossset
     * @return
     */
	private CrossMeasureOrderSet getInitCrossMeasureOrderSet(boolean isRow,Field[] flds,Field[] otherFlds,AnaRepField measureFld,CrossTableSet crossset){
		CrossOrderSet orderInfo=null;
		CrossMeasureOrderSet orderSet=null;
		if(measureFld.getDimInfo().getOrderSet() instanceof CrossOrderSet){
			orderInfo=(CrossOrderSet)measureFld.getDimInfo().getOrderSet();
		}
		if(orderInfo!=null){
			if(isRow){
				orderSet=orderInfo.getRowOrderSet();
			}else{
				orderSet=orderInfo.getColOrderSet();
			}
			if(orderSet!=null&&orderSet.isChange(flds)){
				orderSet=null;
				if(isRow){
					orderInfo.setRowOrderSet(orderSet);
				}else{
					orderInfo.setColOrderSet(orderSet);
				}
			}
		}		
		if(orderSet==null){
			orderSet=(CrossMeasureOrderSet)crossset.geneCrossAnalyseSet(isRow,flds,otherFlds,measureFld.getFieldCountDef(),CrossTableSet.CROSSANALYSETYPE_ORDER);
		}
		return orderSet;
	}
}
 