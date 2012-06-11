package com.ufida.report.anareport.applet;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.util.NCOptionPane;

import com.ufida.dataset.descriptor.DescriptorType;
import com.ufida.dataset.metadata.DataTypeConstant;
import com.ufida.dataset.metadata.Field;
import com.ufida.report.adhoc.model.IFldCountType;
import com.ufida.report.anareport.model.AnaCellCombine;
import com.ufida.report.anareport.model.AnaCrossTableSet;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.anareport.model.FieldCountDef;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.menu.UFPopupMenu;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;

/**
 * 分析表中字段的属性设置
 * 
 * @author ll
 * 
 */
public class AnaFieldTypeExt extends AbsActionExt {

	/**
	 * @i18n miufo1001596=分组
	 */
	private static final String RESID_SETFIELD_GROUP = "miufo1001596";
	/**
	 * @i18n miufo00274=取消分组
	 */
	private static final String RESID_SETFIELD_DETAIL = "miufo00274";
	/**
	 * @i18n miufo00275=取消统计
	 */
	private static final String RESID_CANCELFIELD_CALC = "miufo00275";
	/**
	 * @i18n miufo00276=统计
	 */
	static final String RESID_SETFIELD_CALC = "miufo00276";
	/**
	 * @i18n miufo00277=交叉小计
	 */
	static final String RESID_SETFIELD_SUBTOTAL = "miufo00277";
	/**
	 * @i18n miufo00178=字段设置
	 */
	public static final String RESID_SETFIELD = "miufo00178";
	private JComboBox item = null;
	
	private AnaReportPlugin m_plugin = null;
	private int m_fldType = AnaRepField.TYPE_GROUP_FIELD;
	
	private class AnaFieldCommand extends UfoCommand {
		public void execute(Object[] params) {
			if (params == null)
				return;

			UfoReport report = (UfoReport) params[0];
			CellPosition[] fmtcells = m_plugin.getModel().getFormatPoses(report.getCellsModel(),
					report.getCellsModel().getSelectModel().getSelectedAreas());
			changeAnaFieldType(m_plugin.getModel().getFormatModel(), m_fldType, params, fmtcells);
			if(m_fldType == AnaRepField.TYPE_CALC_FIELD && !m_plugin.getModel().isFormatState())
				m_plugin.refreshDataModel(true);			
		}
	}


	public AnaFieldTypeExt(AnaReportPlugin plugin, int fldType) {
		super();
		m_plugin = plugin;
		m_fldType = fldType;
	}

	public UfoCommand getCommand() {
		return new AnaFieldCommand();
	}

	public Object[] getParams(UfoReport container) {

		return doGetParams(container);
	}

	public boolean isEnabled(Component focusComp) {
		if(!m_plugin.getModel().isFormatState() && m_fldType == AnaRepField.Type_CROSS_SUBTOTAL)
			return false;
		AnaReportModel model = m_plugin.getModel();
		CellsModel fmtModel = model.getFormatModel();
		AreaPosition[] areaPos = m_plugin.getFormatSelected();	
		if(areaPos == null || areaPos.length == 0)
			return false;
		if(isHasChart(areaPos[0]))//图表处不能定义统计字段，分组，排序
			return false;
		
		for (AreaPosition area : areaPos) {
			AreaDataModel areaData = m_plugin.getExAreaModel(fmtModel, area);
			if (areaData == null || areaData.getDSTool() == null)
				return false;
			if (!areaData.getDSTool().isSupport(DescriptorType.AggrDescriptor))// 对于不支持汇总的数据集，此功能不可用
				return false;
			AnaCrossTableSet crossTableSet = areaData.getCrossSet();
			if (m_fldType == AnaRepField.TYPE_GROUP_FIELD) {// 交叉区域中，分组字段不能用
				CellPosition[] selectedPos=m_plugin.getSelectedPos(fmtModel, area);
				if (areaData.isCross())
					return false;
				else if(AnaReportFieldAction.isSelAnaField(fmtModel,selectedPos))
					return true;
				else
					return false;
			}
			if (m_fldType == AnaRepField.TYPE_CALC_FIELD
					|| m_fldType == AnaRepField.TYPE_CANCEL_CALC_FIELD
					|| m_fldType == AnaRepField.Type_CROSS_SUBTOTAL
					|| m_fldType == AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL) {// 统计
				Cell cell = fmtModel.getCell(area.getStart());
				AnaRepField field = null;
				if (cell != null
						&& cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO) != null) {
					field = (AnaRepField) cell
							.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
				}
                
				if (m_fldType == AnaRepField.Type_CROSS_SUBTOTAL || m_fldType == AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL) {// 说明是交叉区
					if(crossTableSet == null){
						return false;
					}else if (AnaReportFieldAction.isInMeasureArea(crossTableSet,
							area.getStart())) {
						if (m_fldType == AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL) {// 取消交叉小计
							if (field!=null&&field.getFieldCountDef() != null
									&& field.getFieldCountDef().getRangeFld() != null) {
								return true;
							}else{
								return false;
							}
							
						}
						return true;
					}
				} else if (m_fldType == AnaRepField.TYPE_CALC_FIELD
						|| m_fldType == AnaRepField.TYPE_CANCEL_CALC_FIELD) {
					if (crossTableSet != null) {// 交叉区指标
						if (AnaReportFieldAction.isInMeasureArea(crossTableSet,
								area.getStart())) {
							if (m_fldType == AnaRepField.TYPE_CALC_FIELD) {// 统计
								if (field!=null&&field.getFieldCountDef() != null
										&& field.getFieldCountDef()
												.getRangeFld() == null) {
									return true;
								}
							}
							return false;
						} else {
							return false;
						}
					} else {
						if(m_fldType == AnaRepField.TYPE_CANCEL_CALC_FIELD){
							if (field!=null&&field.getFieldCountDef() != null){
								return true;
							}
							return false;
						}
                         return true;
					}
				}
				return false;
			}
		}
		return StateUtil.isAreaSel(m_plugin.getReport(), focusComp); 		 
	}

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes3 = new ActionUIDes();
		uiDes3.setToolBar(true);
		uiDes3.setGroup(StringResource.getStringResource(RESID_SETFIELD));
		uiDes3.setPaths(new String[] {});
		if (m_fldType == AnaRepField.TYPE_DETAIL_FIELD) {
			uiDes3.setName(StringResource.getStringResource(RESID_SETFIELD_DETAIL));
			uiDes3.setTooltip(StringResource.getStringResource(RESID_SETFIELD_DETAIL));
			uiDes3.setImageFile("reportcore/remove_group.gif");
		} else if (m_fldType == AnaRepField.TYPE_GROUP_FIELD) {
			uiDes3.setName(StringResource.getStringResource(RESID_SETFIELD_GROUP));
			uiDes3.setTooltip(StringResource.getStringResource(RESID_SETFIELD_GROUP));
			uiDes3.setImageFile("reportcore/set_group.gif");
		} else if (m_fldType == AnaRepField.TYPE_CALC_FIELD) {
			uiDes3.setComboType(2);//下拉菜单
			uiDes3.setListCombo(true);
			uiDes3.setComboComponent(createComboxMenu()); 
			uiDes3.setName(StringResource.getStringResource(RESID_SETFIELD_CALC));
			uiDes3.setTooltip(StringResource.getStringResource(RESID_SETFIELD_CALC));
			uiDes3.setImageFile("reportcore/calc_field.gif");
		} else if(m_fldType==AnaRepField.TYPE_CANCEL_CALC_FIELD){
        	setFldType(AnaRepField.TYPE_CANCEL_CALC_FIELD);
        	uiDes3.setName(StringResource.getStringResource(RESID_CANCELFIELD_CALC));
        	uiDes3.setTooltip(StringResource.getStringResource(RESID_CANCELFIELD_CALC));
        	uiDes3.setImageFile("reportcore/calc_no.gif");
		}else if (m_fldType == AnaRepField.Type_CROSS_SUBTOTAL) {
			uiDes3.setName(StringResource.getStringResource(RESID_SETFIELD_SUBTOTAL));
			uiDes3.setTooltip(StringResource.getStringResource(RESID_SETFIELD_SUBTOTAL));
			uiDes3.setImageFile("reportcore/cross_subtotal.gif");
		}else if (m_fldType == AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL) {
			uiDes3.setName(StringResource.getStringResource(RESID_CANCELFIELD_CALC));
			uiDes3.setTooltip(StringResource.getStringResource("miufo00278"));
			uiDes3.setImageFile("reportcore/remove_group.gif");
		}
		return new ActionUIDes[] { uiDes3 };
	}

	/**
	 * override
	 * @see AbsActionExt.initListenerByComp();
	 */
	public void initListenerByComp(Component stateChangeComp) {	
		
		if(stateChangeComp instanceof JComboBox){
			setItem((JComboBox) stateChangeComp);
		}
		
		if(!(stateChangeComp instanceof JButton))
			return;
		final JButton toolButton = (JButton)stateChangeComp;
		m_plugin.getModel().getCellsModel().getSelectModel().addSelectModelListener(
				new SelectListener(){
					public void selectedChanged(SelectEvent e) {
						if(e.getProperty() != SelectEvent.ANCHOR_CHANGED)
							return;
						AnaReportModel model = m_plugin.getModel();
						CellPosition cellPos = model.getCellsModel().getSelectModel().getAnchorCell();
						Cell cell = model.getFormatCell(model.getCellsModel(), cellPos);
						if (cell == null)
							return;
						AnaRepField fld = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);								
						if (fld == null){
							if(m_fldType==AnaRepField.TYPE_DETAIL_FIELD&&toolButton.getName().equals(StringResource.getStringResource(RESID_SETFIELD_DETAIL))){
								switchButtonProperty(toolButton,AnaRepField.TYPE_GROUP_FIELD);
							}
							return;
						}
							
						
						if(m_fldType == AnaRepField.TYPE_GROUP_FIELD && fld.getFieldType() == AnaRepField.TYPE_GROUP_FIELD){
							switchButtonProperty(toolButton,AnaRepField.TYPE_DETAIL_FIELD);
						} else if(m_fldType == AnaRepField.TYPE_DETAIL_FIELD && fld.getFieldType() == AnaRepField.TYPE_DETAIL_FIELD){
							switchButtonProperty(toolButton,AnaRepField.TYPE_GROUP_FIELD);
						}
						
//						if(m_fldType == AnaRepField.TYPE_CANCEL_CALC_FIELD && m_fldType != AnaRepField.Type_CROSS_SUBTOTAL){//取消统计和取消分组用同一个按钮
//							if(fld.getFieldType() == AnaRepField.TYPE_GROUP_FIELD){
//								switchButtonProperty(toolButton,AnaRepField.TYPE_DETAIL_FIELD);
//							} else if(fld.getFieldType() == AnaRepField.TYPE_DETAIL_FIELD){
//								switchButtonProperty(toolButton,AnaRepField.TYPE_GROUP_FIELD);
//							}							
//						}
							
//						if(m_fldType != AnaRepField.TYPE_CANCEL_CALC_FIELD && fld.getFieldCountDef() != null && m_fldType != AnaRepField.Type_CROSS_SUBTOTAL){//取消统计和取消分组用同一个按钮
//							switchButtonProperty(toolButton,AnaRepField.TYPE_CANCEL_CALC_FIELD);
//						}	
						
//						if(m_fldType == AnaRepField.Type_CROSS_SUBTOTAL && fld.getFieldType() == AnaRepField.Type_CROSS_SUBTOTAL){
//							switchButtonProperty(toolButton,AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL);
//						}else if(m_fldType == AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL&&fld.getFieldCountDef()!=null&&fld.getFieldCountDef().getRangeFld()==null){
//							switchButtonProperty(toolButton,AnaRepField.Type_CROSS_SUBTOTAL);
//						}
						
					}
					
				});
	
			toolButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if (m_fldType == AnaRepField.TYPE_DETAIL_FIELD) {
						switchButtonProperty(toolButton,AnaRepField.TYPE_GROUP_FIELD);
					} else if (m_fldType == AnaRepField.TYPE_GROUP_FIELD) {
						switchButtonProperty(toolButton,AnaRepField.TYPE_DETAIL_FIELD);
					} 
//						else if(m_fldType == AnaRepField.TYPE_CANCEL_CALC_FIELD){
//						switchButtonProperty(toolButton,AnaRepField.TYPE_GROUP_FIELD);
//					} else if(m_fldType == AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL){
//						switchButtonProperty(toolButton,AnaRepField.TYPE_CANCEL_CALC_FIELD);
//					}
					
					if(!m_plugin.getModel().isFormatState())
						m_plugin.refreshDataModel(true);
				}
				
			});		
	}
	
    /**
	 * @i18n miufo00278=取消交叉小计
	 */
    private void switchButtonProperty(JButton toolButton,int iFldType){
    	switch (iFldType) {
		case AnaRepField.TYPE_DETAIL_FIELD:
			setFldType(AnaRepField.TYPE_DETAIL_FIELD);
			toolButton.setName(StringResource.getStringResource(RESID_SETFIELD_DETAIL));
			toolButton.setToolTipText(StringResource.getStringResource(RESID_SETFIELD_DETAIL));
			toolButton.setIcon(ResConst.getImageIcon("reportcore/remove_group.gif"));
			break;
		case AnaRepField.TYPE_GROUP_FIELD:
			setFldType(AnaRepField.TYPE_GROUP_FIELD);
			toolButton.setName(StringResource.getStringResource(RESID_SETFIELD_GROUP));
			toolButton.setToolTipText(StringResource.getStringResource(RESID_SETFIELD_GROUP));
			toolButton.setIcon(ResConst.getImageIcon("reportcore/set_group.gif"));
		    break;
        case AnaRepField.Type_CROSS_SUBTOTAL:
        	setFldType(AnaRepField.Type_CROSS_SUBTOTAL);
			toolButton.setName(StringResource.getStringResource(RESID_SETFIELD_SUBTOTAL));
			toolButton.setToolTipText(StringResource.getStringResource(RESID_SETFIELD_SUBTOTAL));
			toolButton.setIcon(ResConst.getImageIcon("reportcore/cross_subtotal.gif"));
		    break; 
        case AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL:
        	setFldType(AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL);
			toolButton.setName(StringResource.getStringResource(RESID_CANCELFIELD_CALC));
			toolButton.setToolTipText(StringResource.getStringResource("miufo00278"));
			toolButton.setIcon(ResConst.getImageIcon("reportcore/remove_group.gif"));
		default:
			break;
		}
    	toolButton.setEnabled(isEnabled(m_plugin.getReport().getTable().getCells()));
    }
    

	// 执行区域字段的属性设置
	private void changeAnaFieldType(CellsModel fmtModel, int fldType, Object[] param, CellPosition[] formatPoses) {
		if (formatPoses == null || formatPoses.length == 0)
			return;
		ArrayList<CellPosition> al_flds = new ArrayList<CellPosition>();// 发生了字段变化的单元位置
		for (CellPosition pos : formatPoses) {

			Cell cell = fmtModel.getCell(pos);
			if (cell == null && (fldType == AnaRepField.TYPE_CALC_FIELD|| fldType == AnaRepField.Type_CROSS_SUBTOTAL))// 对于统计字段和交叉小计，不要求单元有内容
				cell = fmtModel.getCellIfNullNew(pos.getRow(), pos.getColumn());

			if (cell != null) {
				
				AnaRepField fld = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
				Field[] allFlds = null;
				Field[] countFlds = null;
				
				
				if (fldType == AnaRepField.TYPE_CALC_FIELD || fldType == AnaRepField.Type_CROSS_SUBTOTAL) {
					
					FieldCountDef def = (FieldCountDef) param[1];
					countFlds = (Field[]) param[2];
					allFlds = (Field[]) param[3];
					String dsPK = (String) param[4];

					Field mainFld = null;//原始数据集字段
					String fldName=null;
					for (Field f : countFlds) {
						if(f instanceof FieldCountDef){
							fldName=((FieldCountDef)f).getMainFldName();
						}else{
							fldName=f.getFldname();
						}
						if (fldName.equals(def.getMainFldName())) {
							mainFld = f;
							break;
						}
					}
					if(formatPoses.length == 1){//批量设置统计类型时，不需要生成新的字段
						if (fld == null || (fld.getFieldID() != null && !fld.getFieldID().equals(mainFld.getFldname()))) {
							fld = new AnaRepField(mainFld, fldType, dsPK);							
						}
					}
                    
                    boolean isBefore=def.isBefore();
                    boolean isAfter=def.isAfter();
                    //??new
					def = new FieldCountDef(fld.getField(), def.getCountType(), def.getRangeFld(),def.getRangeName(),
								null);
					def.setBefore(isBefore);
					def.setAfter(isAfter);
					fld.setCountFieldDef(def);
					fld.setOrderType(AnaRepField.ORDERTYPE_NONE);
				}
				if(fld == null)
                	continue;
				if(fldType == AnaRepField.TYPE_GROUP_FIELD)//分组字段，没有设置排序时，自动默认为升序
					if(fld.isNoOrder())
						fld.setOrderType(AnaRepField.ORDERTYPE_ASCENDING);	
				if(fldType == AnaRepField.TYPE_DETAIL_FIELD){
//					fld.setOrderType(AnaRepField.ORDERTYPE_NONE);
					fmtModel.fireExtPropChanged(pos);
				}
				if(fld.getFieldType() == AnaRepField.Type_CROSS_SUBTOTAL && fldType == AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL){//取消交叉小计
					fld.setFieldType(AnaRepField.TYPE_CALC_FIELD);
					FieldCountDef countDef = new FieldCountDef(fld.getField(),IFldCountType.TYPE_SUM,null,null,null);
					fld.setCountFieldDef(countDef);
				}
				
				if (fld != null) {
					fld.setFieldType(fldType);
					AnaReportFieldAction.addFlds(fmtModel, pos, fld);

					al_flds.add(pos);
				}
				
				if(fldType == AnaRepField.TYPE_CANCEL_CALC_FIELD){//取消统计
					removeCountDefFld(fld, cell);
				}
			}		
		}
		if (al_flds.size() > 0 && m_plugin.getOperationState() == UfoReport.OPERATION_INPUT) {// 数据态的设置要自动处理小计
			if (m_fldType == AnaRepField.TYPE_GROUP_FIELD) {// 设置分组字段
				for (CellPosition pos : al_flds) {
					// 设置自动合并属性
					fmtModel.getCell(pos).addExtFmt(AnaCellCombine.KEY, new AnaCellCombine());
					// 并自动追加小计
					// TODO
				}
			} else if (m_fldType == AnaRepField.TYPE_DETAIL_FIELD) {// 设置细节字段
				// 删除自动合并属性
				for (CellPosition pos : al_flds) {
					fmtModel.getCell(pos).removeExtFmt(AnaCellCombine.KEY);
				}
				// 删除小计字段
				// TODO
			}
		}
		return;
	}

	/**
	 * @i18n iufobi00064=交叉区域没有指标或纬度,无法进行交叉设置
	 */
	private Object[] doGetParams(UfoReport container) {
		CellsModel cells = m_plugin.getModel().getFormatModel();
		AreaPosition[] areaPos = m_plugin.getFormatSelected();
		if (areaPos == null || areaPos.length <= 0)
			return null;
		AreaPosition selectedArea=areaPos[0];
		CellPosition[] selectedPos=m_plugin.getSelectedPos(cells, selectedArea);
        Cell cell = null;
        AnaRepField fld = null;
		
		
		if (m_fldType != AnaRepField.TYPE_CALC_FIELD && m_fldType != AnaRepField.Type_CROSS_SUBTOTAL) {// 需要选中字段才可以
			if (!AnaReportFieldAction.isSelAnaField(cells,selectedPos))
				return null;
			if(m_fldType != AnaRepField.TYPE_CANCEL_CALC_FIELD && m_fldType != AnaRepField.TYPE_CANCEL_CROSS_SUBTOTAL){
				for(CellPosition pos : selectedPos){
					cell=cells.getCell(pos);
					if(cell != null)
						fld = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
					if(fld!=null&&!checkField(fld))
						return null;
				}
				
			}			
			return new Object[] { container };
		} else {// 统计字段设置，需要在有数据的扩展区域中
			cell=cells.getCell(selectedPos[0]);
			if(cell != null)
				fld = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);	
			AreaDataModel areaModel = m_plugin.getExAreaModel(cells, selectedArea);
			if (areaModel == null || areaModel.getDSPK() == null)
				return null;
			Field[] allFlds = areaModel.getDSInfo().getDataSetDef().getMetaData().getFields(true);
			// 对于统计字段的设置，需要获取数据集的字段信息
			
			Field[] countFlds = null;
			Field[] aggrFlds = null;
			boolean bSubTotal = m_fldType == AnaRepField.Type_CROSS_SUBTOTAL;
			if (bSubTotal) {// 交叉表的小计
				if (!areaModel.isCross())// 扩展区域模型中没有交叉设置
					return null;
				
				AnaCrossTableSet cross = areaModel.getCrossSet();
				if (cross != null) {
					Field[] meas = cross.getCrossFlds(AnaRepField.Type_CROSS_MEASURE);
					if (meas != null && meas.length > 0)
						countFlds = meas;
					Field[] rows=cross.getCrossFlds(AnaRepField.Type_CROSS_ROW);
					Field[] cols=cross.getCrossFlds(AnaRepField.Type_CROSS_COLUMN);
					if(rows!=null||cols!=null){
						int rowLen=rows==null?0:rows.length;
						int colLen=cols==null?0:cols.length;
						aggrFlds=new Field[rowLen+colLen];
						for(int i=0;i<rowLen;i++){
							aggrFlds[i]=rows[i];
						}
						for(int j=0;j<colLen;j++){
							aggrFlds[j+rowLen]=cols[j];
						}
						
					}
				}
				//modify by guogang 2009-1-14
				if(countFlds==null||aggrFlds==null){
					UfoPublic.sendWarningMessage(StringResource.getStringResource("iufobi00064"), null);
					return null;
				}
			}else{//modify by guogang 2009-1-14
				if (countFlds == null)
					countFlds = allFlds;
				 if(aggrFlds==null){
					aggrFlds=allFlds;
				  }
			}
			
			
			
			if(fld != null && getSelectItem() != null){	//modify by wangyga 直接改变字段的统计类型
				return getCountTypeParams(fld,countFlds,allFlds,areaModel.getDSPK());								
			}
			
			boolean isShowCountList = !(AnaReportFieldAction.isInMeasureArea(areaModel.getCrossSet(),selectedPos[0]) && m_fldType == AnaRepField.TYPE_CALC_FIELD);
			CountFieldDefDlg dlg = new CountFieldDefDlg(container, countFlds, aggrFlds, bSubTotal,isShowCountList);
			if (cell != null) {				
				if (fld != null) {
					FieldCountDef def = null;
					if (fld.getFieldType() == AnaRepField.TYPE_CALC_FIELD)
						def = fld.getFieldCountDef();
					else {
						if (fld.getField() instanceof FieldCountDef)
							def = (FieldCountDef) fld.getField();
						else
							def = new FieldCountDef(fld.getField(), getSelectCountType(), null,null,
									null);
					}
					if (def != null)						
						dlg.setClacFieldInfo(def);
				} else{//字段为NULL时，也初始化选中菜单的统计类型
					FieldCountDef countDef = new FieldCountDef();
					countDef.setCountType(getSelectCountType());
					dlg.setClacFieldInfo(countDef);
				}
			}	
			
			if (dlg.showModal() == UfoDialog.ID_OK) {
				getItem().getModel().setSelectedItem(null);
				return new Object[] { container, dlg.getCalcInfo(), countFlds, allFlds, areaModel.getDSPK() };
			}			
			return null;
		}

	}

	private void resetCrossFld(AnaRepField fld, CellPosition pos, AreaDataModel areaData) {
		if (areaData == null || fld == null)
			return;
		AnaCrossTableSet crossSet = areaData.getCrossSet();
		if (crossSet == null )
			return;
		if(crossSet.getCrossArea().contain(pos)){
		   crossSet.setDirty(true);
		}
	}

	private boolean isHasChart(IArea area){
		AnaReportModel model = m_plugin.getModel();
		ExAreaModel exAreaModel = ExAreaModel.getInstance(model.getFormatModel());
		ExAreaCell exAreaCell = exAreaModel.getExArea(area);
		if(exAreaCell == null)
			return false;
		if(exAreaCell.getExAreaType() == ExAreaCell.EX_TYPE_CHART)
			return true;
		return false;
	}
	
	private Object[] getCountTypeParams(AnaRepField fld,Field[] countFlds,Field[] allFlds,String strDsPk){
		String msg = checkCountType(fld);
		if (msg != null){
			UfoPublic.sendWarningMessage(msg, m_plugin.getReport());
			getItem().getModel().setSelectedItem(null);
			return null;
		}					
		FieldCountDef countDef = fld.getFieldCountDef();
		if(countDef == null){
			countDef = new FieldCountDef(fld.getField(),getSelectCountType(),null,null,null);
		}
		countDef.setCountType(getSelectCountType());
		getItem().getModel().setSelectedItem(null);
		return new Object[] { m_plugin.getReport(), countDef, countFlds, allFlds, strDsPk };	
	}
	
	private void removeCountDefFld(AnaRepField fld,Cell cell){
		FieldCountDef countDef = fld.getFieldCountDef();
		if(countDef == null)
			return ;
		Field dsDield = m_plugin.getModel().getDataSetTool(fld.getDSPK()).getField(countDef.getMainFldName());
		AnaRepField newAnaField = new AnaRepField(dsDield,AnaRepField.TYPE_DETAIL_FIELD,fld.getDSPK());
		cell.setValue(newAnaField.getFieldLabel());
		cell.setExtFmt(AnaRepField.EXKEY_FIELDINFO, newAnaField);
	}
	
	private int getSelectCountType(){
		return getItem().getSelectedIndex()< 0 ? 0 : getItem().getSelectedIndex();
	}
	
	private Object getSelectItem(){
		return getItem().getSelectedItem();
	}
	
	
	
	/**
	 * create JPopupMenu
	 * @return
	 */
	private JPopupMenu createComboxMenu(){
		JPopupMenu popuMenu = new UFPopupMenu();

		String[] countTypeNamesAry = (new FieldCountDef()).getCountTypeNames();
		for (int i = 0;i<countTypeNamesAry.length;i++) {
			JMenuItem item = new UIMenuItem(countTypeNamesAry[i]);
			item.setActionCommand(String.valueOf(i));
			popuMenu.add(item);
		}				
		return popuMenu;
	}

	/**
	 * @i18n miufo00279=只能针对数值类型的字段计算 
	 */
	private String checkCountType(AnaRepField anaField) {		
		if(anaField == null)
			return null;
		Field field = anaField.getField();
		String strFieldName = field.getFldname();
		if(field instanceof FieldCountDef){
			strFieldName = ((FieldCountDef)field).getMainFldName();
		}
		Field oldField = m_plugin.getModel().getDataSetTool(anaField.getDSPK()).getField(strFieldName);
		if(oldField == null)
			return null;
		int iType = getSelectCountType();
		int iFldType = oldField.getDataType();
		if (!(iType == IFldCountType.TYPE_COUNT || iType == IFldCountType.TYPE_MAX || iType == IFldCountType.TYPE_MIN)) {// 除了计数,最大，最小之外，其他统计类型都要求字段是数值类型
			if (!DataTypeConstant.isNumberType(iFldType)) {
				return StringResource.getStringResource("miufo00279");
			}
		}
		return null;
	}
	
	/**
	 * @i18n miufo00280=对不起，统计字段上不能再做分组操作!
	 */
	private boolean checkField(AnaRepField anaField){
		if(anaField == null)
			return false;
		FieldCountDef countDef = anaField.getFieldCountDef();
		if(countDef != null){
			showErrorMessage(StringResource.getStringResource("miufo00280"));
			return false;
		}
		return true;
	}
	
	void showErrorMessage(String error) {
		NCOptionPane.showMessageDialog(m_plugin.getReport(), error, null, NCOptionPane.ERROR_MESSAGE);
	}
	
	public JComboBox getItem() {
		if(item == null)
			item = new UIComboBox();
		return item;
	}
	
	public void setItem(JComboBox item) {
		this.item = item;
	}

	private void setFldType(int type) {
		this.m_fldType = type;
	}
}
  